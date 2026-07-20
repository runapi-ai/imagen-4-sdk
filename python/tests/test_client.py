import pytest

from runapi.core import config
from runapi.core.errors import AuthenticationError, ValidationError
from runapi.imagen_4 import Imagen4Client
from runapi.imagen_4.resources.remix_image import RemixImage
from runapi.imagen_4.resources.text_to_image import TextToImage
from runapi.imagen_4.types import (
    CompletedRemixImageResponse,
    CompletedTextToImageResponse,
    RemixImageResponse,
    TextToImageResponse,
)


class FakeHttp:
    def __init__(self, *responses):
        self._responses = list(responses)
        self.calls = []

    def request(self, method, path, body=None, options=None):
        self.calls.append((method, path, body))
        if self._responses:
            return self._responses.pop(0)
        return {"id": "task_1", "status": "pending"}


@pytest.fixture(autouse=True)
def reset_config(monkeypatch):
    monkeypatch.delenv("RUNAPI_API_KEY", raising=False)
    monkeypatch.setattr(config, "api_key", None)
    yield


# --- auth -----------------------------------------------------------------


def test_accepts_api_key_parameter():
    assert isinstance(Imagen4Client(api_key="k", http_client=FakeHttp()), Imagen4Client)


def test_falls_back_to_global(monkeypatch):
    monkeypatch.setattr(config, "api_key", "global-key")
    assert isinstance(Imagen4Client(http_client=FakeHttp()), Imagen4Client)


def test_falls_back_to_env(monkeypatch):
    monkeypatch.setenv("RUNAPI_API_KEY", "env-key")
    assert isinstance(Imagen4Client(http_client=FakeHttp()), Imagen4Client)


def test_raises_without_api_key():
    with pytest.raises(AuthenticationError, match="API key is required"):
        Imagen4Client()


# --- injection / accessors ------------------------------------------------


def test_uses_injected_http_client():
    fake = FakeHttp()
    client = Imagen4Client(api_key="k", http_client=fake)
    assert client.text_to_image._http is fake
    assert client.remix_image._http is fake


def test_exposes_resource_accessors():
    client = Imagen4Client(api_key="k", http_client=FakeHttp())
    assert isinstance(client.text_to_image, TextToImage)
    assert isinstance(client.remix_image, RemixImage)


# --- request shapes -------------------------------------------------------


def test_create_posts_compacted_body():
    fake = FakeHttp({"id": "t1", "status": "pending"})
    client = Imagen4Client(api_key="k", http_client=fake)
    result = client.text_to_image.create(model="imagen-4", prompt="hello world", aspect_ratio="1:1")
    assert fake.calls == [
        ("post", "/api/v1/imagen_4/text_to_image", {"model": "imagen-4", "prompt": "hello world", "aspect_ratio": "1:1"}),
    ]
    assert isinstance(result, TextToImageResponse)


def test_get_fetches_by_id():
    fake = FakeHttp({"id": "t1", "status": "processing"})
    client = Imagen4Client(api_key="k", http_client=fake)
    client.text_to_image.get("t1")
    assert fake.calls == [("get", "/api/v1/imagen_4/text_to_image/t1", None)]


def test_remix_create_posts_compacted_body():
    fake = FakeHttp({"id": "t1", "status": "pending"})
    client = Imagen4Client(api_key="k", http_client=fake)
    result = client.remix_image.create(
        model="imagen-4-pro-remix-image",
        prompt="golden hour",
        source_image_urls=["https://x/a.png"],
        output_resolution="2k",
    )
    assert fake.calls == [
        (
            "post",
            "/api/v1/imagen_4/remix_image",
            {
                "model": "imagen-4-pro-remix-image",
                "prompt": "golden hour",
                "source_image_urls": ["https://x/a.png"],
                "output_resolution": "2k",
            },
        ),
    ]
    assert isinstance(result, RemixImageResponse)


def test_run_narrows_completed_type():
    fake = FakeHttp(
        {"id": "t1", "status": "pending"},
        {"id": "t1", "status": "completed", "images": [{"url": "https://x/y.png"}]},
    )
    client = Imagen4Client(api_key="k", http_client=fake)
    result = client.text_to_image.run(model="imagen-4", prompt="a serene lake")
    assert isinstance(result, CompletedTextToImageResponse)
    assert result.images[0].url == "https://x/y.png"


def test_remix_run_narrows_completed_type():
    fake = FakeHttp(
        {"id": "t1", "status": "pending"},
        {"id": "t1", "status": "completed", "images": [{"url": "https://x/y.png"}]},
    )
    client = Imagen4Client(api_key="k", http_client=fake)
    result = client.remix_image.run(
        model="imagen-4-pro-remix-image",
        prompt="golden hour",
        source_image_urls=["https://x/a.png"],
    )
    assert isinstance(result, CompletedRemixImageResponse)
    assert result.images[0].url == "https://x/y.png"


# --- validation -----------------------------------------------------------


def test_rejects_unknown_model():
    client = Imagen4Client(api_key="k", http_client=FakeHttp())
    with pytest.raises(
        ValidationError,
        match="model must be one of: imagen-4, imagen-4-fast, imagen-4-ultra",
    ):
        client.text_to_image.create(model="nope", prompt="hi there")


def test_requires_model():
    client = Imagen4Client(api_key="k", http_client=FakeHttp())
    with pytest.raises(
        ValidationError,
        match="model must be one of: imagen-4, imagen-4-fast, imagen-4-ultra",
    ):
        client.text_to_image.create(prompt="hi there")


def test_requires_prompt():
    client = Imagen4Client(api_key="k", http_client=FakeHttp())
    with pytest.raises(ValidationError, match="prompt is required"):
        client.text_to_image.create(model="imagen-4")


def test_rejects_invalid_aspect_ratio():
    client = Imagen4Client(api_key="k", http_client=FakeHttp())
    with pytest.raises(ValidationError, match="aspect_ratio"):
        client.text_to_image.create(model="imagen-4", prompt="hi there", aspect_ratio="2:3")


def test_auto_aspect_ratio_allowed_for_fast():
    fake = FakeHttp({"id": "t1", "status": "pending"})
    client = Imagen4Client(api_key="k", http_client=fake)
    client.text_to_image.create(model="imagen-4-fast", prompt="hi there", aspect_ratio="auto")
    assert fake.calls[0][2]["aspect_ratio"] == "auto"


def test_remix_rejects_non_remix_model():
    client = Imagen4Client(api_key="k", http_client=FakeHttp())
    with pytest.raises(ValidationError, match="model must be one of: imagen-4-pro-remix-image"):
        client.remix_image.create(model="imagen-4", prompt="hi", source_image_urls=["https://x/a.png"])


def test_remix_requires_source_image_urls():
    client = Imagen4Client(api_key="k", http_client=FakeHttp())
    with pytest.raises(ValidationError, match="source_image_urls is required"):
        client.remix_image.create(model="imagen-4-pro-remix-image", prompt="hi")


def test_remix_source_image_urls_max():
    client = Imagen4Client(api_key="k", http_client=FakeHttp())
    with pytest.raises(ValidationError, match="source_image_urls must contain between 1 and 8 items"):
        client.remix_image.create(
            model="imagen-4-pro-remix-image",
            prompt="hi",
            source_image_urls=[f"https://x/{i}.png" for i in range(9)],
        )

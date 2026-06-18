"""Imagen 4 client."""

from __future__ import annotations

from typing import Any, Optional

from runapi.core import ClientOptions, HttpClient, resolve_api_key

from .resources.remix_image import RemixImage
from .resources.text_to_image import TextToImage


class Imagen4Client:
    """Imagen 4 text-to-image and remix-image client.

    Example::

        client = Imagen4Client(api_key="sk-...")
        result = client.text_to_image.run(
            model="imagen-4", prompt="A neon city street"
        )
    """

    def __init__(self, api_key: Optional[str] = None, **options: Any) -> None:
        resolved_api_key = resolve_api_key(api_key)
        client_options = ClientOptions(api_key=resolved_api_key, **options)
        http = client_options.http_client or HttpClient(client_options)
        self.text_to_image = TextToImage(http)
        self.remix_image = RemixImage(http)

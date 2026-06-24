"""Imagen 4 text-to-image resource."""

from __future__ import annotations

from typing import Any, Dict

from runapi.core import Resource, ValidationError

from ..contract_gen import CONTRACT
from ..types import (
    CompletedTextToImageResponse,
    TextToImageResponse,
)


class TextToImage(Resource):
    """Generate images from text prompts with Imagen 4 models."""

    ENDPOINT = "/api/v1/imagen_4/text_to_image"

    RESPONSE_CLASS = TextToImageResponse
    COMPLETED_RESPONSE_CLASS = CompletedTextToImageResponse

    def run(self, **params: Any) -> Any:
        """Generate images from text and poll until it completes.

        Args:
            **params: Text-to-image parameters (model, prompt, ...).

        Returns:
            The completed task with images.
        """
        task = self.create(**params)
        return self._poll_until_complete(lambda: self.get(task.id))

    def create(self, **params: Any) -> Any:
        """Create a text-to-image task and return immediately with an ``id``.

        Args:
            **params: Text-to-image parameters (model, prompt, ...).

        Returns:
            The task creation result with an id.
        """
        compacted = self._compact_params(params)
        self._validate_contract(CONTRACT["text-to-image"], compacted)
        self._validate_params(compacted)
        return self._request("post", self.ENDPOINT, body=compacted)

    def get(self, id: str) -> Any:
        """Fetch the current status of a text-to-image task.

        Args:
            id: The task id.

        Returns:
            The current task status.
        """
        return self._request("get", f"{self.ENDPOINT}/{id}")

    def _validate_params(self, params: Dict[str, Any]) -> None:
        if not params.get("prompt"):
            raise ValidationError("prompt is required")

        if params.get("output_count") and params.get("model") != "imagen-4-fast":
            raise ValidationError("output_count is only supported for imagen-4-fast")

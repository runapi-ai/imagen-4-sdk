"""Imagen 4 remix-image resource."""

from __future__ import annotations

from typing import Any, Dict

from runapi.core import Resource, ValidationError

from ..types import (
    OUTPUT_FORMATS,
    OUTPUT_RESOLUTIONS,
    PRO_ASPECT_RATIOS,
    REMIX_MODELS,
    CompletedRemixImageResponse,
    RemixImageResponse,
)


class RemixImage(Resource):
    """Remix a set of source images with an Imagen 4 pro model."""

    ENDPOINT = "/api/v1/imagen_4/remix_image"

    RESPONSE_CLASS = RemixImageResponse
    COMPLETED_RESPONSE_CLASS = CompletedRemixImageResponse

    SOURCE_IMAGE_URLS_MAX = 8

    def run(self, **params: Any) -> Any:
        """Remix source images and poll until it completes.

        Args:
            **params: Remix-image parameters (model, prompt, source_image_urls, ...).

        Returns:
            The completed task with images.
        """
        task = self.create(**params)
        return self._poll_until_complete(lambda: self.get(task.id))

    def create(self, **params: Any) -> Any:
        """Create a remix-image task and return immediately with an ``id``.

        Args:
            **params: Remix-image parameters (model, prompt, source_image_urls, ...).

        Returns:
            The task creation result with an id.
        """
        compacted = self._compact_params(params)
        self._validate_params(compacted)
        return self._request("post", self.ENDPOINT, body=compacted)

    def get(self, id: str) -> Any:
        """Fetch the current status of a remix-image task.

        Args:
            id: The task id.

        Returns:
            The current task status.
        """
        return self._request("get", f"{self.ENDPOINT}/{id}")

    def _validate_params(self, params: Dict[str, Any]) -> None:
        model = params.get("model")
        if not model:
            raise ValidationError("model is required")
        if model not in REMIX_MODELS:
            raise ValidationError(f"Invalid model: {model}. Must be one of: {', '.join(REMIX_MODELS)}")
        if not params.get("prompt"):
            raise ValidationError("prompt is required")

        self._validate_source_image_urls(params)
        self._validate_optional(params, "aspect_ratio", PRO_ASPECT_RATIOS)
        self._validate_optional(params, "output_resolution", OUTPUT_RESOLUTIONS)
        self._validate_optional(params, "output_format", OUTPUT_FORMATS)

    def _validate_source_image_urls(self, params: Dict[str, Any]) -> None:
        urls = params.get("source_image_urls")
        if urls is None or (hasattr(urls, "__len__") and len(urls) == 0):
            raise ValidationError("source_image_urls is required")
        if hasattr(urls, "__len__") and len(urls) > self.SOURCE_IMAGE_URLS_MAX:
            raise ValidationError(f"source_image_urls supports up to {self.SOURCE_IMAGE_URLS_MAX} images")

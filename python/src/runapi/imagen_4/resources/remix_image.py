"""Imagen 4 remix-image resource."""

from __future__ import annotations

from typing import Any, Dict, Optional

from runapi.core import Resource, ValidationError, RequestOptions

from ..contract_gen import CONTRACT
from ..types import (
    CompletedRemixImageResponse,
    RemixImageResponse,
)


class RemixImage(Resource):
    """Remix a set of source images with an Imagen 4 pro model."""

    ENDPOINT = "/api/v1/imagen_4/remix_image"

    RESPONSE_CLASS = RemixImageResponse
    COMPLETED_RESPONSE_CLASS = CompletedRemixImageResponse

    def run(self, options: Optional[RequestOptions] = None, **params: Any) -> Any:
        """Remix source images and poll until it completes.

        Args:
            **params: Remix-image parameters (model, prompt, source_image_urls, ...).

        Returns:
            The completed task with images.
        """
        task = self.create(options=options, **params)
        return self._poll_until_complete(lambda: self.get(task.id, options=options))

    def create(self, options: Optional[RequestOptions] = None, **params: Any) -> Any:
        """Create a remix-image task and return immediately with an ``id``.

        Args:
            **params: Remix-image parameters (model, prompt, source_image_urls, ...).

        Returns:
            The task creation result with an id.
        """
        compacted = self._compact_params(params)
        self._validate_contract(CONTRACT["remix-image"], compacted)
        self._validate_params(compacted)
        return self._request("post", self.ENDPOINT, body=compacted, options=options)

    def get(self, id: str, options: Optional[RequestOptions] = None) -> Any:
        """Fetch the current status of a remix-image task.

        Args:
            id: The task id.

        Returns:
            The current task status.
        """
        return self._request("get", f"{self.ENDPOINT}/{id}", options=options)

    def _validate_params(self, params: Dict[str, Any]) -> None:
        if not params.get("prompt"):
            raise ValidationError("prompt is required")

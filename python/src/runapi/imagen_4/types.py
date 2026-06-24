"""Imagen 4 model lists, enums, and response models."""

from __future__ import annotations

from runapi.core import BaseModel, TaskResponse, optional, required


class Image(BaseModel):
    url = optional(str)
    origin_url = optional(str)


class TextToImageResponse(TaskResponse):
    """Response for a text-to-image task."""

    id = required(str)
    status = optional(str, enum=lambda: TaskResponse.Status.ALL)
    images = optional([lambda: Image])
    error = optional(str)


class CompletedTextToImageResponse(TextToImageResponse):
    """Narrowed response from ``run()`` once polling observes completion."""

    images = required([lambda: Image])


class RemixImageResponse(TextToImageResponse):
    """Response for a remix-image task."""

    pass


class CompletedRemixImageResponse(CompletedTextToImageResponse):
    """Narrowed remix-image response from ``run()`` once polling observes completion."""

    pass

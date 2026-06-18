"""Imagen 4 model lists, enums, and response models."""

from __future__ import annotations

from runapi.core import BaseModel, TaskResponse, optional, required

TEXT_MODELS = ["imagen-4", "imagen-4-fast", "imagen-4-ultra"]
REMIX_MODELS = ["imagen-4-pro-remix-image"]
MODELS = TEXT_MODELS + REMIX_MODELS
TEXT_ASPECT_RATIOS = ["1:1", "16:9", "9:16", "3:4", "4:3"]
PRO_ASPECT_RATIOS = ["1:1", "2:3", "3:2", "3:4", "4:3", "4:5", "5:4", "9:16", "16:9", "21:9", "auto"]
OUTPUT_RESOLUTIONS = ["1k", "2k", "4k"]
OUTPUT_FORMATS = ["png", "jpg"]
OUTPUT_COUNTS = [1, 2, 3, 4]


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

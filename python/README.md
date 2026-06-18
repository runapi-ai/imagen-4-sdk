# Imagen 4 Python SDK for RunAPI

The Imagen 4 Python SDK is the language-specific package for Imagen 4 on RunAPI. Use this imagen api package for text-to-image, image editing, and creative production flows when your application needs JSON request bodies, task status lookup, and consistent RunAPI errors in Python.

This imagen api README is the Python package guide inside the public `imagen4-sdk` repository. For the repository overview, start at `../README.md`; for model details, use https://runapi.ai/models/imagen-4; for API reference, use https://runapi.ai/docs#imagen-4; for SDK docs, use https://runapi.ai/docs#sdk-imagen-4.

## Install

```bash
pip install runapi-imagen-4
```

## Quick start

```python
from runapi.imagen_4 import Imagen4Client

client = Imagen4Client()  # reads RUNAPI_API_KEY, or pass api_key="sk-..."

task = client.text_to_image.create(
    model="imagen-4",
    prompt="A neon city street after rain, cinematic",
    aspect_ratio="16:9",
)
status = client.text_to_image.get(task.id)

remix = client.remix_image.create(
    model="imagen-4-pro-remix-image",
    prompt="Make it golden hour",
    source_image_urls=["https://example.com/source.jpg"],
)
```

Use `create` to submit a task and return quickly, `get` to fetch the latest task state, and `run` to create and poll until completion:

```python
result = client.text_to_image.run(
    model="imagen-4",
    prompt="A serene mountain lake at dawn",
)
print(result.images[0].url)
```

In web request handlers, prefer `create` plus webhook or later `get` polling so a worker is not held open.

RunAPI-generated file URLs are temporary. Download and store generated images, videos, audio, or other files in your own durable storage within 7 days; do not treat returned URLs as long-term assets.

## Language notes

Pass parameters as keyword arguments and catch the `runapi.imagen_4` error classes when building image jobs or scripts. The available resources are `text_to_image` and `remix_image`. Keep `RUNAPI_API_KEY` in the environment or your secret manager; never commit API keys or callback secrets.

## Links

- Model page: https://runapi.ai/models/imagen-4
- SDK docs: https://runapi.ai/docs#sdk-imagen-4
- Product docs: https://runapi.ai/docs#imagen-4
- Pricing and rate limits: https://runapi.ai/models/imagen-4/imagen-4
- Provider comparison: https://runapi.ai/providers/google
- Full catalog: https://runapi.ai/models
- Repository: https://github.com/runapi-ai/imagen4-sdk

## License

Licensed under the Apache License, Version 2.0.

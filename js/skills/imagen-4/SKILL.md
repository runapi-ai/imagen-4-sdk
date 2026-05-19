---
name: imagen-4
description: Generate images with Imagen 4 text-to-image and pro image-to-image through RunAPI.ai using the @runapi.ai/imagen-4 Node/TypeScript SDK. Use when the user asks for Imagen 4 image generation, pro image-to-image, Google Imagen, or writes against @runapi.ai/imagen-4.
documentation: https://runapi.ai/models/imagen-4
provider_page: https://runapi.ai/providers/google
catalog: https://runapi.ai/models
---
# @runapi.ai/imagen-4

Use `Imagen4Client` for Imagen 4 image tasks.

```ts
import { Imagen4Client } from '@runapi.ai/imagen-4';

const client = new Imagen4Client({ apiKey: process.env.RUNAPI_API_KEY });

const result = await client.textToImage.run({
  model: 'imagen-4-fast',
  prompt: 'A warm editorial photo of a glass studio at sunrise',
  aspect_ratio: '16:9',
});
```

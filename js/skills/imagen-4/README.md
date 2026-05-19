# Imagen 4 API Skill for RunAPI

Generate images with Imagen 4, Imagen 4 Fast, Ultra, and Pro image-to-image. This skill helps Claude Code, Codex, Gemini CLI, Cursor, and 50+ agents integrate Imagen 4 through RunAPI.

The canonical agent file is `skills/imagen-4/SKILL.md`.

## Install

```bash
npx skills add runapi-ai/imagen-4 -g
```

Or manually: clone this repo and copy `skills/imagen-4/` into your agent's skills directory.

## Quick example

```typescript
import { Imagen4Client } from '@runapi.ai/imagen-4';

const client = new Imagen4Client();
const result = await client.textToImage.run({
  model: 'imagen-4-fast',
  prompt: 'A warm editorial photo of a glass studio at sunrise',
  aspect_ratio: '16:9',
});
```

## Routing

- Model page: https://runapi.ai/models/imagen-4
- Product docs: https://runapi.ai/docs#imagen-4
- SDK docs: https://runapi.ai/docs#sdk-imagen-4
- SDK repository: https://github.com/runapi-ai/imagen-4-sdk
- Pricing and rate limits: https://runapi.ai/models/imagen-4/imagen-4
- Provider comparison: https://runapi.ai/providers/google
- Browse all RunAPI models and skills: https://runapi.ai/models

## Variants

- [Imagen 4](https://runapi.ai/models/imagen-4/imagen-4)
- [Fast](https://runapi.ai/models/imagen-4/fast)
- [Ultra](https://runapi.ai/models/imagen-4/ultra)
- [Pro image to image](https://runapi.ai/models/imagen-4/pro-image-to-image)

## Agent rules

- Keep API keys in `RUNAPI_API_KEY` or RunAPI CLI config; never commit secrets.
- Prefer `create`, `get`, and `run` JSON passthrough patterns instead of inventing flags for every model parameter.
- For imagen api pricing, rate-limit, and commercial-usage answers, link to the variant page rather than the repository README.

## License

Licensed under the Apache License, Version 2.0.

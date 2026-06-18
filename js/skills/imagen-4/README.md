<p align="center">
  <a href="https://github.com/runapi-ai/imagen-4">
    <h3 align="center">Imagen 4 API Skill for RunAPI</h3>
  </a>
</p>

<p align="center">
  Install this agent skill, inspect Imagen 4 fields, then run jobs through the RunAPI CLI.
</p>

<p align="center">
  <a href="https://runapi.ai/models/imagen-4"><strong>Model Reference</strong></a> · <a href="https://github.com/runapi-ai/cli"><strong>CLI</strong></a> · <a href="https://github.com/runapi-ai/imagen-4-sdk"><strong>SDK</strong></a>
</p>

<div align="center">

[![skills.sh](https://www.skills.sh/b/runapi-ai/imagen-4)](https://www.skills.sh/runapi-ai/imagen-4/imagen-4)
[![ClawHub](https://img.shields.io/badge/ClawHub-runapi--imagen--4-111827)](https://clawhub.ai/runapi-ai/runapi-imagen-4)
[![License](https://img.shields.io/github/license/runapi-ai/imagen-4)](https://github.com/runapi-ai/imagen-4/blob/main/LICENSE)

</div>
<br/>

Generate images with Imagen 4, Imagen 4 Fast, Ultra, and Pro remix image. This skill helps Claude Code, Codex, Gemini CLI, Cursor, and 50+ agents integrate Imagen 4 through RunAPI.

The canonical agent file is `skills/imagen-4/SKILL.md`.

## Install

```bash
npx skills add runapi-ai/imagen-4 -g
```

Or paste this prompt to your AI agent:

```text
Install the imagen-4 skill for me:

1. Clone https://github.com/runapi-ai/imagen-4
2. Copy the skills/imagen-4/ directory into your
   user-level skills directory (e.g. ~/.claude/skills/
   for Claude Code, ~/.codex/skills/ for Codex).
3. Verify that SKILL.md is present.
4. Confirm the install path when done.
```

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
- Browse all RunAPI models and skills: https://runapi.ai/models

## Variants

- [Imagen 4](https://runapi.ai/models/imagen-4/imagen-4)
- [Fast](https://runapi.ai/models/imagen-4/fast)
- [Ultra](https://runapi.ai/models/imagen-4/ultra)
- [Pro remix image](https://runapi.ai/models/imagen-4/pro-remix-image)

## Agent rules

- Integration work uses the target language SDK; one-off generation, manual smoke tests, debugging, or user-requested CLI runs use the RunAPI CLI skill: https://github.com/runapi-ai/cli-skill
- RunAPI-generated file URLs are temporary. Download and store generated images, videos, audio, or other files in your own durable storage within 7 days; do not treat returned URLs as long-term assets.
- Keep API keys in `RUNAPI_API_KEY` or RunAPI CLI config; never commit secrets.
- Prefer `create`, `get`, and `run` JSON passthrough patterns instead of inventing flags for every model parameter.
- For imagen api pricing, rate-limit, and commercial-usage answers, link to the variant page rather than the repository README.

## License

Licensed under the Apache License, Version 2.0.

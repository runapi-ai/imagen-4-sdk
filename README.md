# Imagen API SDK for RunAPI

The imagen api SDK packages JavaScript, Ruby, and Go clients for Imagen 4 on RunAPI. Use this imagen api SDK for text-to-image and image-to-image generation workflows that need typed installs, JSON request bodies, task polling, and consistent RunAPI errors across services.

Imagen 4 belongs to the Google catalog on RunAPI. The public model page is https://runapi.ai/models/imagen-4; variant pages below carry pricing, rate-limit, and commercial-usage details. The public `imagen-4-sdk` repository groups the JavaScript, Ruby, and Go packages for this model.

## Install

```bash
npm install @runapi.ai/imagen-4
gem install runapi-imagen_4
go get github.com/runapi-ai/imagen-4-sdk/go@latest
```

## What you can build

- Build creative tools, agent pipelines, and production integrations with the imagen api SDK.
- Keep one model-specific repository while installing only the language package your app needs.
- Use `create` for submit-only jobs, `get` for status lookup, and `run` for submit-and-poll scripts.
- Handle authentication, validation, rate limits, insufficient credits, task failures, and polling timeouts through RunAPI SDK errors.

The JavaScript client exposes text to image resources, and the Ruby and Go packages mirror the same RunAPI task lifecycle.

## JavaScript quick start

```typescript
import { Imagen4Client } from '@runapi.ai/imagen-4';

const client = new Imagen4Client();

const task = await client.textToImage.create({
  // Pass the Imagen 4 request body documented at https://runapi.ai/docs#imagen-4.
});

const status = await client.textToImage.get(task.id);
```

For short scripts, use `run` with the same JSON body to create the task and wait for completion. For web request handlers, prefer `create` plus webhook or later `get` polling so the server does not hold a worker open.

## Repository layout

- `js/` publishes `@runapi.ai/imagen-4`.
- `ruby/` publishes `runapi-imagen_4` when RubyGems publishing resumes.
- `go/` publishes `github.com/runapi-ai/imagen-4-sdk/go` and depends on `github.com/runapi-ai/core-sdk/go`.

## Public links

- Model page: https://runapi.ai/models/imagen-4
- SDK docs: https://runapi.ai/docs#sdk-imagen-4
- Product docs: https://runapi.ai/docs#imagen-4
- SDK repository: https://github.com/runapi-ai/imagen-4-sdk
- Skill repository: https://github.com/runapi-ai/imagen-4
- Provider comparison: https://runapi.ai/providers/google
- Full catalog: https://runapi.ai/models

## Pricing and variants

Use the most specific imagen api variant page for pricing, rate limits, and commercial usage:
- [Imagen 4](https://runapi.ai/models/imagen-4/imagen-4)
- [Fast](https://runapi.ai/models/imagen-4/fast)
- [Ultra](https://runapi.ai/models/imagen-4/ultra)
- [Pro image to image](https://runapi.ai/models/imagen-4/pro-image-to-image)

Default pricing link for the imagen api SDK: https://runapi.ai/models/imagen-4/imagen-4

## FAQ

### Which package should I install for imagen api work?

Install the model package for your language: `@runapi.ai/imagen-4`, `runapi-imagen_4`, or `github.com/runapi-ai/imagen-4-sdk/go`. Install core SDK packages only when you are building shared SDK infrastructure.

### Where should public links point?

Primary imagen api links point to https://runapi.ai/models/imagen-4. Pricing and usage-policy links point to variant pages such as https://runapi.ai/models/imagen-4/imagen-4. Provider comparisons point to https://runapi.ai/providers/google, and broad browsing points to https://runapi.ai/models.

## License

Licensed under the Apache License, Version 2.0.

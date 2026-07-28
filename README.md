<p align="center">
  <a href="https://runapi.ai"><img src="https://runapi.ai/icon.svg" height="56" alt="RunAPI"></a>
</p>

<h3 align="center">
  <a href="https://github.com/runapi-ai/imagen-4-sdk">Imagen 4 API SDK for RunAPI</a>
</h3>

<p align="center">
  Imagen 4 API SDKs for JavaScript, Python, Ruby, Go, Java, and PHP on RunAPI.
</p>

<div align="center">

[![npm](https://img.shields.io/npm/v/@runapi.ai/imagen-4)](https://www.npmjs.com/package/@runapi.ai/imagen-4)
[![PyPI](https://img.shields.io/pypi/v/runapi-imagen-4)](https://pypi.org/project/runapi-imagen-4/)
[![RubyGems](https://img.shields.io/gem/v/runapi-imagen-4)](https://rubygems.org/gems/runapi-imagen-4)
[![Go Reference](https://pkg.go.dev/badge/github.com/runapi-ai/imagen-4-sdk/go.svg)](https://pkg.go.dev/github.com/runapi-ai/imagen-4-sdk/go)
[![Maven Central](https://img.shields.io/maven-central/v/ai.runapi/runapi-imagen-4)](https://central.sonatype.com/artifact/ai.runapi/runapi-imagen-4)
[![License](https://img.shields.io/github/license/runapi-ai/imagen-4-sdk)](https://github.com/runapi-ai/imagen-4-sdk/blob/main/LICENSE)

</div>
<br/>

The Imagen 4 API SDK packages JavaScript, Python, Ruby, Go, Java, and PHP clients for Imagen 4 on RunAPI. Use it for text-to-image and remix-image workflows when your app needs typed request builders, predictable task polling, file upload helpers, account helpers, and consistent RunAPI errors.

Imagen 4 is listed in the RunAPI model catalog at https://runapi.ai/models/imagen-4. Variant pages below carry pricing, rate-limit, and commercial-usage details. The public `imagen-4-sdk` repository groups the non-PHP language packages, examples, CI, and release tags for this model. The PHP package is released from a split Composer repository.

## Install

```bash
npm install @runapi.ai/imagen-4
pip install runapi-imagen-4
gem install runapi-imagen-4
go get github.com/runapi-ai/imagen-4-sdk/go@latest
```

Gradle:

```kotlin
dependencies {
  implementation("ai.runapi:runapi-imagen-4:0.1.2")
}
```

Maven:

```xml
<dependency>
  <groupId>ai.runapi</groupId>
  <artifactId>runapi-imagen-4</artifactId>
  <version>0.1.2</version>
</dependency>
```

Use the Java BOM when installing multiple RunAPI Java modules:

```kotlin
dependencies {
  implementation(platform("ai.runapi:runapi-bom:0.2.7"))
  implementation("ai.runapi:runapi-imagen-4")
}
```

The PHP package is published from the split Composer repository as `runapi-ai/imagen-4`; see https://github.com/runapi-ai/imagen-4-php for PHP install and examples.

## What you can build

- Build apps, agent workflows, batch jobs, and production services around Imagen 4 requests.
- Install only the language package your app needs while keeping one model-specific repository for docs and releases.
- Use `create` for submit-only jobs, `get` for status lookup, and `run` for submit-and-poll scripts.
- Upload local files, URL files, or base64 files through shared RunAPI file helpers.
- Handle validation, authentication, rate limits, insufficient credits, task failures, and polling timeouts through RunAPI SDK errors.

## Java quick start

```java
import ai.runapi.imagen4.Imagen4Client;
import ai.runapi.imagen4.types.TextToImageParams;
import ai.runapi.imagen4.types.CompletedTextToImageResponse;
import ai.runapi.imagen4.types.TextToImageModel;

Imagen4Client client = Imagen4Client.builder()
    .apiKey(System.getenv("RUNAPI_API_KEY"))
    .build();

CompletedTextToImageResponse result = client.textToImage().run(
    TextToImageParams.builder()
        .model(TextToImageModel.IMAGEN_4)
        .prompt("A magazine cover photo of a red bicycle by the sea")
        .aspectRatio("4:3")
        .build()
);
```

Java packages target Java 8 bytecode and are tested on Java 8, 11, 17, and 21. Each model artifact depends on `ai.runapi:runapi-core`, so application code normally installs only `ai.runapi:runapi-imagen-4`.

## Task lifecycle

Most media endpoints are asynchronous. `create()` submits a task and returns its id, `get(id)` fetches the latest task state, and `run(params)` creates the task and polls until it reaches a terminal state. In web request handlers, prefer `create()` plus webhook or later `get()` polling so the server does not hold a worker open.

## Repository layout

- `js/` publishes `@runapi.ai/imagen-4`.
- `python/` publishes `runapi-imagen-4`.
- `ruby/` publishes `runapi-imagen-4`.
- `go/` publishes `github.com/runapi-ai/imagen-4-sdk/go` and depends on `github.com/runapi-ai/core-sdk/go`.
- `java/` publishes `ai.runapi:runapi-imagen-4` and depends on `ai.runapi:runapi-core`.

## Public links

- Model page: https://runapi.ai/models/imagen-4
- SDK docs: https://runapi.ai/docs/resources/sdks
- Product docs: https://runapi.ai/docs/api/imagen-4/text-to-image
- SDK repository: https://github.com/runapi-ai/imagen-4-sdk
- PHP package repository: https://github.com/runapi-ai/imagen-4-php
- Skill repository: https://github.com/runapi-ai/imagen-4
- Provider comparison: https://runapi.ai/providers/google
- Full catalog: https://runapi.ai/models

## Pricing and variants

Use the most specific Imagen 4 variant page for pricing, rate limits, and commercial usage:
- [Imagen 4](https://runapi.ai/models/imagen-4/imagen-4)
- [Fast](https://runapi.ai/models/imagen-4/fast)
- [Ultra](https://runapi.ai/models/imagen-4/ultra)
- [Pro remix image](https://runapi.ai/models/imagen-4/pro-remix-image)

Default pricing link for the Imagen 4 SDK: https://runapi.ai/models/imagen-4/imagen-4

## File storage

RunAPI-generated file URLs are temporary. Download and store generated images, videos, audio, or other files in your own durable storage within 7 days; do not treat returned URLs as long-term assets.

## FAQ

### Which package should I install for Imagen 4 work?

Install the model package for your language: `@runapi.ai/imagen-4` on npm, `runapi-imagen-4` on PyPI, `runapi-imagen-4` on RubyGems, `github.com/runapi-ai/imagen-4-sdk/go`, `ai.runapi:runapi-imagen-4` on Maven Central, or `runapi-ai/imagen-4` on Packagist. Install core SDK packages only when you are building shared SDK infrastructure.

### Where should public links point?

Primary Imagen 4 links point to https://runapi.ai/models/imagen-4. Pricing and usage-policy links point to variant pages such as https://runapi.ai/models/imagen-4/imagen-4. Provider comparisons point to https://runapi.ai/providers/google, and broad browsing points to https://runapi.ai/models.

## License

Licensed under the Apache License, Version 2.0.

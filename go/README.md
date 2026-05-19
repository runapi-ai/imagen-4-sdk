# Imagen API Go SDK for RunAPI

The imagen api Go SDK is the language-specific package for Imagen 4 on RunAPI. Use this imagen api package for text-to-image, image-to-image, edit, and creative production flows when your application needs JSON request bodies, task status lookup, and consistent RunAPI errors in Go.

This imagen api README is the Go package guide inside the public `imagen4-sdk` repository. For the repository overview, start at `../README.md`; for model details, use https://runapi.ai/models/imagen-4; for API reference, use https://runapi.ai/docs#imagen-4; for SDK docs, use https://runapi.ai/docs#sdk-imagen-4.

## Install

```bash
go get github.com/runapi-ai/imagen4-sdk/go@latest
```

## Quick start

```go
import (
  "context"

  "github.com/runapi-ai/imagen4-sdk/go/imagen4"
)

client, err := imagen4.NewClient()
task, err := client.Generations.Create(context.Background(), imagen4.GenerationParams{
  // Pass the Imagen 4 JSON request body from https://runapi.ai/docs#imagen-4.
})
status, err := client.Generations.Get(context.Background(), task.ID)
```

Use `create` when you want to submit a task and return quickly, `get` when you need the latest task state, and `run` when a script should create and poll until completion. In web request handlers, prefer `create` plus webhook or later `get` polling so a worker is not held open.

## Language notes

Use the public Go module with `github.com/runapi-ai/core-sdk/go` options when building image services, CLIs, or workers. The available resources include generations. Keep `RUNAPI_API_KEY` in the environment or your secret manager; never commit API keys or callback secrets.

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

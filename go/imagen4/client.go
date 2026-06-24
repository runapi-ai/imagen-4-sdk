// Package imagen4 provides the Imagen 4 image generation API client.
//
//	client, err := imagen4.NewClient(option.WithAPIKey("sk-your-api-key"))
//	result, err := client.TextToImage.Run(ctx, imagen4.TextToImageParams{
//	    Model: "imagen-4", Prompt: "A cat in a spacesuit on the moon",
//	})
package imagen4

import (
	"context"

	"github.com/runapi-ai/core-sdk/go/base"
	"github.com/runapi-ai/core-sdk/go/core"
	"github.com/runapi-ai/core-sdk/go/option"
)

const (
	textToImagePath = "/api/v1/imagen_4/text_to_image"
	remixImagePath  = "/api/v1/imagen_4/remix_image"
)

// Client provides text-to-image generation and remix (image-guided generation) for Imagen 4.
type Client struct {
	base.Base
	TextToImage *TextToImage
	RemixImage  *RemixImage
}

// NewClient creates an Imagen 4 client with the given options.
func NewClient(opts ...option.ClientOption) (*Client, error) {
	resolved, err := option.ResolveClientOptions(opts...)
	if err != nil {
		return nil, err
	}
	httpClient, err := core.NewHTTPClient(resolved)
	if err != nil {
		return nil, err
	}
	return NewClientWithHTTP(httpClient), nil
}

// NewClientWithHTTP creates an Imagen 4 client with a pre-configured HTTP transport.
func NewClientWithHTTP(httpClient core.HTTPClient) *Client {
	return &Client{
		Base:        base.New(httpClient),
		TextToImage: &TextToImage{http: httpClient},
		RemixImage:  &RemixImage{http: httpClient},
	}
}

// TextToImage generates images from a text prompt.
// Three model tiers are available: imagen-4 (standard), imagen-4-fast (lower latency),
// and imagen-4-ultra (highest quality). Use NegativePrompt to steer away from unwanted content.
type TextToImage struct{ http core.HTTPClient }

// Create submits a text-to-image task and returns immediately with a task id.
func (r *TextToImage) Create(ctx context.Context, params TextToImageParams, opts ...option.RequestOption) (*core.TaskCreateResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	body := core.CompactParams(params)
	if err := core.ValidateParams(contractSchema["text-to-image"], body); err != nil {
		return nil, err
	}
	return core.PostJSON[core.TaskCreateResponse](ctx, r.http, textToImagePath, body, requestOptions)
}

// Get fetches the current status of a text-to-image task by id.
func (r *TextToImage) Get(ctx context.Context, id string, opts ...option.RequestOption) (*TextToImageResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.GetJSON[TextToImageResponse](ctx, r.http, core.ResourcePath(textToImagePath, id), requestOptions)
}

// Run submits a text-to-image task and polls until it completes.
func (r *TextToImage) Run(ctx context.Context, params TextToImageParams, opts ...option.RequestOption) (*TextToImageResponse, error) {
	_, pollingOptions := option.ResolveRequestOptions(opts...)
	return core.RunAsync(ctx, func(ctx context.Context) (*core.TaskCreateResponse, error) { return r.Create(ctx, params, opts...) }, func(ctx context.Context, id string) (*TextToImageResponse, error) { return r.Get(ctx, id, opts...) }, pollingOptions)
}

// RemixImage generates new images guided by one or more source images combined with a text prompt.
// Accepts up to 8 source images. Supports output resolution control (1k/2k/4k) and format selection (png/jpg).
type RemixImage struct{ http core.HTTPClient }

// Create submits a remix-image task and returns immediately with a task id.
func (r *RemixImage) Create(ctx context.Context, params RemixImageParams, opts ...option.RequestOption) (*core.TaskCreateResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	body := core.CompactParams(params)
	if err := core.ValidateParams(contractSchema["remix-image"], body); err != nil {
		return nil, err
	}
	return core.PostJSON[core.TaskCreateResponse](ctx, r.http, remixImagePath, body, requestOptions)
}

// Get fetches the current status of a remix-image task by id.
func (r *RemixImage) Get(ctx context.Context, id string, opts ...option.RequestOption) (*RemixImageResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.GetJSON[RemixImageResponse](ctx, r.http, core.ResourcePath(remixImagePath, id), requestOptions)
}

// Run submits a remix-image task and polls until it completes.
func (r *RemixImage) Run(ctx context.Context, params RemixImageParams, opts ...option.RequestOption) (*RemixImageResponse, error) {
	_, pollingOptions := option.ResolveRequestOptions(opts...)
	return core.RunAsync(ctx, func(ctx context.Context) (*core.TaskCreateResponse, error) { return r.Create(ctx, params, opts...) }, func(ctx context.Context, id string) (*RemixImageResponse, error) { return r.Get(ctx, id, opts...) }, pollingOptions)
}

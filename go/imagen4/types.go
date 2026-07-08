package imagen4

// Model selects the Imagen 4 variant. Available models differ by endpoint;
// text-to-image accepts "imagen-4", "imagen-4-fast", and "imagen-4-ultra",
// while remix-image uses "imagen-4-pro-remix-image".
type Model string

// AspectRatio controls the output image dimensions.
// Text-to-image supports "1:1", "16:9", "9:16", "3:4", and "4:3"; imagen-4-fast also supports "auto".
// Remix-image additionally supports "2:3", "3:2", "4:5", "5:4", "21:9", and "auto".
type AspectRatio string

// OutputResolution sets the pixel resolution for remix-image output: "1k", "2k", or "4k".
type OutputResolution string

// OutputFormat selects the image encoding for remix-image output: "png" or "jpg".
type OutputFormat string

// TaskStatus is the async task lifecycle state (e.g. "processing", "completed", "failed").
type TaskStatus string

// TextToImageParams configures text-to-image generation.
// Use NegativePrompt to discourage specific visual elements.
type TextToImageParams struct {
	Model          Model       `json:"model" help:"required; model slug"`
	Prompt         string      `json:"prompt" help:"required; text prompt up to 5000 chars"`
	CallbackURL    string      `json:"callback_url,omitempty" help:"optional; webhook URL"`
	NegativePrompt string      `json:"negative_prompt,omitempty" help:"optional; content to discourage for Imagen 4 models"`
	AspectRatio    AspectRatio `json:"aspect_ratio,omitempty" help:"optional; output aspect ratio"`
	Seed           *int        `json:"seed,omitempty" help:"optional; reproducible generation seed"`
}

// RemixImageParams configures image-guided generation.
// SourceImageURLs (1-8 images) and Prompt are both required.
// OutputResolution and OutputFormat are only available with imagen-4-pro-remix-image.
type RemixImageParams struct {
	Model            Model            `json:"model" help:"required; model slug"`
	Prompt           string           `json:"prompt" help:"required; text prompt up to 10000 chars"`
	SourceImageURLs  []string         `json:"source_image_urls" help:"required; source image URLs, up to 8 images"`
	CallbackURL      string           `json:"callback_url,omitempty" help:"optional; webhook URL"`
	AspectRatio      AspectRatio      `json:"aspect_ratio,omitempty" help:"optional; output aspect ratio"`
	OutputResolution OutputResolution `json:"output_resolution,omitempty" help:"optional; output resolution"`
	OutputFormat     OutputFormat     `json:"output_format,omitempty" help:"optional; output format"`
}

// AsyncTaskResponse carries the task ID, lifecycle status, and error for all Imagen 4 async operations.
type AsyncTaskResponse struct {
	ID     string     `json:"id"`
	Status TaskStatus `json:"status"`
	Error  string     `json:"error,omitempty"`
}

func (r AsyncTaskResponse) GetID() string     { return r.ID }
func (r AsyncTaskResponse) GetStatus() string { return string(r.Status) }
func (r AsyncTaskResponse) GetError() string  { return r.Error }

// Image holds a URL to a generated image. OriginURL is the unprocessed source when available.
type Image struct {
	URL       string `json:"url"`
	OriginURL string `json:"origin_url,omitempty"`
}

// TextToImageResponse is the result of a text-to-image task, containing one or more generated images.
type TextToImageResponse struct {
	AsyncTaskResponse
	Images []Image `json:"images,omitempty"`
}

// RemixImageResponse is the result of a remix-image task. Same structure as [TextToImageResponse].
type RemixImageResponse = TextToImageResponse

package imagen4

type Model string

type AspectRatio string

type OutputResolution string

type OutputFormat string

type TaskStatus string

type TextToImageParams struct {
	Model          Model       `json:"model" help:"required; model slug"`
	Prompt         string      `json:"prompt" help:"required; text prompt up to 5000 chars"`
	CallbackURL    string      `json:"callback_url,omitempty" help:"optional; webhook URL"`
	NegativePrompt string      `json:"negative_prompt,omitempty" help:"optional; content to discourage for Imagen 4 models"`
	AspectRatio    AspectRatio `json:"aspect_ratio,omitempty" help:"optional; output aspect ratio"`
	Seed           *int        `json:"seed,omitempty" help:"optional; reproducible generation seed"`
	OutputCount    int         `json:"output_count,omitempty" help:"optional; number of generated images"`
}

type RemixImageParams struct {
	Model            Model            `json:"model" help:"required; model slug"`
	Prompt           string           `json:"prompt" help:"required; text prompt up to 10000 chars"`
	SourceImageURLs  []string         `json:"source_image_urls" help:"required; source image URLs, up to 8 images"`
	CallbackURL      string           `json:"callback_url,omitempty" help:"optional; webhook URL"`
	AspectRatio      AspectRatio      `json:"aspect_ratio,omitempty" help:"optional; output aspect ratio"`
	OutputResolution OutputResolution `json:"output_resolution,omitempty" help:"optional; output resolution"`
	OutputFormat     OutputFormat     `json:"output_format,omitempty" help:"optional; output format"`
}

type AsyncTaskResponse struct {
	ID     string     `json:"id"`
	Status TaskStatus `json:"status"`
	Error  string     `json:"error,omitempty"`
}

func (r AsyncTaskResponse) GetID() string     { return r.ID }
func (r AsyncTaskResponse) GetStatus() string { return string(r.Status) }
func (r AsyncTaskResponse) GetError() string  { return r.Error }

type Image struct {
	URL       string `json:"url"`
	OriginURL string `json:"origin_url,omitempty"`
}

type TextToImageResponse struct {
	AsyncTaskResponse
	Images []Image `json:"images,omitempty"`
}

type RemixImageResponse = TextToImageResponse

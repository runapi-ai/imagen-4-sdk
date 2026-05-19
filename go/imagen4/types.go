package imagen4

type Model string

type AspectRatio string

type Resolution string

type OutputFormat string

type TaskStatus string

type TextToImageParams struct {
	Model          Model        `json:"model" help:"required; imagen-4, imagen-4-fast, imagen-4-ultra, or imagen-4-pro-image-to-image"`
	Prompt         string       `json:"prompt" help:"required; text prompt (Imagen 4 <=5000 chars, pro image-to-image <=10000 chars)"`
	CallbackURL    string       `json:"callback_url,omitempty" help:"optional; webhook URL"`
	NegativePrompt string       `json:"negative_prompt,omitempty" help:"optional; content to discourage for Imagen 4 models"`
	AspectRatio    AspectRatio  `json:"aspect_ratio,omitempty" help:"optional; 1:1, 16:9, 9:16, 3:4, 4:3; pro image-to-image also supports 2:3, 3:2, 4:5, 5:4, 21:9, auto"`
	Seed           any          `json:"seed,omitempty" help:"optional; reproducible generation seed"`
	NumImages      string       `json:"num_images,omitempty" help:"optional; imagen-4-fast only: 1, 2, 3, or 4"`
	ImageInput     []string     `json:"image_input,omitempty" help:"optional; imagen-4-pro-image-to-image reference image URLs, up to 8 images"`
	Resolution     Resolution   `json:"resolution,omitempty" help:"optional; imagen-4-pro-image-to-image: 1K, 2K, or 4K"`
	OutputFormat   OutputFormat `json:"output_format,omitempty" help:"optional; imagen-4-pro-image-to-image: png or jpg"`
}

type AsyncTaskResponse struct {
	ID     string     `json:"id"`
	Status TaskStatus `json:"status"`
	Error  string     `json:"error,omitempty"`
}

func (r AsyncTaskResponse) GetID() string     { return r.ID }
func (r AsyncTaskResponse) GetStatus() string { return string(r.Status) }
func (r AsyncTaskResponse) GetError() string  { return r.Error }

type TextToImageResponse struct {
	AsyncTaskResponse
	ResultURLs []string `json:"result_urls,omitempty"`
}

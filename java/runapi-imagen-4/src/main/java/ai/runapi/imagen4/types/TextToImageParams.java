package ai.runapi.imagen4.types;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Parameters for text to image operations. */
public final class TextToImageParams {
  private final String model;
  private final String prompt;
  private final String callbackUrl;
  private final String negativePrompt;
  private final String aspectRatio;
  private final Integer seed;

  private TextToImageParams(Builder builder) {
    this.model = builder.model;
    this.prompt = builder.prompt;
    this.callbackUrl = builder.callbackUrl;
    this.negativePrompt = builder.negativePrompt;
    this.aspectRatio = builder.aspectRatio;
    this.seed = builder.seed;
  }

  /** Creates a new TextToImageParams builder. */
  public static Builder builder() {
    return new Builder();
  }

  /** Returns the RunAPI action key for this request. */
  public String action() {
    return "imagen-4/text-to-image";
  }

  /** Converts these parameters to the JSON request body shape. */
  public Map<String, Object> toMap() {
    Map<String, Object> raw = new LinkedHashMap<String, Object>();
    raw.put("model", Imagen4ParamUtils.wireValue(model));
    raw.put("prompt", Imagen4ParamUtils.wireValue(prompt));
    raw.put("callback_url", Imagen4ParamUtils.wireValue(callbackUrl));
    raw.put("negative_prompt", Imagen4ParamUtils.wireValue(negativePrompt));
    raw.put("aspect_ratio", Imagen4ParamUtils.wireValue(aspectRatio));
    raw.put("seed", Imagen4ParamUtils.wireValue(seed));
    return Imagen4ParamUtils.compact(raw);
  }



  /** Builder for {@link TextToImageParams}. */
  public static final class Builder {
    private String model;
    private String prompt;
    private String callbackUrl;
    private String negativePrompt;
    private String aspectRatio;
    private Integer seed;

    private Builder() {}

    /** Sets the model slug using a typed model value. */
    public Builder model(TextToImageModel value) {
      this.model = java.util.Objects.requireNonNull(value, "model").value();
      return this;
    }

    /** Sets the model slug using a string value. */
    public Builder model(String value) {
      this.model = Imagen4ParamUtils.requireNonBlankTrim(value, "model");
      return this;
    }


    /** Sets the text prompt. */
    public Builder prompt(String value) {
      this.prompt = Imagen4ParamUtils.requireNonBlank(value, "prompt");
      return this;
    }

    /** Sets the webhook URL for task completion notifications. */
    public Builder callbackUrl(String value) {
      this.callbackUrl = Imagen4ParamUtils.requireNonBlank(value, "callbackUrl");
      return this;
    }

    /** Sets the negative prompt describing what to avoid. */
    public Builder negativePrompt(String value) {
      this.negativePrompt = Imagen4ParamUtils.requireNonBlank(value, "negativePrompt");
      return this;
    }

    /** Sets the output aspect ratio. */
    public Builder aspectRatio(String value) {
      this.aspectRatio = Imagen4ParamUtils.requireNonBlank(value, "aspectRatio");
      return this;
    }

    /** Sets the random seed. */
    public Builder seed(int value) {
      this.seed = value;
      return this;
    }

    /** Builds immutable text to image parameters. */
    public TextToImageParams build() {
      return new TextToImageParams(this);
    }
  }
}

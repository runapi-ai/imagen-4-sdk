package ai.runapi.imagen4.types;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Parameters for remix image operations. */
public final class RemixImageParams {
  private final String model;
  private final String prompt;
  private final List<String> sourceImageUrls;
  private final String callbackUrl;
  private final String aspectRatio;
  private final String outputResolution;
  private final String outputFormat;

  private RemixImageParams(Builder builder) {
    this.model = builder.model;
    this.prompt = builder.prompt;
    this.sourceImageUrls = Imagen4ParamUtils.requiredStrings(builder.sourceImageUrls, "sourceImageUrls");
    this.callbackUrl = builder.callbackUrl;
    this.aspectRatio = builder.aspectRatio;
    this.outputResolution = builder.outputResolution;
    this.outputFormat = builder.outputFormat;
  }

  /** Creates a new RemixImageParams builder. */
  public static Builder builder() {
    return new Builder();
  }

  /** Returns the RunAPI action key for this request. */
  public String action() {
    return "imagen-4/remix-image";
  }

  /** Converts these parameters to the JSON request body shape. */
  public Map<String, Object> toMap() {
    Map<String, Object> raw = new LinkedHashMap<String, Object>();
    raw.put("model", Imagen4ParamUtils.wireValue(model));
    raw.put("prompt", Imagen4ParamUtils.wireValue(prompt));
    raw.put("source_image_urls", Imagen4ParamUtils.wireValue(sourceImageUrls));
    raw.put("callback_url", Imagen4ParamUtils.wireValue(callbackUrl));
    raw.put("aspect_ratio", Imagen4ParamUtils.wireValue(aspectRatio));
    raw.put("output_resolution", Imagen4ParamUtils.wireValue(outputResolution));
    raw.put("output_format", Imagen4ParamUtils.wireValue(outputFormat));
    return Imagen4ParamUtils.compact(raw);
  }



  /** Builder for {@link RemixImageParams}. */
  public static final class Builder {
    private String model;
    private String prompt;
    private List<String> sourceImageUrls;
    private String callbackUrl;
    private String aspectRatio;
    private String outputResolution;
    private String outputFormat;

    private Builder() {}

    /** Sets the model slug using a typed model value. */
    public Builder model(RemixImageModel value) {
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

    /** Sets the source image URLs. */
    public Builder sourceImageUrls(List<String> value) {
      this.sourceImageUrls = value;
      return this;
    }

    /** Sets the webhook URL for task completion notifications. */
    public Builder callbackUrl(String value) {
      this.callbackUrl = Imagen4ParamUtils.requireNonBlank(value, "callbackUrl");
      return this;
    }

    /** Sets the output aspect ratio. */
    public Builder aspectRatio(String value) {
      this.aspectRatio = Imagen4ParamUtils.requireNonBlank(value, "aspectRatio");
      return this;
    }

    /** Sets the output resolution. */
    public Builder outputResolution(String value) {
      this.outputResolution = Imagen4ParamUtils.requireNonBlank(value, "outputResolution");
      return this;
    }

    /** Sets the output format. */
    public Builder outputFormat(String value) {
      this.outputFormat = Imagen4ParamUtils.requireNonBlank(value, "outputFormat");
      return this;
    }

    /** Builds immutable remix image parameters. */
    public RemixImageParams build() {
      return new RemixImageParams(this);
    }
  }
}

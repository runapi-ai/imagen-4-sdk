package ai.runapi.imagen4.types;

import com.fasterxml.jackson.annotation.JsonCreator;

/** Model slug for text to image operations. */
public final class TextToImageModel extends Imagen4Value {
  /** imagen-4 model slug. */
  public static final TextToImageModel IMAGEN_4 = new TextToImageModel("imagen-4");
  /** imagen-4-fast model slug. */
  public static final TextToImageModel IMAGEN_4_FAST = new TextToImageModel("imagen-4-fast");
  /** imagen-4-ultra model slug. */
  public static final TextToImageModel IMAGEN_4_ULTRA = new TextToImageModel("imagen-4-ultra");

  /** Creates a model value from a literal model slug. */
  @JsonCreator
  public TextToImageModel(String value) {
    super(value);
  }
}

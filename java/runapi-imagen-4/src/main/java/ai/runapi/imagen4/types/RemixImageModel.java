package ai.runapi.imagen4.types;

import com.fasterxml.jackson.annotation.JsonCreator;

/** Model slug for remix image operations. */
public final class RemixImageModel extends Imagen4Value {
  /** imagen-4-pro-remix-image model slug. */
  public static final RemixImageModel IMAGEN_4_PRO_REMIX_IMAGE = new RemixImageModel("imagen-4-pro-remix-image");

  /** Creates a model value from a literal model slug. */
  @JsonCreator
  public RemixImageModel(String value) {
    super(value);
  }
}

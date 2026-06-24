package ai.runapi.imagen4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ai.runapi.core.RequestOptions;
import ai.runapi.core.errors.ValidationException;
import ai.runapi.core.http.HttpRequest;
import ai.runapi.core.http.HttpResponse;
import ai.runapi.core.http.HttpTransport;
import ai.runapi.core.http.JsonRequestBody;
import ai.runapi.core.json.Json;
import ai.runapi.imagen4.types.CompletedTextToImageResponse;
import ai.runapi.imagen4.types.TextToImageResponse;
import ai.runapi.imagen4.types.CompletedRemixImageResponse;
import ai.runapi.imagen4.types.CompletedTextToImageResponse;
import ai.runapi.imagen4.types.RemixImageModel;
import ai.runapi.imagen4.types.RemixImageParams;
import ai.runapi.imagen4.types.RemixImageResponse;
import ai.runapi.imagen4.types.TextToImageModel;
import ai.runapi.imagen4.types.TextToImageParams;
import ai.runapi.imagen4.types.TextToImageResponse;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.ByteArrayOutputStream;
import java.time.Duration;
import java.util.Collections;
import org.junit.jupiter.api.Test;

class Imagen4ClientTest {
  @Test
  void builderCreatesClientAndUniversalResources() {
    Imagen4Client client = Imagen4Client.builder().apiKey("sk-test").build();

    assertNotNull(client.textToImage());
    assertNotNull(client.files());
    assertNotNull(client.account());
  }

  @Test
  void openValueClassesSerializeAsScalarStrings() throws Exception {
    String json = Json.mapper().writeValueAsString(new TextToImageModel("imagen-4-fast"));

    assertEquals("\"imagen-4-fast\"", json);
    assertEquals(new TextToImageModel("imagen-4-fast"), Json.mapper().readValue(json, TextToImageModel.class));
  }

  @Test
  void createSendsExpectedRequestShape() throws Exception {
    CapturingTransport transport = new CapturingTransport("{\"id\":\"task_123\",\"status\":\"processing\"}");
    Imagen4Client client = Imagen4Client.builder().apiKey("sk-test").transport(transport).build();

    client.textToImage().create(
        TextToImageParams.builder()
            .model(TextToImageModel.IMAGEN_4_FAST)
            .prompt("A small red cube on a plain white table, studio product photo")
            .aspectRatio("1:1")
            .outputCount(1)
            .build()
    );

    assertEquals("POST", transport.request.getMethod().name());
    assertEquals("/api/v1/imagen_4/text_to_image", transport.request.getPath());
    JsonNode body = bodyJson(transport.request);
    assertNotNull(body);
  }

  @Test
  void getDecodesTaskResponseAndExtraFields() {
    CapturingTransport transport = new CapturingTransport("{\"id\":\"task_456\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}],\"custom\":\"kept\"}");
    Imagen4Client client = Imagen4Client.builder().apiKey("sk-test").transport(transport).build();

    TextToImageResponse response = client.textToImage().get("task_456");

    assertEquals("GET", transport.request.getMethod().name());
    assertEquals("/api/v1/imagen_4/text_to_image/task_456", transport.request.getPath());
    assertEquals("completed", response.getStatus().value());
    assertNotNull(response.getImages());
    assertEquals("kept", response.extraFields().get("custom").asText());
  }

  @Test
  void runPollsUntilCompletedAndKeepsExtraFields() {
    SequenceTransport transport = new SequenceTransport(
        "{\"id\":\"task_789\",\"status\":\"processing\"}",
        "{\"id\":\"task_789\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}],\"custom\":\"kept\"}");
    Imagen4Client client = Imagen4Client.builder().apiKey("sk-test").transport(transport).build();

    CompletedTextToImageResponse response = client.textToImage().run(
        TextToImageParams.builder()
            .model(TextToImageModel.IMAGEN_4_FAST)
            .prompt("A small red cube on a plain white table, studio product photo")
            .aspectRatio("1:1")
            .outputCount(1)
            .build(),
        RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build());

    assertEquals("completed", response.getStatus().value());
    assertNotNull(response.getImages());
    assertEquals("kept", response.extraFields().get("custom").asText());
    assertEquals(2, transport.calls);
  }

  @Test
  void runRejectsCompletedResponseMissingResultField() {
    SequenceTransport transport = new SequenceTransport(
        "{\"id\":\"task_missing\",\"status\":\"processing\"}",
        "{\"id\":\"task_missing\",\"status\":\"completed\"}");
    Imagen4Client client = Imagen4Client.builder().apiKey("sk-test").transport(transport).build();

    assertThrows(
        ValidationException.class,
        () -> client.textToImage().run(
                TextToImageParams.builder()
                    .model(TextToImageModel.IMAGEN_4_FAST)
                    .prompt("A small red cube on a plain white table, studio product photo")
                    .aspectRatio("1:1")
                    .outputCount(1)
                    .build(),
            RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build()));
  }

    @Test
    void coversRemiximageResourceMethods() {
      CapturingTransport createTransport = new CapturingTransport("{\"id\":\"task_remix_image\",\"status\":\"processing\"}");
      Imagen4Client createClient = Imagen4Client.builder().apiKey("sk-test").transport(createTransport).build();
      assertNotNull(createClient.remixImage().create(
              RemixImageParams.builder()
                  .model(RemixImageModel.IMAGEN_4_PRO_REMIX_IMAGE)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .sourceImageUrls(java.util.Arrays.asList("https://cdn.runapi.ai/public/samples/image.jpg"))
                  .build()
      ));

      CapturingTransport createWithOptionsTransport = new CapturingTransport("{\"id\":\"task_remix_image_options\",\"status\":\"processing\"}");
      Imagen4Client createWithOptionsClient = Imagen4Client.builder().apiKey("sk-test").transport(createWithOptionsTransport).build();
      assertNotNull(createWithOptionsClient.remixImage().create(
              RemixImageParams.builder()
                  .model(RemixImageModel.IMAGEN_4_PRO_REMIX_IMAGE)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .sourceImageUrls(java.util.Arrays.asList("https://cdn.runapi.ai/public/samples/image.jpg"))
                  .build(),
          RequestOptions.none()));

      CapturingTransport getTransport = new CapturingTransport("{\"id\":\"task_remix_image\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      Imagen4Client getClient = Imagen4Client.builder().apiKey("sk-test").transport(getTransport).build();
      assertNotNull(getClient.remixImage().get("task_remix_image"));

      CapturingTransport getWithOptionsTransport = new CapturingTransport("{\"id\":\"task_remix_image_options\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      Imagen4Client getWithOptionsClient = Imagen4Client.builder().apiKey("sk-test").transport(getWithOptionsTransport).build();
      assertNotNull(getWithOptionsClient.remixImage().get("task_remix_image_options", RequestOptions.none()));

      SequenceTransport runTransport = new SequenceTransport(
          "{\"id\":\"task_remix_image_run\",\"status\":\"processing\"}",
          "{\"id\":\"task_remix_image_run\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      Imagen4Client runClient = Imagen4Client.builder().apiKey("sk-test").transport(runTransport).build();
      CompletedRemixImageResponse runResponse = runClient.remixImage().run(
              RemixImageParams.builder()
                  .model(RemixImageModel.IMAGEN_4_PRO_REMIX_IMAGE)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .sourceImageUrls(java.util.Arrays.asList("https://cdn.runapi.ai/public/samples/image.jpg"))
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build());
      assertNotNull(runResponse);

      SequenceTransport runWithOptionsTransport = new SequenceTransport(
          "{\"id\":\"task_remix_image_run_options\",\"status\":\"processing\"}",
          "{\"id\":\"task_remix_image_run_options\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      Imagen4Client runWithOptionsClient = Imagen4Client.builder().apiKey("sk-test").transport(runWithOptionsTransport).build();
      assertNotNull(runWithOptionsClient.remixImage().run(
              RemixImageParams.builder()
                  .model(RemixImageModel.IMAGEN_4_PRO_REMIX_IMAGE)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .sourceImageUrls(java.util.Arrays.asList("https://cdn.runapi.ai/public/samples/image.jpg"))
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build()));
    }

    @Test
    void coversTexttoimageResourceMethods() {
      CapturingTransport createTransport = new CapturingTransport("{\"id\":\"task_text_to_image\",\"status\":\"processing\"}");
      Imagen4Client createClient = Imagen4Client.builder().apiKey("sk-test").transport(createTransport).build();
      assertNotNull(createClient.textToImage().create(
              TextToImageParams.builder()
                  .model(TextToImageModel.IMAGEN_4_FAST)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .aspectRatio("1:1")
                  .outputCount(1)
                  .build()
      ));

      CapturingTransport createWithOptionsTransport = new CapturingTransport("{\"id\":\"task_text_to_image_options\",\"status\":\"processing\"}");
      Imagen4Client createWithOptionsClient = Imagen4Client.builder().apiKey("sk-test").transport(createWithOptionsTransport).build();
      assertNotNull(createWithOptionsClient.textToImage().create(
              TextToImageParams.builder()
                  .model(TextToImageModel.IMAGEN_4_FAST)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .aspectRatio("1:1")
                  .outputCount(1)
                  .build(),
          RequestOptions.none()));

      CapturingTransport getTransport = new CapturingTransport("{\"id\":\"task_text_to_image\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      Imagen4Client getClient = Imagen4Client.builder().apiKey("sk-test").transport(getTransport).build();
      assertNotNull(getClient.textToImage().get("task_text_to_image"));

      CapturingTransport getWithOptionsTransport = new CapturingTransport("{\"id\":\"task_text_to_image_options\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      Imagen4Client getWithOptionsClient = Imagen4Client.builder().apiKey("sk-test").transport(getWithOptionsTransport).build();
      assertNotNull(getWithOptionsClient.textToImage().get("task_text_to_image_options", RequestOptions.none()));

      SequenceTransport runTransport = new SequenceTransport(
          "{\"id\":\"task_text_to_image_run\",\"status\":\"processing\"}",
          "{\"id\":\"task_text_to_image_run\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      Imagen4Client runClient = Imagen4Client.builder().apiKey("sk-test").transport(runTransport).build();
      CompletedTextToImageResponse runResponse = runClient.textToImage().run(
              TextToImageParams.builder()
                  .model(TextToImageModel.IMAGEN_4_FAST)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .aspectRatio("1:1")
                  .outputCount(1)
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build());
      assertNotNull(runResponse);

      SequenceTransport runWithOptionsTransport = new SequenceTransport(
          "{\"id\":\"task_text_to_image_run_options\",\"status\":\"processing\"}",
          "{\"id\":\"task_text_to_image_run_options\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      Imagen4Client runWithOptionsClient = Imagen4Client.builder().apiKey("sk-test").transport(runWithOptionsTransport).build();
      assertNotNull(runWithOptionsClient.textToImage().run(
              TextToImageParams.builder()
                  .model(TextToImageModel.IMAGEN_4_FAST)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .aspectRatio("1:1")
                  .outputCount(1)
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build()));
    }

  private static JsonNode bodyJson(HttpRequest request) throws Exception {
    JsonRequestBody body = (JsonRequestBody) request.getBody();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    body.writeTo(out);
    return Json.mapper().readTree(out.toByteArray());
  }

  private static final class CapturingTransport implements HttpTransport {
    private final String body;
    private HttpRequest request;

    private CapturingTransport(String body) {
      this.body = body;
    }

    public HttpResponse send(HttpRequest request) {
      this.request = request;
      return new HttpResponse(200, body, Collections.<String, java.util.List<String>>emptyMap());
    }

    public void close() {}
  }

  private static final class SequenceTransport implements HttpTransport {
    private final String[] responses;
    private int calls;

    private SequenceTransport(String... responses) {
      this.responses = responses;
    }

    public HttpResponse send(HttpRequest request) {
      String response = responses[Math.min(calls, responses.length - 1)];
      calls++;
      return new HttpResponse(200, response, Collections.<String, java.util.List<String>>emptyMap());
    }

    public void close() {}
  }
}

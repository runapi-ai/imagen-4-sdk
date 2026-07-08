    package ai.runapi.imagen4;

    import static org.junit.jupiter.api.Assertions.assertEquals;
    import static org.junit.jupiter.api.Assertions.assertNotNull;
    import static org.junit.jupiter.api.Assumptions.assumeTrue;

        import ai.runapi.core.errors.TaskFailedException;
    import ai.runapi.core.RequestOptions;
    import ai.runapi.core.json.Json;
    import ai.runapi.imagen4.types.CompletedTextToImageResponse;
    import ai.runapi.imagen4.types.CompletedTextToImageResponse;
import ai.runapi.imagen4.types.TextToImageModel;
import ai.runapi.imagen4.types.TextToImageParams;
import ai.runapi.imagen4.types.TextToImageResponse;
    import com.fasterxml.jackson.databind.node.ObjectNode;
    import java.nio.charset.StandardCharsets;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.Paths;
    import java.time.Duration;
    import org.junit.jupiter.api.Test;

    class Imagen4LiveApiSmokeTest {
      @Test
      void primaryResourceRunAgainstLiveRunApi() throws Exception {
        assumeTrue("true".equals(System.getenv("RUNAPI_JAVA_LIVE_IMAGEN_4_SMOKE")));

        String baseUrl = requireEnv("RUNAPI_BASE_URL");
        String apiKey = requireEnv("RUNAPI_API_KEY");
        String callbackUrl = callbackUrl("imagen-4");
        Path outputPath = Paths.get(System.getenv().getOrDefault("RUNAPI_JAVA_LIVE_IMAGEN_4_OUTPUT", "build/live-imagen-4-smoke-result.json"));
        Files.createDirectories(outputPath.getParent());
        try (Imagen4Client client = Imagen4Client.builder().apiKey(apiKey).baseUrl(baseUrl).build()) {
          ObjectNode result = Json.mapper().createObjectNode();
          result.put("action", "imagen-4/text-to-image");
          result.put("result_field", "images");
          result.put("callback_url", callbackUrl);
          try {
      CompletedTextToImageResponse response =
          client.textToImage().run(
              TextToImageParams.builder()
                  .model(TextToImageModel.IMAGEN_4_FAST)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .aspectRatio("1:1")
                  .callbackUrl(callbackUrl)
                  .build(),
              RequestOptions.builder()
                  .pollingInterval(Duration.ofSeconds(10))
                  .pollingMaxWait(Duration.ofMinutes(15))
                  .maxRetries(0)
                  .build());

          assertEquals("completed", response.getStatus().value());
            assertNotNull(response.getImages());
            result.put("id", response.getId());
            result.put("status", response.getStatus().value());
            Files.write(outputPath, Json.mapper().writerWithDefaultPrettyPrinter().writeValueAsString(result).getBytes(StandardCharsets.UTF_8));
          } catch (TaskFailedException failure) {
            result.put("status", "failed");
            result.put("exception", failure.getClass().getSimpleName());
            result.put("message", failure.getMessage());
            Object taskResponse = failure.getTaskResponse();
            if (taskResponse instanceof TextToImageResponse) {
              result.put("id", ((TextToImageResponse) taskResponse).getId());
              result.put("status", ((TextToImageResponse) taskResponse).getStatus().value());
              result.put("error", ((TextToImageResponse) taskResponse).getError());
            }
            Files.write(outputPath, Json.mapper().writerWithDefaultPrettyPrinter().writeValueAsString(result).getBytes(StandardCharsets.UTF_8));
            throw failure;
          } catch (RuntimeException failure) {
            result.put("status", "error");
            result.put("exception", failure.getClass().getSimpleName());
            result.put("message", failure.getMessage());
            Files.write(outputPath, Json.mapper().writerWithDefaultPrettyPrinter().writeValueAsString(result).getBytes(StandardCharsets.UTF_8));
            throw failure;
          }
        }
      }

      private static String callbackUrl(String modelSlug) {
        String base = requireEnv("RUNAPI_CALLBACK_URL");
        String normalized = base.endsWith("/") ? base.substring(0, base.length() - 1) : base;
        return normalized + "/java-live-smoke/" + modelSlug + "/" + System.currentTimeMillis();
      }

      private static String requireEnv(String name) {
        String value = System.getenv(name);
        if (value == null || value.trim().isEmpty()) {
          throw new IllegalStateException(name + " is required");
        }
        return value;
      }
    }

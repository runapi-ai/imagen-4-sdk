package imagen4

import (
	"context"
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/runapi-ai/core-sdk/go/option"
)

func TestTextToImageCreate(t *testing.T) {
	server := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		if r.Method != http.MethodPost || r.URL.Path != textToImagePath {
			t.Fatalf("unexpected request %s %s", r.Method, r.URL.Path)
		}
		var body map[string]any
		if err := json.NewDecoder(r.Body).Decode(&body); err != nil {
			t.Fatal(err)
		}
		if body["model"] != "imagen-4-pro-image-to-image" {
			t.Fatalf("unexpected model %v", body["model"])
		}
		w.Header().Set("Content-Type", "application/json")
		_, _ = w.Write([]byte(`{"id":"task_123","status":"processing"}`))
	}))
	defer server.Close()

	client, err := NewClient(option.WithAPIKey("test"), option.WithBaseURL(server.URL))
	if err != nil {
		t.Fatal(err)
	}
	resp, err := client.TextToImage.Create(context.Background(), TextToImageParams{Model: "imagen-4-pro-image-to-image", Prompt: "Restyle this image", ImageInput: []string{"https://upload.wikimedia.org/wikipedia/commons/a/a9/Example.jpg"}})
	if err != nil {
		t.Fatal(err)
	}
	if resp.ID != "task_123" {
		t.Fatalf("unexpected id %s", resp.ID)
	}
}

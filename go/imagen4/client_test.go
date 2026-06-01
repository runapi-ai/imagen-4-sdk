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
		if body["model"] != "imagen-4-fast" {
			t.Fatalf("unexpected model %v", body["model"])
		}
		if body["output_count"] != float64(2) {
			t.Fatalf("unexpected output_count %v", body["output_count"])
		}
		if _, ok := body["resolution"]; ok {
			t.Fatalf("unexpected provider resolution key %v", body["resolution"])
		}
		w.Header().Set("Content-Type", "application/json")
		_, _ = w.Write([]byte(`{"id":"task_123","status":"processing"}`))
	}))
	defer server.Close()

	client, err := NewClient(option.WithAPIKey("test"), option.WithBaseURL(server.URL))
	if err != nil {
		t.Fatal(err)
	}
	resp, err := client.TextToImage.Create(context.Background(), TextToImageParams{
		Model:       "imagen-4-fast",
		Prompt:      "A warm editorial photo",
		OutputCount: 2,
	})
	if err != nil {
		t.Fatal(err)
	}
	if resp.ID != "task_123" {
		t.Fatalf("unexpected id %s", resp.ID)
	}
}

func TestTextToImageGetDecodesImages(t *testing.T) {
	server := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		if r.Method != http.MethodGet || r.URL.Path != textToImagePath+"/task_123" {
			t.Fatalf("unexpected request %s %s", r.Method, r.URL.Path)
		}
		w.Header().Set("Content-Type", "application/json")
		_, _ = w.Write([]byte(`{"id":"task_123","status":"completed","images":[{"url":"https://file.runapi.ai/generated/image.png"}]}`))
	}))
	defer server.Close()

	client, err := NewClient(option.WithAPIKey("test"), option.WithBaseURL(server.URL))
	if err != nil {
		t.Fatal(err)
	}
	resp, err := client.TextToImage.Get(context.Background(), "task_123")
	if err != nil {
		t.Fatal(err)
	}
	if len(resp.Images) != 1 || resp.Images[0].URL != "https://file.runapi.ai/generated/image.png" {
		t.Fatalf("unexpected images %#v", resp.Images)
	}
}

func TestRemixImageCreate(t *testing.T) {
	server := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		if r.Method != http.MethodPost || r.URL.Path != remixImagePath {
			t.Fatalf("unexpected request %s %s", r.Method, r.URL.Path)
		}
		var body map[string]any
		if err := json.NewDecoder(r.Body).Decode(&body); err != nil {
			t.Fatal(err)
		}
		if body["model"] != "imagen-4-pro-remix-image" {
			t.Fatalf("unexpected model %v", body["model"])
		}
		sourceURLs, ok := body["source_image_urls"].([]any)
		if !ok || len(sourceURLs) != 1 || sourceURLs[0] != "https://upload.wikimedia.org/wikipedia/commons/a/a9/Example.jpg" {
			t.Fatalf("unexpected source_image_urls %v", body["source_image_urls"])
		}
		if _, ok := body["image_input"]; ok {
			t.Fatalf("unexpected provider image_input key %v", body["image_input"])
		}
		if body["output_resolution"] != "2k" {
			t.Fatalf("unexpected output_resolution %v", body["output_resolution"])
		}
		w.Header().Set("Content-Type", "application/json")
		_, _ = w.Write([]byte(`{"id":"task_123","status":"processing"}`))
	}))
	defer server.Close()

	client, err := NewClient(option.WithAPIKey("test"), option.WithBaseURL(server.URL))
	if err != nil {
		t.Fatal(err)
	}
	resp, err := client.RemixImage.Create(context.Background(), RemixImageParams{
		Model:            "imagen-4-pro-remix-image",
		Prompt:           "Restyle this image",
		SourceImageURLs:  []string{"https://upload.wikimedia.org/wikipedia/commons/a/a9/Example.jpg"},
		OutputResolution: "2k",
	})
	if err != nil {
		t.Fatal(err)
	}
	if resp.ID != "task_123" {
		t.Fatalf("unexpected id %s", resp.ID)
	}
}

func TestRemixImageGetDecodesImages(t *testing.T) {
	server := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		if r.Method != http.MethodGet || r.URL.Path != remixImagePath+"/task_456" {
			t.Fatalf("unexpected request %s %s", r.Method, r.URL.Path)
		}
		w.Header().Set("Content-Type", "application/json")
		_, _ = w.Write([]byte(`{"id":"task_456","status":"completed","images":[{"url":"https://file.runapi.ai/remixed/image.png"}]}`))
	}))
	defer server.Close()

	client, err := NewClient(option.WithAPIKey("test"), option.WithBaseURL(server.URL))
	if err != nil {
		t.Fatal(err)
	}
	resp, err := client.RemixImage.Get(context.Background(), "task_456")
	if err != nil {
		t.Fatal(err)
	}
	if len(resp.Images) != 1 || resp.Images[0].URL != "https://file.runapi.ai/remixed/image.png" {
		t.Fatalf("unexpected images %#v", resp.Images)
	}
}

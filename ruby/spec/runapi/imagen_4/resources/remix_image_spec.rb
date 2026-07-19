# frozen_string_literal: true

require "spec_helper"

RSpec.describe RunApi::Imagen4::Resources::RemixImage do
  let(:http) { instance_double(RunApi::Core::HttpClient) }
  let(:remix_image) { described_class.new(http) }
  let(:endpoint) { "/api/v1/imagen_4/remix_image" }

  it "POSTs to the correct endpoint with source_image_urls" do
    params = {
      model: "imagen-4-pro-remix-image",
      prompt: "Restyle this image",
      source_image_urls: ["https://upload.wikimedia.org/wikipedia/commons/a/a9/Example.jpg"],
      output_resolution: "2k"
    }
    expect(http).to receive(:request).with(:post, endpoint, body: params)
      .and_return("id" => "task-1", "status" => "processing")

    result = remix_image.create(**params)
    expect(result).to be_a(RunApi::Imagen4::Types::RemixImageResponse)
    expect(result.id).to eq("task-1")
  end

  it "raises ValidationError when source_image_urls is missing" do
    expect {
      remix_image.create(model: "imagen-4-pro-remix-image", prompt: "test")
    }.to raise_error(RunApi::Core::ValidationError, /source_image_urls is required/)
  end

  it "raises ValidationError for invalid output_resolution" do
    expect {
      remix_image.create(
        model: "imagen-4-pro-remix-image",
        prompt: "test",
        source_image_urls: ["https://cdn.runapi.ai/public/samples/source.jpg"],
        output_resolution: "2K"
      )
    }.to raise_error(RunApi::Core::ValidationError, /output_resolution must be one of: 1k, 2k, 4k/)
  end

  it "GETs completed responses with image objects" do
    expect(http).to receive(:request).with(:get, "#{endpoint}/task-1")
      .and_return("id" => "task-1", "status" => "completed", "images" => [{"url" => "https://file.runapi.ai/remixed/image.png"}])

    result = remix_image.get("task-1")
    expect(result.images.first.url).to eq("https://file.runapi.ai/remixed/image.png")
    expect(result["images"].first["url"]).to eq("https://file.runapi.ai/remixed/image.png")
  end
end

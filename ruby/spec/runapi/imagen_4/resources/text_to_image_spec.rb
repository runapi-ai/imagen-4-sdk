# frozen_string_literal: true

require "spec_helper"

RSpec.describe RunApi::Imagen4::Resources::TextToImage do
  let(:http) { instance_double(RunApi::Core::HttpClient) }
  let(:text_to_image) { described_class.new(http) }
  let(:endpoint) { "/api/v1/imagen_4/text_to_image" }

  it "POSTs to the correct endpoint with required params" do
    params = {
      model: "imagen-4-fast",
      prompt: "A warm editorial photo",
      aspect_ratio: "auto"
    }
    expect(http).to receive(:request).with(:post, endpoint, body: params)
      .and_return("id" => "task-1", "status" => "processing")

    result = text_to_image.create(**params)
    expect(result).to be_a(RunApi::Imagen4::Types::TextToImageResponse)
    expect(result.id).to eq("task-1")
  end

  it "raises ValidationError when model is missing" do
    expect { text_to_image.create(prompt: "test") }
      .to raise_error(RunApi::Core::ValidationError, /model must be one of: imagen-4, imagen-4-fast, imagen-4-ultra/)
  end

  it "GETs completed responses with image objects" do
    expect(http).to receive(:request).with(:get, "#{endpoint}/task-1")
      .and_return("id" => "task-1", "status" => "completed", "images" => [{"url" => "https://file.runapi.ai/generated/image.png"}])

    result = text_to_image.get("task-1")
    expect(result.images.first.url).to eq("https://file.runapi.ai/generated/image.png")
    expect(result["images"].first["url"]).to eq("https://file.runapi.ai/generated/image.png")
  end
end

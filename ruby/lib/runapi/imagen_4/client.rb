# frozen_string_literal: true

module RunApi
  module Imagen4
    # Imagen 4 text-to-image and remix API client.
    #
    # Three text-to-image quality tiers (imagen-4, imagen-4-fast, imagen-4-ultra)
    # and a dedicated remix model for guided image transformation.
    #
    # @example
    #   client = RunApi::Imagen4::Client.new(api_key: "your-api-key")
    #   result = client.text_to_image.run(
    #     model: "imagen-4", prompt: "A photorealistic mountain landscape"
    #   )
    class Client < RunApi::Core::Client
      # @return [Resources::TextToImage] Text-to-image generation across quality tiers.
      attr_reader :text_to_image
      # @return [Resources::RemixImage] Remix existing images with text-guided transformations.
      attr_reader :remix_image

      def initialize(api_key: nil, **options)
        super
        @text_to_image = Resources::TextToImage.new(http)
        @remix_image = Resources::RemixImage.new(http)
      end
    end
  end
end

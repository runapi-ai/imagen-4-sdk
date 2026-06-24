# frozen_string_literal: true

module RunApi
  module Imagen4
    module Resources
      # Imagen 4 text-to-image generation resource.
      # Generate images from text with three quality tiers; imagen-4-fast supports batch output.
      class TextToImage
        include RunApi::Core::ResourceHelpers

        ENDPOINT = "/api/v1/imagen_4/text_to_image"
        RESPONSE_CLASS = Types::TextToImageResponse
        COMPLETED_RESPONSE_CLASS = Types::CompletedTextToImageResponse

        def initialize(http)
          @http = http
        end

        def run(**params)
          task = create(**params)
          poll_until_complete { get(task.id) }
        end

        def create(**params)
          params = compact_params(params)
          validate_params!(params)
          request(:post, ENDPOINT, body: params)
        end

        def get(id)
          request(:get, "#{ENDPOINT}/#{id}")
        end

        private

        def validate_params!(params)
          validate_contract!(CONTRACT["text-to-image"], params)

          raise Core::ValidationError, "prompt is required" unless param(params, :prompt)

          if param(params, :output_count) && param(params, :model) != "imagen-4-fast"
            raise Core::ValidationError, "output_count is only supported for imagen-4-fast"
          end
        end
      end
    end
  end
end

# frozen_string_literal: true

module RunApi
  module Imagen4
    module Resources
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
          raise Core::ValidationError, "model is required" unless param(params, :model)
          raise Core::ValidationError, "prompt is required" unless param(params, :prompt)

          model = param(params, :model)
          raise Core::ValidationError, "Invalid model: #{model}. Must be: #{Types::MODELS.join(", ")}" unless Types::MODELS.include?(model)

          validate_text_params!(params, model)
        end

        def validate_text_params!(params, model)
          validate_optional!(params, :aspect_ratio, Types::TEXT_ASPECT_RATIOS)
          return unless param(params, :output_count)

          raise Core::ValidationError, "output_count is only supported for imagen-4-fast" unless model == "imagen-4-fast"
          validate_optional!(params, :output_count, Types::OUTPUT_COUNTS)
        end
      end
    end
  end
end

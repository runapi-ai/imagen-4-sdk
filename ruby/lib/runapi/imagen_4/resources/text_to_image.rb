# frozen_string_literal: true

module RunApi
  module Imagen4
    module Resources
      class TextToImage
        include RunApi::Core::ResourceHelpers

        ENDPOINT = "/api/v1/imagen_4/text_to_image"
        RESPONSE_CLASS = Types::TextToImageResponse
        COMPLETED_RESPONSE_CLASS = Types::CompletedTextToImageResponse
        TEXT_ONLY_FIELDS = %i[negative_prompt seed num_images].freeze
        PRO_ONLY_FIELDS = %i[image_input resolution output_format].freeze

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

          text_model?(model) ? validate_text_params!(params, model) : validate_pro_params!(params, model)
        end

        def validate_text_params!(params, model)
          reject_present!(params, PRO_ONLY_FIELDS, model)
          validate_optional!(params, :aspect_ratio, Types::TEXT_ASPECT_RATIOS)
          return unless param(params, :num_images)

          raise Core::ValidationError, "num_images is only supported for imagen-4-fast" unless model == "imagen-4-fast"
          validate_optional!(params, :num_images, Types::NUM_IMAGES)
        end

        def validate_pro_params!(params, model)
          reject_present!(params, TEXT_ONLY_FIELDS, model)
          validate_optional!(params, :aspect_ratio, Types::PRO_ASPECT_RATIOS)
          validate_optional!(params, :resolution, Types::RESOLUTIONS)
          validate_optional!(params, :output_format, Types::OUTPUT_FORMATS)
          return unless param(params, :image_input)&.size.to_i > 8

          raise Core::ValidationError, "image_input supports up to 8 images"
        end

        def reject_present!(params, fields, model)
          invalid = fields.find { |field| param(params, field) }
          raise Core::ValidationError, "#{invalid} is not supported for #{model}" if invalid
        end

        def text_model?(model)
          Types::TEXT_MODELS.include?(model)
        end
      end
    end
  end
end

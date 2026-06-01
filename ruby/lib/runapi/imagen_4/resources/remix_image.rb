# frozen_string_literal: true

module RunApi
  module Imagen4
    module Resources
      class RemixImage
        include RunApi::Core::ResourceHelpers

        ENDPOINT = "/api/v1/imagen_4/remix_image"
        RESPONSE_CLASS = Types::RemixImageResponse
        COMPLETED_RESPONSE_CLASS = Types::CompletedRemixImageResponse
        SOURCE_IMAGE_URLS_MAX = 8

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
          model = param(params, :model)
          raise Core::ValidationError, "model is required" unless model
          unless Types::REMIX_MODELS.include?(model)
            raise Core::ValidationError, "Invalid model: #{model}. Must be one of: #{Types::REMIX_MODELS.join(", ")}"
          end
          raise Core::ValidationError, "prompt is required" unless param(params, :prompt)

          validate_source_image_urls!(params)
          validate_optional!(params, :aspect_ratio, Types::PRO_ASPECT_RATIOS)
          validate_optional!(params, :output_resolution, Types::OUTPUT_RESOLUTIONS)
          validate_optional!(params, :output_format, Types::OUTPUT_FORMATS)
        end

        def validate_source_image_urls!(params)
          urls = param(params, :source_image_urls)
          raise Core::ValidationError, "source_image_urls is required" if urls.nil? || (urls.respond_to?(:empty?) && urls.empty?)
          return unless urls.respond_to?(:size) && urls.size > SOURCE_IMAGE_URLS_MAX

          raise Core::ValidationError, "source_image_urls supports up to #{SOURCE_IMAGE_URLS_MAX} images"
        end
      end
    end
  end
end

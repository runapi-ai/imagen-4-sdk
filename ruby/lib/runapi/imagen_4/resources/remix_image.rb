# frozen_string_literal: true

module RunApi
  module Imagen4
    module Resources
      # Imagen 4 image remix resource.
      # Transform source images guided by a text prompt (up to 8 source images).
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
          validate_contract!(CONTRACT["remix-image"], params)

          raise Core::ValidationError, "prompt is required" unless param(params, :prompt)

          urls = param(params, :source_image_urls)
          return unless urls.respond_to?(:size) && urls.size > SOURCE_IMAGE_URLS_MAX

          raise Core::ValidationError, "source_image_urls supports up to #{SOURCE_IMAGE_URLS_MAX} images"
        end
      end
    end
  end
end

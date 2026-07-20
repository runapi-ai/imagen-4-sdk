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

        def initialize(http)
          @http = http
        end

        def run(options: nil, **params)
          task = create(options: options, **params)
          poll_until_complete { get(task.id, options: options) }
        end

        def create(options: nil, **params)
          params = compact_params(params)
          validate_params!(params)
          request(:post, ENDPOINT, body: params, options: options)
        end

        def get(id, options: nil)
          request(:get, "#{ENDPOINT}/#{id}", options: options)
        end

        private

        def validate_params!(params)
          validate_contract!(CONTRACT["remix-image"], params)

          raise Core::ValidationError, "prompt is required" unless param(params, :prompt)
        end
      end
    end
  end
end

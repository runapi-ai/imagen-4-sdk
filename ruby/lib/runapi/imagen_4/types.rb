# frozen_string_literal: true

module RunApi
  module Imagen4
    # Imagen 4 type constants and response models.
    # Text-to-image supports three quality tiers; remix accepts source images
    # with extended aspect ratios, resolution, and format control.
    module Types
      # A generated image with its CDN URL and optional unprocessed origin URL.
      class Image < RunApi::Core::BaseModel
        optional :url, String
        optional :origin_url, String
      end

      class TextToImageResponse < RunApi::Core::TaskResponse
        required :id, String
        optional :status, String, enum: -> { RunApi::Core::TaskResponse::Status::ALL }
        optional :images, [-> { Image }]
        optional :error, String
      end

      class CompletedTextToImageResponse < TextToImageResponse
        required :images, [-> { Image }]
      end

      class RemixImageResponse < TextToImageResponse
      end

      class CompletedRemixImageResponse < CompletedTextToImageResponse
      end
    end
  end
end

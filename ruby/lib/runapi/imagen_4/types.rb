# frozen_string_literal: true

module RunApi
  module Imagen4
    module Types
      TEXT_MODELS = %w[imagen-4 imagen-4-fast imagen-4-ultra].freeze
      REMIX_MODELS = %w[imagen-4-pro-remix-image].freeze
      MODELS = (TEXT_MODELS + REMIX_MODELS).freeze
      TEXT_ASPECT_RATIOS = %w[1:1 16:9 9:16 3:4 4:3].freeze
      PRO_ASPECT_RATIOS = %w[1:1 2:3 3:2 3:4 4:3 4:5 5:4 9:16 16:9 21:9 auto].freeze
      OUTPUT_RESOLUTIONS = %w[1k 2k 4k].freeze
      OUTPUT_FORMATS = %w[png jpg].freeze
      OUTPUT_COUNTS = [1, 2, 3, 4].freeze

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

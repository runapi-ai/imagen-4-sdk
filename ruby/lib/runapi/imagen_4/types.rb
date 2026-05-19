# frozen_string_literal: true

module RunApi
  module Imagen4
    module Types
      TEXT_MODELS = %w[imagen-4 imagen-4-fast imagen-4-ultra].freeze
      MODELS = (TEXT_MODELS + %w[imagen-4-pro-image-to-image]).freeze
      TEXT_ASPECT_RATIOS = %w[1:1 16:9 9:16 3:4 4:3].freeze
      PRO_ASPECT_RATIOS = %w[1:1 2:3 3:2 3:4 4:3 4:5 5:4 9:16 16:9 21:9 auto].freeze
      RESOLUTIONS = %w[1K 2K 4K].freeze
      OUTPUT_FORMATS = %w[png jpg].freeze
      NUM_IMAGES = %w[1 2 3 4].freeze

      class TextToImageResponse < RunApi::Core::TaskResponse
        required :id, String
        optional :status, String, enum: -> { RunApi::Core::TaskResponse::Status::ALL }
        optional :result_urls, [ String ]
        optional :error, String
      end

      class CompletedTextToImageResponse < TextToImageResponse
        required :result_urls, [ String ]
      end
    end
  end
end

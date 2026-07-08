# frozen_string_literal: true

module RunApi
  module Imagen4
    CONTRACT = {
      "remix-image" => {
        "models" => ["imagen-4-pro-remix-image"],
        "fields_by_model" => {
          "imagen-4-pro-remix-image" => {
            "aspect_ratio" => {
              "enum" => ["1:1", "2:3", "3:2", "3:4", "4:3", "4:5", "5:4", "9:16", "16:9", "21:9", "auto"]
            },
            "output_format" => {
              "enum" => ["png", "jpg"]
            },
            "output_resolution" => {
              "enum" => ["1k", "2k", "4k"]
            },
            "source_image_urls" => {
              "required" => true
            }
          }
        }
      },
      "text-to-image" => {
        "models" => ["imagen-4", "imagen-4-fast", "imagen-4-ultra"],
        "fields_by_model" => {
          "imagen-4" => {
            "aspect_ratio" => {
              "enum" => ["1:1", "16:9", "9:16", "3:4", "4:3"]
            },
            "seed" => {
              "type" => "integer"
            }
          },
          "imagen-4-fast" => {
            "aspect_ratio" => {
              "enum" => ["1:1", "16:9", "9:16", "3:4", "4:3", "auto"]
            },
            "seed" => {
              "type" => "integer"
            }
          },
          "imagen-4-ultra" => {
            "aspect_ratio" => {
              "enum" => ["1:1", "16:9", "9:16", "3:4", "4:3"]
            },
            "seed" => {
              "type" => "integer"
            }
          }
        }
      }
    }.freeze
  end
end

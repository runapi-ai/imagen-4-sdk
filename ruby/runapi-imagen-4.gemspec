# frozen_string_literal: true

Dir.chdir(__dir__) do

  Gem::Specification.new do |spec|
    spec.name = "runapi-imagen-4"
    spec.version = "0.2.11"
    spec.metadata["runapi_slug"] = "imagen-4"
    spec.authors = ["RunAPI"]
    spec.email = ["contact@runapi.ai"]
    spec.summary = "Imagen 4 API Ruby SDK for RunAPI"
    spec.description = "The Imagen 4 Ruby SDK is the language-specific package for Imagen 4 on RunAPI. Use this package for image generation, image editing, and creative production workflows when your application needs request bodies, task status lookup, and consistent RunAPI errors in Ruby."
    spec.homepage = "https://runapi.ai/models/imagen-4"
    spec.license = "Apache-2.0"
    spec.required_ruby_version = ">= 3.1.0"
    spec.metadata["homepage_uri"] = "https://runapi.ai/models/imagen-4"
    spec.metadata["documentation_uri"] = "https://github.com/runapi-ai/imagen-4-sdk/blob/main/ruby/README.md"
    spec.metadata["source_code_uri"] = "https://github.com/runapi-ai/imagen-4-sdk"
    spec.metadata["bug_tracker_uri"] = "https://github.com/runapi-ai/imagen-4-sdk/issues"
    spec.metadata["changelog_uri"] = "https://github.com/runapi-ai/imagen-4-sdk/blob/main/CHANGELOG.md"

    spec.files = Dir.glob("lib/**/*") + %w[LICENSE README.md]
    spec.extra_rdoc_files = ["README.md"]
        spec.require_paths = ["lib"]
    spec.add_dependency "runapi-core", "~> 0.4.0"
  end
end

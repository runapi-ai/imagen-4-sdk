# frozen_string_literal: true

Dir.chdir(__dir__) do

  Gem::Specification.new do |spec|
    spec.name = "runapi-imagen-4"
    spec.version = "0.2.3"
    spec.authors = [ "RunAPI" ]
    spec.email = [ "contact@runapi.ai" ]
    spec.summary = "Imagen 4 API SDKs for JavaScript, Ruby, and Go on RunAPI."
    spec.description = "RunAPI Imagen 4 SDK for JavaScript, Ruby, and Go"
    spec.homepage = "https://runapi.ai/models/imagen-4"
    spec.license = "Apache-2.0"
    spec.required_ruby_version = ">= 3.1.0"
    spec.files = Dir.glob("lib/**/*") + %w[LICENSE]
    spec.require_paths = [ "lib" ]
    spec.add_dependency "runapi-core", "~> 0.2.3"
  end
end

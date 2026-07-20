plugins {
  `java-library`
  `maven-publish`
}

extra["runapiSlug"] = "imagen-4"

description = "RunAPI Imagen 4 Java SDK for Imagen 4 workflows."

java {
  withSourcesJar()
  withJavadocJar()
}

dependencies {
  api("ai.runapi:runapi-core:0.2.2")

  testImplementation(platform("org.junit:junit-bom:5.10.3"))
  testImplementation("org.junit.jupiter:junit-jupiter")
}

publishing {
  publications {
    create<MavenPublication>("mavenJava") {
      from(components["java"])
      artifactId = "runapi-imagen-4"
      pom {
        name = "RunAPI Imagen 4 Java SDK"
        description = "RunAPI Imagen 4 Java SDK for Imagen 4 workflows."
        url = "https://runapi.ai/models/imagen-4"
        licenses {
          license {
            name = "Apache License, Version 2.0"
            url = "https://www.apache.org/licenses/LICENSE-2.0"
          }
        }
        developers {
          developer {
            id = "runapi"
            name = "RunAPI"
            email = "contact@runapi.ai"
          }
        }
        scm {
          url = "https://github.com/runapi-ai/imagen-4-sdk"
          connection = "scm:git:https://github.com/runapi-ai/imagen-4-sdk.git"
          developerConnection = "scm:git:ssh://git@github.com/runapi-ai/imagen-4-sdk.git"
        }
      }
    }
  }
}

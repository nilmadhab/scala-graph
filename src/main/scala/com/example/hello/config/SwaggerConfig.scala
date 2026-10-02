package com.example.hello.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.{Contact, Info, License}
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.{Bean, Configuration}

import java.util.Arrays

@Configuration
class SwaggerConfig {

  @Bean
  def openAPI(): OpenAPI = {
    new OpenAPI()
      .info(
        new Info()
          .title("Graph API")
          .description("A REST API for graph operations built with Scala, Spring Boot, and scala-graph library. " +
            "Create graphs, find shortest paths, traverse nodes, and analyze graph properties.")
          .version("1.0.0")
          .contact(
            new Contact()
              .name("API Support")
              .email("support@example.com")
          )
          .license(
            new License()
              .name("Apache 2.0")
              .url("https://www.apache.org/licenses/LICENSE-2.0")
          )
      )
      .servers(Arrays.asList(
        new Server().url("http://localhost:8080").description("Local Development Server")
      ))
  }
}

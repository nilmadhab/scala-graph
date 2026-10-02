package com.example.hello

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class GraphApplication

object GraphApplication {
  def main(args: Array[String]): Unit = {
    SpringApplication.run(classOf[GraphApplication], args: _*)
  }
}

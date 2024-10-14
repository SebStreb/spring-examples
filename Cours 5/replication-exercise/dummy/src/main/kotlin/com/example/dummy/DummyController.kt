package com.example.dummy

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class DummyController(
    val server: ServerProperties
) {

    @GetMapping("/")
    fun hello() = "Hello from ${server.port}"

}
package com.example.dummy

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class DummyController(@Value("\${server.port}") private val port: Int) {

    @GetMapping("/")
    fun hello() = "Hello from $port"

}
package com.example.gateway

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
class GatewayController(private val dummyProxy: DummyProxy, private val nodeProxy: NodeProxy) {

    @GetMapping("/")
    fun hello() = runCatching { dummyProxy.hello() }.getOrElse { throw ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE) }

    @GetMapping("/node")
    fun helloNode() = runCatching { nodeProxy.hello() }.getOrElse { throw ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE) }

}
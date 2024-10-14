package com.example.gateway

import feign.FeignException.FeignClientException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
class GatewayController(private val dummyProxy: DummyProxy) {

    @GetMapping("/")
    fun hello() = try {
        dummyProxy.hello()
    } catch (e: FeignClientException) {
        throw ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE)
    }

}
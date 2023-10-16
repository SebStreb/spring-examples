package com.example.gateway

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.GetMapping

@Repository
@FeignClient("dummy")
interface DummyProxy {

    @GetMapping("/")
    fun hello(): String

}
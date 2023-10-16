package com.example.gateway

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.GetMapping

@Repository
@FeignClient("node-client")
interface NodeProxy {

    @GetMapping("/")
    fun hello(): String

}
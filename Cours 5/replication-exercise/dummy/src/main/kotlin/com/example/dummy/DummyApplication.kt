package com.example.dummy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
class DummyApplication

fun main(args: Array<String>) {
    runApplication<DummyApplication>(*args)
}

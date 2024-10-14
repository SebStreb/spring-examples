package com.example.dummy

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("server")
class ServerProperties {
    lateinit var port: String
}
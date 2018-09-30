package com.demo.shipping

import org.springframework.boot.SpringApplication
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication


@SpringBootApplication
class ShippingApplication

fun main(args: Array<String>) {

    val app = SpringApplication(ShippingApplication::class.java)
    app.webApplicationType = WebApplicationType.REACTIVE
    app.run(*args)

}

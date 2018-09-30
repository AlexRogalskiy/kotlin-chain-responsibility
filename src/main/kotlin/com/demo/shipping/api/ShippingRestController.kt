package com.demo.shipping.api

import com.demo.shipping.core.ShippingService
import com.demo.shipping.domain.Shipment
import com.demo.shipping.domain.Tracking
import mu.KLogging
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class GreetingRestController(private val shippingService: ShippingService) {

    companion object: KLogging()

    @PostMapping("/api/register", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun register(@RequestBody shipment: Shipment){
        logger.info { "registering ${shipment}" }
        shippingService.register(shipment)

    }


    @PutMapping("/api/push", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun push(@RequestBody tracking: Tracking){
        logger.info { "pushing ${tracking}" }
        shippingService.push(tracking)
    }



}

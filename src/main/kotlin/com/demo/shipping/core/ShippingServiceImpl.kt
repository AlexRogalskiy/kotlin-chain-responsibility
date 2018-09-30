package com.demo.shipping.core

import com.demo.shipping.core.rules.TrackingRule
import com.demo.shipping.domain.Reference
import com.demo.shipping.domain.Shipment
import com.demo.shipping.domain.Tracking
import com.demo.shipping.event.EventService

class ShippingServiceImpl(private val eventService: EventService,
                          private val rules:List<TrackingRule> ):ShippingService {

    val nullShipment = Shipment(Reference("DONT_EXIST"))
    val shipments = mutableMapOf<Reference, Shipment>()

    override fun register(shipment: Shipment) {
        shipments[shipment.reference] = shipment
    }

    override fun push(tracking: Tracking) {
        val shipment = shipments.getOrDefault(tracking.reference, nullShipment)
        val event = tracking.handle(shipment, rules).first()
        eventService.send(event)
    }


}

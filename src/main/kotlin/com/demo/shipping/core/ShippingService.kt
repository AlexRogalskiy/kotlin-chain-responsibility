package com.demo.shipping.core

import com.demo.shipping.domain.Shipment
import com.demo.shipping.domain.Tracking

interface ShippingService {

    fun register(shipment: Shipment)
    fun push(tracking: Tracking)

}
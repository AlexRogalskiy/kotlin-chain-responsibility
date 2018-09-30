package com.demo.shipping.event

import com.demo.shipping.domain.Event
import com.demo.shipping.json.toJson

class EventServiceImpl : EventService {
    override fun send(event: Event) {
        println(event.toJson())
    }
}
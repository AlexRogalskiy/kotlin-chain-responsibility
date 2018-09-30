package com.demo.shipping.event

import com.demo.shipping.domain.Event

interface EventService {

    fun send(event: Event)
}
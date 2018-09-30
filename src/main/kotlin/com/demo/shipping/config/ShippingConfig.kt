package com.demo.shipping.config

import com.demo.shipping.core.ShippingService
import com.demo.shipping.core.ShippingServiceImpl
import com.demo.shipping.core.rules.RulesContainer
import com.demo.shipping.core.rules.TrackingRule
import com.demo.shipping.domain.Reference
import com.demo.shipping.event.EventService
import com.demo.shipping.event.EventServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime



@Configuration
class ShippingConfig {

    @Bean
    fun shippingService(eventService: EventService): ShippingService {
        return ShippingServiceImpl(
                eventService,
                listOf<TrackingRule>(
                        RulesContainer.matchesConcilliationRequest,
                        RulesContainer.matchesNotNeededHandler,
                        RulesContainer.matchesIncompleteHandler,
                        RulesContainer.matchesNotFoundHandler,
                        RulesContainer.fallbackHandler))
    }

    @Bean
    fun eventService(): EventService {
        return EventServiceImpl()
    }

    @Bean
    fun referenceConverter():Converter<String, Reference>{
        return Converter { Reference(it)}
    }


}
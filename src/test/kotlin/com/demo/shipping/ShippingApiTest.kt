package com.demo.shipping

import com.demo.shipping.domain.*
import com.demo.shipping.event.EventService
import com.demo.shipping.json.toJson
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.capture
import com.nhaarman.mockito_kotlin.reset
import io.kotlintest.be
import io.kotlintest.should
import io.kotlintest.shouldNot
import io.kotlintest.specs.BehaviorSpec
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


//@RunWith(SpringRunner::class)
@ExtendWith(SpringExtension::class)
@SpringBootTest
@AutoConfigureMockMvc
class ShippingApiTest(@Autowired val mockMvc: MockMvc,
                      @Autowired val eventService: EventService) : BehaviorSpec() {

    @TestConfiguration
    class MyTestConfiguration {

        @Bean
        fun eventService(): EventService {
            return Mockito.mock(EventService::class.java)
        }

    }

    init {
        Given("Given the provided shipment") {

            val shipment = Shipment(
                    Reference("ABCD123456"),
                    arrayOf(
                            Parcel(1f, 10f, 10f, 10f),
                            Parcel(2f, 20f, 20f, 20f)
                    ))

            mockMvc.perform(post("/api/register/")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(shipment.toJson()))
                    .andExpect(status().isOk)

            When("""
                |shipment reference should be equal to tracking reference
                |shipment parcel number should be equal to tracking parcel number
                |shipment total weight should be less than tracking weight
                |tracking status should be DELIVERED (F)
                |""") {

                reset(eventService)

                val tracking = Tracking(
                        Reference("ABCD123456"),
                        Status.DELIVERED,
                        2,
                        30f)

                mockMvc.perform(put("/api/push/")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(tracking.toJson()))
                        .andExpect(status().isOk)


                Then("""
                    |dispatch an application event
                    (status=CONCILLIATION_REQUEST),
                    |and print it into the console
                    |""") {

                    argumentCaptor<Event>().apply {
                        verify(eventService).send(capture())
                        firstValue.reference should be(shipment.reference)
                        firstValue.status should be(Status.CONCILLIATION_REQUEST)
                    }
                }
            }

            When("""
                |shipment reference should be equal to tracking reference.
                |shipment parcel number should be equal to tracking parcel number.
                |shipment total weight should be greater or equal than tracking weight.
                |tracking status should be DELIVERED (E).
                |""") {

                reset(eventService)

                val tracking = Tracking(
                        Reference("ABCD123456"),
                        Status.DELIVERED,
                        2,
                        2f)

                mockMvc.perform(put("/api/push/")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(tracking.toJson()))
                        .andExpect(status().isOk)


                Then("""
                    |dispatch an application event
                    |(status=NOT_NEEDED),
                    |and print it into the console
                    """) {

                    argumentCaptor<Event>().apply {
                        verify(eventService).send(capture())
                        firstValue.reference should be(shipment.reference)
                        firstValue.status should be(Status.NOT_NEEDED)
                    }

                }
            }

            When("""
                |shipment reference should be equal to tracking reference.
                |tracking status is not DELIVERED (D).
                |""") {

                reset(eventService)

                val tracking = Tracking(
                        Reference("ABCD123456"),
                        Status.WAITING_IN_HUB,
                        2,
                        30f)

                mockMvc.perform(put("/api/push/")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(tracking.toJson()))
                        .andExpect(status().isOk)


                Then("""
                    |dispatch an application event
                    |(status=INCOMPLETE),
                    |and print it into the console
                    """) {

                    argumentCaptor<Event>().apply {
                        verify(eventService).send(capture())
                        firstValue.reference should be(shipment.reference)
                        firstValue.status should be(Status.INCOMPLETE)
                    }

                }
            }

            When("""
                |shipment reference should be equal to tracking reference.
                |any other tracking field is null(?).
                |""") {

                reset(eventService)

                val tracking = Tracking(Reference("ABCD123456"))

                mockMvc.perform(put("/api/push/")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(tracking.toJson()))
                        .andExpect(status().isOk)


                Then("""
                    |dispatch an application event
                    |(status=INCOMPLETE),
                    |and print it into the console
                    """) {

                    argumentCaptor<Event>().apply {
                        verify(eventService).send(capture())
                        firstValue.reference should be(shipment.reference)
                        firstValue.status should be(Status.INCOMPLETE)
                    }

                }
            }

            When("""
                |tracking reference is not equal to shipment reference.
                |""") {

                reset(eventService)

                val tracking = Tracking(Reference("BOGUS_ID"))

                mockMvc.perform(put("/api/push/")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(tracking.toJson()))
                        .andExpect(status().isOk)


                Then("""
                    |dispatch an application event
                    |(status=NOT_FOUND),
                    |and print it into the console
                    """) {

                    argumentCaptor<Event>().apply {
                        verify(eventService).send(capture())
                        firstValue.reference shouldNot be(shipment.reference)
                        firstValue.status should be(Status.NOT_FOUND)
                    }

                }
            }

            When("""
                |shipment reference should be equal to tracking reference.
                |No other state matches (H).
                |""") {

                reset(eventService)

                val tracking = Tracking(
                        Reference("ABCD123456"),
                        weight = 30f,
                        status = Status.DELIVERED)

                mockMvc.perform(put("/api/push/")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(tracking.toJson()))
                        .andExpect(status().isOk)


                Then("""
                    |dispatch an application event
                    |(status=UNKNOWN),
                    |and print it into the console
                    """) {

                    argumentCaptor<Event>().apply {
                        verify(eventService).send(capture())
                        firstValue.reference should be(shipment.reference)
                        firstValue.status should be(Status.UNKNOWN)
                    }

                }
            }



        }

    }

}


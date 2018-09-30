package com.demo.shipping.domain

import com.demo.shipping.core.rules.TrackingRule
import com.demo.shipping.json.ReferenceJsonDeserializer
import com.demo.shipping.json.ReferenceJsonSerializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonDeserialize(using = ReferenceJsonDeserializer::class)
@JsonSerialize(using = ReferenceJsonSerializer::class)
data class Reference(val id:String){}


data class Shipment(val reference: Reference, val parcels: Array<Parcel>?=null){

    fun parcels():Int = parcels?.size?:0
    val weight:Float

    init {
        weight = parcels?.map { it.weight }?.fold(0f){acc, fl ->  acc+fl}?:0f
    }

    fun handle(tracking: Tracking):List<Event>{
        return listOf()
    }
}

data class Event(val reference: Reference,
                 val status: Status)


data class Parcel(val weight: Float,
                  val width: Float,
                  val height: Float,
                  val lenght: Float)


enum class Status {
    WAITING_IN_HUB,
    DELIVERED,
    CONCILLIATION_REQUEST,
    NOT_NEEDED,
    INCOMPLETE,
    NOT_FOUND,
    UNKNOWN
}
data class Tracking(val reference: Reference,
                    val status: Status?=null,
                    val parcels: Int?=null,
                    val weight: Float?=null) {



    fun handle(shipment: Shipment, rules: List<TrackingRule>):List<Event> {
        return rules.fold(listOf()) { list, rule ->
            val (eventStatus, matcher) = rule
            if (this.matcher(shipment))
                list + Event(reference, eventStatus)
            else list
        }
    }
    fun isDelivered() = status == Status.DELIVERED

    fun empty():Boolean {
        return status == null &&
                parcels == null &&
                weight == null;
    }
}
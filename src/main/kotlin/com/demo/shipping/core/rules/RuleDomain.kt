package com.demo.shipping.core.rules

import com.demo.shipping.domain.Shipment
import com.demo.shipping.domain.Status
import com.demo.shipping.domain.Tracking


typealias ShipmentPredicate = Tracking.(Shipment) -> Boolean

data class TrackingRule(val status: Status, val matcher: ShipmentPredicate) {

}
object RulesContainer {

    val matchesConcilliationRequest = TrackingRule(Status.CONCILLIATION_REQUEST)
    { shipment ->
        matchesReference(shipment) &&
                matchesParcels(shipment) &&
                moreOrEqWeight(shipment) &&
                isDelivered()
    }

    val matchesNotNeededHandler = TrackingRule(Status.NOT_NEEDED)
    { shipment ->
        matchesReference(shipment) &&
                matchesParcels(shipment) &&
                lessWeight(shipment) &&
                isDelivered()
    }
    val matchesIncompleteHandler = TrackingRule(Status.INCOMPLETE)
    { shipment ->
        matchesReference(shipment) && (!isDelivered() || empty())
    }


    val matchesNotFoundHandler = TrackingRule(Status.NOT_FOUND) { shipment ->
        !matchesReference(shipment)
    }

    val fallbackHandler = TrackingRule(Status.UNKNOWN) { shipment ->
        true
    }



    private fun Tracking.matchesReference(shipment: Shipment) =
            reference == shipment.reference


    private fun Tracking.matchesParcels(shipment: Shipment) =
            parcels == shipment.parcels()

    private fun Tracking.lessWeight(shipment: Shipment): Boolean {
        return weight != null &&
                weight < shipment.weight
    }

    private fun Tracking.moreOrEqWeight(shipment: Shipment): Boolean {
        return weight != null &&
                weight >= shipment.weight
    }


}


package com.dxfeed.perftestapp.tools

data class Metrics(
    val rateOfEvent: Double,
    val rateOfListeners: Double,
    val numberOfEventsInCall: Double,

    val currentTime: Long
)

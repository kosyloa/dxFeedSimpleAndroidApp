package com.dxfeed.perftestapp.tools

data class Metrics(
    val rateOfEvent: Double,
    val rateOfListeners: Double = 0.0,
    val numberOfEventsInCall: Double = 0.0,

    val currentTime: Long
)

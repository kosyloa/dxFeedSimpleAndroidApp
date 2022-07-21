package com.dxfeed.dxfeedsimpleandroidapp.tools

import com.devexperts.logging.Logging
import java.util.*
import java.util.concurrent.atomic.AtomicLong
import kotlin.concurrent.timer

class Speedometer(private val period: Long, private val uiLogger: UiLogger) {
    private val logger = Logging.getLogging(Speedometer::class.java)
    private var counter: AtomicLong = AtomicLong(0)
    private var lastCount: AtomicLong = AtomicLong(0)
    private var countingTimer : Timer? = Timer()

    fun start() {
        countingTimer = timer("CountingTimer", false, 0L, period, action = {
            val eventsPerSecond = (counter.get() - lastCount.get()).toDouble() / period.toDouble() * 1000

            logger.info("${String.format("%.3f", eventsPerSecond)} events/s")
            uiLogger.log("UI Speedometer: ${String.format("%.3f", eventsPerSecond)} events/s")

            lastCount.set(counter.get())
        })
    }

    fun addEvent() {
        counter.incrementAndGet()
    }

    fun addEvents(number: Long) {
        counter.addAndGet(number)
    }
}
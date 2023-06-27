package com.dxfeed.perftestapp.tools

import com.devexperts.logging.Logging
import com.dxfeed.event.market.MarketEvent
import com.dxfeed.event.market.Quote
import com.dxfeed.event.market.TimeAndSale
import com.dxfeed.event.market.Trade
import com.dxfeed.event.market.TradeETH
import java.time.LocalTime

import java.util.Timer
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.concurrent.timer
import kotlin.math.pow
import kotlin.math.sqrt
import java.time.Duration
import java.util.concurrent.atomic.AtomicLong

class Speedometer(private val period: Long, private val handler: (Metrics) -> Unit) {
    private var countingTimer : Timer? = Timer()
    private val logger = Logging.getLogging(Speedometer::class.java)
    private var startDate: LocalTime? = null

    private var lastCount: AtomicLong = AtomicLong(0)
    private var counter: AtomicLong = AtomicLong(0)

    private var listenerCounter: AtomicLong = AtomicLong(0)
    private var lastListenerCounter: AtomicLong = AtomicLong(0)

    fun start() {

        countingTimer = timer("CountingTimer", false, 0L, period, action = {
            if (startDate == null) {
                startDate = LocalTime.now()
            }
            val dur = Duration.between(startDate, LocalTime.now())
            val eventsPerSecond = (counter.get() - lastCount.get()).toDouble() / period.toDouble() * 1000
            val listenersPerSeconds = (listenerCounter.get() - lastListenerCounter.get()).toDouble() / period.toDouble() * 1000
            lastCount.set(counter.get())
            lastListenerCounter.set(listenerCounter.get())
            val metrics = Metrics(
                rateOfEvent = eventsPerSecond,
                rateOfListeners = listenersPerSeconds,
                numberOfEventsInCall = eventsPerSecond / listenersPerSeconds,
                currentTime = dur.toMillis())
            handler(metrics)
        })
    }
    fun cleanTime() {
        startDate = null
    }
    fun update(size: Int) {
        counter.addAndGet(size.toLong())
        listenerCounter.addAndGet(1)
    }

    companion object {
        private fun calculatePercentile(values: List<Double>, excelPercentile: Double): Double {
            if (values.isEmpty()) {
                return 0.0
            }
            val sortedValues = values.sorted()
            val N = sortedValues.size
            var n = ((N - 1) * excelPercentile) + 1
            if (n == 1.0) {
                return sortedValues.first()
            }
            if (n == N.toDouble()) {
                return  sortedValues.last()
            }
            val k = n.toInt()
            var d = n - k
            return sortedValues[k-1] + (d * (sortedValues[k] - sortedValues[k-1]))
        }

        private fun calculateStdDev(values: List<Double>): Double {
            var stdDev = 0.0
            var count = values.size
            if (count <= 1) {
                return  stdDev
            }
            count -= 1
            val avg = values.average()
            val sum = values.sumOf {
                (it - avg).pow(2)
            }
            stdDev = sqrt(sum / count)
            return stdDev
        }

        private fun calculateStdErr(values: List<Double>, stdDev: Double): Double {
            val count = values.size.toDouble()
            return stdDev / sqrt(count)
        }
    }


}
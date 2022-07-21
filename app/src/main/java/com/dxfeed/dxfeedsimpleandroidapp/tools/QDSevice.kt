package com.dxfeed.dxfeedsimpleandroidapp.tools

import com.devexperts.logging.Logging
import com.dxfeed.api.DXEndpoint
import com.dxfeed.event.market.TimeAndSale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class QDService(private val speedometer: Speedometer, private val uiLogger: UiLogger) {
    private val logger = Logging.getLogging(QDService::class.java)
    private val executorService: ExecutorService = Executors.newFixedThreadPool(1)

    fun testTnsSubscription(address: String, symbols: List<String>, timeout: Long) {
        val calculatedTimout = if (timeout == 0L) 1000000 else timeout

        logger.info("TnsSub: Connecting")
        uiLogger.log("UI QDService: TnsSub: Connecting")

        executorService.execute {
            DXEndpoint.newBuilder()
                .build()
                .connect(address).use { endpoint ->
                    endpoint.feed.createTimeSeriesSubscription(TimeAndSale::class.java).use { sub ->
                        sub.fromTime = 0L
                        sub.addEventListener { items ->
                            speedometer.addEvents(items.size.toLong())
                        }
                        sub.addSymbols(symbols)

                        Thread.sleep(calculatedTimout * 1000)

                        logger.info("TnsSub: Disconnecting")
                        uiLogger.log("UI QDService: TnsSub: Disconnecting")
                    }
                }
        }
    }
}
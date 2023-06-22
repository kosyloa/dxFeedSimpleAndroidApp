package com.dxfeed.latencytestapp

import com.devexperts.logging.Logging
import com.dxfeed.api.DXEndpoint
import com.dxfeed.event.market.Profile
import com.dxfeed.event.market.Quote
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class QDService() {

    val state: DXEndpoint.State
        get() = endpoint?.state ?: DXEndpoint.State.NOT_CONNECTED

    private var endpoint: DXEndpoint? = null

    private val logger = Logging.getLogging(QDService::class.java)
    private val executorService: ExecutorService = Executors.newFixedThreadPool(1)
    fun connect(address: String,
                symbols: List<String>,
                connectionHandler: (DXEndpoint.State) -> Unit,
                eventsHandler: (List<Any>) -> Unit){
        System.setProperty("com.devexperts.connector.proto.heartbeatTimeout", "10s")

        executorService.execute {

            endpoint = DXEndpoint.newBuilder()
                .withProperty("dxfeed.aggregationPeriod", "1")
                .build()

            endpoint?.addStateChangeListener {
                connectionHandler(it.newValue as DXEndpoint.State)
            }
            endpoint?.connect(address)

            val quoteSubscription = endpoint?.feed?.createSubscription(Quote::class.java)
            quoteSubscription?.addEventListener {
                eventsHandler(it)
            }
            quoteSubscription?.addSymbols(symbols)
        }
    }
    fun disconnect() {
        executorService.execute {
            endpoint?.disconnect()
        }
    }
}
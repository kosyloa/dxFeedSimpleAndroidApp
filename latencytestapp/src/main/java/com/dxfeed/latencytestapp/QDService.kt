package com.dxfeed.latencytestapp

import com.devexperts.logging.Logging
import com.dxfeed.api.DXEndpoint
import com.dxfeed.event.market.Profile
import com.dxfeed.event.market.Quote
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class QDService() {
    private val logger = Logging.getLogging(QDService::class.java)
    private val executorService: ExecutorService = Executors.newFixedThreadPool(1)
    private fun DXEndpoint.State.convertConnectionState(): String {
        return when (this) {
            DXEndpoint.State.CONNECTING -> "Connecting \uD83D\uDFE0"
            DXEndpoint.State.CONNECTED-> "Connected \uD83D\uDFE2"
            DXEndpoint.State.CLOSED -> "Closed \uD83D\uDD34"
            else -> {
                "Not connected \uD83D\uDD34"
            }
        }
    }
    fun subscribe(address: String,
                  symbols: List<String>,
                  connectionHandler: (String) -> Unit,
                  eventsHandler: (List<Any>) -> Unit){
        System.setProperty("com.devexperts.connector.proto.heartbeatTimeout", "10s")

        executorService.execute {

            val endpoint = DXEndpoint.newBuilder()
                .withProperty("dxfeed.aggregationPeriod", "1")
                .build()

            endpoint.addStateChangeListener {
                val connectionState = (it.newValue as DXEndpoint.State)?.convertConnectionState()
                connectionHandler(connectionState)
            }
            endpoint.connect(address)

            val quoteSubscription = endpoint.feed.createSubscription(Quote::class.java)
            quoteSubscription.addEventListener {
                eventsHandler(it)
            }
            quoteSubscription.addSymbols(symbols)
            val profileSubscription = endpoint.feed.createSubscription(Profile::class.java)
            profileSubscription.addEventListener {
                eventsHandler(it)
            }
            profileSubscription.addSymbols(symbols)
        }
    }
}
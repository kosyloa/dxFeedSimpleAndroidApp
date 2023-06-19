package com.dxfeed.quotetableapp

import android.os.Handler
import android.os.Looper
import com.devexperts.logging.Logging
import com.dxfeed.api.DXEndpoint
import com.dxfeed.event.market.Profile
import com.dxfeed.event.market.Quote
import com.dxfeed.event.market.TimeAndSale
import org.xml.sax.helpers.ParserAdapter
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class QDService(private val diagnostic: Diagnostic) {
    private val logger = Logging.getLogging(QDService::class.java)
    private val executorService: ExecutorService = Executors.newFixedThreadPool(1)

    fun subscribe(address: String, symbols: List<String>, adapter: CustomAdapter) {
        logger.info("StreamTnsSub: Connecting")

        executorService.execute {
            val endpoint = DXEndpoint.newBuilder()
                .withProperty("dxfeed.aggregationPeriod", "1")
                .build().connect(address)
            val quoteSubscription = endpoint.feed.createSubscription(Quote::class.java)
            quoteSubscription.addEventListener {
                logger.info("received events $it")
                it.forEach { quote ->
                    adapter.update(quote)
                }
                Handler(Looper.getMainLooper()).post {
                    adapter.notifyDataSetChanged()
                }
            }
            quoteSubscription.addSymbols(symbols)
            val profileSubscription = endpoint.feed.createSubscription(Profile::class.java)
            profileSubscription.addEventListener {
                logger.info("received profiles $it")
                it.forEach { profile ->
                    adapter.update(profile)
                }
            }
            profileSubscription.addSymbols(symbols)
        }
    }
}
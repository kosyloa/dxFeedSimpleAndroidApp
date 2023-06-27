package com.dxfeed.quotetableapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dxfeed.event.market.MarketEvent
import com.dxfeed.event.market.Profile
import com.dxfeed.event.market.Quote
import com.dxfeed.quotetableapp.adapters.QuoteAdapter
import com.dxfeed.quotetableapp.extensions.stringValue
import com.dxfeed.quotetableapp.tools.QDService


class MainActivity : AppCompatActivity() {
//    private var quoteTableLayout = findViewById<TableLayout>(R.id.quoteTableLayout)
    private val symbols = listOf(
    "AAPL",
    "IBM",
    "MSFT",
    "EUR/CAD",
    "ETH/USD:GDAX",
    "GOOG",
    "BAC",
    "CSCO",
    "ABCE",
    "INTC",
    "PFE"
    )

    private val eventTypes = listOf(
        Quote::class.java,
        Profile::class.java
    ) as List<Class<out MarketEvent>>

    private val adapter = QuoteAdapter(symbols, this)
    private val service = QDService()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view);
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        service.connect("demo.dxfeed.com:7300",
            symbols = symbols,
            eventTypes = eventTypes,
            connectionHandler = {
                Handler(Looper.getMainLooper()).post {
                    val connectionTextView = findViewById<TextView>(R.id.connectionTextView);
                    connectionTextView.text = it.stringValue(this)
                }
            },
            eventsHandler = { events ->
                events.forEach {
                    when(it) {
                        is Profile -> adapter.update(it)
                        is Quote -> adapter.update(it)
                    }
                }
                Handler(Looper.getMainLooper()).post {
                    adapter.notifyDataSetChanged()
                }
            })
    }

}
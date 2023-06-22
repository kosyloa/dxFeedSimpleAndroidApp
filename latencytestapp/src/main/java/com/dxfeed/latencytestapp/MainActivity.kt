package com.dxfeed.latencytestapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dxfeed.api.DXEndpoint
import com.dxfeed.event.market.Profile
import com.dxfeed.event.market.Quote

class MainActivity : AppCompatActivity() {
//    private var quoteTableLayout = findViewById<TableLayout>(R.id.quoteTableLayout)
    private val symbols = listOf(
    "ETH/USD:GDAX"
    )

    private val adapter = QuoteAdapter(symbols)
    private val service = QDService()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view);
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        findViewById<TextView>(R.id.connectionTextView).text = convertConnectionState(DXEndpoint.State.NOT_CONNECTED)

        findViewById<Button>(R.id.connect_button)
            .setOnClickListener {
                if (service.state == DXEndpoint.State.CONNECTED) {
                    service.disconnect()
                } else {
                    service.connect("mddqa.in.devexperts.com:7400", symbols, connectionHandler = {
                        Handler(Looper.getMainLooper()).post {
                            val connectionTextView = findViewById<TextView>(R.id.connectionTextView);
                            connectionTextView.text = convertConnectionState(it)
                            findViewById<Button>(R.id.connect_button).text  =
                                if (it == DXEndpoint.State.CONNECTED) getString(R.string.disconnect)
                                else getString(R.string.connect)
                        }
                    }, eventsHandler = { events ->
                        events.forEach {
                            when(it) {
                                is Quote -> adapter.update(it)

                            }
                        }
                        Handler(Looper.getMainLooper()).post {
                            adapter.notifyDataSetChanged()
                        }
                    })
                }
            }
    }

    private fun convertConnectionState(state: DXEndpoint.State): String {
        return when (state) {
            DXEndpoint.State.CONNECTING -> getString(R.string.state_connecting)
            DXEndpoint.State.CONNECTED-> getString(R.string.state_connected)
            DXEndpoint.State.CLOSED -> getString(R.string.state_closed)
            else -> {
                getString(R.string.state_other)
            }
        }
    }



}
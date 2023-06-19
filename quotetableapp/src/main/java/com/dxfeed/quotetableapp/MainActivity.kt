package com.dxfeed.quotetableapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


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
    "PFE",
    "CSCO1",
    "ABCE1",
    "INTC2",
    "PFE3"
    )
    private val adapter = CustomAdapter(symbols)
    private val diagnostic = Diagnostic()
    private val service = QDService(diagnostic)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        service.subscribe("demo.dxfeed.com:7300", symbols, adapter)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view);

        recyclerView.setLayoutManager(LinearLayoutManager(this))
        recyclerView.setAdapter(adapter)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

}
package com.dxfeed.dxfeedsimpleandroidapp.tools

import android.os.Handler
import android.os.Looper
import android.text.format.DateFormat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dxfeed.dxfeedsimpleandroidapp.R
import com.dxfeed.dxfeedsimpleandroidapp.adapters.LogViewAdapter
import java.util.*

class UiLogger(logSize: Int) {
    val adapter = LogViewAdapter(mutableListOf(), logSize)
    private lateinit var logView: RecyclerView

    fun setView(logView: RecyclerView) {
        this.logView = logView
    }

    fun log(message: String) {
        Handler(Looper.getMainLooper()).post {
            adapter.add("${DateFormat.format("HH:mm:ss", Date())} > $message")
            logView.smoothScrollToPosition(0);
        }
    }
}
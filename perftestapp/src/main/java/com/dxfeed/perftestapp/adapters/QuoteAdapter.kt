package com.dxfeed.perftestapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.devexperts.logging.Logging
import com.dxfeed.perftestapp.tools.Metrics
import com.dxfeed.perftestapp.R
import com.dxfeed.perftestapp.extensions.*


class QuoteAdapter(private val mList: List<String>) : RecyclerView.Adapter<QuoteAdapter.ViewHolder>() {
    private val logger = Logging.getLogging(QuoteAdapter::class.java)
    private val runningTime = "Running time"
    private val rateEvents = "Rate of events (avg), events/s"
    private val rateListeners = "Rate of listener calls, calls/s"
    private val eventsPerCall = "Number of events in call (avg), events"

    private val dataSource = linkedMapOf<String, String>(
        rateEvents to "",
        rateListeners to "",
        eventsPerCall to "",
        runningTime to "")
    fun reload(metrics: Metrics) {
        dataSource[rateEvents] = metrics.rateOfEvent.format(0)
        dataSource[rateListeners] = metrics.rateOfListeners.format(0)
        dataSource[eventsPerCall] = if (metrics.numberOfEventsInCall.isNaN()) "0.0" else metrics.numberOfEventsInCall.format(0)
        dataSource[runningTime] = metrics.currentTime.toTimeFormat()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val key = dataSource.keys.elementAt(position)
        val value = dataSource[key]
        holder.metricTextView.text = key
        holder.valueTextView.text = value
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val metricTextView: TextView = itemView.findViewById(R.id.metricTextView)
        val valueTextView: TextView = itemView.findViewById(R.id.valueTextView)
    }


}

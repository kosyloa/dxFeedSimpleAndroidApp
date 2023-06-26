package com.dxfeed.latencytestapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.devexperts.logging.Logging

fun Long.toTimeFormat(): String {
    val inSec = this / 1000
    val millis = this % 1000
    val seconds = inSec  % 60
    val minutes = (inSec / 60) % 60
    val hours = inSec / 3600
    return "${"%02d".format(hours)}:${"%02d".format(minutes)}:${"%02d".format(seconds)}.${"%03d".format(millis)}"
}

private const val rateKey = "Rate of unique symbols per interval"
private const val min = "Min, ms"
private const val max = "Max, ms"
private const val percentile = "99th percentile, ms"
private const val mean = "Mean, ms"

private const val stdDev = "StdDev, ms"

private const val error = "Error, ms"

private const val sampleSize = "Sample size (N), events"

private const val measurementInterval = "Measurement interval, s"

private const val runningTime = "Running time"

private const val rateEvents = "Rate of events (avg), events/s"

class QuoteAdapter(private val mList: List<String>) : RecyclerView.Adapter<QuoteAdapter.ViewHolder>() {
    private val logger = Logging.getLogging(QuoteAdapter::class.java)

    private val dataSource = linkedMapOf<String, String>(
        rateEvents to "",
        rateKey to "",
        min to "",
        max to "",
        percentile to "",
        mean to "",
        stdDev to "",
        error to "",
        sampleSize to "",
        measurementInterval to "",
        runningTime to "")
    fun reload(metrics: Metrics) {
        println(metrics)
        dataSource[rateEvents] = metrics.rateOfEvent.toString()
        dataSource[rateKey] = metrics.rateOfSymbols.toString()
        dataSource[min] = metrics.min.toString()
        dataSource[max] = metrics.max.toString()
        dataSource[percentile] = metrics.percentile.toString()
        dataSource[mean] = metrics.mean.toString()
        dataSource[stdDev] = metrics.stdDev.toString()
        dataSource[error] = metrics.error.toString()
        dataSource[sampleSize] = metrics.sampleSize.toString()
        dataSource[measurementInterval] = metrics.measureInterval.toString()
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

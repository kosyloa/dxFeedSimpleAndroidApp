package com.dxfeed.dxfeedsimpleandroidapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dxfeed.dxfeedsimpleandroidapp.R

class LogViewAdapter(private val logItems : MutableList<String>, private val logSize: Int) : RecyclerView.Adapter<LogViewAdapter.Holder>() {
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView : TextView = itemView.findViewById(R.id.log_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.log_item, parent, false)

        return Holder(itemView)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.textView.text = logItems[position]
    }

    override fun getItemCount(): Int = logItems.size

    fun add(item: String) {
        if (logItems.size == logSize) {
            logItems.removeAt(logSize - 1)
            notifyItemRemoved(logSize - 1)
        }

        logItems.add(0, item)
        notifyItemInserted(0)
    }
}
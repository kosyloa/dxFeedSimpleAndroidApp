package com.dxfeed.latencytestapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dxfeed.event.market.Profile
import com.dxfeed.event.market.Quote
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap


class QuoteAdapter(private val mList: List<String>) : RecyclerView.Adapter<QuoteAdapter.ViewHolder>() {
    private var greenColor: Int? = null
    private var redColor: Int? = null
    private var defaultPriceColor: Int? = null
    private var symbols = ConcurrentHashMap<String, String>()
    private var deltas = Collections.synchronizedList(listOf<Double>())
    private val dataSource: Map<String, QuoteModel> = mList.associateWith {
        QuoteModel(it)
    }

    fun update(quote: Quote) {
        print(deltas)
        synchronized(deltas) {

            println(deltas)
        }
        dataSource[quote.eventSymbol]?.update(quote)
    }

    fun update(profile: Profile) {
        dataSource[profile.eventSymbol]?.update(profile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val symbol = mList[position]
        val quote = dataSource[symbol]
        if (greenColor == null) {
            greenColor = holder.itemView.context.resources.getColor(R.color.green, null)
        }
        if (redColor == null) {
            redColor = holder.itemView.context.resources.getColor(R.color.red, null)
        }
        if (defaultPriceColor == null) {
            defaultPriceColor = holder.itemView.context.resources.getColor(R.color.priceBackground, null)
        }

        holder.textView.text = symbol + "\n" + quote?.description
        holder.askButton.text = quote?.ask
        holder.bidButton.text = quote?.bid
        holder.askButton.setBackgroundColor(priceColor(quote?.increaseAsk))
        holder.bidButton.setBackgroundColor(priceColor(quote?.increasedBid))
    }

    private fun priceColor(increased: Boolean?): Int {
        increased?.let {
            if (it) {
                return greenColor!!
            } else {
                return  redColor!!
            }
        } ?: return defaultPriceColor!!
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textView: TextView = itemView.findViewById(R.id.symboltextView)
        val askButton: Button = itemView.findViewById(R.id.askButton)
        val bidButton: Button = itemView.findViewById(R.id.bidButton)
    }


}

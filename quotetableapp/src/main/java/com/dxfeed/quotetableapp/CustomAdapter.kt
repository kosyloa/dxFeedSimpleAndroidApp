package com.dxfeed.quotetableapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dxfeed.event.market.Profile
import com.dxfeed.event.market.Quote

class CustomAdapter(private val mList: List<String>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    private var greenColor: Int? = null
    private var redColor: Int? = null
    private var defaultPriceColor: Int? = null

    private val dataSource: Map<String, QuoteModel> = mList.associateWith {
        QuoteModel(it)
    }

    fun update(quote: Quote) {
        dataSource[quote.eventSymbol]?.let {
            it.update(quote)
        }
    }

    fun update(profile: Profile) {
        dataSource[profile.eventSymbol]?.let {
            it.update(profile)
        }
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
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
        holder.askTextview.text = quote?.ask
        holder.bidTextView.text = quote?.bid
        holder.askTextview.setBackgroundColor(priceColor(quote?.increaseAsk))
        holder.bidTextView.setBackgroundColor(priceColor(quote?.increasedBid))
    }

    private fun priceColor(increased: Boolean?): Int {
        if (increased != null) {
            if (increased) {
                return greenColor!!
            } else {
                return redColor!!
            }
        } else {
            return defaultPriceColor!!
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textView: TextView = itemView.findViewById(R.id.symboltextView)
        val bidTextView: TextView = itemView.findViewById(R.id.bidTextView)
        val askTextview: TextView = itemView.findViewById(R.id.askTextView)
    }


}

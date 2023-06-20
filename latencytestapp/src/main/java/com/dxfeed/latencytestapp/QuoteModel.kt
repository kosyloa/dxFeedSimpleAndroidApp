package com.dxfeed.quotetableapp

import com.dxfeed.event.market.Profile
import com.dxfeed.event.market.Quote

class QuoteModel(private val symbol: String) {
    private var quote: Quote? = null
    private var previousQuote: Quote? = null
    private var profileDesc: String = ""
    fun update(quote: Quote) {
        previousQuote = this.quote
        this.quote = quote
    }

    fun update(profile: Profile) {
        profileDesc = profile.description
    }

    val ask: String
        get() = (quote?.askPrice ?: 0).toString()

    val bid: String
        get() = (quote?.bidPrice ?: 0).toString()

    val description: String
        get() = profileDesc

    val increasedBid: Boolean?
        get() {
            if (previousQuote == null || quote == null) {
                return null
            }
            return quote!!.bidPrice > previousQuote!!.bidPrice
        }

    val increaseAsk: Boolean?
        get() {
            if (previousQuote == null || quote == null) {
                return null
            }
            return quote!!.askPrice > previousQuote!!.askPrice
        }
}
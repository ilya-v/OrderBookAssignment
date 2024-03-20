package orderbook.engine

import orderbook.engine.internal.DefaultOrderBook

interface OrderBookSide : MutableIterable<Order> {
    fun add(order: Order)
}

interface OrderBook {
    fun add(order: Order)

    fun bids(): MutableIterable<Order>

    fun asks(): MutableIterable<Order>

    companion object {
        fun create(): OrderBook = DefaultOrderBook()
    }
}

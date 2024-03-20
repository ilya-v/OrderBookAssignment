import orderbook.engine.Order
import orderbook.engine.OrderBook
import orderbook.engine.OrderBookSide
import orderbook.engine.Side

class SimpleOrderBookSide(private val comparator: Comparator<Order>) : OrderBookSide {
    private val orders = mutableListOf<Order>()

    override fun add(order: Order) = orders.add(order).let { }

    override fun iterator(): MutableIterator<Order> = orders.apply { sortWith(comparator) }.iterator()
}

class SimpleOrderBook : OrderBook {
    private val bids = SimpleOrderBookSide { left, right -> right.price.compareTo(left.price) }
    private val asks = SimpleOrderBookSide { left, right -> left.price.compareTo(right.price) }

    override fun add(order: Order) = (if (order.side == Side.BUY) bids else asks).add(order)

    override fun bids(): MutableIterable<Order> = bids

    override fun asks(): MutableIterable<Order> = asks
}

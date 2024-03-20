package orderbook.engine.internal

import orderbook.engine.Order
import orderbook.engine.OrderBook
import orderbook.engine.OrderBookSide
import orderbook.engine.Side

import java.util.*
import kotlin.Comparator

internal class TreeMapOrderBookSide(comparator: Comparator<Int>) : OrderBookSide {
    private val orders = TreeMap<Int, ArrayList<Order>>(comparator)

    override fun add(order: Order) = orders.computeIfAbsent(order.price) { ArrayList() }.add(order).let { }


    override fun iterator(): MutableIterator<Order> {
        return object : MutableIterator<Order> {
            private var treeIterator = orders.iterator()
            private var list: ArrayList<Order> = ArrayList<Order>()
            private var listIterator: MutableIterator<Order>? = null

            override fun hasNext(): Boolean {
                return listIterator?.hasNext() == true || treeIterator.hasNext()
            }

            override fun next(): Order {
                while (listIterator?.hasNext() != true && treeIterator.hasNext()) {
                    treeIterator.next().value.let { nextList ->
                        list = nextList
                        listIterator = nextList.iterator()
                    }
                }
                require(listIterator?.hasNext() == true)
                return listIterator!!.next()
            }

            override fun remove() {
                require(listIterator != null)
                listIterator!!.remove()
                if (list.isEmpty()) {
                    treeIterator.remove()
                }
            }
        }
    }
}


internal class DefaultOrderBook : OrderBook {
    private val bids = TreeMapOrderBookSide { left, right -> right.compareTo(left) }
    private val asks = TreeMapOrderBookSide { left, right -> left.compareTo(right) }

    override fun add(order: Order) = (if (order.side == Side.BUY) bids else asks).add(order)

    override fun bids() = bids
    override fun asks() = asks
}

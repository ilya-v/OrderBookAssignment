package orderbook.output

import orderbook.engine.Order
import orderbook.engine.OrderBook
import orderbook.engine.Trade
import java.util.*

class OutputPrinter(private val printString: (String) -> Unit) {
    fun print(trade: Trade) =
        with(trade) {
            printString("trade ${String(aggressiveId.digits)},${String(passiveId.digits)},$price,$volume\n")
        }

    fun print(orderBook: OrderBook) =
        with(orderBook) {
            val bidOrders: MutableList<Order?> = bids().toMutableList()
            val askOrders: MutableList<Order?> = asks().toMutableList()

            val missingBidsNum = maxOf(askOrders.size - bidOrders.size, 0)
            val missingAsksNum = maxOf(bidOrders.size - askOrders.size, 0)

            bidOrders += List<Order?>(missingBidsNum) { null }
            askOrders += List<Order?>(missingAsksNum) { null }

            val emptySideString = " ".repeat(18)
            for ((bid, ask) in bidOrders.zip(askOrders)) {
                val bidString = bid?.let { "%,11d %6d".format(Locale.US, bid.volume, bid.price) } ?: emptySideString
                val askString = ask?.let { "%6d %,11d".format(Locale.US, ask.price, ask.volume) } ?: emptySideString
                printString("$bidString | $askString\n")
            }
        }
}

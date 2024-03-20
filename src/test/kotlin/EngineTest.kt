import orderbook.engine.Order
import orderbook.engine.OrderBook
import orderbook.engine.OrderId
import orderbook.engine.Side
import orderbook.engine.Trade
import orderbook.engine.matchOrder
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class EngineTest {
    @Test
    fun `some orders no trades`() {
        val orders =
            listOf(
                Order(OrderId(10000), Side.BUY, 98, 25500),
                Order(OrderId(10005), Side.SELL, 105, 20000),
                Order(OrderId(10001), Side.SELL, 100, 500),
                Order(OrderId(10002), Side.SELL, 100, 10000),
                Order(OrderId(10003), Side.BUY, 99, 50000),
                Order(OrderId(10004), Side.SELL, 103, 100),
            )

        val orderBook = OrderBook.create()
        orders.forEach { order ->
            val trades = orderBook.matchOrder(order)
            assertTrue { trades.isEmpty() }
        }

        val simpleOrderBook = SimpleOrderBook()
        orders.forEach { order ->
            val trades = simpleOrderBook.matchOrder(order)
            assertTrue { trades.isEmpty() }
        }

        assertEquals(2, orderBook.bids().count())
        assertEquals(4, orderBook.asks().count())

        assertEquals(simpleOrderBook.bids().toList(), orderBook.bids().toList())
        assertEquals(simpleOrderBook.asks().toList(), orderBook.asks().toList())
    }

    @Test
    fun `orders with trades`() {
        val orders =
            listOf(
                Order(OrderId(10000), Side.BUY, 98, 25500),
                Order(OrderId(10005), Side.SELL, 105, 20000),
                Order(OrderId(10001), Side.SELL, 100, 500),
                Order(OrderId(10002), Side.SELL, 100, 10000),
                Order(OrderId(10003), Side.BUY, 99, 50000),
                Order(OrderId(10004), Side.SELL, 103, 100),
                Order(OrderId(10006), Side.BUY, 105, 16000),
            )

        val orderBook = OrderBook.create()
        val trades = mutableListOf<Trade>()
        orders.forEach { order ->
            trades += orderBook.matchOrder(order)
        }

        val simpleOrderBook = SimpleOrderBook()
        val simpleTrades = mutableListOf<Trade>()
        orders.forEach { order ->
            simpleTrades += simpleOrderBook.matchOrder(order)
        }

        assertTrue { trades.isNotEmpty() }
        assertEquals(4, trades.size)

        assertEquals(trades[0], Trade(OrderId(10006), OrderId(10001), 100, 500))
        assertEquals(trades[1], Trade(OrderId(10006), OrderId(10002), 100, 10000))
        assertEquals(trades[2], Trade(OrderId(10006), OrderId(10004), 103, 100))
        assertEquals(trades[3], Trade(OrderId(10006), OrderId(10005), 105, 5400))

        assertEquals(simpleTrades, trades)

        assertEquals(2, orderBook.bids().count())
        assertEquals(1, orderBook.asks().count())

        assertEquals(orderBook.bids().toList()[0].price, 99)
        assertEquals(orderBook.bids().toList()[1].price, 98)
        assertEquals(orderBook.asks().toList()[0].price, 105)

        assertEquals(simpleOrderBook.bids().toList(), orderBook.bids().toList())
        assertEquals(simpleOrderBook.asks().toList(), orderBook.asks().toList())
    }
}

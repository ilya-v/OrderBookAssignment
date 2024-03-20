import orderbook.engine.Order
import orderbook.engine.OrderBook
import orderbook.engine.OrderId
import orderbook.engine.Side
import orderbook.engine.Trade
import orderbook.engine.matchOrder
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ReferenceEngineTest {
    @Test
    fun `feed engine 100K orders and compare output with simple book`() {
        val simpleBook = SimpleOrderBook()
        val orderBook = OrderBook.create()

        val orders = generateOrders(100_000)
        val referenceTrades = mutableListOf<Trade>()
        val testTrades = mutableListOf<Trade>()

        for ((index, order) in orders.withIndex()) {
            referenceTrades += simpleBook.matchOrder(order)
            testTrades += orderBook.matchOrder(order)

            if (index % (orders.size / 100) == 0) {
                println("$index out of ${orders.size} orders")
            }
        }

        assertEquals(referenceTrades.size, testTrades.size)
        assertEquals(referenceTrades, testTrades)
        assertEquals(simpleBook.bids().toList(), orderBook.bids().toList())
        assertEquals(simpleBook.asks().toList(), orderBook.asks().toList())
    }

    private fun generateOrders(
        n: Int,
        seed: Int = 42,
    ): List<Order> =
        with(Random(seed)) {
            (0 until n).map {
                Order(
                    id = OrderId(it.toLong()),
                    side = if (nextBoolean()) Side.BUY else Side.SELL,
                    price = nextInt(1000, 2000),
                    volume = nextInt(1, 10_000),
                )
            }.toList()
        }
}

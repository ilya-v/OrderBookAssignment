package orderbook.input

import orderbook.engine.Order
import orderbook.engine.OrderId
import orderbook.engine.Side

fun parseOrderString(input: String): Order {
    val parts = input.split(",")
    require(parts.size == 4) { "Invalid input format" }
    require(parts[1] in listOf("B", "S"))
    return Order(
        id = OrderId(parts[0]),
        side = if (parts[1] == "B") Side.BUY else Side.SELL,
        price = parts[2].toIntOrNull() ?: throw IllegalArgumentException("Invalid price: ${parts[2]}"),
        volume = parts[3].toIntOrNull() ?: throw IllegalArgumentException("Invalid volume: ${parts[3]}"),
    )
}

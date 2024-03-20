package orderbook.engine

fun OrderBook.matchOrder(order: Order): List<Trade> {
    val trades = mutableListOf<Trade>()
    val aggressiveOrder = order.copy()

    val isBuy = aggressiveOrder.side == Side.BUY
    val passiveSideIterator = (if (isBuy) asks() else bids()).iterator()
    val priceComparator: Comparator<Int> = if (isBuy) Comparator.naturalOrder() else Comparator.reverseOrder()

    while (passiveSideIterator.hasNext() && aggressiveOrder.volume > 0) {
        val passiveOrder = passiveSideIterator.next()
        if (priceComparator.compare(aggressiveOrder.price, passiveOrder.price) >= 0) {
            trades.add(tradeOrders(aggressiveOrder, passiveOrder))
        }
        if (passiveOrder.volume == 0) {
            passiveSideIterator.remove()
        }
    }

    if (aggressiveOrder.volume > 0) {
        add(aggressiveOrder)
    }

    return trades
}

internal fun tradeOrders(
    aggressiveOrder: Order,
    passiveOrder: Order,
): Trade {
    val tradeVolume = minOf(aggressiveOrder.volume, passiveOrder.volume)
    passiveOrder.volume -= tradeVolume
    aggressiveOrder.volume -= tradeVolume
    return Trade(
        aggressiveId = aggressiveOrder.id,
        passiveId = passiveOrder.id,
        price = passiveOrder.price,
        volume = tradeVolume,
    )
}

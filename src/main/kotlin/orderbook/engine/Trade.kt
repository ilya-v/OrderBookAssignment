package orderbook.engine

data class Trade(val aggressiveId: OrderId, val passiveId: OrderId, val price: Int, val volume: Int)

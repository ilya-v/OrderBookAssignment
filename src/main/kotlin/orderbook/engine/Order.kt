package orderbook.engine

enum class Side { BUY, SELL }

/**
 * Represents the id of an order.
 * A wrapper for the char array, but with a hand-made `equals` to avoid dangerous array comparisons.
 */
data class OrderId(val digits: CharArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return digits.contentEquals((other as OrderId).digits)
    }

    override fun hashCode(): Int = digits.contentHashCode()

    constructor(string: String) : this(string.toCharArray())
    constructor(num: Long) : this(num.toString())
}

data class Order(val id: OrderId, val side: Side, val price: Int, var volume: Int)

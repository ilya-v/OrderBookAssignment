package orderbook

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import orderbook.engine.OrderBook
import orderbook.engine.Trade
import orderbook.engine.matchOrder
import orderbook.input.parseOrderString
import orderbook.output.OutputPrinter
import orderbook.utils.runningResult

fun main() {
    runBlocking {
        val lineInputFlow =
            flow {
                while (true) {
                    // replace a whitespace or empty string with null
                    val line = readlnOrNull()?.takeUnless { it.isBlank() }

                    // eventually emits null to signal the end of the flow
                    emit(line)
                    line ?: break
                }
            }

        processInputLines(lineInputFlow) { outputString ->
            print(outputString) // can't use println here because of Windows CRLF
            System.out.flush()
        }
    }
}

suspend fun processInputLines(
    lineInputFlow: Flow<String?>,
    printString: (String) -> Unit,
) {
    data class MatchingResult(val orderBook: OrderBook, val trades: List<Trade>, val more: Boolean)

    val printer = OutputPrinter(printString)

    lineInputFlow
        .map { lineOrNull -> lineOrNull?.let { parseOrderString(it) } }
        .runningResult(initial = OrderBook.create()) { orderBook, order ->
            MatchingResult(
                orderBook = orderBook,
                trades = order?.let { orderBook.matchOrder(it) } ?: emptyList(),
                more = order != null,
            )
        }
        .collect { matchingResult ->
            val (orderBook, trades, more) = matchingResult
            trades.forEach { printer.print(it) }
            if (!more) {
                printer.print(orderBook)
            }
        }
}

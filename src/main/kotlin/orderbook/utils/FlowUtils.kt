package orderbook.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Folds the given flow with [operation], emitting every intermediate result of the [operation].
 * Modeled after [kotlinx.coroutines.flow.runningFold].
 */
fun <T, U, V> Flow<T>.runningResult(
    initial: U,
    operation: suspend (accumulator: U, value: T) -> V,
): Flow<V> =
    flow {
        val accumulator: U = initial
        collect { value -> emit(operation(accumulator, value)) }
    }

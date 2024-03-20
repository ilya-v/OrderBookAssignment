import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import orderbook.processInputLines
import java.security.MessageDigest
import kotlin.test.Test
import kotlin.test.assertEquals

class TextInputOutputTest {
    @Test
    fun `end-to-end text-based test with trades`() {
        val test2Input =
            """
            10000,B,98,25500
            10005,S,105,20000
            10001,S,100,500
            10002,S,100,10000
            10003,B,99,50000
            10004,S,103,100
            10006,B,105,16000
            """.trimIndent()

        val inputFlow =
            flow {
                test2Input.lines().forEach { emit(it) }
                emit(null)
            }

        val outputLines = mutableListOf<String>()

        fun printString(s: String) = outputLines.add(s)

        runBlocking {
            processInputLines(inputFlow, ::printString)
        }

        val expectedOutputLines =
            listOf(
                "trade 10006,10001,100,500\n",
                "trade 10006,10002,100,10000\n",
                "trade 10006,10004,103,100\n",
                "trade 10006,10005,105,5400\n",
                "     50,000     99 |    105      14,600\n",
                "     25,500     98 |                   \n",
            )

        assertEquals(expectedOutputLines, outputLines)

        val digest =
            MessageDigest.getInstance("MD5").run {
                update(outputLines.joinToString(separator = "").toByteArray())
                digest().joinToString(separator = "") { "%02x".format(it) }
            }
        assertEquals("ce8e7e5ab26ab5a7db6b7d30759cf02e", digest)
    }

    @Test
    fun `end-to-end text-based test without trades`() {
        val test1Input =
            """
            10000,B,98,25500
            10005,S,105,20000
            10001,S,100,500
            10002,S,100,10000
            10003,B,99,50000
            10004,S,103,100
            """.trimIndent()

        val inputFlow =
            flow {
                test1Input.lines().forEach { emit(it) }
                emit(null)
            }

        val outputLines = mutableListOf<String>()

        fun printString(s: String) = outputLines.add(s)

        runBlocking {
            processInputLines(inputFlow, ::printString)
        }

        val expectedTest1Output =
            """
            .....50,000.....99.|....100.........500
            .....25,500.....98.|....100......10,000
            ...................|....103.........100
            ...................|....105......20,000
            """.trimIndent().replace('.', ' ')

        val expectedOutputLines = expectedTest1Output.lines().map { it + "\n" }

        assertEquals(expectedOutputLines, outputLines)
    }
}

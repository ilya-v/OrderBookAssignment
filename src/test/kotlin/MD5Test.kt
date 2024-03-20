import java.security.MessageDigest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class MD5Test {
    @Test
    fun testMD5() {
        val output =
            listOf(
                "trade 10006,10001,100,500\n",
                "trade 10006,10002,100,10000\n",
                "trade 10006,10004,103,100\n",
                "trade 10006,10005,105,5400\n",
                "     50,000     99 |    105      14,600\n",
                "     25,500     98 |                   \n",
            ).joinToString(separator = "")

        val digest = MessageDigest.getInstance("MD5").run {
            update(output.toByteArray())
            digest().joinToString(separator = "") { "%02x".format(it) }
        }
        assertEquals("ce8e7e5ab26ab5a7db6b7d30759cf02e", digest)
    }
}

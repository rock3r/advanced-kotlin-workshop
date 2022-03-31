package dev.sebastiano.workshop

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.junit.jupiter.api.Test
import javax.script.ScriptEngineManager

internal class HtmlDslTest {

    @Test
    internal fun `should produce HTML with the right title in the head`() {
        val document = evaluateAndParse(
            """
            html {
                head {
                    title("The title")
                }
            }.toString()
            """.trimIndent()
        )

        assertThat(document.select("head title").text()).isEqualTo("The title")
    }

    @Test
    internal fun `should produce HTML with the right h1 in the body`() {
        val document = evaluateAndParse(
            """
            html {
                body {
                    h1("The title")
                }
            }.toString()
            """.trimIndent()
        )

        assertThat(document.select("body h1").text()).isEqualTo("The title")
    }

    @Test
    internal fun `should produce HTML with a properly rendered list in the body`() {
        val document = evaluateAndParse(
            """
            html {
                body {
                    h1("The title")
                    orderedList {
                        listItem { rawText("First item") }
                        listItem { rawText("Second item") }
                        listItem { /* Empty item */ }
                        listItem { rawText("Third item") }
                    }
                }
            }.toString()    
            """.trimIndent()
        )

        assertThat(document.select("body ol li:eq(0)").text()).isEqualTo("First item")
        assertThat(document.select("body ol li:eq(1)").text()).isEqualTo("Second item")
        assertThat(document.select("body ol li:eq(2)").hasText()).isFalse()
        assertThat(document.select("body ol li:eq(3)").text()).isEqualTo("Third item")
    }
}

private val ktsEngine = ScriptEngineManager(HtmlDslTest::class.java.classLoader).getEngineByExtension("kts")!!

fun evaluateAndParse(snippet: String): Document = Jsoup.parse(
    ktsEngine.eval("import dev.sebastiano.workshop.*\n\n$snippet")?.toString()!!
)

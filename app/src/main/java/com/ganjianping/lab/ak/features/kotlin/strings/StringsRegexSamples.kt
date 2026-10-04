package com.ganjianping.lab.ak.features.kotlin.strings

import com.ganjianping.lab.ak.common.codesample.CodeSample
import com.ganjianping.lab.ak.common.codesample.SampleLog
import java.text.Normalizer

/** Samples for the Strings & regex topic. Each function body below is the code shown in its `CodeSample`. */
object StringsRegexSamples {
    val all: List<CodeSample> = listOf(
        CodeSample(
            title = "Characters and Unicode",
            explanation = "A Kotlin String is UTF-16. length counts UTF-16 units, so accents and emoji can count more than they look.",
            code = """
                for (text in listOf("café", "é", "👍🏽")) {
                    val points = text.codePointCount(0, text.length)
                    val bytes = text.toByteArray().size
                    log("${'$'}text: length ${'$'}{text.length}, ${'$'}points code points, ${'$'}bytes UTF-8 bytes")
                }
            """.trimIndent(),
            run = { unicode(it) }
        ),
        CodeSample(
            title = "Raw strings and trimIndent",
            explanation = "Triple-quoted raw strings keep line breaks and quotes as written; trimIndent removes the shared indentation.",
            code = "val language = \"Kotlin\"\n" +
                "val json = \"\"\"\n" +
                "    {\n" +
                "      \"language\": \"${'$'}language\",\n" +
                "      \"year\": 2016\n" +
                "    }\n" +
                "\"\"\".trimIndent()\n" +
                "json.lines().forEach { log(it) }",
            run = { rawStrings(it) }
        ),
        CodeSample(
            title = "Regex and named groups",
            explanation = "Regex finds patterns in text; named groups give each captured part a name instead of a number.",
            code = """
                val date = Regex("(?<year>\\d{4})-(?<month>\\d{2})-(?<day>\\d{2})")
                val match = date.find("Released on 2026-10-04.")
                log("year: ${'$'}{match?.groups?.get("year")?.value}")
                log("month: ${'$'}{match?.groups?.get("month")?.value}")
                log("all dates: ${'$'}{date.findAll("2024-01-02 and 2025-03-04").map { it.value }.toList()}")
            """.trimIndent(),
            run = { regex(it) }
        ),
        CodeSample(
            title = "Building strings",
            explanation = "buildString appends to one StringBuilder instead of creating a new string for every +.",
            code = """
                val receipt = buildString {
                    appendLine("Receipt")
                    listOf("tea" to 3, "cake" to 5).forEach { (item, price) ->
                        appendLine("${'$'}{item.padEnd(6, '.')}${'$'}price")
                    }
                    append("total: 8")
                }
                receipt.lines().forEach { log(it) }
            """.trimIndent(),
            run = { buildingStrings(it) }
        ),
        CodeSample(
            title = "Comparing strings",
            explanation = "== compares UTF-16 units, so an accent typed two ways differs until both are normalized.",
            code = """
                val composed = "café"
                val decomposed = "café"
                log("== : ${'$'}{composed == decomposed}")
                val normalized = Normalizer.normalize(decomposed, Normalizer.Form.NFC)
                log("after NFC: ${'$'}{composed == normalized}")
                log("ignoreCase: ${'$'}{"Kotlin".equals("KOTLIN", ignoreCase = true)}")
            """.trimIndent(),
            run = { comparing(it) }
        )
    )
}

private fun unicode(log: SampleLog) {
    for (text in listOf("café", "é", "👍🏽")) {
        val points = text.codePointCount(0, text.length)
        val bytes = text.toByteArray().size
        log("$text: length ${text.length}, $points code points, $bytes UTF-8 bytes")
    }
}

private fun rawStrings(log: SampleLog) {
    val language = "Kotlin"
    val json = """
        {
          "language": "$language",
          "year": 2016
        }
    """.trimIndent()
    json.lines().forEach { log(it) }
}

private fun regex(log: SampleLog) {
    val date = Regex("(?<year>\\d{4})-(?<month>\\d{2})-(?<day>\\d{2})")
    val match = date.find("Released on 2026-10-04.")
    log("year: ${match?.groups?.get("year")?.value}")
    log("month: ${match?.groups?.get("month")?.value}")
    log("all dates: ${date.findAll("2024-01-02 and 2025-03-04").map { it.value }.toList()}")
}

private fun buildingStrings(log: SampleLog) {
    val receipt = buildString {
        appendLine("Receipt")
        listOf("tea" to 3, "cake" to 5).forEach { (item, price) ->
            appendLine("${item.padEnd(6, '.')}$price")
        }
        append("total: 8")
    }
    receipt.lines().forEach { log(it) }
}

private fun comparing(log: SampleLog) {
    val composed = "café"
    val decomposed = "café"
    log("== : ${composed == decomposed}")
    val normalized = Normalizer.normalize(decomposed, Normalizer.Form.NFC)
    log("after NFC: ${composed == normalized}")
    log("ignoreCase: ${"Kotlin".equals("KOTLIN", ignoreCase = true)}")
}

package com.ganjianping.lab.ak.features.kotlin.nullsafety

import com.ganjianping.lab.ak.common.codesample.CodeSample
import com.ganjianping.lab.ak.common.codesample.SampleLog

/** Samples for the Null safety topic. Each function body below is the code shown in its `CodeSample`. */
object NullSafetySamples {
    val all: List<CodeSample> = listOf(
        CodeSample(
            title = "Nullable types",
            explanation = "A type ending in ? may hold null. toIntOrNull returns Int? because the text may not be a number.",
            code = """
                val number: Int? = "42".toIntOrNull()
                val invalid: Int? = "forty-two".toIntOrNull()
                log("\"42\".toIntOrNull() = ${'$'}number")
                log("\"forty-two\".toIntOrNull() = ${'$'}invalid")
                // val length: Int = invalid  would not compile: Int? is not Int
            """.trimIndent(),
            run = { nullableTypes(it) }
        ),
        CodeSample(
            title = "Smart casts",
            explanation = "After a null check, Kotlin treats the value as non-null inside the branch, so no unwrapping is needed.",
            code = """
                fun greet(nickname: String?): String {
                    if (nickname != null) {
                        // Smart cast: nickname is a String here.
                        return "Hello, ${'$'}nickname (${'$'}{nickname.length} letters)!"
                    }
                    return "Hello, guest!"
                }
                log(greet("Ada"))
                log(greet(null))
            """.trimIndent(),
            run = { smartCasts(it) }
        ),
        CodeSample(
            title = "Elvis and early return",
            explanation = "?: supplies a fallback when the left side is null; the fallback can even return from the function.",
            code = """
                fun nextAge(text: String): String {
                    val age = text.toIntOrNull() ?: return "\"${'$'}text\" is not a number"
                    return "Next year you will be ${'$'}{age + 1}"
                }
                log(nextAge("36"))
                log(nextAge("abc"))
            """.trimIndent(),
            run = { elvis(it) }
        ),
        CodeSample(
            title = "Safe calls and let",
            explanation = "?. stops at the first null and gives null; ?.let runs a block only when the value is there.",
            code = """
                data class Address(val city: String?)
                data class User(val address: Address?)
                val resident = User(Address("Singapore"))
                val visitor = User(null)
                log("city: ${'$'}{resident.address?.city ?: "unknown"}")
                log("city: ${'$'}{visitor.address?.city ?: "unknown"}")
                resident.address?.city?.let { log("letters: ${'$'}{it.length}") }
                visitor.address?.city?.let { log("never logged") }
            """.trimIndent(),
            run = { safeCalls(it) }
        ),
        CodeSample(
            title = "Not-null assertion !!",
            explanation = "!! turns a nullable value into a non-null one and throws if it is null. Prefer ?: or ?.let.",
            code = """
                val stock = mapOf("apple" to 3)
                log("stock[\"pear\"] ?: 0 = ${'$'}{stock["pear"] ?: 0}")
                // stock["pear"]!! would throw NullPointerException.
                val apples = stock["apple"]!!
                log("stock[\"apple\"]!! = ${'$'}apples, safe only because the key exists")
            """.trimIndent(),
            run = { notNullAssertion(it) }
        )
    )
}

private fun nullableTypes(log: SampleLog) {
    val number: Int? = "42".toIntOrNull()
    val invalid: Int? = "forty-two".toIntOrNull()
    log("\"42\".toIntOrNull() = $number")
    log("\"forty-two\".toIntOrNull() = $invalid")
    // val length: Int = invalid  would not compile: Int? is not Int
}

private fun smartCasts(log: SampleLog) {
    fun greet(nickname: String?): String {
        if (nickname != null) {
            // Smart cast: nickname is a String here.
            return "Hello, $nickname (${nickname.length} letters)!"
        }
        return "Hello, guest!"
    }
    log(greet("Ada"))
    log(greet(null))
}

private fun elvis(log: SampleLog) {
    fun nextAge(text: String): String {
        val age = text.toIntOrNull() ?: return "\"$text\" is not a number"
        return "Next year you will be ${age + 1}"
    }
    log(nextAge("36"))
    log(nextAge("abc"))
}

private fun safeCalls(log: SampleLog) {
    data class Address(val city: String?)
    data class User(val address: Address?)
    val resident = User(Address("Singapore"))
    val visitor = User(null)
    log("city: ${resident.address?.city ?: "unknown"}")
    log("city: ${visitor.address?.city ?: "unknown"}")
    resident.address?.city?.let { log("letters: ${it.length}") }
    visitor.address?.city?.let { log("never logged") }
}

private fun notNullAssertion(log: SampleLog) {
    val stock = mapOf("apple" to 3)
    log("stock[\"pear\"] ?: 0 = ${stock["pear"] ?: 0}")
    // stock["pear"]!! would throw NullPointerException.
    val apples = stock["apple"]!!
    log("stock[\"apple\"]!! = $apples, safe only because the key exists")
}

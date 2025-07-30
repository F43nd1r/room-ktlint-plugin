package com.faendir.ktlint.room

import com.pinterest.ktlint.rule.engine.api.Code
import com.pinterest.ktlint.rule.engine.api.KtLintRuleEngine
import com.pinterest.ktlint.rule.engine.core.api.AutocorrectDecision
import com.pinterest.ktlint.rule.engine.core.api.RuleProvider
import com.pinterest.ktlint.test.KtLintAssertThat.Companion.assertThatRule
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import java.io.File
import java.nio.file.Paths
import kotlin.io.path.listDirectoryEntries

class RoomAnnotationSqlFormattingRuleTest {
    private val assertThatRule = assertThatRule { RoomAnnotationSqlFormattingRule() }

    fun ruleAssert(@Language("kotlin") code: String) = assertThatRule(code)

    @TestFactory
    fun `should format correctly`(): List<DynamicTest> {
        val url = checkNotNull(javaClass.classLoader.getResource("testCases"))
        val files = File(url.toURI()).listFiles()?.toList().orEmpty()
        val cases = files.filter { it.isFile && it.name.endsWith(".case.kt") }
        val results = files.filter { it.isFile && it.name.endsWith(".result.kt") }
        val tests = cases.mapNotNull { case ->
            val testName = case.name.removeSuffix(".case.kt")
            val result = results.find { it.name.removeSuffix(".result.kt") == testName }
            if (result != null) {
                Triple(testName, case.readText(), result.readText())
            } else {
                println("No result file found for ${case.name}, skipping test")
                null
            }
        }
        return tests.map { (name, input, expected) ->
            DynamicTest.dynamicTest("should format $name") {
                ruleAssert(input).isFormattedAs(expected)
            }
        }
    }

    @Disabled // comment out to write result files (after adding new cases or changing implementation)
    @TestFactory
    fun `write results`(): List<DynamicTest> {
        val ktLintRuleEngine = KtLintRuleEngine(setOf(RuleProvider { RoomAnnotationSqlFormattingRule() }))
        val files =  Paths.get("src","test","resources", "testCases").listDirectoryEntries().map { it.toFile() }
        val cases = files.filter { it.isFile && it.name.endsWith(".case.kt") }
        return cases.map { case ->
            val resultFile = File(case.parentFile, case.name.removeSuffix(".case.kt") + ".result.kt")
            DynamicTest.dynamicTest("Write ${resultFile.name}") {
                resultFile.writeText(ktLintRuleEngine.format(Code.fromSnippet(case.readText())) { AutocorrectDecision.ALLOW_AUTOCORRECT })
            }
        }
    }
}
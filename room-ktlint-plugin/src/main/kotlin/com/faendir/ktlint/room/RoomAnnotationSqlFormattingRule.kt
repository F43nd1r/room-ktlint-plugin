package com.faendir.ktlint.room

import com.github.vertical_blank.sqlformatter.SqlFormatter
import com.github.vertical_blank.sqlformatter.core.FormatConfig
import com.github.vertical_blank.sqlformatter.languages.StringLiteral
import com.pinterest.ktlint.rule.engine.core.api.*
import com.pinterest.ktlint.rule.engine.core.api.editorconfig.EditorConfig
import com.pinterest.ktlint.rule.engine.core.api.editorconfig.INDENT_SIZE_PROPERTY
import com.pinterest.ktlint.rule.engine.core.api.editorconfig.INDENT_STYLE_PROPERTY
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.LeafPsiElement


class RoomAnnotationSqlFormattingRule : Rule(
    RuleId("$RULESET_ID:sql-formatting"), About(
        maintainer = "F43nd1r",
        repositoryUrl = "https://github.com/F43nd1r/room-ktlint-plugin",
        issueTrackerUrl = "https://github.com/F43nd1r/room-ktlint-plugin/issues"
    )
), RuleAutocorrectApproveHandler {
    override val usesEditorConfigProperties = setOf(INDENT_STYLE_PROPERTY, INDENT_SIZE_PROPERTY)

    private lateinit var indentConfig: IndentConfig
    private lateinit var sqlConfig: FormatConfig

    // sql-formatter does not support sqlite or room dialects directly, so we extend the default formatter
    private val formatter = SqlFormatter.extend {
        it.plusNamedPlaceholderTypes(":")
            .plusOperators("!=")
            .plusReservedWords("ROWID")
            .plusStringTypes(StringLiteral.BACK_QUOTE, StringLiteral.BRACKET)
    }

    override fun beforeFirstNode(editorConfig: EditorConfig) {
        indentConfig = IndentConfig(editorConfig[INDENT_STYLE_PROPERTY], editorConfig[INDENT_SIZE_PROPERTY])
        sqlConfig = FormatConfig.builder().indent(indentConfig.indent).uppercase(true).maxColumnLength(100).build()

    }

    override fun beforeVisitChildNodes(
        node: ASTNode,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> AutocorrectDecision
    ) {
        if (node.elementType != ElementType.ANNOTATION_ENTRY) return
        val annotationName = node.findChildByType(ElementType.CONSTRUCTOR_CALLEE)?.findChildByType(ElementType.TYPE_REFERENCE)?.text
        if (annotationName != "Query") return
        val stringNode = node.findChildByType(ElementType.VALUE_ARGUMENT_LIST)?.findChildByType(ElementType.VALUE_ARGUMENT)?.findChildByType(ElementType.STRING_TEMPLATE) ?: return
        val text = stringNode.text
        val formattedText = formatSqlString(text, node)
        if (text != formattedText) {
            emit(stringNode.startOffset, "SQL should be formatted.", true).ifAutocorrectAllowed {
                stringNode.treeParent.replaceChild(stringNode, LeafPsiElement(ElementType.STRING_TEMPLATE, formattedText))
            }
        }
    }

    private fun formatSqlString(text: String, node: ASTNode): String {
        val cleanedText = text.removeSurrounding("\"\"\"").removeSurrounding("\"").trim()
        val formattedSql = formatter.format(cleanedText, sqlConfig)
        return "\"\"\"\n$formattedSql\n\"\"\"".replace("\n", indentConfig.childIndentOf(node))
    }
}
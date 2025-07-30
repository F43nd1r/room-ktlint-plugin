package com.faendir.ktlint.room

import com.pinterest.ktlint.cli.ruleset.core.api.RuleSetProviderV3
import com.pinterest.ktlint.rule.engine.core.api.RuleProvider
import com.pinterest.ktlint.rule.engine.core.api.RuleSetId

const val RULESET_ID = "room-query"

class RoomRuleSetProvider : RuleSetProviderV3(RuleSetId(RULESET_ID)) {
    override fun getRuleProviders(): Set<RuleProvider> = setOf(RuleProvider { RoomAnnotationSqlFormattingRule() })
}
package com.jetbrains.kotlin.structuralsearch.impl.matcher

import com.intellij.psi.PsiComment
import com.intellij.psi.TokenType
import org.jetbrains.kotlin.lexer.KtTokens

object KotlinMatchUtil {

    fun getCommentText(comment: PsiComment): String {
        return when (comment.tokenType) {
            KtTokens.EOL_COMMENT -> comment.text.drop(2)
            KtTokens.BLOCK_COMMENT -> comment.text.drop(2).dropLast(2)
            else -> ""
        }
    }

}
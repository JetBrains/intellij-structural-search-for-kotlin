package com.jetbrains.kotlin.structuralsearch.impl.matcher.handlers

import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.structuralsearch.impl.matcher.MatchContext
import com.intellij.structuralsearch.impl.matcher.handlers.MatchingHandler
import org.jetbrains.kotlin.kdoc.psi.api.KDoc
import org.jetbrains.kotlin.psi.KtDeclaration
import org.jetbrains.kotlin.psi.psiUtil.getChildrenOfType

class DeclarationHandler : MatchingHandler() {

    private fun KtDeclaration.getNonKDocCommentChild(): PsiComment? =
        this.getChildrenOfType<PsiComment>().firstOrNull { it !is KDoc }

    override fun match(patternNode: PsiElement?, matchedNode: PsiElement?, context: MatchContext?): Boolean {
        if (context == null) return false
        when (patternNode) {
            is PsiComment -> {
                return when (matchedNode) {
                    // Match PsiComment-s
                    is PsiComment -> context.matcher.match(patternNode, matchedNode)

                    // Match [PsiComment, PROPERTY] with [PROPERTY[PsiComment]]
                    is KtDeclaration -> context.matcher.match(patternNode, matchedNode.getNonKDocCommentChild())
                    else -> false
                }
            }
            is KtDeclaration -> {
                val patternComment = patternNode.getNonKDocCommentChild()

                return when {
                    // Comment already matched
                    PsiTreeUtil.skipWhitespacesBackward(patternNode) is PsiComment ->
                        context.matcher.match(patternNode, matchedNode)

                    // Match [PROPERTY[PsiComment]] with [PROPERTY[PsiComment]]
                    matchedNode is KtDeclaration && patternComment != null ->
                        context.matcher.match(patternNode, matchedNode)
                                && context.matcher.match(patternComment, matchedNode.getNonKDocCommentChild())

                    // Match [PROPERTY[]] with [PROPERTY[PsiComment?]]
                    matchedNode is KtDeclaration -> context.matcher.match(patternNode, matchedNode)

                    // Match [PROPERTY[PsiComment]] with [PsiComment, PROPERTY]
                    matchedNode is PsiComment && PsiTreeUtil.skipWhitespacesForward(matchedNode) is KtDeclaration ->
                        patternComment != null
                                && context.matcher.match(patternComment, matchedNode)
                    else -> false
                }
            }
        }
        return false
    }

    override fun shouldAdvanceTheMatchFor(patternElement: PsiElement?, matchedElement: PsiElement?): Boolean {
        if (patternElement is PsiComment && matchedElement is KtDeclaration) return false
        return super.shouldAdvanceTheMatchFor(patternElement, matchedElement)
    }

    override fun shouldAdvanceThePatternFor(patternElement: PsiElement?, matchedElement: PsiElement?): Boolean {
        if (patternElement is KtDeclaration && matchedElement is PsiComment) return false
        return super.shouldAdvanceThePatternFor(patternElement, matchedElement)
    }

}
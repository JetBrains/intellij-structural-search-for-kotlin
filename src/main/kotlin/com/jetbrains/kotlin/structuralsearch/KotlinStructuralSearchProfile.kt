package com.jetbrains.kotlin.structuralsearch

import com.intellij.dupLocator.util.NodeFilter
import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.project.Project
import com.intellij.psi.*
import com.intellij.psi.impl.DebugUtil
import com.intellij.structuralsearch.*
import com.intellij.structuralsearch.impl.matcher.CompiledPattern
import com.intellij.structuralsearch.impl.matcher.GlobalMatchingVisitor
import com.intellij.structuralsearch.impl.matcher.PatternTreeContext
import com.intellij.structuralsearch.impl.matcher.compiler.GlobalCompilingVisitor
import com.intellij.structuralsearch.plugin.replace.ReplaceOptions
import com.intellij.structuralsearch.plugin.ui.Configuration
import com.intellij.util.SmartList
import com.jetbrains.kotlin.structuralsearch.impl.matcher.KotlinCompiledPattern
import com.jetbrains.kotlin.structuralsearch.impl.matcher.KotlinMatchingVisitor
import com.jetbrains.kotlin.structuralsearch.impl.matcher.KotlinRecursiveElementWalkingVisitor
import com.jetbrains.kotlin.structuralsearch.impl.matcher.compiler.KotlinCompilingVisitor
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.idea.KotlinLanguage
import org.jetbrains.kotlin.idea.liveTemplates.KotlinTemplateContextType
import org.jetbrains.kotlin.psi.*

class KotlinStructuralSearchProfile : StructuralSearchProfile() {
    override fun getLexicalNodesFilter(): NodeFilter = NodeFilter { element -> element is PsiWhiteSpace }

    override fun createMatchingVisitor(globalVisitor: GlobalMatchingVisitor): KotlinMatchingVisitor =
        KotlinMatchingVisitor(globalVisitor)

    override fun createCompiledPattern(): KotlinCompiledPattern = KotlinCompiledPattern()

    override fun isMyLanguage(language: Language): Boolean = language == KotlinLanguage.INSTANCE

    override fun getTemplateContextTypeClass(): Class<KotlinTemplateContextType> = KotlinTemplateContextType::class.java

    override fun getPredefinedTemplates(): Array<Configuration> = KotlinPredefinedConfigurations.createPredefinedTemplates()

    override fun getDefaultFileType(fileType: LanguageFileType?): LanguageFileType = fileType ?: KotlinFileType.INSTANCE

    override fun compile(elements: Array<out PsiElement>?, globalVisitor: GlobalCompilingVisitor) {
        KotlinCompilingVisitor(globalVisitor).compile(elements)
    }

    override fun getPresentableElement(element: PsiElement?): PsiElement {
        val pElement = super.getPresentableElement(element)
        val parent = pElement.parent
        return if(parent is KtProperty || parent is KtNamedFunction || parent is KtClass) parent else pElement
    }

    override fun createPatternTree(
        text: String,
        context: PatternTreeContext,
        fileType: LanguageFileType,
        language: Language,
        contextId: String?,
        project: Project,
        physical: Boolean
    ): Array<PsiElement> {
        val fragment = KtPsiFactory(project, false).createBlockCodeFragment("Unit\n$text", null)
        val elements = when (fragment.lastChild) {
            is PsiComment -> getNonWhitespaceChildren(fragment).drop(1)
            else -> getNonWhitespaceChildren(fragment.firstChild).drop(1)
        }
        for (element in elements) print(DebugUtil.psiToString(element, false))

        return when {
            elements.isEmpty() -> PsiElement.EMPTY_ARRAY
            else -> elements.toTypedArray()
        }
    }

    inner class KotlinValidator : KotlinRecursiveElementWalkingVisitor() {
        override fun visitErrorElement(element: PsiErrorElement) {
            super.visitErrorElement(element)
            if (shouldShowProblem(element)) {
                throw MalformedPatternException(element.text)
            }
        }
    }

    override fun checkSearchPattern(pattern: CompiledPattern) {
        val visitor = KotlinValidator()
        val nodes = pattern.nodes
        while (nodes.hasNext()) {
            nodes.current().accept(visitor)
            nodes.advance()
        }
        nodes.reset()
    }

    override fun shouldShowProblem(error: PsiErrorElement): Boolean {
        val description = error.errorDescription
        val parent = error.parent
        if (parent is KtTryExpression && KSSRBundle.message("expected.catch.or.finally") == description) {
            // searching for naked try allowed
            return false
        }
        return true
    }

    override fun checkReplacementPattern(project: Project, options: ReplaceOptions) {
        val matchOptions = options.matchOptions
        val fileType = matchOptions.fileType
        val dialect = matchOptions.dialect
        val searchIsDeclaration = isProbableExpression(matchOptions.searchPattern, fileType, dialect, project)
        val replacementIsDeclaration = isProbableExpression(options.replacement, fileType, dialect, project)
        if(searchIsDeclaration != replacementIsDeclaration) {
            throw UnsupportedPatternException(
                if (searchIsDeclaration) SSRBundle.message("replacement.template.is.not.expression.error.message")
                else SSRBundle.message("search.template.is.not.expression.error.message")
            )
        }
    }

    private fun isProbableExpression(pattern: String, fileType: LanguageFileType, dialect: Language, project: Project): Boolean {
        val searchElements = createPatternTree(pattern, PatternTreeContext.Block, fileType, dialect, null, project, false)
        return searchElements[0] is KtDeclaration
    }

    override fun getReplaceHandler(project: Project, replaceOptions: ReplaceOptions): DocumentBasedReplaceHandler =
        DocumentBasedReplaceHandler(project)

    companion object {
        fun getNonWhitespaceChildren(fragment: PsiElement): List<PsiElement> {
            var element = fragment.firstChild
            val result: MutableList<PsiElement> = SmartList()
            while (element != null) {
                if (element !is PsiWhiteSpace) result.add(element)
                element = element.nextSibling
            }
            return result
        }
    }
}
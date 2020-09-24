package com.jetbrains.kotlin.structuralsearch.sanity

import com.intellij.ide.impl.ProjectUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.structuralsearch.Matcher
import com.intellij.structuralsearch.plugin.ui.SearchConfiguration
import com.intellij.structuralsearch.plugin.util.CollectingMatchResultSink
import com.intellij.testFramework.HeavyPlatformTestCase
import com.intellij.testFramework.UsefulTestCase
import junit.framework.TestCase
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.idea.search.fileScope
import java.util.*
import kotlin.random.Random

class KotlinSSSanityTest : HeavyPlatformTestCase() {
    private val myConfiguration = SearchConfiguration().apply {
        name = "SSR"
        matchOptions.fileType = KotlinFileType.INSTANCE
    }
    private var random = Random(System.currentTimeMillis())

    override fun setUp() {
        super.setUp()
        random = Random(project.hashCode())
        myProject = ProjectUtil.openOrImport("/", null, false)
    }

    private fun doTest(psiFile: PsiFile): Boolean {
        val subtree = SanityTestElementPicker.pickFrom(psiFile.children)
        if (subtree == null) {
            println("No element picked.")
            return true
        }

        println(
            "- Search pattern from ${subtree::class.toString().split('.').last()} element:\n\t${
                subtree.text.trimMargin().replace("\n", "\n\t")
            }"
        )

        val matchOptions = myConfiguration.matchOptions.apply {
            fillSearchCriteria(subtree.text)
            fileType = KotlinFileType.INSTANCE
            scope = psiFile.fileScope()
        }
        val matcher = Matcher(project, matchOptions)
        val sink = CollectingMatchResultSink()
        matcher.findMatches(sink)

        return sink.matches.size > 0
    }

    /** Picks a random .kt file from this project and returns its content and PSI tree. */
    private fun randomLocalKotlinSource(): PsiFile? {
        val projectScope = GlobalSearchScope.projectScope(project)

        // List searchable files
        val allFiles: List<VirtualFile> = ArrayList(FilenameIndex.getAllFilesByExt(project, "kt", projectScope)).filter {
                "test" !in it.path && "PredefinedConfigurations" !in it.path // Exclude templates because they contain '_ syntax
        }
        check(allFiles.isNotEmpty()) { "No Kotlin files in the project" }

        val file = allFiles.random()
        println("- File:\n\t${file.presentableUrl}")
        return PsiManager.getInstance(project).findFile(file)
    }

    fun testLocalSSS() {
        val psiFile = randomLocalKotlinSource()
        TestCase.assertNotNull("Couldn't find Kotlin source code", psiFile)
        assert(doTest(psiFile!!)) { "No match found." }
    }

}
package com.jetbrains.kotlin.structuralsearch.search

import com.jetbrains.kotlin.structuralsearch.KotlinSSResourceInspectionTest

class KotlinSSLoopStatementTest : KotlinSSResourceInspectionTest() {
    override fun getBasePath(): String = "loopStatement"

    fun testForLoop() {
        doTest(
            """
            for(i in 0..10) {
                println(i)
            }
            """
        )
    }

    fun testWhileLoop() {
        doTest(
            """
            while(true) {
                println(0)
            }
            """
        )
    }

    fun testDoWhileLoop() {
        doTest(
            """
            do {
                println(0)
            } while(true)
            """
        )
    }
    
    fun testWhileTwoStatements() { doTest("while ('_) { '_{2,2} }") }
}
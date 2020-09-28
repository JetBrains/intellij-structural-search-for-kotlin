package com.jetbrains.kotlin.structuralsearch.sanity

import com.intellij.openapi.module.Module
import com.intellij.openapi.roots.ContentEntry
import com.intellij.openapi.roots.ModifiableRootModel
import com.intellij.openapi.roots.OrderRootType
import com.intellij.psi.PsiElement
import com.jetbrains.kotlin.structuralsearch.KotlinLightProjectDescriptor
import org.jetbrains.kotlin.psi.KtElement

class KotlinLightSanityProjectDescriptor : KotlinLightProjectDescriptor() {
    override fun configureModule(module: Module, model: ModifiableRootModel, contentEntry: ContentEntry) {
        addLibrary(AccessDeniedException::class.java, OrderRootType.CLASSES, model)
        addLibrary(PsiElement::class.java, OrderRootType.CLASSES, model)
        addLibrary(KtElement::class.java, OrderRootType.CLASSES, model)
    }
}
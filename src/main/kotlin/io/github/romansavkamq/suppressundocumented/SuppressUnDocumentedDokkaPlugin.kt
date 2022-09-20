package io.github.romansavkamq.suppressundocumented

import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.transformers.documentables.SuppressedByConditionDocumentableFilterTransformer
import org.jetbrains.dokka.model.DPackage
import org.jetbrains.dokka.model.DProperty
import org.jetbrains.dokka.model.Documentable
import org.jetbrains.dokka.plugability.DokkaContext
import org.jetbrains.dokka.plugability.DokkaPlugin

@Suppress("unused")
class SuppressUnDocumentedDokkaPlugin : DokkaPlugin() {
    val filter by extending {
        plugin<DokkaBase>().preMergeDocumentableTransformer providing ::SuppressUnDocumentedTransformer
    }
}

class SuppressUnDocumentedTransformer(
    context: DokkaContext
) : SuppressedByConditionDocumentableFilterTransformer(context) {
    override fun shouldBeSuppressed(d: Documentable): Boolean {
        return d !is DPackage && !d.hasDocumentation()
    }

    private fun Documentable.hasDocumentation(): Boolean {
        fun Documentable.hasDocumentation(): Boolean {
            return documentation.any { (_, node) -> node.children.isNotEmpty() }
        }
        return when (this) {
            is DProperty -> hasDocumentation() ||
                    getter?.hasDocumentation() ?: false ||
                    setter?.hasDocumentation() ?: false

            else -> hasDocumentation()
        }
    }
}

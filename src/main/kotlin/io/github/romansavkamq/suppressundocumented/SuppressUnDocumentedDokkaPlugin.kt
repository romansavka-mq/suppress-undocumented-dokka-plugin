package io.github.romansavkamq.suppressundocumented

import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.transformers.documentables.SuppressedByConditionDocumentableFilterTransformer
import org.jetbrains.dokka.model.DPackage
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
        return d !is DPackage && d.documentation.all { (_, node) -> node.children.isEmpty() }
    }
}

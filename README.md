# Suppress Undocumented Dokka plugin

This repository provides a [Dokka](https://github.com/Kotlin/dokka) plugin to generate docs only for documented members.

### Applying the plugin

In order to apply the plugin you need to add it to project dependencies:
```kotlin
dependencies {
    dokkaPlugin("io.github.romansavka-mq:suppress-undocumented-dokka-plugin:1.0")
}
```

After that you can run Dokka on this project and see the results. 
You should use a Dokka command you desire to write a plugin for (eg. `dokkaHtml`, `dokkaGfm` or other) with `--info` logging level.
In project logs you should see the name of a plugin:
```
...
Initializing plugins
Loaded plugins: [org.jetbrains.dokka.base.DokkaBase, io.github.romansavkamq.suppressundocumented.SuppressUnDocumentedDokkaPlugin]
Loaded: [
...
```

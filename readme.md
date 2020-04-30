# Structural Search for Kotlin IntelliJ IDEA plugin

Plugin that implements structural search support for the Kotlin language in IntelliJ IDEA.

## Getting started

- [IntelliJ IDEA help for Structural search and replace](https://www.jetbrains.com/help/idea/structural-search-and-replace.html).
- Predefined templates show what can be searched. They can be found:
    - In IntelliJ IDEA if the plugin is installed, **Edit | Find | Search Structurally...** then **Tools | Existing Templates... | Kotlin**.
    - In [`KotlinPredefinedConfigurations.kt`](src/main/kotlin/com/jetbrains/kotlin/structuralsearch/KotlinPredefinedConfigurations.kt).

## Design choices
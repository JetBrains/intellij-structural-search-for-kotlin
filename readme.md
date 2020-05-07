# Structural Search for Kotlin IntelliJ IDEA plugin

Plugin that implements structural search support for the Kotlin language in IntelliJ IDEA.

## Getting started

- [IntelliJ IDEA help for Structural search and replace](https://www.jetbrains.com/help/idea/structural-search-and-replace.html).
- Predefined templates show what can be searched. They can be found:
    - In IntelliJ IDEA if the plugin is installed, **Edit | Find | Search Structurally...** then **Tools | Existing Templates... | Kotlin**.
    - In [`KotlinPredefinedConfigurations.kt`](src/main/kotlin/com/jetbrains/kotlin/structuralsearch/KotlinPredefinedConfigurations.kt).

## Matching Behaviour

### Block Matching

- Block matching is strict: the pattern `{ foo = 1 }` will not match `{ foo = 2 \n foo = 1 }` in the code.
- Loose matching can be achieved with the pattern `{ $x$ \n foo = 1 \n $y$ }` and `[0; ∞]` count filters on `x` and `y`.

### Matching strings

Strings are divided into entries. For instance `"foo: $foo"` is composed of a `KtLiteralStringTemplateEntry` (`foo: `) and a `KtSimpleNameStringTemplateEntry` (`$foo`).
- `"$$entry$"` matches strings with one entry.
- `"$$before$${ $expr$ }$$after$"` with `[0; ∞]` count filters on `before` and `after` matches strings containing a `KtBlockStringTemplateEntry` (`${ expression }`).

### Call Matching

- Named value arguments are matched in any order
- Unnamed value arguments and type arguments are matched if the arguments are placed in the same order
- Loose matching can be achieved with the pattern `{ $x$ \n foo = 1 \n $y$ }` and `[0; ∞[` count filters on `x` and `y`.
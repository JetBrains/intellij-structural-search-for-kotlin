# Structural Search for Kotlin IntelliJ IDEA plugin

Plugin that implements structural search support for the Kotlin language in IntelliJ IDEA.

## Getting started

- [IntelliJ IDEA help for Structural search and replace](https://www.jetbrains.com/help/idea/structural-search-and-replace.html).
- Predefined templates show what can be searched. They can be found:
    - In IntelliJ IDEA if the plugin is installed, **Edit | Find | Search Structurally...** then **Tools | Existing Templates... | Kotlin**.
    - In [`KotlinPredefinedConfigurations.kt`](src/main/kotlin/com/jetbrains/kotlin/structuralsearch/KotlinPredefinedConfigurations.kt).

## Reporting issues

Issues can be reported [in YouTrack](https://youtrack.jetbrains.com/newIssue?project=KT&summary=Structural%20Search%3A&description=This%20pattern%3A%0A%60%60%60kt%0Afun%20%24x%24()%0A%60%60%60%0A%0AShould%20match%20the%20following%20code%3A%0A%60%60%60kt%0Afun%20foo()%20%7B%7D%0A%60%60%60).

## Matching Behaviour

### Block Matching

- Block matching is strict: the pattern `{ foo = 1 }` will not match `{ foo = 2 \n foo = 1 }` in the code.
- Loose matching can be achieved with the pattern `{ $x$ \n foo = 1 \n $y$ }` and `[0; ∞]` count filters on `x` and `y`.

### String Matching

Strings are divided into entries. For instance `"foo: $foo"` is composed of a `KtLiteralStringTemplateEntry` (`foo: `) and a `KtSimpleNameStringTemplateEntry` (`$foo`).
- `"$$entry$"` matches strings with one entry.
- Variables with text filters can be used in literals, but they must be separated with a space.
- `"$$before$${ $expr$ }$$after$"` with `[0; ∞]` count filters on `before` and `after` matches strings containing a `KtBlockStringTemplateEntry` (`${ expression }`).

### Call Matching

- Named value arguments are matched in any order.
- Unnamed value arguments and type arguments are matched if the arguments are placed in the same order.

### Object declaration matching

- Object searches match both companion objects and normal object declarations.

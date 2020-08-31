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

### Blocks

Series of instructions are matched strictly. The following pattern:

```kotlin
fun x() {
    val foo = 1
}
```

will not match the following piece of code:

```kotlin
fun x() {
    val foo = 1
    val bar = 2
}
```

Loose matching can be achieved using variables with a [0,∞] count filter to match surrounding instructions:

```kotlin
fun x() {
    $before$
    val foo = 1
    $after$
}
```


<!-- TODO: Remove comment when custom filters are in master
### Variable declarations

The `val` and `var` keywords aren’t taken into account by default. Type reference and initializer are optional. The following pattern:

```kotlin
val $x$: Int
```

will match the following piece of code:

```kotlin
var myVariable = 6
```

It is possible to match variables with custom getters or setters.
The `Kotlin — Property with explicit getter/setter` file type must be selected.
-->

### Strings

Strings in Kotlin are made of successive entries.
These entries can be:
 
```kotlin
"bug"       // literals
"\n"        // escapes
"$name"     // simple names
"${ name }" // blocks
```
In order to be able to match strings containing one of these entries, simple names with a variable match any entry.
The following template will match any string with only one entry (any of the previous four examples for instance):

```kotlin
“$$$name$”
```

The following template will match any string containing a block with one instruction
(with, again, [0,∞] count filters on `$before$` and `$after$`):

```kotlin
“$$$before$${ $instruction$ }$$$after$”
```

### Functions

In function calls, named value arguments are matched in any order. Unnamed value arguments and type arguments are matched if the arguments are placed in the same order.

In function declarations, expressions bodies (`fun x() = 1`) are matched with block bodies containing a return expression (`fun x() { return 1 }`).

### Object declarations

Object searches match both companion objects and normal object declarations.

### Binary expressions

Binary operations are matched with their functional counterparts.
The following pattern:

```kotlin
x + y
```

will match this piece of code:

```kotlin
x.plus(y)
```

Augmented assignments are matched with their binary and functional counterparts.
The following pattern:

```kotlin
x += y
```

will match these two lines of code:

```kotlin
x = x + y
x.plusEquals(y)
```
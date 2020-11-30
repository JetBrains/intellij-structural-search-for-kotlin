# Structural Search for Kotlin IntelliJ IDEA plugin (legacy)

Plugin that implements structural search support for the Kotlin language in IntelliJ IDEA. It is compatible with IDEA 2020.1 and 2020.2.

Kotlin Structural Search support is provided by the Kotlin plugin from IDEA 2020.3.

## Getting started

- [IntelliJ IDEA help for Structural search and replace](https://www.jetbrains.com/help/idea/structural-search-and-replace.html).
- Predefined templates show what can be searched. They can be found:
    - In IntelliJ IDEA if the plugin is installed, **Edit | Find | Search Structurally...** then **Tools | Existing Templates... | Kotlin**.
    - In [`KotlinPredefinedConfigurations.kt`](src/main/kotlin/com/jetbrains/kotlin/structuralsearch/KotlinPredefinedConfigurations.kt).

## Reporting issues

Issues can be reported [in YouTrack](https://youtrack.jetbrains.com/newIssue?project=KTIJ&summary=SSR%3A).

## Matching Behaviour

### Blocks

Series of instructions are matched strictly. The following template:

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


### Variable declarations

The `val` and `var` keywords aren’t taken into account by default. Type reference and initializer are optional. The following template:

```kotlin
val $x$: Int
```

will match the following piece of code:

```kotlin
var myVariable = 6
```

It is possible to match variables with custom getters or setters.
The `Kotlin — Property with explicit getter/setter` file type must be selected.

Type filters should be applied on name identifiers:

```kotlin
val $name$
```

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
The following template:

```kotlin
x + y
```

will match this piece of code:

```kotlin
x.plus(y)
```

Augmented assignments are matched with their binary and functional counterparts.
The following template:

```kotlin
x += y
```

will match these two lines of code:

```kotlin
x = x + y
x.plusEquals(y)
```
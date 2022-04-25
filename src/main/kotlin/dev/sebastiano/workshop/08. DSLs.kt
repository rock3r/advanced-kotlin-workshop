package dev.sebastiano.workshop

// TODO: create a DSL so that HtmlDslTest passes.
//
// Required DSL method names and expected toString output:
//
//      * html              -> <html>...</html>
//        * head            -> <head>...</head>
//          * title         -> <title>...</title>
//        * body            -> <body>...</body>
//          * h1            -> <h1>...</h1>
//          * orderedList   -> <ol>...</ol>
//            * listItem    -> <li>...</li>
//              * rawText   -> [text value]
//
// Where each ... in a tag is essentially the toString() of all child nodes.
// For example, the toString() of:
//
//      html { head { title("Title") } }
//
// Should be an equivalent to: <html><head><title>Title</title></head></html>
// (the tests use JSoup to normalize the resulting HTML to give you some freedom)

abstract class Tag(
    private val name: String,
    val children: MutableList<Tag> = mutableListOf()
) {

    override fun toString(): String {
        fun <T> Collection<T>.joinToStringIfNotEmpty() = if (isNotEmpty()) joinToString(separator = " ") else ""
        return "<$name>${children.joinToStringIfNotEmpty()}</$name>"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Tag

        if (name != other.name) return false
        if (children != other.children) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + children.hashCode()
        return result
    }
}

class Html : Tag("html")

class Head : Tag("head")
class Body : Tag("body")
class Title(text: String) : Tag("title", children = mutableListOf(RawText(text)))
class Header1(text: String) : Tag("h1", children = mutableListOf(RawText(text)))
class OrderedList : Tag("ol")
class ListItem : Tag("li")

class RawText(private val text: String) : Tag("[raw text]") {
    override fun toString(): String = text
}

package dev.sebastiano.workshop

fun html(creator: Html.() -> Unit): Html = Html().apply { creator() }

fun Html.head(creator: Head.() -> Unit) {
    addChild(Head(), creator, mustBeUnique = true)
}

fun Html.body(creator: Body.() -> Unit) {
    addChild(Body(), creator, mustBeUnique = true)
}

infix fun Head.title(text: String) {
    addChild(Title(text), {}, mustBeUnique = true)
}

fun Body.h1(text: String) {
    addChild(Header1(text), {})
}

fun Body.orderedList(creator: OrderedList.() -> Unit) {
    addChild(OrderedList(), creator)
}

fun OrderedList.listItem(creator: ListItem.() -> Unit) {
    addChild(ListItem(), creator)
}

fun ListItem.rawText(text: String) {
    addChild(RawText(text), {})
}

private inline fun <reified T : Tag> Tag.addChild(child: T, creator: T.() -> Unit, mustBeUnique: Boolean = false) {
    if (mustBeUnique && children.any { it is T }) {
        throw IllegalArgumentException("Unable to add child $child to $this since it already has a parent of type ${T::class.simpleName}")
    }

    children += child.apply { creator() }
}

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

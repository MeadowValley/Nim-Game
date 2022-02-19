package de.meadowvalley.index.html

import kotlinx.html.HTML
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.head
import kotlinx.html.title

fun HTML.index() {
    head {
        title("Nim-Game")
    }
    body {
        div {
            +"Nim-Game by MeadowValley"
        }
    }
}
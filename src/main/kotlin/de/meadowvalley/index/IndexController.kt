package de.meadowvalley.index

import de.meadowvalley.index.html.index
import io.ktor.application.call
import io.ktor.html.respondHtml
import io.ktor.http.HttpStatusCode
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import kotlinx.html.HTML

fun Route.indexRoute() {
    route("/") {
        get { call.respondHtml(HttpStatusCode.OK, HTML::index) }
    }
}
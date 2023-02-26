package dev.nikitatarasov.plugins

import io.ktor.http.HttpMethod
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.cors.routing.CORS

fun Application.configureCORS() {
    if (isDevMode()) {
        install(CORS) {
            allowMethod(HttpMethod.Post)

            // default vite dev server port
            allowHost("127.0.0.1:5173")
            allowHost("localhost:5173")

            // default vite preview server port
            allowHost("127.0.0.1:4173")
            allowHost("localhost:4173")
        }
    }

}

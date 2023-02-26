package dev.nikitatarasov.plugins

import io.ktor.server.application.Application

private var devMode = false
fun Application.configureDevMode() {
    devMode = developmentMode
}

fun isDevMode() = devMode


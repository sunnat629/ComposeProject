package dev.sunnat629

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
package dev.sunnat629

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "${platform.name}!"
    }
}
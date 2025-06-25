@file:JvmName("Log")

/**
 * Faking out some unit tests for classes with Log statements.
 */
package android.util

fun e(tag: String, msg: String, t: Throwable): Int {
    println("$tag: $msg")
    return 0
}

fun d(tag: String, msg: String): Int {
    println("$tag: $msg")
    return 0
}

fun w(tag: String, msg: String): Int {
    println("$tag: $msg")
    return 0
}
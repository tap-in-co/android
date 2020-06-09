package com.tapin.tapin.utils

fun String.toHoursAndMinutes(): String {
    if (this.contains(":")) {
        val splitByColon = this.split(":")
        if (splitByColon.size == 3) {
            return "${splitByColon[0]}:${splitByColon[1]}"
        }
    }
    return this
}
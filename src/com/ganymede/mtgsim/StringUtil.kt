package com.ganymede.mtgsim

fun String.countOccurancesOf(other: String): Int = this.windowed(size = other.length).count { it == other }
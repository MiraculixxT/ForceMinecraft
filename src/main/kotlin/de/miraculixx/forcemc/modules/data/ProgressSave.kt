package de.miraculixx.forcemc.modules.data

data class ProgressSave<T>(val remaining: MutableList<T> = mutableListOf(), val finished: MutableList<T> = mutableListOf())
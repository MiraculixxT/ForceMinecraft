package de.miraculixx.forcemc.modules.data

data class ProgressSave<T>(val remaining: MutableList<T> = mutableListOf(), val finished: MutableList<T> = mutableListOf()) {
    fun clear() {
        remaining.clear()
        finished.clear()
    }
    fun addFinished(obj: T) {
        remaining.remove(obj)
        finished.add(obj)
    }
    fun addFinished(obj: List<T>) {
        remaining.removeAll(obj)
        finished.addAll(obj)
    }
}
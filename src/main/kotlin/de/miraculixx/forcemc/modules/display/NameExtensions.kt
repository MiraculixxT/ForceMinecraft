package de.miraculixx.forcemc.modules.display

fun fancy(s: String): String {
    return buildString {
        s.forEachIndexed { index, c ->
            when {
                index == 0 -> append(c.uppercase())
                c == '_' -> append(' ')
                s[(index - 1).coerceIn(s.indices)] == ' ' -> append(c.uppercase())
            }
        }
    }
}
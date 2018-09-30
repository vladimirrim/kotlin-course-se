package ru.hse.spb

fun getSuffix(str: String): String? {
    return when {
        str.endsWith("etr") -> "etr"
        str.endsWith("etra") -> "etra"
        str.endsWith("initis") -> "initis"
        str.endsWith("inites") -> "inites"
        str.endsWith("lios") -> "lios"
        str.endsWith("liala") -> "liala"
        else -> null
    }
}

fun checkSameGender(prev: String?, cur: String?): Boolean {
    val woman = arrayOf("etra", "inites", "liala")
    val man = arrayOf("etr", "initis", "lios")

    return (prev in man && cur in man) || (prev in woman && cur in woman)
}


fun checkOrder(prevString: String?, curString: String?): Boolean {
    val nouns = arrayOf("etr", "etra")
    val verbs = arrayOf("initis", "inites")
    val adjs = arrayOf("lios", "liala")

    if (prevString == null)
        return true

    if (!checkSameGender(prevString, curString))
        return false

    return when (prevString) {
        "etr", "etra", "initis", "inites" -> curString in verbs

        "lios", "liala" -> (curString in nouns || curString in adjs)

        else -> false
    }
}

fun main(args: Array<String>) {
    var prevString: String? = null
    var answer = true
    var hasNoun = false
    val nouns = arrayOf("etr", "etra")
    val words = readLine()!!.split(' ')

    if (words.size == 1) {
        if (getSuffix(words[0]) != null)
            print("YES")
        else
            print("NO")
        return
    }
    run breaker@{
        words.forEach { curString ->
            val suffix = getSuffix(curString)
            if (suffix == null || !checkOrder(prevString, suffix)) {
                answer = false
                return@breaker
            }
            if (suffix in nouns)
                hasNoun = true
            prevString = suffix
        }
    }
    if (answer && hasNoun)
        print("YES")
    else
        print("NO")
}
package ru.hse.spb

fun getSuffix(str: String): String {
    return when {
        str.endsWith("etr") -> "etr"
        str.endsWith("etra") -> "etra"
        str.endsWith("initis") -> "initis"
        str.endsWith("inites") -> "inites"
        str.endsWith("lios") -> "lios"
        str.endsWith("liala") -> "liala"
        else -> ""
    }
}

fun checkSameGender(prev: String, cur: String): Boolean {
    val woman = arrayOf("etra", "inites", "liala")
    val man = arrayOf("etr", "initis", "lios")

    return (prev in man && cur in man) || (prev in woman && cur in woman)
}


fun checkOrder(prevString: String, curString: String): Boolean {
    val nouns = arrayOf("etr", "etra")
    val verbs = arrayOf("initis", "inites")
    val adjs = arrayOf("lios", "liala")

    return when (prevString) {
        "" -> true

        "etr", "etra", "initis", "inites" -> curString in verbs && checkSameGender(prevString, curString)

        "lios", "liala" -> (curString in nouns || curString in adjs) && checkSameGender(prevString, curString)

        else -> false
    }
}

fun main(args: Array<String>) {
    var prevString = ""
    var ans = true
    var wordCount = 0
    var hasNoun = false
    val nouns = arrayOf("etr", "etra")

    readLine()!!.split(' ').forEach { curString ->
        val suf = getSuffix(curString)
        if (suf == "" || !checkOrder(prevString, suf)) {
            ans = false
        }
        wordCount++
        if (suf in nouns)
            hasNoun = true
        prevString = suf
    }
    if (ans && (wordCount == 1 || hasNoun))
        print("YES")
    else
        print("NO")
}
package year_2016.day05

import java.math.BigInteger
import java.security.MessageDigest


fun main() {
    val input = "reyedfim"

//    println(part1(input))
    println(part2(input))
}

fun part1(input: String): String {

    var index = 0
    val pwd = mutableListOf<Char>()
    while (true) {
        val md5 = md5("$input$index")
        if (md5.startsWith("00000")) {
            pwd.add(md5[5])
        }
        if (pwd.size == 8) return pwd.joinToString("")
        index++
    }


}

fun part2(input: String): String {
    var index = 0
    val pwd = mutableMapOf<Int, Char>()
    while (true) {
        val md5 = md5("$input$index")
        if (md5.startsWith("00000")) {
            val pos = md5[5]
            if (pos.isDigit() && pos.digitToInt() < 8) {
                if (pwd[pos.digitToInt()] == null) {
                    pwd[md5[5].digitToInt()] = md5[6]
                }
            }
        }
        if (pwd.size == 8)
            return pwd.keys.sorted().map { pwd[it] }.joinToString("")
        index++
    }
}

fun md5(input: String): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
}
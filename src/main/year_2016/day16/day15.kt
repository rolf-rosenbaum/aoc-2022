package year_2016.day16

const val input = "11110010111001001"

fun main() {
    val len1 = 272
    val len2 = 35651584

    println(part1(input, len1))
    println(part1(input, len2))
}


fun part1(input: String, len: Int): String {
    var b = input
    while (b.length < len)
        b = "${b}0${b.reversed().process()}"

    val data = b.substring(0, len)

    val checksum = data.checksum()

    return checksum
}

fun part2(input: String): Int {
    return 0
}


fun String.process() = this.map { if (it == '0') '1' else '0' }.joinToString("")

fun String.checksum(): String {
    var foo = this.windowed(2, 2).map {
        if (it.first() == it.last()) '1' else '0'
    }.joinToString("")

    while (foo.length % 2 == 0)
        foo = foo.windowed(2, 2).map {
            if (it.first() == it.last()) '1' else '0'
        }.joinToString("")

    return foo
}
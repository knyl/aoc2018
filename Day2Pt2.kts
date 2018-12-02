#!/usr/bin/env kscript

import java.io.File

if (args.size != 1) {
    System.out.println("Need to provide an input file!")
    System.exit(1)
}
val fileName = args.toList().get(0)

val lines = File(fileName).readLines()
lateinit var words : Pair<String, String>

for (i in lines.indices) {
    for (j in lines.indices) {
        if (hammingDistance(lines.get(i), lines.get(j)) == 1) {
            words = Pair(lines.get(i), lines.get(j))
            break;
        }
    }
}

val result = findCommonLetters(words.first, words.second)

System.out.println("Result: $result")

fun hammingDistance(w1: String, w2: String) : Int {
    var sum = 0
    for (i in w1.indices) {
        if (w1.get(i) != w2.get(i)) {
            sum += 1
        }
    }
    return sum
    // "Bad type on operand stack" on the code below. Wtf
    //return w1.zip(w2).map { p -> p.first.equals(p.second) }.filter { it -> !it }.count()
}

fun findCommonLetters(w1: String, w2: String) : String {
    var res = ""
    for (i in w1.indices) {
        if (w1.get(i) == w2.get(i)) {
            res += w1.get(i)
        }
    }
    return res
    // "Bad type on operand stack" on the code below. Wtf
    //return w1.zip(w2).filter { p -> p.first.equals(p.second) }.map { p -> p.first }.toList().toString()
}

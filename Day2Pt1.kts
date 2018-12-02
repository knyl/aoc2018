#!/usr/bin/env kscript

import java.io.File

if (args.size != 1) {
    System.out.println("Need to provide an input file!")
    System.exit(1)
}
val fileName = args.toList().get(0)

val lines = File(fileName).readLines()

val nrOfTwoIdentical = lines.map { it -> areTwoLettersTheSame(it)}.filter { it -> it}.count()
val nrOfThreeIdentical = lines.map { it -> areThreeLettersTheSame(it)}.filter { it -> it }.count()

val result = nrOfThreeIdentical * nrOfTwoIdentical

System.out.println("Result: $result")

fun areTwoLettersTheSame(s: String): Boolean {
    val map = mutableMapOf<Char, Int>()
    s.map { c -> map.put(c, map.getOrDefault(c, 0) + 1) }
    return map.values.filter { v -> v == 2 }.isNotEmpty()
}

fun areThreeLettersTheSame(s: String): Boolean {
    val map = mutableMapOf<Char, Int>()
    s.map { c -> map.put(c, map.getOrDefault(c, 0) + 1) }
    return map.values.filter { v -> v == 3 }.isNotEmpty()
}

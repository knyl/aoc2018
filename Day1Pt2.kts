#!/usr/bin/env kscript

import java.io.File

if (args.size != 1) {
    System.out.println("Need to provide an input file!")
    System.exit(1)
}
val fileName = args.toList().get(0)

val numbers = File(fileName).readLines().map { it -> it.toInt() }


val result = findRepeatingFrequency(numbers)

System.out.println("Result: $result")

fun findRepeatingFrequency(numbers: List<Int>): Int {
    val frequencies = mutableSetOf<Int>()
    frequencies.add(0)

    var currentFrequency = 0

    while (true) {
        for (n in numbers) {
            currentFrequency += n;
            if (frequencies.contains(currentFrequency)) {
                return currentFrequency
            }
            frequencies.add(currentFrequency)
        }
    }
}
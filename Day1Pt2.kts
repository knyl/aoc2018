#!/usr/bin/env kscript

import java.io.File

if (args.size != 1) {
    System.out.println("Need to provide an input file!")
    System.exit(1)
}
val fileName = args.toList().get(0)

val numbers = File(fileName).readLines().map { it -> it.toInt() }

var solutionNotFound = true

var frequencies : Set<Int> = HashSet()
frequencies = frequencies.plus(0)
var currentFrequency : Int = 0

while (solutionNotFound) {
    for (n in numbers) {
        currentFrequency += n;
        //System.out.println("n: $n $currentFrequency")
        if (frequencies.contains(currentFrequency)) {
            solutionNotFound = false
            break
        }
        frequencies = frequencies.plus(currentFrequency)
    }
}
val result = currentFrequency

System.out.println("Result: $result")
#!/usr/bin/env kscript

import java.io.File
import java.time.LocalDate

if (args.size != 1) {
    System.out.println("Need to provide an input file!")
    System.exit(1)
}
val fileName = args.toList().get(0)

val originalString = File(fileName).readLines().get(0)
var string = originalString

var currInd = 1

while (currInd != string.length - 1) {
    if (string[currInd].toUpperCase() == string[currInd - 1].toUpperCase() && string[currInd] != string[currInd - 1]) {
        string = string.removeRange(currInd - 1, currInd+1)
        currInd = Integer.max(1, currInd - 1)
    } else {
        currInd += 1
    }
}

val result = string.length


System.out.println("Result: $result")

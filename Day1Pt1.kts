#!/usr/bin/env kscript

import java.io.File

if (args.size != 1) {
    System.out.println("Need to provide an input file!")
    System.exit(1)
}
val fileName = args.toList().get(0)

val result = File(fileName).readLines().map { it -> it.toInt() }.sum()

System.out.println("Result: $result")
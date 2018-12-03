#!/usr/bin/env kscript

import java.io.File

if (args.size != 1) {
    System.out.println("Need to provide an input file!")
    System.exit(1)
}
val fileName = args.toList().get(0)

val lines = File(fileName).readLines()

data class Instruction(val id : String, val coords : Pair<Int, Int>, val size : Pair<Int, Int>)

val size = 1001
val fabric = Array(size, {IntArray(size)})

// #1 @ 1,3: 4x4

val instructions = lines
        .map { it -> it.replace("@", "") }
        .map {it -> it.replace(":", "")}
        .map {it -> it.replace("  ", " ")}
        .map { it -> it.split(" ")}
        .map {it -> parseData(it)}

instructions.forEach { inst ->
    val (x, y) = inst.coords
    val (x_len, y_len) = inst.size
    for (i in x..x+x_len-1) {
        for (j in y..y+y_len-1) {
            fabric[i][j] = fabric[i][j] + 1
        }
    }
}

var sum = 0

for (i in fabric.indices) {
    for (j in fabric[i].indices) {
        if (fabric[i][j] > 1) {
            sum += 1
        }
        //System.out.print(fabric[i][j])
    }
    //System.out.println()
}

System.out.println("Result: $sum")

fun parseData(list : List<String>) : Instruction {
    val id = list.get(0)
    val (x, y) = list.get(1).split(",")
    val (x_len, y_len) = list.get(2).split("x")
    return Instruction(id, Pair(x.toInt(), y.toInt()), Pair(x_len.toInt(), y_len.toInt()))

}


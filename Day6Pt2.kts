#!/usr/bin/env kscript

import java.io.File
import kotlin.math.absoluteValue

if (args.size != 1) {
    System.out.println("Need to provide an input file!")
    System.exit(1)
}
val fileName = args.toList().get(0)

val coordinates = File(fileName).readLines()
        .map { l ->
            l.split(",")
                    .map { it.trim() }
                    .map { str -> str.toInt() }
        }
        .map { l -> Pair(l.get(0), l.get(1)) }

val (maxXC, _) = coordinates.maxBy { it.first }!!
val (minXC, _) = coordinates.minBy { it.first }!!
val (_, maxYC) = coordinates.maxBy { it.second }!!
val (_, minYC) = coordinates.minBy { it.second }!!

val maxX = maxXC + 10
val maxY = maxYC + 10
val minX = minXC - 10
val minY = minYC - 10

val maxDistance = 10000

val field: MutableMap<Pair<Int, Int>, Int> = mutableMapOf()

for (i in minX..maxX) {
    for (j in minY..maxY) {
        val point = Pair(i, j)
        coordinates.forEach {
            val distance = distance(point, it)
            field[point] = distance + field.getOrDefault(point, 0)
        }
    }
}
val result = field.values.filter { it < maxDistance }.size

System.out.println("Result: $result")

fun distance(p1: Pair<Int, Int>, p2: Pair<Int, Int>): Int {
    return (p1.first - p2.first).absoluteValue + (p1.second - p2.second).absoluteValue
}
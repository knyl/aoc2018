#!/usr/bin/env kscript

import java.io.File
import java.time.LocalDate
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

val maxX = maxXC + 0
val maxY = maxYC + 0
val minX = minXC - 0
val minY = minYC - 0

val field: MutableMap<Pair<Int, Int>, Int> = mutableMapOf()
val count: MutableMap<Pair<Int, Int>, MutableList<Pair<Int, Int>>> = mutableMapOf()

for (i in minX..maxX) {
    for (j in minY..maxY) {
        val point = Pair(i, j)
        coordinates.forEach {
            val distance = distance(point, it)
            val currentMinDist = field.getOrDefault(point, Integer.MAX_VALUE)
            if (currentMinDist == distance) {
                val points = count.getOrDefault(point, mutableListOf())
                points.add(it)
            } else if (distance < currentMinDist) {
                //System.out.println("larger $point curr $currentMinDist dist $distance coord $it")
                field[point] = distance
                count.put(point, mutableListOf(it))
            }
        }
    }
}

val excludingNotDistinct: MutableList<Pair<Int, Int>> = mutableListOf()

// TODO: This is not correct, some of these might not be infinite
for (i in minX..maxX) {
    val point = Pair(i, maxY)
    if (count.get(point)!!.size == 1) {
        excludingNotDistinct.addAll(count.get(point)!!)
    }
}
for (i in minX..maxX) {
    val point = Pair(i, minY)
    if (count.get(point)!!.size == 1) {
        excludingNotDistinct.addAll(count.get(point)!!)
    }
}
for (i in minY..maxY) {
    val point = Pair(maxX, i)
    if (count.get(point)!!.size == 1) {
        excludingNotDistinct.addAll(count.get(point)!!)
    }
}
for (i in minY..maxY) {
    val point = Pair(minX, i)
    if (count.get(point)!!.size == 1) {
        excludingNotDistinct.addAll(count.get(point)!!)
    }
}

val excluding = excludingNotDistinct.distinct()

val areaCount: MutableMap<Pair<Int, Int>, Int> = mutableMapOf()

for (i in minX..maxX) {
    for (j in minY..maxY) {
        val point = Pair(i, j)
        if (count.get(point)!!.size == 1 && !excluding.contains(count.get(point)!!.get(0))) {
            val coord = count.get(point)!!.get(0)
            areaCount[coord] = areaCount.getOrDefault(coord, 0) + 1
        }
    }
}
/*
for (i in minX..maxX) {
    for (j in minY..maxY) {
        val point = Pair(i, j)
        if (point == Pair(5, 5)) {
            System.out.println(count.get(point))
        }
        if (!excluding.contains(point) && count.get(point)!!.size == 1) {
            val list = count.get(point)!!.get(0)
            System.out.print("$list ")
        }
    }
    System.out.println("")
}
*/

val result = areaCount.maxBy { it.value }

System.out.println("Result: $result")

fun distance(p1: Pair<Int, Int>, p2: Pair<Int, Int>): Int {
    return (p1.first - p2.first).absoluteValue + (p1.second - p2.second).absoluteValue
}
#!/usr/bin/env kscript

import java.io.File
import kotlin.math.absoluteValue

if (args.size != 1) {
    System.out.println("Need to provide an input file!")
    System.exit(1)
}
val fileName = args.toList().get(0)

val lines = File(fileName).readLines().filter { it.isNotEmpty() }

val data = lines.map { parseLine(it) }
var points: Map<Pair<Int, Int>, Pair<Int, Int>> = data.toMap()

var sizeDecreasing = true
var height = Int.MAX_VALUE
var time = 0

while (sizeDecreasing) {
    time++
    val newPoints = tick(points)
    val minX = newPoints.minBy { it.key.first }!!.key.first
    val maxX = newPoints.maxBy { it.key.first }!!.key.first
    val newHeight = (maxX - minX).absoluteValue
    sizeDecreasing = height > newHeight
    height = newHeight
    if (!sizeDecreasing) {
        output(points)
    }
    points = newPoints
}

System.out.println("Result: ${time-1}")

fun parseLine(it: String): Pair<Pair<Int, Int>, Pair<Int, Int>> {
    val (x, y, vel_x, vel_y) = """position=< *([-]?\d+), *([-]?\d+)> velocity=< *([-]?\d+), *([-]?\d+)>""".toRegex().matchEntire(it)!!.destructured
    return Pair(Pair(x.trim().toInt(), y.trim().toInt()), Pair(vel_x.trim().toInt(), vel_y.trim().toInt()))
}

fun tick(data: Map<Pair<Int, Int>, Pair<Int, Int>>): Map<Pair<Int, Int>, Pair<Int, Int>> {
    return data.map { Pair(Pair(it.key.first + it.value.first, it.key.second + it.value.second), it.value) }.toMap()
}

fun output(data: Map<Pair<Int, Int>, Pair<Int, Int>>) {
    val minX = data.minBy { it.key.first }!!.key.first
    val minY = data.minBy { it.key.second }!!.key.second
    val maxX = data.maxBy { it.key.first }!!.key.first
    val maxY = data.maxBy { it.key.second }!!.key.second

    val points = data.map { it.key }.toSet()
    for (j in minY..maxY) {
        for (i in minX..maxX) {
            if (points.contains(Pair(i, j))) {
                System.out.print("H")
            } else {
                System.out.print(".")
            }
        }
        System.out.println()
    }
}

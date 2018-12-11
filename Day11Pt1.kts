#!/usr/bin/env kscript

if (args.size != 1) {
    System.out.println("Need to provide input!")
    System.exit(1)
}
val serialNumber = args.toList().get(0).toInt()
val grid : MutableMap<Pair<Int, Int>, Int> = mutableMapOf()

val gridSize = 300

for (i in 1..gridSize) {
    for (j in 1..gridSize) {
        val powerLevel = calculatePowerLevel(i, j, serialNumber)
        grid[Pair(i, j)] = powerLevel + grid.getOrDefault(Pair(i, j), 0)
        grid[Pair(i, j-1)] = powerLevel + grid.getOrDefault(Pair(i, j-1), 0)
        grid[Pair(i, j+1)] = powerLevel + grid.getOrDefault(Pair(i, j+1), 0)
        grid[Pair(i-1, j)] = powerLevel + grid.getOrDefault(Pair(i-1, j), 0)
        grid[Pair(i-1, j-1)] = powerLevel + grid.getOrDefault(Pair(i-1, j-1), 0)
        grid[Pair(i-1, j+1)] = powerLevel + grid.getOrDefault(Pair(i-1, j+1), 0)
        grid[Pair(i+1, j)] = powerLevel + grid.getOrDefault(Pair(i+1, j), 0)
        grid[Pair(i+1, j-1)] = powerLevel + grid.getOrDefault(Pair(i+1, j-1), 0)
        grid[Pair(i+1, j+1)] = powerLevel + grid.getOrDefault(Pair(i+1, j+1), 0)
    }
}
var maxPowerEntry = grid.maxBy { it.value }!!

System.out.println("Result: ${maxPowerEntry}")

fun calculatePowerLevel(x: Int, y: Int, serialNumber: Int): Int {
    val rackId = (x + 10)
    val powerLevel = (rackId * y + serialNumber) * rackId
    val levelAsString = powerLevel.toString()
    val hundredDigit = levelAsString.length-3
    return levelAsString.getOrElse(hundredDigit) { '0' }.toString().toInt() - 5
}

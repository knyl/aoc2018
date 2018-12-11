#!/usr/bin/env kscript

if (args.size != 1) {
    System.out.println("Need to provide input!")
    System.exit(1)
}
val serialNumber = args.toList().get(0).toInt()
val grid: Array<Array<Int>> = Array(302) { Array(302, { 0 }) }

val gridSize = 300

for (i in 1..gridSize) {
    for (j in 1..gridSize) {
        val powerLevel = calculatePowerLevel(i, j, serialNumber)
        grid[i][j] = powerLevel
    }
}

var maxPower = Int.MIN_VALUE
var maxPoint = Pair(-1, -1)
var maxSize = -1

for (i in 1..gridSize) {
    for (j in 1..gridSize) {
        var sumPower = grid[i][j]
        val size = getMaxSize(i, j, gridSize)
        if (size > 0) {
            val powers = (1..size).map { Pair(it, sumPower(grid, i, j, it)) }
            for ((s, p) in powers) {
                sumPower += p
                if (sumPower > maxPower) {
                    maxPower = sumPower
                    maxPoint = Pair(i, j)
                    maxSize = s + 1
                }
            }
        }
    }
}

System.out.println("Result: $maxPoint $maxSize $maxPower")

fun sumPower(grid: Array<Array<Int>>, x: Int, y: Int, size: Int): Int {
    return grid[x + size].sliceArray(y..y + size).sum() +
            grid.sliceArray(x until x + size).map { it[y + size] }.sum()

}

fun calculatePowerLevel(x: Int, y: Int, serialNumber: Int): Int {
    val rackId = (x + 10)
    val powerLevel = (rackId * y + serialNumber) * rackId
    val levelAsString = powerLevel.toString()
    val hundredDigit = levelAsString.length - 3
    return levelAsString.getOrElse(hundredDigit) { '0' }.toString().toInt() - 5
}

fun getMaxSize(i: Int, j: Int, gridSize: Int): Int {
    return minOf(maxOf(gridSize - i, 0), maxOf(gridSize - j, 0))
}

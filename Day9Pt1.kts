#!/usr/bin/env kscript

import java.io.File

if (args.size != 1) {
    System.out.println("Need to provide an input file!")
    System.exit(1)
}
val fileName = args.toList().get(0)

val lines = File(fileName).readLines()

val (nrPlayers, lastMarble) = """(\d+) players; last marble is worth (\d+) points""".toRegex().matchEntire(lines.get(0))!!.destructured

val game: MutableList<Int> = mutableListOf()
val score: Array<Int> = Array(nrPlayers.toInt()) { 0 }
var currentMarble = 1
var currentInd = 1
game.add(0)
game.add(1)

for (marble in 2..lastMarble.toInt()) {
    if ((marble % 23) == 0) {
        val currentPlayer = currentMarble % nrPlayers.toInt()
        val newInd = if (currentInd - 7 < 0) game.size + (currentInd - 7) else currentInd - 7
        val removedMarble = game[newInd]
        game.removeAt(newInd)
        currentInd = newInd
        score[currentPlayer] += removedMarble + marble
    } else {
        val newInd = (currentInd + 2) % game.size
        game.add(newInd, marble)
        currentInd = newInd
    }
    currentMarble += 1
}


System.out.println("Result: ${score.max()}")


#!/usr/bin/env kscript

import java.io.File

if (args.size != 1) {
    System.out.println("Need to provide an input file!")
    System.exit(1)
}
val fileName = args.toList().get(0)

val lines = File(fileName).readLines()

val (nrPlayers, lastMarble) = """(\d+) players; last marble is worth (\d+) points""".toRegex().matchEntire(lines.get(0))!!.destructured

val score: Array<Long> = Array(nrPlayers.toInt()) { 0.toLong() }
var currentMarble = 1
val node0 = Node(null, null, 0)
val node1 = Node(node0, node0, 1)
node0.prev = node1
node0.next = node1
var currentNode = node1

for (marble in 2..lastMarble.toInt() * 100) {
    if ((marble % 23) == 0) {
        val currentPlayer = currentMarble % nrPlayers.toInt()
        val nodeToRemove = currentNode.prev!!.prev!!.prev!!.prev!!.prev!!.prev!!.prev
        score[currentPlayer] += nodeToRemove!!.elem.toLong() + marble
        currentNode = nodeToRemove.next!!
        remove(nodeToRemove)
    } else {
        currentNode = insert(currentNode.next!!, marble)
    }
    currentMarble += 1
}


System.out.println("Result: ${score.max()}")

data class Node(var prev: Node?, var next: Node?, val elem: Int)

fun remove(node: Node) {
    val prev = node.prev
    val next = node.next
    prev!!.next = next
    next!!.prev = prev
}

fun insert(node: Node, value: Int): Node {
    val nextNode = node.next!!
    val newNode = Node(node, nextNode, value)
    nextNode.prev = newNode
    node.next = newNode
    return newNode
}

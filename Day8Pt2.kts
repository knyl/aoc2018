#!/usr/bin/env kscript

import java.io.File

if (args.size != 1) {
    System.out.println("Need to provide an input file!")
    System.exit(1)
}
val fileName = args.toList().get(0)

val lines = File(fileName).readLines()

val data = lines.get(0).split(" ").map { it.toInt() }

val children: MutableMap<Int, List<Int>> = mutableMapOf()
val metadata: MutableMap<Int, List<Int>> = mutableMapOf()
val hasChildren: MutableMap<Int, Boolean> = mutableMapOf()
val nodeValues : MutableMap<Int, Int> = mutableMapOf()

var currentNode = 0
var nodeNr = 0
val parent = 0

// för varje nod, läs header, läs antal
val nrOfChildren = data.get(currentNode)
val nrOfMetadata = data.get(currentNode + 1)
currentNode += 2
nodeNr += 1
for (childNr in 1..nrOfChildren) {
    children[parent] = children.getOrDefault(parent, listOf()).plus(nodeNr + childNr - 1)
    val (c, n) = readChild(currentNode, nodeNr + childNr - 1)
    currentNode = c
    nodeNr = n
}
for (metadataInd in 0 until nrOfMetadata) {
    val value = data.get(currentNode + metadataInd)
    metadata[parent] = metadata.getOrDefault(parent, listOf()).plus(value)
}

val rootNode = 0
val result = countData(rootNode)

System.out.println("Result: ${result}")

fun readChild(currentIndex: Int,
              parent: Int): Pair<Int, Int> {

    var index = currentIndex;
    var currentNode = parent
    val nrOfChildren = data.get(index)
    val nrOfMetadata = data.get(index + 1)


    index += 2

    for (childNr in 1..nrOfChildren) {
        children[parent] = children.getOrDefault(parent, listOf()).plus(currentNode + 1)

        val (ind, nodeN) = readChild(index, currentNode + 1)
        index = ind
        currentNode = nodeN
    }

    hasChildren[parent] = nrOfChildren > 0

    for (metadataInd in 0 until nrOfMetadata) {
        val value = data.get(index + metadataInd)
        metadata[parent] = metadata.getOrDefault(parent, listOf()).plus(value)

    }
    return Pair(index + nrOfMetadata, currentNode)
}

fun countData(node: Int): Int {

    if (nodeValues.contains(node)) {
        return nodeValues[node]!!
    }

    var sum = 0
    val nodeMetadata = metadata.getOrDefault(node, mutableListOf())
    if (children.containsKey(node)) {
        val nodeChildren = children.get(node)!!
        for (i in nodeMetadata) {
            if (i != 0 && nodeChildren.size >= i) {
                sum += countData(nodeChildren.get(i-1))
            }
        }
    } else {
        if (metadata.containsKey(node)) {
            val value = nodeMetadata.sum()
            nodeValues[node] = value
            return value
        }
    }
    nodeValues[node] = sum
    return sum
}

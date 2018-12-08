#!/usr/bin/env kscript

import java.io.File

if (args.size != 1) {
    System.out.println("Need to provide an input file!")
    System.exit(1)
}
val fileName = args.toList().get(0)

val lines = File(fileName).readLines()

val data = lines.get(0).split(" ").map { it.toInt() }

val children : MutableMap<Int, List<Int>> = mutableMapOf()
val metadata : MutableMap<Int, List<Int>> = mutableMapOf()

var currentNode = 0
var nodeNr = 0
var sum = 0

// för varje nod, läs header, läs antal
val nrOfChildren = data.get(currentNode)
val nrOfMetadata = data.get(currentNode+1)
currentNode += 2
for (childNr in 1..nrOfChildren) {
    System.out.println("Child nr $childNr")
    val (c, n) = readChild(currentNode, 0)
    currentNode = c
    nodeNr = n
}
//System.out.println("NrOfMetadata $nrOfMetadata")
for (metadataInd in 0 until nrOfMetadata) {
    val get = data.get(currentNode + metadataInd)
    // System.out.println("Metadata: $get")
    sum += get
}

//System.out.println("Finishednode $currentNode")


System.out.println("Result: ${sum}")

fun readChild(currentIndex: Int,
              parent : Int) : Pair<Int, Int> {

    var index = currentIndex;
    var currentNode = parent
    val nrOfChildren = data.get(index)
    val nrOfMetadata = data.get(index + 1)

    //System.out.println("parent $parent current index $index")
    //System.out.println("Children $nrOfChildren metadata $nrOfMetadata")

    index += 2

    for (childNr in 1..nrOfChildren) {
        //System.out.println("Child nr $childNr")
        children[parent] = children.getOrDefault(parent, listOf()).plus(parent+childNr)

        if (nrOfChildren > 0) {
            val (ind, nodeN) = readChild(index, parent + 1)
            index = ind
            currentNode = nodeN
        }

    }
    //System.out.println("NrOfMetadata $nrOfMetadata")
    for (metadataInd in 0 until nrOfMetadata) {
        val get = data.get(index + metadataInd)
        //System.out.println("Metadata: $get")
        sum += get

    }
    return Pair(index + nrOfMetadata, currentNode)
}

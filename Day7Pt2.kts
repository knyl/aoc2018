#!/usr/bin/env kscript

import java.io.File

if (args.size != 1) {
    System.out.println("Need to provide an input file!")
    System.exit(1)
}
val fileName = args.toList().get(0)

val lines = File(fileName).readLines()

val stepRequires: MutableMap<String, MutableList<String>> = mutableMapOf()
val nextSteps: MutableMap<String, MutableList<String>> = mutableMapOf()
val visited: MutableSet<String> = mutableSetOf()
val allSteps: MutableSet<String> = mutableSetOf()

for (l in lines) {
    val (s1, s2) = "Step ([A-Z]) must be finished before step ([A-Z]) can begin.".toRegex().matchEntire(l)!!.destructured
    val required = stepRequires.getOrDefault(s2, mutableListOf())
    required.add(s1)
    stepRequires[s2] = required

    val next = nextSteps.getOrDefault(s1, mutableListOf())
    next.add(s2)
    nextSteps[s1] = next
    allSteps.add(s1)
    allSteps.add(s2)
}

val originalSteps = allSteps.size

var starting = allSteps.filter { !stepRequires.containsKey(it) }.toMutableSet()

val result : MutableList<String> = mutableListOf()
val workingOn : Array<String> = Array(5) {""}
val timeLeft : Array<Int> = Array(5) {-1}

val freeWorkers : MutableList<Int> = mutableListOf()
freeWorkers.add(0)
freeWorkers.add(1)
freeWorkers.add(2)
freeWorkers.add(3)
freeWorkers.add(4)

val workDone : MutableSet<String> = mutableSetOf()
var time = 0

while (workDone.size != originalSteps ) {
    time += 1

    for (i in timeLeft.indices) {
        if (timeLeft[i] == 0) {
            timeLeft[i] = -1
            freeWorkers.add(i)
            val step = workingOn[i]
            result.add(step)

            val nextSteps = nextSteps.getOrDefault(step, mutableListOf())

            starting.addAll(nextSteps)
            workDone.add(step)
        }
    }

    while (freeWorkers.isNotEmpty() && starting.isNotEmpty()) {
        val step: String = getStart(starting)
        starting.remove(step)

        if (!visited.contains(step) && hasAllRequired(step, stepRequires, workDone)) {
            val nextWorker = freeWorkers.get(0)
            freeWorkers.removeIf { it == nextWorker }


            workingOn[nextWorker] = step
            timeLeft[nextWorker] = 60 + step.codePointAt(0) - 64

            visited.add(step)
        }
    }

    for (i in timeLeft.indices) {
        if (timeLeft[i] > 0) {
            timeLeft[i] -= 1
        }
    }

}

System.out.println("Result: ${time-1}")

fun getStart(starting: MutableSet<String>): String {
    return starting.sorted().get(0)
}

fun hasAllRequired(step: String, stepRequires: MutableMap<String, MutableList<String>>, visited: MutableSet<String>): Boolean {
    val requires = stepRequires.getOrDefault(step, mutableListOf())
    return visited.containsAll(requires)
}

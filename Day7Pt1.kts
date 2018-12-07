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

var starting = allSteps.filter { !stepRequires.containsKey(it) }.toMutableSet()

val result : MutableList<String> = mutableListOf()

while (starting.isNotEmpty()) {
    val step : String = getStart(starting)
    starting.remove(step)
    if (!visited.contains(step) && hasAllRequired(step, stepRequires, visited)) {
        result.add(step)
        System.out.println("Visiting $step")
        visited.add(step)
        val nextSteps = nextSteps.getOrDefault(step, mutableListOf())
        System.out.println("Next steps $nextSteps")

        starting.addAll(nextSteps)
    }
}

System.out.println("Result: ${result.joinToString("")}")

fun getStart(starting: MutableSet<String>): String {
    return starting.sorted().get(0)
}

fun hasAllRequired(step: String, stepRequires: MutableMap<String, MutableList<String>>, visited: MutableSet<String>): Boolean {
    val requires = stepRequires.getOrDefault(step, mutableListOf())
    return visited.containsAll(requires)
}

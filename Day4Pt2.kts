#!/usr/bin/env kscript

import java.io.File
import java.time.LocalDate

if (args.size != 1) {
    System.out.println("Need to provide an input file!")
    System.exit(1)
}
val fileName = args.toList().get(0)

val lines = File(fileName).readLines()

var currentId = ""
var sleepStart = 0

val map: MutableMap<String, MutableMap<Int, Int>> = mutableMapOf()
for (l in lines.sorted()) {

    val beginShiftRegex = """\[\d{4}-\d{2}-\d{2} \d\d:\d\d] Guard #(\d+) begins shift""".toRegex()
    val fallAsleepRegex = """\[\d{4}-\d{2}-\d{2} \d\d:(\d\d)] falls asleep""".toRegex()
    val wakeUpRegex = """\[\d{4}-\d{2}-\d{2} \d\d:(\d\d)] wakes up""".toRegex()

    if (beginShiftRegex.containsMatchIn(l)) {
        val (id) = beginShiftRegex.matchEntire(l)!!.destructured
        currentId = id
    } else if (fallAsleepRegex.containsMatchIn(l)) {
        val (min) = fallAsleepRegex.matchEntire(l)!!.destructured
        sleepStart = min.toInt()
    } else if (wakeUpRegex.containsMatchIn(l)) {
        val (min) = wakeUpRegex.matchEntire(l)!!.destructured
        if (sleepStart != 0) {
            val currMap = map.getOrDefault(currentId, mutableMapOf())
            for (i in sleepStart until min.toInt()) {
                currMap[i] = currMap.getOrDefault(i, 0) + 1
            }
            map[currentId] = currMap
        }
    }
}

val (guard, min) = findMinuteMostAsleep(map)

val result = guard.toInt() * min


System.out.println("Result: $result")

fun minuteMostAsleep(m: MutableMap<Int, Int>): Pair<Int, Int> {
    val maxEntry = m.entries.maxBy {it -> it.value }
    return Pair(maxEntry!!.key, maxEntry.value)
}

//

fun findMinuteMostAsleep(m: MutableMap<String, MutableMap<Int, Int>>) : Pair<String, Int> {
    // Per guard, find minute the guard is most asleep
    val (finalGuard, finalRes) = m.entries.fold(Pair("", Pair(-1, -1))
    ) { (currGuard, currData), (guard, guardMap) ->
        val (min, time) = minuteMostAsleep(guardMap)
        if (currData.second <= time) {
            Pair(guard, Pair(min, time))
        } else {
            Pair(currGuard, currData)
        }
    }
    return Pair(finalGuard, finalRes.first)
}

#!/usr/bin/env kscript

import java.io.File
import java.time.LocalDate

if (args.size != 1) {
    System.out.println("Need to provide an input file!")
    System.exit(1)
}
val fileName = args.toList().get(0)

val lines = File(fileName).readLines()

data class Data(val id: String, val date: LocalDate, val sleeping: Int)

var list: MutableList<Data> = mutableListOf()

var currentId = ""
var sleepStart = 0
//lines.sorted().forEach { l -> System.out.println(l) }

val map: MutableMap<String, MutableMap<Int, Int>> = mutableMapOf()
for (l in lines.sorted()) {

    val beginShiftRegex = """\[(\d{4}-\d{2}-\d{2}) \d\d:\d\d] Guard #(\d+) begins shift""".toRegex()
    val fallAsleepRegex = """\[(\d{4}-\d{2}-\d{2}) \d\d:(\d\d)] falls asleep""".toRegex()
    val wakeUpRegex = """\[(\d{4}-\d{2}-\d{2}) \d\d:(\d\d)] wakes up""".toRegex()

    if (beginShiftRegex.containsMatchIn(l)) {
        val (date, id) = beginShiftRegex.matchEntire(l)!!.destructured
        currentId = id
    } else if (fallAsleepRegex.containsMatchIn(l)) {
        val (date, min) = fallAsleepRegex.matchEntire(l)!!.destructured
        sleepStart = min.toInt()
        //System.out.println("Guard $currentId falls asleep at $sleepStart")
    } else if (wakeUpRegex.containsMatchIn(l)) {
        val (date, min) = wakeUpRegex.matchEntire(l)!!.destructured
        if (sleepStart != 0) {
            val (year, month, day) = """(\d\d\d\d)-(\d\d)-(\d\d)""".toRegex().find(date)!!.destructured
            val sleeping = min.toInt() - sleepStart
            list.add(Data(currentId, LocalDate.of(year.toInt(), month.toInt(), day.toInt()), sleeping))
            val currMap = map.getOrDefault(currentId, mutableMapOf())
            for (i in sleepStart..min.toInt() - 1) {
                //System.out.println("Guard $currentId sleeps at $i")
                val currValue = currMap.getOrDefault(i, 0)
                currMap[i] = currValue + 1
                map[currentId] = currMap
            }
            //System.out.println("Guard $currentId wakes up at $min")
        }
    } else {
        //System.out.println("No match for")
        //System.out.println(l)

    }
}

val id: String = guardWithMostMinutesAsleep(map)
System.out.println("Guard that sleeps the most is $id")
val minute: Int = minuteMostAsleep(map.get(id)!!)
System.out.println("Sleeps most during minute $minute")

val result = id.toInt() * minute


System.out.println("Result: $result")

/**
 *
 * val regex = """([\w\s]+) is (\d+) years old""".toRegex()
val matchResult = regex.find("Mickey Mouse is 95 years old")
val (name, age) = matchResult!!.destructured

[1518-04-15 00:49] wakes up
[1518-09-14 00:04] Guard #179 begins shift
[1518-07-21 00:02] falls asleep
[1518-07-23 00:38] falls asleep
 */

fun guardWithMostMinutesAsleep(map: MutableMap<String, MutableMap<Int, Int>>): String {
    val (guard, num) = map.map { (id, m) -> Pair(id, m.values.sum()) }.fold(Pair("", -1), { (id, sleep), (k, v) -> if (v > sleep) Pair(k, v) else Pair(id, sleep) })
    System.out.println("Guard $guard slept for $num minutes")
    return guard
}

fun minuteMostAsleep(m: MutableMap<Int, Int>): Int {
    val (min, time) = m.entries.fold(Pair(-1, -1), { (k1, v1), (k2, v2) -> if (v2 > v1) Pair(k2, v2) else Pair(k1, v1) })
    return min
}

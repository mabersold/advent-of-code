package puzzles.year2023

import puzzles.Puzzle

class Day19: Puzzle(2023, 19, "Aplenty") {
    override fun part1(): Any {
        val input = getInputAsSingleString(1).split("\n\n")

        val parts = input[1].split("\n").map { line ->
            val attributes = line.substringAfter("{").substringBefore("}").split(",")
            Part(
                attributes[0].substringAfter("x=").toInt(),
                attributes[1].substringAfter("m=").toInt(),
                attributes[2].substringAfter("a=").toInt(),
                attributes[3].substringAfter("s=").toInt(),
            )
        }

        val workflows = input[0].split("\n").map { line ->
            Workflow(
                line.substringBefore("{"),
                line.substringAfter("{").substringBefore("}").split(",").filter { it.contains(":") }.map { rule ->
                    Rule(
                        rule[0],
                        rule[1],
                        rule.filter { c -> c.isDigit() }.toInt(),
                        rule.substringAfter(":"))
                },
                line.substringAfterLast(",").substringBefore("}")
            )
        }.groupBy({ it.label }, { it }).mapValues { it.value.first() }

        return parts.sumOf { part ->
            while (part.isWorkflowActive()) {
                part.workflowStatus = workflows[part.workflowStatus]?.process(part)!!
            }

            part.rating()
        }
    }

    override fun part2(): Any {
        val input = getInputAsSingleString(1).split("\n\n")

        val workflows = input[0].split("\n").map { line ->
            Workflow(
                line.substringBefore("{"),
                line.substringAfter("{").substringBefore("}").split(",").filter { it.contains(":") }.map { rule ->
                    Rule(
                        rule[0],
                        rule[1],
                        rule.filter { c -> c.isDigit() }.toInt(),
                        rule.substringAfter(":"))
                },
                line.substringAfterLast(",").substringBefore("}")
            )
        }.groupBy({ it.label }, { it }).mapValues { it.value.first() }

        val possibleRanges = hashMapOf('x' to 1..4000, 'm' to 1..4000, 'a' to 1..4000, 's' to 1..4000)

        return allPossibleCombinations(possibleRanges, workflows, workflows["in"]!!, 0)
    }

    private fun allPossibleCombinations(ranges: HashMap<Char, IntRange>, workflows: Map<String, Workflow>, currentWorkflow: Workflow, currentRule: Int): Long {
        if (currentRule >= currentWorkflow.rules.size) {
            return when (currentWorkflow.finalResult) {
                "R" -> 0
                "A" -> ranges.getProduct()
                else -> allPossibleCombinations(ranges, workflows, workflows[currentWorkflow.finalResult]!!, 0)
            }
        }

        val rule = currentWorkflow.rules[currentRule]
        val outcome = rule.result

        val (passingSet, failingSet) = ranges.split(rule)

        val failingRangeResult = allPossibleCombinations(failingSet, workflows, currentWorkflow, currentRule + 1)
        val passingRangeResult = when(outcome) {
            "R" -> 0
            "A" -> passingSet.getProduct()
            else -> allPossibleCombinations(passingSet, workflows, workflows[outcome]!!, 0)
        }

        return failingRangeResult + passingRangeResult
    }

    data class Part(val x: Int, val m: Int, val a: Int, val s: Int, var workflowStatus: String = "in") {
        fun rating(): Int = if(workflowStatus == "A") x + m + a + s else 0

        fun isWorkflowActive() = !listOf("R", "A").contains(workflowStatus)
    }

    data class Workflow(val label: String, val rules: List<Rule>, val finalResult: String) {
        fun process(part: Part): String {
            rules.forEach { rule ->
                if (rule.validate(part)) {
                    return rule.result
                }
            }

            return finalResult
        }
    }

    data class Rule(val attribute: Char, val symbol: Char, val number: Int, val result: String) {
        fun validate(part: Part): Boolean {
            val partData = when(attribute) {
                'x' -> part.x
                'm' -> part.m
                'a' -> part.a
                else -> part.s
            }

            return if (symbol == '>') {
                partData > number
            } else {
                partData < number
            }
        }
    }

    private fun Map<Char, IntRange>.getProduct(): Long {
        val x = this['x']?.let { it.last - it.first + 1 } ?: 1
        val m = this['m']?.let { it.last - it.first + 1 } ?: 1
        val a = this['a']?.let { it.last - it.first + 1 } ?: 1
        val s = this['s']?.let { it.last - it.first + 1 } ?: 1

        return x.toLong() * m * a * s
    }

    private fun HashMap<Char, IntRange>.split(rule: Rule): Pair<HashMap<Char, IntRange>, HashMap<Char, IntRange>> {
        val rangeToBeModified = this[rule.attribute] ?: throw RuntimeException("Range ${rule.attribute} not found")

        val passingSet = HashMap(this)
        val failingSet = HashMap(this)

        return if (rule.symbol == '>') {
            val gtRange = (rule.number + 1)..rangeToBeModified.last
            val ltRange = rangeToBeModified.first..rule.number

            passingSet[rule.attribute] = gtRange
            failingSet[rule.attribute] = ltRange

            Pair(passingSet, failingSet)
        } else {
            val ltRange = rangeToBeModified.first..<rule.number
            val gtRange = (rule.number)..rangeToBeModified.last

            passingSet[rule.attribute] = ltRange
            failingSet[rule.attribute] = gtRange

            Pair(passingSet, failingSet)
        }
    }
}
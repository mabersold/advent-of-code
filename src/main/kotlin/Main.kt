import puzzles.year2023.Day1
import puzzles.year2023.Day2
import puzzles.year2023.Day3
import puzzles.year2023.Day4
import puzzles.year2023.Day5
import puzzles.year2023.Day6
import puzzles.year2023.Day7

fun main(args: Array<String>) {
    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")

    val day1 = Day1()
    day1.displayResult()

    val day2 = Day2()
    day2.displayResult()

    val day3 = Day3()
    day3.displayResult()

    Day4().displayResult()
    Day5().displayResult()
    Day6().displayResult()
    Day7().displayResult()
}
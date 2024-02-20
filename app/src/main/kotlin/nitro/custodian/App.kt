/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package nitro.custodian

class App {
    val greeting: String
        get() {
            return "Hello World!"
        }
}

fun main() {
    println("How many mines do you want on the field?")
    val grid = Grid(readln().toInt())
    println(grid)

    while (!grid.isSolved()) {
        println("Set/unset mine marks or claim a cell as free:")
        val input = readln().split(" ")

        when (input[2]) {
            "free" -> {
                if (!grid.guess(input[1].toInt(), input[0].toInt())) {
                    return grid.endGame(false)
                } else {
                    println(grid)
                }
            }
            "mine" -> {
                if (grid.markMine(input[1].toInt(), input[0].toInt())) {
                    println(grid)
                }
            }
            else -> println("Invalid command. Expecting 'x y free|mine'")
        }
    }
    grid.endGame(true)
}

package nitro.custodian

import kotlin.random.Random

class Grid(private val n: Int) {

    init {
        assert(n in 0..SIZE*SIZE) { "Must be fewer nitros than slots" }
        generate()
        populate()
        hints()
    }

    private fun generate() {
        (1..SIZE).forEach { y ->
            val row = mutableListOf<Cell>()
            (1..SIZE).forEach{
                val cell = Cell(it to y)
                row.add(cell)
            }
            grid.add(row)
        }
    }

    private fun populate() {
        var nitros = 0
        while (nitros < n) {
            nitros += Random.nextInt(SIZE*SIZE-1).run {
                if (grid[this/SIZE][this%SIZE].value != BOMB) {
                    grid[this/SIZE][this%SIZE].value = BOMB
                    return@run 1
                }
                return@run 0
            }
        }
    }

    private fun hints() {
        for (y in 0 until SIZE) {
            for (x in 0 until SIZE) {
                if (grid[x][y].value != BOMB) {
                    var bombCt = 0
                    if (x > 0) {
                        if (grid[x-1][y].value == BOMB) bombCt++
                        if (y > 0) {
                            if (grid[x-1][y-1].value == BOMB) bombCt++
                        }
                        if (y < SIZE-1) {
                            if (grid[x-1][y+1].value == BOMB) bombCt++
                        }
                    }
                    if (x < SIZE-1) {
                        if (grid[x+1][y].value == BOMB) bombCt++
                        if (y > 0) {
                            if (grid[x+1][y-1].value == BOMB) bombCt++
                        }
                        if (y < SIZE-1) {
                            if (grid[x+1][y+1].value == BOMB) bombCt++
                        }
                    }
                    if (y > 0) {
                        if (grid[x][y-1].value == BOMB) bombCt++
                    }
                    if (y < SIZE-1) {
                        if (grid[x][y+1].value == BOMB) bombCt++
                    }
                    if (bombCt > 0) {
                        grid[x][y].value = bombCt.digitToChar()
                    }
                }
            }
        }
    }

    private fun revealBombs() {
        grid.flatten().filter{ it.value == BOMB }.forEach { it.makeVisible() }
    }

    fun stepInIt(x: Int, y: Int): Boolean {
        if (!grid.flatten().first { it.xy == (x to y) }.makeVisible()) {
            revealBombs()
            return false
        }
        return true
    }

    fun markMine(x: Int, y: Int): Boolean {
        val cell = grid.flatten().first { it.xy == (x to y) }
        if (cell.visible) {
            println("This is visible!")
            return false
        }
        cell.guessed = !cell.guessed
        return true
    }

    fun isSolved(): Boolean {
        return !grid.flatten().any { !it.isSolved() } || grid.flatten().count{ !it.visible } == n
    }

    fun endGame(success: Boolean) {
        if (success) {
            println("Congratulations! You found all the mines!")
        } else {
            revealBombs()
            println("You stepped on a mine and failed!")
        }
        println(this)
    }

    override fun toString(): String {
        return buildString {
            append(" |${(1..SIZE).joinToString("")}|\n")
            append("-|${"-".repeat(SIZE)}|\n")
            for (y in 0 until SIZE) {
                append("${y+1}|${grid[y].joinToString("")}|\n")
            }
            append("-|${"-".repeat(SIZE)}|\n")
        }
    }

    companion object {
        internal const val BOMB = 'X'
        internal const val SAFE = '/'
        internal const val GUESS = '*'
        internal const val HIDDEN = '.'
        private const val SIZE = 3
        private val grid = mutableListOf<MutableList<Cell>>()
    }
}

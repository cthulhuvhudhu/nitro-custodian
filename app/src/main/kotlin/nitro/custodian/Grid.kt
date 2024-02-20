package nitro.custodian

import kotlin.random.Random

class Grid(private val numBombs: Int) {

    private val field: List<List<Cell>>

    init {
        check(numBombs in 0..SIZE*SIZE) { "Must be fewer nitros than slots" }
        field = generate()
        populate()
        createHints()
    }

    private fun generate(): List<List<Cell>> {
        val rows = mutableListOf<List<Cell>>()
        (1..SIZE).forEach { r ->
            val row = mutableListOf<Cell>()
            (1..SIZE).forEach{ c ->
                val cell = Cell(r to c)
                row.add(cell)
            }
            rows.add(row)
        }
        return rows
    }

    private fun populate() {
        var nitros = 0
        while (nitros < numBombs) {
            nitros += Random.nextInt(SIZE*SIZE-1).run {
                if (field[this/SIZE][this%SIZE].value != BOMB) {
                    field[this/SIZE][this%SIZE].value = BOMB
                    return@run 1
                }
                return@run 0
            }
        }
    }

    private fun createHints() {
        (0 until SIZE).forEach { r ->
            (0 until SIZE).forEach { c ->
                if (field[r][c].value != BOMB) {
                    val nMines = countMines(r, c)
                    if (nMines > 0) {
                        field[r][c].value = nMines.digitToChar()
                    }
                }
            }
        }
    }

    private fun countMines(rCenter: Int, cCenter: Int): Int {
        require( field[rCenter][cCenter].value != BOMB) { "Cannot create hint on bomb space" }
        var bombCt = 0
        // From row above, to row below, accounting for borders
        ((rCenter - 1).coerceAtLeast(0)..(rCenter + 1).coerceAtMost(SIZE - 1)).forEach { r ->
            // From col above, to col below, accounting for borders
            ((cCenter - 1).coerceAtLeast(0)..(cCenter + 1).coerceAtMost(SIZE - 1)).forEach { c ->
                if (field[r][c].value == BOMB) bombCt++
            }
        }
        return bombCt
    }

    private fun revealBombs() {
        field.flatten().filter{ it.value == BOMB }.forEach { it.reveal() }
    }

    fun guess(r: Int, c: Int): Boolean {
        val value = field.flatten().first { it.rc == (r to c) }.reveal()
        if (value == BOMB) {
            revealBombs()
            return false
        } else if (value == SAFE) {
            processBatchReveal(r, c)
        }
        return true
    }

    private fun processBatchReveal(rCenter: Int, cCenter: Int) {
        val queue = ArrayDeque<Cell>()
        queue.add(field[rCenter-1][cCenter-1])

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            val value = current.reveal()
            if (value == SAFE) {
                val r = current.rc.first
                val c = current.rc.second
                val neighbors = mutableListOf<Pair<Int, Int>>()
                ((r - 1).coerceAtLeast(0)..(r + 1).coerceAtMost(SIZE)).forEach { rNeighbor ->
                    ((c - 1).coerceAtLeast(0)..(c + 1).coerceAtMost(SIZE)).forEach { cNeighbor ->
                        neighbors.add(rNeighbor to cNeighbor)
                    }
                }
                queue.addAll(field.flatten().filter { it.rc in neighbors && it.rc != (rCenter to cCenter)
                        && it.value != BOMB && !it.visible })
            }
        }
    }

    fun markMine(r: Int, c: Int): Boolean {
        val cell = field.flatten().first { it.rc == (r to c) }
        if (cell.visible) {
            println("This is visible!")
            return false
        }
        cell.guessed = !cell.guessed
        return true
    }

    fun isSolved(): Boolean {
        return field.flatten().filter { it.value == BOMB }.all { it.isSolved() } ||
                field.flatten().filter { it.value != BOMB }.all { it.isSolved() }
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
            appendLine(" |${(1..SIZE).joinToString("")}|")
            appendLine("-|${"-".repeat(SIZE)}|")
            for (r in 0 until SIZE) {
                appendLine("${r+1}|${field[r].joinToString("")}|")
            }
            appendLine("-|${"-".repeat(SIZE)}|")
        }
    }

    companion object {
        internal const val BOMB = 'X'
        internal const val SAFE = '/'
        internal const val GUESS = '*'
        internal const val HIDDEN = '.'
        private const val SIZE = 9
    }
}

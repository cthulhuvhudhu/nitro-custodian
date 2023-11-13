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
        (1..SIZE).forEach { _ ->
            field.add("$SAFE".repeat(SIZE).toCharArray().toMutableList())
        }
    }

    private fun populate() {
        var nitros = 0
        while (nitros < n) {
            nitros += Random.nextInt(SIZE*SIZE-1).run {
                if (field[this/SIZE][this%SIZE] != BOMB) {
                    field[this/SIZE][this%SIZE] = BOMB
                    locations.add((this/SIZE) to (this%SIZE))
                    return@run 1
                }
                return@run 0
            }
        }
    }

    private fun hints() {
        for (x in 0 until SIZE) {
            for (y in 0 until SIZE) {
                if (field[x][y] != BOMB) {
                    var bombCt = 0
                    if (x > 0) {
                        if (field[x-1][y] == BOMB) bombCt++
                        if (y > 0) {
                            if (field[x-1][y-1] == BOMB) bombCt++
                        }
                        if (y < SIZE-1) {
                            if (field[x-1][y+1] == BOMB) bombCt++
                        }
                    }
                    if (x < SIZE-1) {
                        if (field[x+1][y] == BOMB) bombCt++
                        if (y > 0) {
                            if (field[x+1][y-1] == BOMB) bombCt++
                        }
                        if (y < SIZE-1) {
                            if (field[x+1][y+1] == BOMB) bombCt++
                        }
                    }
                    if (y > 0) {
                        if (field[x][y-1] == BOMB) bombCt++
                    }
                    if (y < SIZE-1) {
                        if (field[x][y+1] == BOMB) bombCt++
                    }
                    if (bombCt > 0) {
                        field[x][y] = bombCt.digitToChar()
                    }
                }
            }
        }
    }

    fun guess(r: Int, c: Int): Boolean {
        if (Regex("\\d").matches(field[r][c].toString())) {
            return false
        }
        if (!guesses.remove(r to c)) {
            guesses.add(r to c)
        }
        return true
    }

    fun isSolved(): Boolean {
        return guesses.containsAll(locations) && guesses.size == n
    }

    fun gmView(): String {
        return buildString {
            for (i in 0 until SIZE) {
                append(field[i].joinToString(" "))
                append("\n")
            }
        }
    }

    override fun toString(): String {
        return buildString {
            append(" |${(1..SIZE).joinToString("")}|\n")
            append("-|${"-".repeat(SIZE)}|\n")
            for (i in 0 until SIZE) {
                var masked = field[i].joinToString("")
                guesses.filter { it.first == i }.onEach { masked = masked.replaceRange(it.second, it.second+1, "*") }
                append("${i+1}|${masked.replace('X', '.')}|\n")
            }
            append("-|${"-".repeat(SIZE)}|\n")
        }
    }

    companion object {
        private const val BOMB = 'X'
        private const val SAFE = '.'
        private const val SIZE = 9
        private val field = mutableListOf<MutableList<Char>>()
        private val locations = mutableListOf<Pair<Int,Int>>()
        private val guesses = mutableListOf<Pair<Int,Int>>()
    }
}

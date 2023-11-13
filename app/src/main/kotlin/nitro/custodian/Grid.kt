package nitro.custodian

import kotlin.random.Random

class Grid(private val n: Int) {

    private val field = mutableListOf<MutableList<Char>>()

    init {
        assert(n in 0..SIZE*SIZE) { "Must be fewer nitros than slots" }
        generate()
        populate()
        hints()

    }

    private fun generate() {
        (1..SIZE).forEach { _ ->
            field.add(".".repeat(SIZE).toCharArray().toMutableList())
        }
    }

    private fun populate() {
        var nitros = 0
        while (nitros < n) {
            nitros += Random.nextInt(SIZE*SIZE-1).run {
                if (field[this/SIZE][this%SIZE] != 'X') {
                    field[this/SIZE][this%SIZE] = 'X'
                    return@run 1
                }
                return@run 0
            }
        }
    }

    private fun hints() {
        for (x in 0 until SIZE) {
            for (y in 0 until SIZE) {
                if (field[x][y] != 'X') {
                    var bombCt = 0
                    if (x > 0) {
                        if (field[x-1][y] == 'X') bombCt++
                        if (y > 0) {
                            if (field[x-1][y-1] == 'X') bombCt++
                        }
                        if (y < SIZE-1) {
                            if (field[x-1][y+1] == 'X') bombCt++
                        }
                    }
                    if (x < SIZE-1) {
                        if (field[x+1][y] == 'X') bombCt++
                        if (y > 0) {
                            if (field[x+1][y-1] == 'X') bombCt++
                        }
                        if (y < SIZE-1) {
                            if (field[x+1][y+1] == 'X') bombCt++
                        }
                    }
                    if (y > 0) {
                        if (field[x][y-1] == 'X') bombCt++
                    }
                    if (y < SIZE-1) {
                        if (field[x][y+1] == 'X') bombCt++
                    }
                    field[x][y] = bombCt.digitToChar()
                }
            }
        }
    }

    override fun toString(): String {
        return buildString {
            for (i in 0 until SIZE) {
                append(field[i].joinToString(" "))
                append("\n")
            }
        }
    }

    companion object {
        private const val BOMB = 'X'
        private const val SAFE = '.'
        private const val SIZE = 9
    }
}

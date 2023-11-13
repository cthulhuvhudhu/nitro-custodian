package nitro.custodian

import kotlin.random.Random

class Grid(private val n: Int) {

    private val field = mutableListOf<MutableList<Char>>()

    init {
        assert(n in 0..SIZE*SIZE) { "Must be fewer nitros than slots" }
        (1..SIZE).forEach { _ ->
            field.add(".".repeat(SIZE).toCharArray().toMutableList())
        }
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

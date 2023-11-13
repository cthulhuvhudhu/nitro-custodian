package nitro.custodian

import kotlin.random.Random

class Grid(private val n: Int) {

    private val field = mutableListOf<MutableList<Char>>()

    init {
        (1..n).forEach { _ ->
            field.add(generate().toCharArray().toMutableList())

        }
    }

    private fun generate(): String {
        return buildString {
            for (i in 1..n) {
                if (Random.nextInt() % 6 == 0) {
                    append(BOMB)
                } else {
                    append(SAFE)
                }
            }
        }
    }

    override fun toString(): String {
        return buildString {
            for (i in 0 until n) {
                append(field[i].joinToString(" "))
                append("\n")
            }
        }
    }

    companion object {
        private const val BOMB = 'X'
        private const val SAFE = '.'
    }
}

package nitro.custodian

class Cell(val rc: Pair<Int, Int>,
           var visible: Boolean = false,
           var guessed: Boolean = false,
           var value: Char = Grid.SAFE) {

    fun isSolved(): Boolean {
       if (value == Grid.BOMB) {
           return guessed
       }
       return visible
    }

    fun reveal(): Char {
        visible = true
        return value
    }

    override fun toString(): String {
        if (visible) {
            return "$value"
        }
        if (guessed) {
            return "${Grid.GUESS}"
        }
        return "${Grid.HIDDEN}"
    }
}
package nitro.custodian

class Cell(val xy: Pair<Int, Int>,
           var visible: Boolean = false,
           var guessed: Boolean = false,
           var value: Char = Grid.SAFE) {

    fun isSolved(): Boolean {
       if (value == Grid.BOMB) {
           return guessed
       }
       return !guessed
    }

    fun makeVisible(): Boolean {
        visible = true
        return value != Grid.BOMB
    }

    override fun toString(): String {
        if (visible) {
            return value.toString()
        }
        if (guessed) {
            return Grid.GUESS.toString()
        }
        return Grid.HIDDEN.toString()
    }
}
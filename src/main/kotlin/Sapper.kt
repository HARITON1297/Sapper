import java.util.*

object Sapper {
    var gridW // grid width
            = 0
    var gridH // grid height
            = 0
    var numMines // number of mines on the board
            = 0
    lateinit var mines // entry is 1 for having a mine and 0 for not
            : Array<IntArray>
    lateinit var revealed // entry is true if that spot is revealed
            : Array<BooleanArray>

    fun outBounds(x: Int, y: Int): Boolean {
        return x < 0 || y < 0 || x >= gridW || y >= gridH
    }

    fun calcNear(x: Int, y: Int): Int {
        if (outBounds(x, y)) return 0
        var i = 0
        for (offsetX in -1..1) {
            for (offsetY in -1..1) {
                if (outBounds(offsetX + x, offsetY + y)) continue
                i += mines[offsetX + x][offsetY + y]
            }
        }
        return i
    }

    fun reveal(x: Int, y: Int) {
        if (outBounds(x, y)) return
        if (revealed[x][y]) return
        revealed[x][y] = true
        if (calcNear(x, y) != 0) return
        reveal(x - 1, y - 1)
        reveal(x - 1, y + 1)
        reveal(x + 1, y - 1)
        reveal(x + 1, y + 1)
        reveal(x - 1, y)
        reveal(x + 1, y)
        reveal(x, y - 1)
        reveal(x, y + 1)
    }

    fun setup() {
        //initialize and clear the arrays
        mines = Array(gridW) { IntArray(gridH) }
        revealed = Array(gridW) { BooleanArray(gridH) }
        for (x in 0 until gridW) {
            for (y in 0 until gridH) {
                mines[x][y] = 0
                revealed[x][y] = false
            }
        }
    }

    //Place numMines mines on the grid
    fun placeMines() {
        var i = 0
        while (i < numMines) { //We don't want mines to overlap, so while loop
            val x = (Math.random() * 10).toInt()
            val y = (Math.random() * 10).toInt()
            if (mines[x][y] == 1) continue
            mines[x][y] = 1
            i++
        }
    }

    //Clear the mines
    fun clearMines() {
        for (x in 0 until gridW) {
            for (y in 0 until gridH) {
                mines[x][y] = 0
            }
        }
    }

    fun checkWinState(): Boolean {
        var cn = 0
        for (x in 0 until gridW) {
            for (y in 0 until gridH) {
                if (revealed[x][y] == false) {
                    cn++
                }
            }
        }
        return if (cn == numMines) {
            true
        } else false
    }

    fun showField() {
        for (i in 0 until gridW) {
            print("($i)\t")
        }
        println()
        for (x in 0 until gridW) {
            for (y in 0 until gridH) {
                if (revealed[x][y] == false) {
                    print("[?]\t")
                } else {
                    if (mines[x][y] == 0) {
                        print(calcNear(x, y).toString() + "\t")
                    } else {
                        print("[*]\t")
                    }
                }
            }
            println("($x)")
            println()
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val inn = Scanner(System.`in`)
        println("Input grid width:")
        gridW = inn.nextInt()
        println("Input grid height:")
        gridH = inn.nextInt()
        println("Input number of mines on the board:")
        numMines = inn.nextInt()
        println("write 0. And another 0 in the next line:")
        val `in` = Scanner(System.`in`)
        var gameOver = false
        setup()
        placeMines()
        while (!gameOver) {
            val first = `in`.nextInt()
            val second = `in`.nextInt()
            println("------------")
            reveal(first, second)
            println("------------")
            showField()
            if (mines[first][second] == 1) {
                println("Game over")
                gameOver = true
            }
            if (checkWinState()) {
                println("You Win!")
                gameOver = true
            }
        }
    }
}

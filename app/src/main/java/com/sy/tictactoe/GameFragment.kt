package com.sy.tictactoe

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class GameFragment : Fragment() {

    private lateinit var gameBoard: Array<Array<Button>>
    private var currentPlayer = "X"
    private var board = Array(3) { Array(3) { "" } }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game, container, false)

        // Initialize the game board buttons
        gameBoard = Array(3) { row ->
            Array(3) { col ->
                val buttonId = resources.getIdentifier("cell_${row}_${col}", "id", requireContext().packageName)
                view.findViewById<Button>(buttonId).apply {
                    setOnClickListener { onCellClicked(this, row, col) }
                }
            }
        }

        view.findViewById<Button>(R.id.play_again_button).setOnClickListener {
            resetGame()
        }

        return view
    }

    private fun onCellClicked(button: Button, row: Int, col: Int) {
        if (board[row][col].isNotEmpty()) return // Ignore already filled cells

        board[row][col] = currentPlayer
        button.text = currentPlayer
        button.setTextColor(if (currentPlayer == "X") Color.RED else Color.BLUE)

        if (checkWinner()) {
            view?.findViewById<TextView>(R.id.game_status)?.text = "Player $currentPlayer Wins!"
            endGame()
        } else if (isBoardFull()) {
            view?.findViewById<TextView>(R.id.game_status)?.text = "It's a Draw!"
            endGame()
        } else {
            currentPlayer = if (currentPlayer == "X") "O" else "X"
            view?.findViewById<TextView>(R.id.game_status)?.text = "Player $currentPlayer's Turn"
        }
    }

    private fun checkWinner(): Boolean {
        // Check rows and columns
        for (i in 0..2) {
            if (board[i][0] == currentPlayer && board[i][1] == currentPlayer && board[i][2] == currentPlayer) return true
            if (board[0][i] == currentPlayer && board[1][i] == currentPlayer && board[2][i] == currentPlayer) return true
        }

        // Check diagonals
        if (board[0][0] == currentPlayer && board[1][1] == currentPlayer && board[2][2] == currentPlayer) return true
        if (board[0][2] == currentPlayer && board[1][1] == currentPlayer && board[2][0] == currentPlayer) return true

        return false
    }

    private fun isBoardFull(): Boolean {
        return board.all { row -> row.all { it.isNotEmpty() } }
    }

    private fun endGame() {
        gameBoard.flatten().forEach { it.isEnabled = false }
        view?.findViewById<Button>(R.id.play_again_button)?.visibility = View.VISIBLE
    }

    private fun resetGame() {
        board = Array(3) { Array(3) { "" } }
        gameBoard.flatten().forEach {
            it.text = ""
            it.isEnabled = true
        }
        currentPlayer = "X"
        view?.findViewById<TextView>(R.id.game_status)?.text = "Player X's Turn"
        view?.findViewById<Button>(R.id.play_again_button)?.visibility = View.GONE
    }
}

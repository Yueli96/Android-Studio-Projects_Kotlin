package edu.stanford.cs193a.cs193a_hw6_yueli96

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import kotlinx.android.synthetic.main.activity_snake_game.*

class SnakeGameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_snake_game)
        snake_canvas.startGame()
    }

    // left btn onclick
    fun turnLeft(view: View){
        snake_canvas.turnLeft()
    }

    // right btn onclick
    fun turnRight(view: View){
        snake_canvas.turnRight()
    }

    // restart btn onclick
    fun onRestart(view: View){
        snake_canvas.restartGame(false)
    }

    // exit btn onclick
    fun onExit(view: View){
        snake_canvas.saveHighestScore()
        finish()
    }

    // keyboard control listener
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                snake_canvas.turnLeft()
                true
            }
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                snake_canvas.turnRight()
                true
            }
            else -> super.onKeyUp(keyCode, event)
        }
    }
}

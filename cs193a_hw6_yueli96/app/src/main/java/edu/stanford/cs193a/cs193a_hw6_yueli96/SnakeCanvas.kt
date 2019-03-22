package edu.stanford.cs193a.cs193a_hw6_yueli96

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.*
import android.media.MediaPlayer
import android.util.*
import android.view.*
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_snake_game.view.*
import stanford.androidlib.graphics.*
import java.util.*

class SnakeCanvas(context: Context, attrs: AttributeSet): GCanvas(context, attrs) {

    // private fields
    private var GRID_SIZE = 20
    private var STEP_X = 0f
    private var STEP_Y = 0f
    private var CANVAS_X = 0
    private var CANVAS_Y = 0
    private var MOVING_RATE = 5
    private var ADD_TAIL = false

    private var snakeBodyList = ArrayList<GSprite>()
    private val directionList = Arrays.asList("Right", "Down", "Left", "Up")
    private var curDir = 0
    private lateinit var Food: GSprite
    private lateinit var snakeHeadBitmap: Bitmap
    private val ScoreText = GLabel()
    private val LifeText = GLabel()

    private var LIVES: Int = 3
    var SCORE: Int = 0

    // called first when activity starts
    fun startGame(){
        Log.d("canvas", "startGame")
        val mp = MediaPlayer.create(activity, R.raw.game_start)
        mp.start()
        backgroundColor = GColor.WHITE
    }

    // get canvas size, initialize private variables
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        Log.d("canvas", "changeSize")
        super.onSizeChanged(w, h, oldw, oldh)
        CANVAS_X = w
        CANVAS_Y = h

        removeAll()
        initPrivateVar(false)
        initSnakeHead()
    }

    // initialize and animate the canvas
    override fun init() {
        Log.d("canvas", "Init")
        animateGame(MOVING_RATE)
    }

    // initialize private variables, called when start a new game
    private fun initPrivateVar(saveScore: Boolean){
        STEP_X = Math.min(CANVAS_X / GRID_SIZE.toFloat(), CANVAS_Y / GRID_SIZE.toFloat())
        STEP_Y = STEP_X
        ADD_TAIL = false

        // set GObject
        snakeBodyList = ArrayList<GSprite>()
        curDir = 0
        snakeHeadBitmap = BitmapFactory.decodeResource(resources, R.drawable.snakehead)
            .scale(STEP_X, STEP_Y)
        generateRandomFood()

        // set text object
        if(!saveScore){
            LIVES = 3
        }
        SCORE = 0
        add(ScoreText)
        add(LifeText)
        ScoreText.setText(R.string.score)
        ScoreText.sendToFront()
        ScoreText.fontSize = 40f
        ScoreText.color = GColor.BLACK
        ScoreText.setX(0f)
        ScoreText.setY(0f)

        LifeText.setText(R.string.remain_chances)
        LifeText.color = GColor.BLACK
        LifeText.sendToFront()
        LifeText.fontSize = 40f
        LifeText.setX(0f)
        LifeText.setY(2 * ScoreText.bottomY)
    }

    // update score info and remain chances, called at each move
    private fun updateInfo(){
        ScoreText.setText(R.string.score)
        ScoreText.text += "$SCORE"

        LifeText.setText(R.string.remain_chances)
        LifeText.text += "$LIVES"
    }

    // initialize snake head
    private fun initSnakeHead(){
        val snakeHead = GSprite(snakeHeadBitmap)
        snakeHead.setCollisionMargin(Math.min(STEP_X, STEP_Y) / 4f)
        snakeHead.x = STEP_X
        snakeHead.y = STEP_Y
        snakeBodyList.add(snakeHead)
        add(snakeHead)
    }

    // initialize the random food object
    private fun generateRandomFood(){
        val foodImage = BitmapFactory.decodeResource(resources, R.drawable.apple1)
            .scale(STEP_X, STEP_Y)
        Food = GSprite(foodImage)
        updateFood()
        add(Food)
    }

    // update food position, called after being eaten
    private fun updateFood(){
        Food.x = (1..GRID_SIZE - 1).random() * STEP_X
        Food.y = (1..GRID_SIZE - 1).random() * STEP_Y
    }

    // animate the game, check collisions at each move and update text info
    fun animateGame(fps: Int){
        animate(fps){
            moveSnake()
            checkCollisions()
            updateInfo()
        }
    }

    // check if a tail should be added at each move
    // update position of each tail
    // rotate or flip the head according to its moving direction
    private fun moveSnake(){
        val snakeHead = snakeBodyList[0]
        val curDirection = directionList[curDir]
        if(ADD_TAIL){
            val tailImage = BitmapFactory.decodeResource(resources, R.drawable.snakebody)
                .scale(STEP_X, STEP_Y)
            val tail = GSprite(tailImage)
            tail.setCollisionMargin(Math.min(STEP_X, STEP_Y) / 6f)
            snakeBodyList.add(tail)
            add(tail)
            ADD_TAIL = false
        }
        for(i in snakeBodyList.size-1 downTo 1){
            val curTail = snakeBodyList[i]
            curTail.x = snakeBodyList[i-1].x
            curTail.y = snakeBodyList[i-1].y
        }
        if(curDirection.equals("Right")){
            snakeHead.x += STEP_X
            flipBitMap(snakeHead, true)
        }
        else if(curDirection.equals("Left")){
            snakeHead.x -= STEP_X
            snakeHead.setBitmap(snakeHeadBitmap)
        }
        else if(curDirection.equals("Up")){
            snakeHead.y -= STEP_Y
            snakeHead.setBitmap(snakeHeadBitmap.rotate(90f))
        }
        else if(curDirection.equals("Down")){
            snakeHead.y += STEP_Y
            snakeHead.setBitmap(snakeHeadBitmap.rotate(-90f))
        }
        updateAll()
    }

    // flip bitmap
    private fun Bitmap.flip(x: Float, y: Float, cx: Float, cy: Float): Bitmap {
        val matrix = Matrix().apply { postScale(x, y, cx, cy) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }

    // set the flipped bitmapt to GSprite
    private fun flipBitMap(gsprite: GSprite, horizontal: Boolean){
        val bitmap = snakeHeadBitmap
        val cx = bitmap.width / 2f
        val cy = bitmap.height / 2f
        if(horizontal){
            gsprite.setBitmap(bitmap.flip(-1f, 1f, cx, cy))
        }else{
            gsprite.setBitmap(bitmap.flip(1f, -1f, cx, cy))
        }
    }

    // check collision at each move
    // check tail head collision
    // check head wall collision
    // check head food collision
    private fun checkCollisions(){
        val snakeHead = snakeBodyList[0]
        for(tail in snakeBodyList){
            if(tail.equals(snakeHead)){
                continue
            }
            if(snakeHead.collidesWith(tail)){
                stopGame()
                return
            }
        }
        if(snakeHead.x < 0 || snakeHead.x + snakeHead.width > CANVAS_X
            || snakeHead.y < 0 || snakeHead.y > CANVAS_Y - snakeHead.height/2){
            stopGame()
        }
        if(snakeHead.collidesWith(Food)){
            val mp = MediaPlayer.create(activity, R.raw.food_eaten)
            mp.start()
            updateFood()
            SCORE += 10
            ADD_TAIL = true
        }
    }


    // restart the game when its over or restart btn clicked
    fun restartGame(saveScore: Boolean){
        val mp = MediaPlayer.create(activity, R.raw.game_start)
        mp.start()
        animationStop()
        removeAll()
        initPrivateVar(saveScore)
        initSnakeHead()
        init()
    }

    // stop the game if user loses, check remaining chances
    private fun stopGame(){
        saveHighestScore()
        LIVES -= 1
        if(LIVES > 0){
            restartGame(true)
            return
        }
        animationStop()
        val mp = MediaPlayer.create(activity, R.raw.game_over)
        mp.start()
        Toast.makeText(activity, R.string.try_again, Toast.LENGTH_LONG).show()
    }

    // left btn onClick
    fun turnLeft(){
        if(curDir == 0){
            curDir = directionList.size - 1
        }else{
            curDir -= 1
        }
    }

    // right btn onCLick
    fun turnRight(){
        curDir = (curDir + 1) % directionList.size
    }

    // save the highest score to preference
    fun saveHighestScore(){
        val prefs = activity.getSharedPreferences("SCORE", MODE_PRIVATE)
        val highestScore = prefs.getInt("HIGHEST_SCORE", 0)
        if(SCORE > highestScore){
            val editor = prefs.edit()
            editor.putInt("HIGHEST_SCORE", SCORE)
            editor.apply()
        }
    }
}

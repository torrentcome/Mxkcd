package com.example.mxkcd.ui.home

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mxkcd.R
import com.example.mxkcd.ui.compo.theme.XkcdAndroidTheme
import com.example.mxkcd.ui.compo.theme.customTypeface
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        customTypeface = resources.getFont(R.font.pacfont)
        setContent {
            XkcdAndroidTheme {
                val game: Game = remember { Game() }
                val focusRequester = remember { FocusRequester() }
                var direction = remember { 1 }

                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                    while (isActive) {
                        Log.e("Tag", "while")
                        delay(400L)
                        game.step()
                    }
                }

                Box(
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .focusTarget()
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    val (x, y) = dragAmount
                                    if (kotlin.math.abs(x) > kotlin.math.abs(y)) {
                                        when {
                                            x > 0 -> direction = 0
                                            x < 0 -> direction = 1
                                        }
                                    } else {
                                        when {
                                            y > 0 -> direction = 2
                                            y < 0 -> direction = 3
                                        }
                                    }
                                },
                                onDragEnd = {
                                    when (direction) {
                                        0 -> game.setDirection(Direction.RIGHT).let { true }
                                        1 -> game.setDirection(Direction.LEFT).let { true }
                                        2 -> game.setDirection(Direction.DOWN).let { true }
                                        3 -> game.setDirection(Direction.UP).let { true }
                                    }
                                }
                            )
                        }
                ) {
                    Board(game.board.value)
                }
            }
        }
    }
}

@Composable
private fun Board(board: Board) {
    if (board.snake == null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "GaMe", color = Color.Yellow, fontSize = 32.sp)
            Text(text = "over", color = Color.Yellow, fontSize = 64.sp)
        }
        return
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(modifier = Modifier.border(
            width = 1.dp,
            color = Color.Yellow,
            shape = RoundedCornerShape(5.dp)
        )
        ) {
            board.grid.forEach { row ->
                Row {
                    row.forEach { cell ->
                        when (cell) {
                            board.food -> FoodCell()
                            in board.snake.points -> SnakeCell(isHead = cell == board.snake.head)
                            else -> EmptyCell()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FoodCell() {
    Text(text = "f", color = Color.White, modifier = Modifier.width(FIX_W.dp))
}

@Composable
private fun EmptyCell() {
    Text(text = " ", color = Color.White, modifier = Modifier.width(FIX_W.dp))
}

@Composable
private fun SnakeCell(isHead: Boolean) {
    when {
        isHead -> Text(text = "x", color = Color.Red, modifier = Modifier.width(FIX_W.dp))
        else -> Text(text = "o", color = Color.Green, modifier = Modifier.width(FIX_W.dp))
    }
}

const val FIX_W = 14

class Game {
    private val _board: MutableState<Board>
    private val randomPointGenerator: RandomPointGenerator

    init {
        val width = 16
        val height = width
        val cy = height / 2

        val snake = setOf(
            Point(x = 0, y = cy),
            Point(x = 1, y = cy),
            Point(x = 2, y = cy),
            Point(x = 3, y = cy),
            Point(x = 4, y = cy)
        )

        val grid = List(height) { y ->
            List(width) { x ->
                Point(x = x, y = y)
            }
        }

        randomPointGenerator = RandomPointGenerator()
        grid.forEach { row ->
            row.forEach(randomPointGenerator::free)
        }

        snake.forEach(randomPointGenerator::occupy)

        _board =
            mutableStateOf(
                Board(
                    snake = Snake(points = snake, head = snake.last()),
                    grid = grid,
                    cells = grid.flatten().toSet(),
                    direction = Direction.RIGHT,
                    food = randomPointGenerator.generate()
                )
            )
    }

    val board: State<Board> = _board

    fun step() {
        update {
            Log.e("Tag", "step : update")
            Log.e("Tag", "step : direction$direction")

            val newSnake = snake?.step(direction = direction, food = food, cells = cells)
            snake?.points?.forEach(randomPointGenerator::free)
            newSnake?.points?.forEach(randomPointGenerator::occupy)

            copy(
                snake = newSnake,
                food = when {
                    newSnake == null -> null
                    newSnake.head == food -> randomPointGenerator.generate()
                    else -> food
                }
            )
        }
    }

    private fun Snake.step(direction: Direction, food: Point?, cells: Set<Point>): Snake? {
        val newPoints = LinkedHashSet<Point>(points)
        val newHead = points.last().step(direction)

        if ((newHead in newPoints) || (newHead !in cells)) {
            return null
        }

        newPoints += newHead

        if (newHead != food) {
            newPoints -= points.first()
        }

        return copy(points = newPoints, head = newHead)
    }

    private fun Point.step(direction: Direction): Point =
        when (direction) {
            Direction.LEFT -> copy(x = x - 1)
            Direction.UP -> copy(y = y - 1)
            Direction.RIGHT -> copy(x = x + 1)
            Direction.DOWN -> copy(y = y + 1)
        }

    fun setDirection(direction: Direction) {
        update {
            copy(
                direction = if (direction != this.direction.invert()) direction else this.direction
            )
        }
    }

    private inline fun update(func: Board.() -> Board) =
        _board.value.func().also { _board.value = it }
}

data class Board(
    val snake: Snake?,
    val grid: List<List<Point>>,
    val cells: Set<Point>,
    val direction: Direction,
    val food: Point?
)

data class Snake(
    val points: Set<Point>,
    val head: Point
)

data class Point(
    val x: Int,
    val y: Int
)

enum class Direction {
    LEFT, UP, RIGHT, DOWN
}

fun Direction.invert(): Direction =
    when (this) {
        Direction.LEFT -> Direction.RIGHT
        Direction.UP -> Direction.DOWN
        Direction.RIGHT -> Direction.LEFT
        Direction.DOWN -> Direction.UP
    }

class RandomPointGenerator {
    private val available = HashSet<Point>()
    fun occupy(point: Point) {
        available -= point
    }

    fun free(point: Point) {
        available += point
    }

    fun generate(): Point = available.random()
}
import kotlinx.coroutines.*
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

fun main() = runBlocking {
    val table = Table()
    val mode = SolutionMode.TRY_LOCK //РЕЖИМ ТУТ!!  HIERARCHY, WAITER, MONITOR, TRY_LOCK
    
    println("=== launching philosophers (Kotlin Coroutines) ===")
    println("_SET_MODE --$mode")

    coroutineScope {
        repeat(5) { id ->
            launch(Dispatchers.Default) {
                philosopher(id, table, mode)
            }
        }
    }

    println("=== All the philosophers have finished their meal. The table is empty. ===")
}

suspend fun philosopher(id: Int, table: Table, mode: SolutionMode) {
    val rightFork = id
    val leftFork = (id + 1) % 5

    repeat(10) { i ->
        println("Philosopher $id thinking (${i + 1}/10)")
        delay(Random.nextLong(50, 150))

        mode.pickForks(id, leftFork, rightFork, table)

        println("Philosopher $id EATS (${i + 1}/10)")
        delay(Random.nextLong(50, 150))

        mode.putForks(id, leftFork, rightFork, table)
    }
    println("--- Philosopher $id FINISHED ---")
}
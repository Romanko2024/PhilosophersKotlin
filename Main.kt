import kotlinx.coroutines.*
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

fun main() = runBlocking {
    val table = Table()
    val mode = SolutionMode.HIERARCHY // Оберіть режим тут
    
    println("=== Запуск філософів (Kotlin Coroutines) ===")
    println("Режим: $mode")

    coroutineScope {
        repeat(5) { id ->
            launch(Dispatchers.Default) {
                philosopher(id, table, mode)
            }
        }
    }

    println("=== Усі філософи завершили трапезу. Стіл порожній. ===")
}

suspend fun philosopher(id: Int, table: Table, mode: SolutionMode) {
    val rightFork = id
    val leftFork = (id + 1) % 5

    repeat(10) { i ->
        println("Філософ $id думає (${i + 1}/10)")
        delay(Random.nextLong(50, 150))

        mode.pickForks(id, leftFork, rightFork, table)

        println("Філософ $id ЇСТЬ (${i + 1}/10)")
        delay(Random.nextLong(50, 150))

        // Звільнення виделок
        mode.putForks(id, leftFork, rightFork, table)
    }
    println("--- Філософ $id ЗАКІНЧИВ ---")
}
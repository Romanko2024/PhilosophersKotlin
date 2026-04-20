import kotlinx.coroutines.delay
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

enum class SolutionMode {
    HIERARCHY, WAITER, MONITOR, TRY_LOCK;

    suspend fun pickForks(id: Int, left: Int, right: Int, table: Table) {
        when (this) {
            HIERARCHY -> {
                val first = min(left, right)
                val second = max(left, right)
                table.forks[first].acquire()
                table.forks[second].acquire()
            }
            WAITER -> {
                table.waiter.acquire()
                table.forks[right].acquire()
                table.forks[left].acquire()
            }
            MONITOR -> {
                table.getBothForks(left, right)
            }
            TRY_LOCK -> {
                while (true) {
                    table.forks[right].acquire()
                    if (table.forks[left].tryAcquire()) return
                    table.forks[right].release()
                    delay(Random.nextLong(10, 30))
                }
            }
        }
    }

    fun putForks(id: Int, left: Int, right: Int, table: Table) {
        when (this) {
            MONITOR -> table.putBothForks(left, right)
            WAITER -> {
                table.forks[left].release()
                table.forks[right].release()
                table.waiter.release()
            }
            else -> {
                table.forks[left].release()
                table.forks[right].release()
            }
        }
    }
}
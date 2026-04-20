import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.delay

class Table {
    val forks = Array(5) { Semaphore(1) }

    val waiter = Semaphore(4)

    private val forkInUse = BooleanArray(5) { false }
    private val monitorLock = Any()

    suspend fun getBothForks(left: Int, right: Int) {
        var success = false
        while (!success) {
            synchronized(monitorLock) {
                if (!forkInUse[left] && !forkInUse[right]) {
                    forkInUse[left] = true
                    forkInUse[right] = true
                    success = true
                }
            }
            if (!success) delay(10)
        }
    }

    fun putBothForks(left: Int, right: Int) {
        synchronized(monitorLock) {
            forkInUse[left] = false
            forkInUse[right] = false
        }
    }
}
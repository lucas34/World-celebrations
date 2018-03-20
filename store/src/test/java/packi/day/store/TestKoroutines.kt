package packi.day.store

import android.os.AsyncTask.execute
import android.support.annotation.MainThread
import kotlinx.coroutines.experimental.*
import org.junit.Test
import java.lang.Thread.sleep
import kotlin.coroutines.experimental.suspendCoroutine
import kotlin.system.measureNanoTime

class TestKoroutines {

    fun foobar(param: () -> Unit) { foo(param) }
    fun foo(param: () -> Unit) { bar(param) }
    fun bar(param: () -> Unit) { baz(param) }
    fun baz(param: () -> Unit) { qux(param) }
    fun qux(param: () -> Unit) { quux(param) }
    fun quux(param: () -> Unit) { quuz(param) }
    fun quuz(param: () -> Unit) { corge(param) }
    fun corge(param: () -> Unit) { grault(param) }
    fun grault(param: () -> Unit) { garply(param) }
    fun garply(param: () -> Unit) { waldo(param) }
    fun waldo(param: () -> Unit) { fred(param) }
    fun fred(param: () -> Unit) { plugh(param) }
    fun plugh(param: () -> Unit) { xyzzy(param) }
    fun xyzzy(param: () -> Unit) { thud(param) }
    fun thud(param: () -> Unit) { param() }


    fun testAsync() : Deferred<Int> {
        lazy<InternationalDay> {
            return@lazy null
        }


        return async(NonCancellable, CoroutineStart.UNDISPATCHED) {
            System.out.println("Run async in " + Thread.currentThread())
            1
        }
    }

    suspend fun asyncTest() {
        System.out.println("Start")

        val foo1 = testAsync()
        val foo2 = testAsync()

        sleep(100)
        System.out.println("Before await")

        val value1 = foo1.await()
        val value2 = foo2.await()

        val total = value1 + value2
        System.out.println("Total $total")
    }

    @Test
    fun testAsyncTest() {
        runBlocking {
            asyncTest()
        }
    }


    @Test
    fun testPerformance() {


        "".repeat()
        List(100) {  it }.

        listOf<Int>(1,2,3)

        val a: A? = null

        val b = a as B



//        System.out.print(measureNanoTime(10000) {
//            foobar {
//                System.out.print("")
//            }
//        }.average())
    }

}

open class A {

}

class B {

}

public inline fun measureNanoTime(iteration: Int, block: () -> Unit) : Array<Long> {
    return Array<Long>(iteration) {
        val start = System.nanoTime()
        block()
        return@Array System.nanoTime() - start
    }
}
package com.example.timesculptor

import com.example.timesculptor.util.AppUtil.formatMilliSeconds
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun `formatMilliSeconds for less than 60 seconds`() {
        val input = 59 * 1000L
        val expected = "59s"
        val actual = formatMilliSeconds(input)

        assertEquals(expected, actual)
    }

    @Test
    fun `formatMilliSeconds for duration between 60 seconds and 3600 seconds`() {
        val input = (61 * 60) * 1000L  // 61 minutes
        val expected = "1h 1m 0s"
        val actual = formatMilliSeconds(input)

        assertEquals(expected, actual)
    }

    @Test
    fun `formatMilliSeconds for more than 3600 seconds`() {
        val input = (2 * 3600 + 31 * 60 + 45) * 1000L  // 2 hours, 31 minutes, 45 seconds
        val expected = "2h 31m 45s"
        val actual = formatMilliSeconds(input)

        assertEquals(expected, actual)
    }
}

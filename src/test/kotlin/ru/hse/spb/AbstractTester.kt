package ru.hse.spb

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import java.io.ByteArrayOutputStream
import java.io.PrintStream

abstract class AbstractTester {
    protected val outContent = ByteArrayOutputStream()
    protected val errContent = ByteArrayOutputStream()
    private val originalOut = System.out
    private val originalErr = System.err
    protected val sep = System.lineSeparator()!!

    @BeforeEach
    fun setUpStreams() {
        System.setOut(PrintStream(outContent))
        System.setErr(PrintStream(errContent))
    }

    @AfterEach
    fun restoreStreams() {
        System.setOut(originalOut)
        System.setErr(originalErr)
    }

    companion object {
        const val testDir = "src/test/resources/"
    }
}
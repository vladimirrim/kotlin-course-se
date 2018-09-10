package ru.hse.spb

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.PrintStream
import java.io.ByteArrayOutputStream



class TestSource {

    private val outContent = ByteArrayOutputStream()
    private val errContent = ByteArrayOutputStream()
    private val originalOut = System.out
    private val originalErr = System.err

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

    @Test
    fun mainTestOneWord() {
        System.setIn(ByteArrayInputStream("petr".toByteArray()))
        main(arrayOf())
        assertEquals("YES", outContent.toString())
    }

    @Test
    fun mainTestCorrectSentence() {
        System.setIn(ByteArrayInputStream("nataliala kataliala vetra feinites".toByteArray()))
        main(arrayOf())
        assertEquals("YES", outContent.toString())
    }

    @Test
    fun mainTestIncorrectSentence() {
        System.setIn(ByteArrayInputStream("etis atis animatis etis atis amatis".toByteArray()))
        main(arrayOf())
        assertEquals("NO", outContent.toString())
    }




}
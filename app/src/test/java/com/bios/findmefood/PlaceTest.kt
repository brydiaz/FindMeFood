package com.bios.findmefood

import org.junit.Assert.*
import org.junit.Before

class PlaceTest {
    private val califications = ArrayList<Double>()
    private val testPlace: Place = Place("test", "justForTest",
        califications, 1.23,1.23)

    @org.junit.Test
    fun getName() {
        assertEquals("test",testPlace.getName())
    }

    @org.junit.Test
    fun getCalifications() {
        califications.add(2.3)
        val testCalifications:ArrayList<Double> = testPlace.getCalifications() as ArrayList<Double>
        assertEquals(2.3 as Double, califications[0], 2.3)
    }

    @org.junit.Test
    fun getLatitude() {
        assertEquals(1.23,testPlace.getLatitude(),1.23)
    }

    @org.junit.Test
    fun getLongitude() {
        assertEquals(1.23,testPlace.getLongitude(),1.23)
    }
}
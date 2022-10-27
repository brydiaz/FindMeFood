package com.bios.findmefood

import java.util.ArrayList

class Place(
    private val name: String,
    private val description: String,
    private val califications: ArrayList<*>,
    private val latitude: Double,
    private val longitude: Double
    ) {

    fun getName(): String {
      return name
    }
    fun getCalifications(): ArrayList<*> {
        return califications
    }
    fun getDescription(): String {
        return description
    }
    fun getLatitude(): Double {
        return latitude
    }
    fun getLongitude(): Double {
        return longitude
    }
}
package com.bios.findmefood

import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.Assert.*

import org.junit.Test

class DataBaseControlTest {

    private val califications = ArrayList<Double>()
    private val testPlace: Place = Place("test", "justForTest",
        califications, 1.23,1.23)

    @org.junit.Test
    fun addPlace() {
        mockkStatic(FirebaseFirestore::class)
        every { FirebaseFirestore.getInstance() } returns mockk(relaxed = true)

        val db = FirebaseFirestore.getInstance()
        califications.add(2.3)
        db.collection("placesTEST").document(testPlace.getName()).set(
                hashMapOf("name" to testPlace.getName(),
                    "description" to testPlace.getDescription(),
                    "califications" to testPlace.getCalifications(),
                    "latitude" to testPlace.getLatitude(),
                    "longitude" to testPlace.getLongitude())
        )

        db.collection("placesTEST").document(testPlace.getName()).get().addOnSuccessListener {
            val name = it.get("name") as String
            assertEquals(name, testPlace.getName())
        }

    }

    @org.junit.Test
    fun getPlace() {
        mockkStatic(FirebaseFirestore::class)
        every { FirebaseFirestore.getInstance() } returns mockk(relaxed = true)
        val db = FirebaseFirestore.getInstance()

        db.collection("placesTEST").document(testPlace.getName()).get().addOnSuccessListener {
            val name = it.get("name") as String
            assertEquals(name, testPlace.getName())
        }
    }

    @org.junit.Test
    fun addCalification() {
        mockkStatic(FirebaseFirestore::class)
        every { FirebaseFirestore.getInstance() } returns mockk(relaxed = true)
        val db = FirebaseFirestore.getInstance()


        db.collection("placesTEST").document(testPlace.getName()).get().addOnSuccessListener {
            val califications = it.get("califications") as java.util.ArrayList<Float>
            califications.add(1.23F)
            db.collection("placesTEST").document(testPlace.getName()).update("califications", califications)
        }
    }

}
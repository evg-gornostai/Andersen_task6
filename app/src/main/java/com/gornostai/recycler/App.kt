package com.gornostai.recycler

import android.app.Application
import com.github.javafaker.Faker
import java.util.*

class App : Application() {

    val contactsList = mutableListOf<ContactModel>()

    override fun onCreate() {
        super.onCreate()

        initContactsList()

    }

    fun initContactsList() {
        val faker = Faker()

        for (i in 0..200){
            contactsList.add(ContactModel(
                id = i,
                name = faker.name().firstName(),
                surname = faker.name().lastName(),
                phone = faker.phoneNumber().phoneNumber(),
                image = "https://picsum.photos/200/300?random=$i"
            ))
        }

    }

}
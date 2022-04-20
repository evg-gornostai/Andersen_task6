package com.gornostai.recycler

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContactModel(
    val id: Int,
    val name: String,
    val surname: String,
    val phone: String,
    val image: String
): Parcelable

package com.gornostai.recycler

import androidx.fragment.app.Fragment

fun Fragment.navigator(): Navigator{
    return requireActivity() as Navigator
}

interface Navigator {

    fun launchDetailsFragment(contact: ContactModel, query: String)

    fun launchContactsListFragment(contact: ContactModel, query: String)

}
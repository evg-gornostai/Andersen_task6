package com.gornostai.recycler

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager

class MainActivity : AppCompatActivity(), Navigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.mainFragmentContainer, ContactsListFragment())
            .commit()

    }

    override fun launchDetailsFragment(contact: ContactModel, query: String) {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.mainFragmentContainer, DetailsFragment.newInstance(contact = contact,  query = query))
            .commit()
    }

    override fun launchContactsListFragment(contact: ContactModel,  query: String) {
        val fm = supportFragmentManager
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        fm.beginTransaction()
            .replace(R.id.mainFragmentContainer, ContactsListFragment.newInstance(contact = contact, query = query))
            .commit()
    }

}
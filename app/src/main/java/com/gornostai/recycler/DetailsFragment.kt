package com.gornostai.recycler

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.gornostai.recycler.databinding.FragmentDetailsBinding

private const val ARG_CONTACT = "contact"
private const val ARG_QUERY = "query"

class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding

    private var contact: ContactModel? = null
    private var query: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            contact = it.getParcelable(ARG_CONTACT)
            query = it.getString(ARG_QUERY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)

        binding.tvHeader.text = requireContext().getString(R.string.contacts_header)
            .replace("[CONTACT_NAME]", contact?.name ?: "")
        binding.edName.setText(contact?.name)
        binding.edSurname.setText(contact?.surname)
        binding.edPhoneNumber.setText(contact?.phone)
        Glide.with(this)
            .load(contact?.image)
            .placeholder(R.drawable.ic_image_place_holder)
            .error(R.drawable.ic_image_place_holder)
            .into(binding.ivDetails)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSave.setOnClickListener {
            val newContact = ContactModel(
                id = contact?.id ?: 0,
                name = binding.edName.text.toString(),
                surname = binding.edSurname.text.toString(),
                phone = binding.edPhoneNumber.text.toString(),
                image = contact?.image ?: ""
            )
            contact?.let { contact -> navigator().launchContactsListFragment(newContact, query ?: "") }

        }

    }

    companion object {
        @JvmStatic
        fun newInstance(contact: ContactModel, query: String) =
            DetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_CONTACT, contact)
                    putString(ARG_QUERY, query)
                }
            }
    }
}
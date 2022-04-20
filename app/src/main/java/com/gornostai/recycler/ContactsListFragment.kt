package com.gornostai.recycler


import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.gornostai.recycler.databinding.FragmentContactsListBinding

private const val ARG_CONTACT = "contact"
private const val ARG_QUERY = "query"

class ContactsListFragment : Fragment() {

    private lateinit var binding: FragmentContactsListBinding
    private lateinit var adapter: ContactsListAdapter
    private var modifiedContact: ContactModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactsListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            modifiedContact = it.getParcelable(ARG_CONTACT)
        }

        adapter = ContactsListAdapter(object : ContactsListAdapter.Listener {
            override fun onLongClicked(contact: ContactModel) {
                showDeleteAlertDialog(contact)
            }

            override fun onClicked(contact: ContactModel) {
                navigator().launchDetailsFragment(contact = contact, query = binding.edSearch.text.toString())
            }
        })
        binding.mainRecycler.adapter = adapter

        val decorator = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        binding.mainRecycler.addItemDecoration(decorator)

        modifiedContact?.let { contact ->
            (requireContext().applicationContext as App).contactsList.forEach {
                if (it.id == contact.id) {
                    val index =
                        (requireContext().applicationContext as App).contactsList.indexOf(it)
                    (requireContext().applicationContext as App).contactsList.set(index, contact)
                }
            }
        }

        adapter.setData(ArrayList((requireContext().applicationContext as App).contactsList))

        binding.edSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {
                adapter.filter(query.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        arguments?.let {
            binding.edSearch.setText(it.getString(ARG_QUERY))
        }

    }

    private fun showDeleteAlertDialog(contact: ContactModel) {
        AlertDialog.Builder(requireContext())
            .setTitle("Вы действительно хотите удалить контакт?")
            .setPositiveButton("удалить", DialogInterface.OnClickListener { dialog, i ->
                (requireContext().applicationContext as App).contactsList.remove(contact)
                adapter.setData(
                    ArrayList((requireContext().applicationContext as App).contactsList),
                    query = binding.edSearch.text.toString()
                )
                dialog.cancel()
            })
            .setNegativeButton("нет", DialogInterface.OnClickListener { dialog, i ->
                dialog.cancel()
            })
            .create()
            .show()
    }

    companion object {
        @JvmStatic
        fun newInstance(contact: ContactModel, query: String) =
            ContactsListFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_CONTACT, contact)
                    putString(ARG_QUERY, query)
                }
            }
    }

}
package com.gornostai.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gornostai.recycler.databinding.CellContactBinding

class ContactsListAdapter(val listener: Listener): RecyclerView.Adapter<ContactsListAdapter.ContactsViewHolder>() {

    private var dataList = ArrayList<ContactModel>()
    private var displayList = ArrayList<ContactModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val binding = CellContactBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactsViewHolder(binding, parent, listener)
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.bind(displayList[position])
    }

    override fun getItemCount(): Int = displayList.size

    fun setData(data: ArrayList<ContactModel>, query: String = ""){
        dataList = ArrayList(data)
        filter(query = query)
    }

    fun filter(query: String) {

        if (query.isEmpty()){
            val diffResult = DiffUtil.calculateDiff(
                ItemComparator(oldList = displayList, newList = dataList), false)
            displayList = ArrayList(dataList)
            diffResult.dispatchUpdatesTo(this)
            return
        }

        val oldList1 = ArrayList(displayList)
        displayList = ArrayList(dataList.filter {
            it.name.contains(query, true) ||
                    it.surname.contains(query, true)
        })
        val diffResult = DiffUtil.calculateDiff(
            ItemComparator(oldList = oldList1, newList = displayList), false)
        diffResult.dispatchUpdatesTo(this)
    }

    class ContactsViewHolder(
        val binding: CellContactBinding,
        val parent: ViewGroup,
        val listener: Listener): RecyclerView.ViewHolder(binding.root) {
        fun bind(model: ContactModel){
            binding.tvName.text = parent.context.getString(R.string.cell_contact_name)
                .replace("[CONTACT_NAME]", model.name)
            binding.tvSurname.text = parent.context.getString(R.string.cell_contact_surname)
                .replace("[CONTACT_SURNAME]", model.surname)
            binding.tvPhoneNumber.text = parent.context.getString(R.string.cell_contact_phone)
                .replace("[CONTACT_PHONE]", model.phone)
            Glide.with(parent.context)
                .load(model.image)
                .placeholder(R.drawable.ic_image_place_holder)
                .error(R.drawable.ic_image_place_holder)
                .into(binding.ivContactPhoto)
            itemView.setOnClickListener {
                listener.onClicked(model)
            }
            itemView.setOnLongClickListener(object : View.OnLongClickListener{
                override fun onLongClick(view: View?): Boolean {
                    listener.onLongClicked(model)
                    return true
                }
            })
        }
    }

    class ItemComparator(
        private val oldList: List<ContactModel>,
        private val newList: List<ContactModel>
    ): DiffUtil.Callback(){

        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldContact = oldList[oldItemPosition]
            val newContact = newList[newItemPosition]
            return oldContact.id == newContact.id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldContact = oldList[oldItemPosition]
            val newContact = newList[newItemPosition]
            return oldContact == newContact
        }
    }

    interface Listener {
        fun onLongClicked(contact: ContactModel)
        fun onClicked(contact: ContactModel)
    }
}
package com.example.myapplication.db

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.EditActivity
import com.example.myapplication.R

class AdapterOfElement(list: ArrayList<ListItem>, contextMain: Context) :
    RecyclerView.Adapter<AdapterOfElement.Holder>() {
    var listArray = list
    var context = contextMain

    class Holder(itemView: View, contextView: Context) : RecyclerView.ViewHolder(itemView) {
        val newTitle = itemView.findViewById<TextView>(R.id.textViewFromElement)
        val context = contextView
        fun setData(item: ListItem) {
            newTitle.text = item.title
            itemView.setOnClickListener {
                val intent = Intent(context, EditActivity::class.java).apply {
                    putExtra(IntentConst.INT_TITLE_KEY, item.title)
                    putExtra(IntentConst.INT_DESC_KEY, item.description)
                    putExtra(IntentConst.INT_URI_KEY, item.uri)
                    putExtra(IntentConst.INT_ID_KEY, item.id)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        var inflater = LayoutInflater.from(parent.context)
        return Holder(inflater.inflate(R.layout.rc_element, parent, false), context)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(listArray.get(position))
    }

    override fun getItemCount(): Int {
        return listArray.size
    }

    fun update(list: ArrayList<ListItem>) {
        listArray.clear()
        listArray.addAll(list)
        notifyDataSetChanged()
    }

    fun toDelete(position: Int, dbManager: MyDbManager) {
        dbManager.removeToDb(listArray[position].id.toString())
        listArray.removeAt(position)
        notifyItemRangeChanged(0, listArray.size)
        notifyItemRemoved(position)
    }

}
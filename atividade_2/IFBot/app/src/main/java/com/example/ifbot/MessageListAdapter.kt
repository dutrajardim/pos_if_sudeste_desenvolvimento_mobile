package com.example.ifbot

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class MessageListAdapter(private var itemsList: MutableSet<MessageData>, private var tag: String): RecyclerView.Adapter<MessageListAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val messageTextView: TextView = view.findViewById(R.id.tv_message)
        val innerLayout: LinearLayout = view.findViewById(R.id.ll_inner_layout)
        val layout: LinearLayout = view.findViewById(R.id.ll_layout)
        val messageTimeTextView: TextView = view.findViewById(R.id.tv_message_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.message_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.messageTextView.text = itemsList.elementAt(position).text
        if (itemsList.elementAt(position).from == tag) {
            holder.layout.gravity = Gravity.START
            holder.innerLayout.gravity = Gravity.START
        }
        val datetime = Date(itemsList.elementAt(position).createdAt)
        holder.messageTimeTextView.text = datetime.toString()

    }

    override fun getItemCount(): Int = itemsList.size
}
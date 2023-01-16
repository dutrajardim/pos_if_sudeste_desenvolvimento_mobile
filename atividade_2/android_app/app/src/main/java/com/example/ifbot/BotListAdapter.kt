package com.example.ifbot

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BotListAdapter(private var itemsList: MutableSet<String>): RecyclerView.Adapter<BotListAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val itemTextView: TextView = view.findViewById(R.id.tv_name)
        val iconButton: ImageButton = view.findViewById(R.id.btn_talk)
        val context: Context = view.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.bot_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemTextView.text = itemsList.elementAt(position)
        holder.iconButton.setOnClickListener {
            val intent = Intent(holder.context, ChatActivity::class.java)
            intent.putExtra("tag", itemsList.elementAt(position))
            holder.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = itemsList.size
}
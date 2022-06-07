package com.example.assistant.Recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.assistant.Models.HistoryBodyModel
import com.example.assistant.R

class RecyclerAdapter(private val names: List<HistoryBodyModel>) :
    RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var queryTextView: TextView? = null
        var responseTextView: TextView? = null
        var dateTextView: TextView? = null

        init {
            queryTextView = itemView.findViewById(R.id.txt_qry)
            responseTextView = itemView.findViewById(R.id.txt_resp)
            dateTextView = itemView.findViewById(R.id.txt_date)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return names.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.queryTextView?.text = names[position].query
        holder.responseTextView?.text = names[position].response
        holder.dateTextView?.text = names[position].date
    }
}
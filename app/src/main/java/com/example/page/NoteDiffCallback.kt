package com.example.page

import androidx.recyclerview.widget.DiffUtil
import com.example.page.models.Note

class NoteDiffCallback : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem._id == newItem._id  // compare unique IDs
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem == newItem  // full equality check
    }
}

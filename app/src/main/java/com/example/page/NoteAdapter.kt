import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.page.NoteDiffCallback
import com.example.page.R
import com.example.page.databinding.NoteItemBinding
import com.example.page.models.Note
import com.example.page.models.NotesResponceGet

class NotesAdapter(
    private val onItemClick: ((Note) -> Unit)? = null
) : ListAdapter<Note, NotesAdapter.NoteViewHolder>(NoteDiffCallback()) {

    private val colors = listOf(
        R.color.noteColor1,
        R.color.noteColor2,
        R.color.noteColor3,
        R.color.noteColor4,
        R.color.noteColor5,
        R.color.noteColor6
    )

    inner class NoteViewHolder(val binding: NoteItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(note: Note) {
            binding.title.text = note.title
            binding.desc.text = note.content
            val randomColor = colors.random()

            val drawable = ContextCompat.getDrawable(
                binding.root.context,
                R.drawable.note_background
            )?.mutate()

            drawable?.setTint(ContextCompat.getColor(binding.root.context, randomColor))

            binding.root.background = drawable

            binding.root.setOnClickListener { onItemClick?.invoke(note) }

            binding.root.setOnClickListener { onItemClick?.invoke(note) }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {



        holder.bind(getItem(position))

    }
}


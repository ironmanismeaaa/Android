package com.example.note.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.note.R
import com.example.note.data.Note
import com.example.note.utils.NoteDiffUtil


class NoteRVAdapter(private val context: Context, private val listener: INoteRVAdapter): RecyclerView.Adapter<NoteRVAdapter.NoteViewHolder>() {

    private var allNotes = emptyList<Note>()
    private var num = 0
    private lateinit var globalHolder:NoteViewHolder

    inner class NoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val noteTitle: TextView = itemView.findViewById(R.id.tvNoteTitle)
        val noteDes: TextView = itemView.findViewById(R.id.tvNoteDes)
        //        val deleteButton: ImageView = itemView.findViewById(R.id.btnDelete)
        val noteCard: CardView = itemView.findViewById(R.id.cvNote)
        val llNote: LinearLayout = itemView.findViewById(R.id.llNote)
        val noteTime:TextView=itemView.findViewById(R.id.tvNoteTime)//zyy
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val viewHolder = NoteViewHolder(LayoutInflater.from(context).inflate(R.layout.item_note, parent, false))
        //实现 delete button click
//        viewHolder.deleteButton.setOnClickListener {
//            //获取被点击项目的 position
//            listener.onDeleteClicked(allNotes[viewHolder.adapterPosition])
//        }
        //实现 note card click
        viewHolder.noteCard.setOnClickListener {
            listener.onCardClicked(allNotes[viewHolder.adapterPosition])
        }
        Log.d("Adapter","onCreateViewHolder is called")
        return viewHolder
    }

    override fun getItemCount(): Int {
        Log.d("Adapter","getItemCount is called")
        return allNotes.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = allNotes[position]
        holder.noteTitle.text = currentNote.title
        holder.noteDes.text = currentNote.des

        holder.noteTime.text=currentNote.time//zyy

        val colorsArray = context.resources.getIntArray(R.array.cardColors) as IntArray
        if (num >=32) {
            num = 0
        }
        val randomColor = colorsArray[num]
        num++
        holder.llNote.setBackgroundColor(randomColor)
        currentNote.color = randomColor

//        Log.d("Adapter","onBindViewHolder is called")
    }



    //此函数用于通过diffUtil更新回收器；仅更改通过viewModel观察到的更改，而不是再次更改整个列表
    fun updateList(newList: List<Note>) {
        val diffUtil = NoteDiffUtil(allNotes, newList)

        //计算可以将一个列表转换为另一个列表的更新操作列表
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        //首先用新的更新列表替换旧列表以进行下一次更新
        this.allNotes = newList
        //update list
        diffResult.dispatchUpdatesTo(this)
        Log.d("Adapter","updateList is called")
    }

    fun getNote(position: Int): Note {
        Log.d("Adapter","getNote is called")
        return allNotes[position]
    }
}

//处理点击事件
interface INoteRVAdapter {
    //        fun onDeleteClicked(note: Note)
    fun onCardClicked(note: Note)
}
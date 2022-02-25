package com.example.note.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "notes_table")
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "noteTitle")
    val title: String?,

    @ColumnInfo(name = "noteDes")
    val des: String?,

    @ColumnInfo(name = "noteColor")
    var color: Int?,

    @ColumnInfo(name = "noteImgUri")
    var uri: String?,

    @ColumnInfo(name = "noteTime")//zyy到此一游
    var time: String?

): Serializable {


}



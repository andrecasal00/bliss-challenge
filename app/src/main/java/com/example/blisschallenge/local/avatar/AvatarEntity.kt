package com.example.blisschallenge.local.avatar

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_avatar")
data class AvatarEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "url") val url: String
)
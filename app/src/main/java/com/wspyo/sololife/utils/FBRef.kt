package com.wspyo.sololife.utils

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FBRef {
    companion object{
        private var database = Firebase.database

        val category1 = database.getReference("contents")
        val category2 = database.getReference("contents2")

        val bookmarkRef = database.getReference("bookmark_list")

        val boardRef = database.getReference("board")

        val commentRef = database.getReference("comment")
    }
}
package com.wspyo.sololife.contentsList

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.wspyo.sololife.R
import com.wspyo.sololife.utils.FBAuth
import com.wspyo.sololife.utils.FBRef

class ContentsListActivity : AppCompatActivity() {

    lateinit var myRef: DatabaseReference
    val bookmarkIdList = mutableListOf<String>()
    lateinit var rvAdapter: ContentsRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_contents)

        val database = Firebase.database
        val items = ArrayList<ContentsModel>()
        val keyList = ArrayList<String>()
        rvAdapter = ContentsRVAdapter(baseContext,items,keyList,bookmarkIdList)

        val category = intent.getStringExtra("category")

        if(category == "category1"){
            myRef = database.getReference("contents")
        }else if(category == "category2"){
            myRef = database.getReference("contents2")
        }

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(dataModel in dataSnapshot.children){
                    Log.d("ContentsListActivity",dataModel.toString())
                    Log.d("ContentsListActivity",dataModel.key.toString())
                    val item = dataModel.getValue(ContentsModel::class.java)
                    items.add(item!!)
                    keyList.add(dataModel.key.toString())
                }
                rvAdapter.notifyDataSetChanged() // 리스트 동기화
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("loadPost:onCancelled", databaseError.toException())
            }
        }
        myRef.addValueEventListener(postListener)
        val rv : RecyclerView = findViewById(R.id.rv)
        rv.adapter = rvAdapter
        rv.layoutManager = GridLayoutManager(this,2)

        getBookmarkData()
    }

    private fun getBookmarkData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // 중복값 제거를 위해
                bookmarkIdList.clear()
                for(dataModel in dataSnapshot.children){
                    bookmarkIdList.add(dataModel.key.toString())
                }
                Log.d("Bookmark : ",bookmarkIdList.toString())
                rvAdapter.notifyDataSetChanged() // 리스트 동기화
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("loadPost:onCancelled", databaseError.toException())
            }
        }
        // 사용자의 uid 아래의 데이터를 불러옴으로써 DataSnapshot { key = -NyzavgeuKLwFdMUT2sG, value = {bookmarkIsTrue=true} } key,value를 분리한다.
        FBRef.bookmarkRef.child(FBAuth.getUid()).addValueEventListener(postListener)
    }
}
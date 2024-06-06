package com.wspyo.sololife.board

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.wspyo.sololife.R
import com.wspyo.sololife.databinding.ActivityBoardEditBinding
import com.wspyo.sololife.utils.FBAuth
import com.wspyo.sololife.utils.FBRef

class BoardEditActivity : AppCompatActivity() {

    private lateinit var key : String

    private lateinit var  binding : ActivityBoardEditBinding

    private val TAG = BoardEditActivity::class.java.simpleName

    private lateinit var writerUid : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_board_edit)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_board_edit)

        key = intent.getStringExtra("key").toString()

        getImageData(key)
        getBoardData(key)
        binding.editBtn.setOnClickListener{
            editBoardData(key)
        }
    }


    private fun editBoardData(key:String) {
        FBRef.boardRef
            .child(key)
            .setValue(BoardModel(
                binding.titleArea.text.toString(),
                binding.contentArea.text.toString(),
                writerUid,
                FBAuth.getTime()))
        finish()
    }


    private  fun getImageData(key : String){
        // Reference to an image file in Cloud Storage
        val storageReference = Firebase.storage.reference.child(key + ".png")

        // ImageView in your Activity
        val imageViewFromFB = binding.imageArea

        // imageViewArea에 key값을 통해 FireBase에서 불러온 image를 Glide를 사용하여 넣어줘라.
        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if(task.isSuccessful){
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)

            }else{
            }
        })
    }

    private fun getBoardData(key : String){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    val item = dataSnapshot.getValue(BoardModel::class.java)
                    binding.titleArea.setText(item?.title)
                    binding.contentArea.setText(item?.content)

                    writerUid = item!!.uid
                }catch (e : Exception){
                    Log.d(TAG,"삭제완료")
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("loadPost:onCancelled", databaseError.toException())
            }
        }
        // 인자로 받아온 key의 하위 DataModel을 받아온다.
        FBRef.boardRef.child(key).addValueEventListener(postListener)
    }

}
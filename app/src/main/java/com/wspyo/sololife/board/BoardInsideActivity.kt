package com.wspyo.sololife.board

import android.content.Intent
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.wspyo.sololife.R
import com.wspyo.sololife.comment.CommentListLVAdapter
import com.wspyo.sololife.comment.CommentModel
import com.wspyo.sololife.databinding.ActivityBoardInsideBinding
import com.wspyo.sololife.databinding.ActivityBoardWriteBinding
import com.wspyo.sololife.utils.FBAuth
import com.wspyo.sololife.utils.FBRef

class BoardInsideActivity : AppCompatActivity() {


    private val TAG = BoardInsideActivity::class.java.simpleName

    private  lateinit var binding : ActivityBoardInsideBinding

    private  lateinit var key : String

    private  val commentList = mutableListOf<CommentModel>()

    private lateinit var commentAdapter : CommentListLVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_board_inside)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_board_inside)


        // 1. title,content,time을 받아온다.
//        val title = intent.getStringExtra("title").toString()
//        val content = intent.getStringExtra("content").toString()
//        val time = intent.getStringExtra("time").toString()
//
//        binding.titleArea.text = title
//        binding.contentArea.text = content
//        binding.timeArea.text = time
//
//        Log.d(TAG,title)
//        Log.d(TAG,content)
//        Log.d(TAG,time)

        // 2. key를 받아온다.
        key = intent.getStringExtra("key").toString()
        getBoardData(key)
        getImageData(key)

        binding.boardSettingIcon.setOnClickListener{
            showDialog()
        }

        binding.commentBtn.setOnClickListener{
            insertComment(key)
        }

        commentAdapter = CommentListLVAdapter(commentList)
        binding.commentLV.adapter = commentAdapter

        getCommentData(key)
    }

    private  fun getCommentData(key: String){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                commentList.clear()
                for(dataModel in dataSnapshot.children){
                    val item = dataModel.getValue(CommentModel::class.java)
                    commentList.add(item!!)
                }
                commentAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.commentRef.child(key).addValueEventListener(postListener)
    }

    private fun insertComment(key: String){
        // comment
        //  - boardKey
        //      - commentKey
        //          - commentData

        FBRef.commentRef
            .child(key)
            .push()
            .setValue(CommentModel(binding.commentArea.text.toString(),FBAuth.getTime()))

        Toast.makeText(this,"댓글 입력",Toast.LENGTH_LONG).show()
        binding.commentArea.setText("")

    }

    private fun  showDialog() {

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog,null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("게시글 수정/삭제")
        val alertDialog = mBuilder.show()

        alertDialog.findViewById<Button>(R.id.editBtn)?.setOnClickListener{
            Toast.makeText(this,"수정버튼을 눌렀습니다",Toast.LENGTH_LONG).show()
            val intent = Intent(this,BoardEditActivity::class.java)
            intent.putExtra("key",key)
            startActivity(intent)
            finish()

        }
        alertDialog.findViewById<Button>(R.id.removeBtn)?.setOnClickListener{
            Toast.makeText(this,"삭제 완료",Toast.LENGTH_LONG).show()
            FBRef.boardRef.child(key).removeValue()
            finish()
        }
    }

    private  fun getImageData(key : String){
        // Reference to an image file in Cloud Storage
        val storageReference = Firebase.storage.reference.child(key + ".png")

        // ImageView in your Activity
        val imageViewFromFB = binding.getImageArea

        // imageViewArea에 key값을 통해 FireBase에서 불러온 image를 Glide를 사용하여 넣어줘라.
        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if(task.isSuccessful){
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)

            }else{
                binding.getImageArea.isVisible = false
            }
        })
    }

    private fun getBoardData(key : String){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    val item = dataSnapshot.getValue(BoardModel::class.java)
                    binding.titleArea.text = item!!.title
                    binding.contentArea.text = item!!.content
                    binding.timeArea.text = item!!.time

                    val myUid = FBAuth.getUid()
                    val writerUid = item.uid

                    if(myUid.equals(writerUid)){
                        binding.boardSettingIcon.isVisible = true
                    } else{
                    }

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
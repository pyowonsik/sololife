package com.wspyo.sololife.board

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.output.ByteArrayOutputStream
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.wspyo.sololife.R
import com.wspyo.sololife.contentsList.BookmarkModel
import com.wspyo.sololife.databinding.ActivityBoardWriteBinding
import com.wspyo.sololife.databinding.ActivityJoinBinding
import com.wspyo.sololife.utils.FBAuth
import com.wspyo.sololife.utils.FBRef

class BoardWriteActivity : AppCompatActivity() {

    private  lateinit var  binding: ActivityBoardWriteBinding

    private val TAG = BoardWriteActivity::class.java.simpleName

    private var isImageUpload = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_board_write)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_board_write)

        binding.writeBtn.setOnClickListener{
            val title = binding.titleArea.text.toString()
            val content = binding.contentArea.text.toString()
            val uid = FBAuth.getUid()
            val time = FBAuth.getTime()
            Log.d(TAG,title)
            Log.d(TAG,content)

            // 파이어베이스 스토리지에 이미지를 저장하고 싶다.
            // 만약 내가 게시글을 클릭했을때 , 게시글에 대한 정보를 받아와야 하는데
            // 이미지 이름에 대한 정보를 모르기 때문에,
            // 이미지 이름을 문서의 key값으로 지정하여 찾기 쉽게한다.

            // .push()를 하게 되면 임의의 key값을 바탕으로 (밑으로) 데이터가 들어간다. // ??? -> BoardModel
            // 반면 .child(key)를 사용하면 지정한 key값을 바탕으로 (밑으로) 데이터가 들어간다. -> key -> BoardModel
            val key = FBRef.boardRef.push().key.toString()

            FBRef.boardRef
                .child(key)
                .setValue(BoardModel(title,content,uid,time))

            Toast.makeText(this,"게시글 입력 완료",Toast.LENGTH_LONG).show()

            // firebase storage에 이미지를 업로드
            if(isImageUpload == true) {
                imageUpload(key)
            }

            finish()
        }

        binding.imageArea.setOnClickListener{
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI) // device의 갤러리 Activity를 여는 Intent 생성
            startActivityForResult(gallery,100)
            isImageUpload = true

        }
    }

    private fun imageUpload(key : String){

        val storage = Firebase.storage
        val storageRef = storage.reference

        // Create a reference to "mountains.jpg"
        val mountainsRef = storageRef.child(key + ".png")

        val imageView = binding.imageArea
        // Get the data from an ImageView as bytes
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
    }

    // Activity에서 반환되는 결과를 처리하는 메소드
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK && requestCode == 100){
            binding.imageArea.setImageURI(data?.data)
        }
    }
}
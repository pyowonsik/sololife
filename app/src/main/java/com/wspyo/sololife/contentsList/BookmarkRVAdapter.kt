package com.wspyo.sololife.contentsList

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.persistableBundleOf
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wspyo.sololife.R
import com.wspyo.sololife.utils.FBAuth
import com.wspyo.sololife.utils.FBRef

class BookmarkRVAdapter(
    val context : Context,
    val items : ArrayList<ContentsModel>,
    val keyList : ArrayList<String>,
    val bookmarkIdList : MutableList<String>
) : RecyclerView.Adapter<BookmarkRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookmarkRVAdapter.ViewHolder {
        // 아이템 하나의 레이아웃을 가져와 생성
        val v = LayoutInflater.from(parent.context).inflate(R.layout.content_rv_item,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: BookmarkRVAdapter.ViewHolder, position: Int) {
        holder.bindItems(items[position],keyList[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)  {
        // items를 onCreateViewHolder의 item에 연결
        fun bindItems(item : ContentsModel,key : String) {

//            itemView.setOnClickListener{
//                Toast.makeText(context,item.title,Toast.LENGTH_LONG).show()
//                val intent = Intent(context,ContentShowActivity::class.java)
//                intent.putExtra("url",item.webUrl)
//                itemView.context.startActivity(intent)
//            }

            val contentTitle = itemView.findViewById<TextView>(R.id.textArea)
            contentTitle.text = item.title
            val imageViewArea = itemView.findViewById<ImageView>(R.id.imageArea)
            val bookmarkArea = itemView.findViewById<ImageView>(R.id.bookmarkArea)

            if(bookmarkIdList.contains(key)){
                bookmarkArea.setImageResource(R.drawable.bookmark_color)
            }else{
                bookmarkArea.setImageResource(R.drawable.bookmark_white)
            }

//            bookmarkArea.setOnClickListener{
//                Toast.makeText(context,key,Toast.LENGTH_LONG).show()
//                // 북마크가 있는 경우
//                if(bookmarkIdList.contains(key)){
//                    FBRef.bookmarkRef
//                        .child(FBAuth.getUid())
//                        .child(key)
//                        .removeValue()
//                }else{
//                    // 북마크가 없는 경우
//                    // bookmark_list -> uid ->key(contentsModel - key) -> ??
//                    FBRef.bookmarkRef
//                        .child(FBAuth.getUid())
//                        .child(key)
//                        .setValue(BookmarkModel(true))
//                }
//
//                Log.d("bookmarkArea",keyList.toString())
//                Log.d("bookmarkArea",bookmarkIdList.toString())
//            }


            // glide 라이브러리를 사용해 webView url을 image의 url에 넣어서 이미지를 보여준다.
            Glide.with(context)
                .load(item.imageUrl)
                .into(imageViewArea)
        }
    }
}
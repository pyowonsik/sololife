package com.wspyo.sololife.board

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.wspyo.sololife.R
import com.wspyo.sololife.utils.FBAuth

class BoardListLVAdapter(val boardList : MutableList<BoardModel> ) : BaseAdapter() {
    override fun getCount(): Int {
        return boardList.size
    }

    override fun getItem(position: Int): Any {
        return boardList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
//        if(convertView == null){
            convertView = LayoutInflater.from(parent?.context).inflate(R.layout.board_list_item,parent,false)
//        }


        val title = convertView?.findViewById<TextView>(R.id.titleArea)
        val content = convertView?.findViewById<TextView>(R.id.contentArea)
        val time = convertView?.findViewById<TextView>(R.id.timeArea)

        val linearLayoutItem = convertView?.findViewById<LinearLayout>(R.id.itemLinearLayoutView)

        if(boardList[position].uid.equals(FBAuth.getUid())){
            linearLayoutItem?.setBackgroundColor(Color.parseColor("#ffa500"))
        }

        title!!.text = boardList[position].title
        content!!.text = boardList[position].content
        time!!.text = boardList[position].time

        return convertView!!
    }
}
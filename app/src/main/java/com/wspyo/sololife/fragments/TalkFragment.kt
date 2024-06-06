package com.wspyo.sololife.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.wspyo.sololife.R
import com.wspyo.sololife.board.BoardInsideActivity
import com.wspyo.sololife.board.BoardListLVAdapter
import com.wspyo.sololife.board.BoardModel
import com.wspyo.sololife.board.BoardWriteActivity
import com.wspyo.sololife.contentsList.ContentsModel
import com.wspyo.sololife.contentsList.ContentsRVAdapter
import com.wspyo.sololife.databinding.FragmentStoreBinding
import com.wspyo.sololife.databinding.FragmentTalkBinding
import com.wspyo.sololife.utils.FBAuth
import com.wspyo.sololife.utils.FBRef

class TalkFragment : Fragment() {

    private lateinit var binding : FragmentTalkBinding

    private  val TAG = TalkFragment::class.java.simpleName

    private val boardDataList = mutableListOf<BoardModel>()

    private lateinit var  boardRVAdapter : BoardListLVAdapter

    private val boardKeyList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_talk,container,false)

        binding.boardListView.setOnItemClickListener { parent, view, position, id ->

            // 1. 리스트 뷰에 있는 데이터 title,content,time 을 모두 다른 Activity로 전달
//            val intent = Intent(context,BoardInsideActivity::class.java)
//            intent.putExtra("title",boardDataList[position].title)
//            intent.putExtra("content",boardDataList[position].content)
//            intent.putExtra("time",boardDataList[position].time)
//            startActivity(intent)

            // 2. 파이어베이스에 board에 대한 id를 기반으로 데이터를 받아오는 방법
            val intent = Intent(context,BoardInsideActivity::class.java)
            intent.putExtra("key",boardKeyList[position])
            startActivity(intent)
        }


        // Adapter 연결
        boardRVAdapter = BoardListLVAdapter(boardDataList)
        binding.boardListView.adapter = boardRVAdapter


        binding.writeBtn.setOnClickListener{
            val intent = Intent(context,BoardWriteActivity::class.java)
            startActivity(intent)
        }

        binding.homeTap.setOnClickListener{
            it.findNavController().navigate(R.id.action_talkFragment_to_homeFragment)
        }
        binding.tipTap.setOnClickListener{
            it.findNavController().navigate(R.id.action_talkFragment_to_tipFragment)
        }
        binding.bookmarkTap.setOnClickListener{
            it.findNavController().navigate(R.id.action_talkFragment_to_bookmarkFragment)
        }
        binding.storeTap.setOnClickListener{
            it.findNavController().navigate(R.id.action_talkFragment_to_storeFragment)
        }

        getFBBoardData()
        return binding.root
    }

    private fun getFBBoardData(){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                boardDataList.clear()
                for(dataModel in dataSnapshot.children){
                    val item = dataModel.getValue(BoardModel::class.java)
                    boardDataList.add(item!!)
                    boardKeyList.add(dataModel.key.toString())
                }
                boardKeyList.reverse()
                boardDataList.reverse()
//                Log.d(TAG,boardDataList.toString())
                boardRVAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.boardRef.addValueEventListener(postListener)
    }


}
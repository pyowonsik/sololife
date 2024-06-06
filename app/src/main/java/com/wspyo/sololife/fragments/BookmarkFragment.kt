package com.wspyo.sololife.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.wspyo.sololife.R
import com.wspyo.sololife.contentsList.BookmarkRVAdapter
import com.wspyo.sololife.contentsList.ContentsModel
import com.wspyo.sololife.contentsList.ContentsRVAdapter
import com.wspyo.sololife.databinding.FragmentBookmarkBinding
import com.wspyo.sololife.utils.FBAuth
import com.wspyo.sololife.utils.FBRef

class BookmarkFragment : Fragment() {

    private lateinit var binding:FragmentBookmarkBinding
    private val Tag = BookmarkFragment::class.java.simpleName
    lateinit var rvAdapter: BookmarkRVAdapter
    val items = ArrayList<ContentsModel>()
    val keyList = ArrayList<String>()
    val bookmarkIdList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_bookmark,container,false)


        // 2. 북마크 contentList 가져옴
        getBookmarkData()

        rvAdapter = BookmarkRVAdapter(requireContext(),items,keyList,bookmarkIdList)
        val rv : RecyclerView = binding.bookmarkRV

        rv.adapter = rvAdapter
        rv.layoutManager = GridLayoutManager(requireContext(),2)


        binding.homeTap.setOnClickListener{
            it.findNavController().navigate(R.id.action_bookmarkFragment_to_homeFragment)
        }
        binding.tipTap.setOnClickListener{
            it.findNavController().navigate(R.id.action_bookmarkFragment_to_tipFragment)
        }
        binding.talkTap.setOnClickListener{
            it.findNavController().navigate(R.id.action_bookmarkFragment_to_talkFragment)
        }
        binding.storeTap.setOnClickListener{
            it.findNavController().navigate(R.id.action_bookmarkFragment_to_storeFragment)
        }


        return binding.root
    }


    private fun getCategoryData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(dataModel in dataSnapshot.children){
                    val item = dataModel.getValue(ContentsModel::class.java)

                    // 3. 1중에 2만 필터링하여 보여준다.
                    if(bookmarkIdList.contains(dataModel.key.toString())){
                        items.add(item!!)
                        keyList.add(dataModel.key.toString())
                    }
                    Log.d(Tag,dataModel.toString())
                }
                rvAdapter.notifyDataSetChanged() // 리스트 동기화

            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("loadPost:onCancelled", databaseError.toException())
            }
        }
        // 사용자의 uid 아래의 데이터를 불러옴으로써 DataSnapshot { key = -NyzavgeuKLwFdMUT2sG, value = {bookmarkIsTrue=true} } key,value를 분리한다.
        FBRef.category1.addValueEventListener(postListener)
        FBRef.category2.addValueEventListener(postListener)
    }

    private  fun getBookmarkData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(dataModel in dataSnapshot.children){
                    Log.e(Tag,dataModel.toString())
                    bookmarkIdList.add(dataModel.key.toString())
                }
                // 1. 전체 카테고리의 contentsList 가져옴
                getCategoryData()
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
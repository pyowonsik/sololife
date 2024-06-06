package com.wspyo.sololife.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.wspyo.sololife.databinding.FragmentHomeBinding
import com.wspyo.sololife.utils.FBRef

class HomeFragment : Fragment() {

    private  lateinit var  binding : FragmentHomeBinding

    private val TAG = HomeFragment::class.java.simpleName
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false)

        binding.tipTap.setOnClickListener{
//            Toast.makeText(context,"Clicked",Toast.LENGTH_LONG). show()
            it.findNavController().navigate(R.id.action_homeFragment_to_tipFragment)
        }

        binding.talkTap.setOnClickListener{
            it.findNavController().navigate(R.id.action_homeFragment_to_talkFragment)
        }

        binding.bookmarkTap.setOnClickListener{
            it.findNavController().navigate(R.id.action_homeFragment_to_bookmarkFragment)
        }

        binding.storeTap.setOnClickListener{
            it.findNavController().navigate(R.id.action_homeFragment_to_storeFragment)
        }

        rvAdapter = BookmarkRVAdapter(requireContext(),items,keyList,bookmarkIdList)
        val rv : RecyclerView = binding.mainRV
        rv.adapter = rvAdapter


        rv.layoutManager = GridLayoutManager(requireContext(),2)

        getCategoryData()

        return binding.root
    }


    private fun getCategoryData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(dataModel in dataSnapshot.children){
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
        // 사용자의 uid 아래의 데이터를 불러옴으로써 DataSnapshot { key = -NyzavgeuKLwFdMUT2sG, value = {bookmarkIsTrue=true} } key,value를 분리한다.
        FBRef.category1.addValueEventListener(postListener)
        FBRef.category2.addValueEventListener(postListener)
    }
}
package kr.huijoo.dailyinterview.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.nitrico.lastadapter.LastAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kr.huijoo.dailyinterview.BR
import kr.huijoo.dailyinterview.R
import kr.huijoo.dailyinterview.activity.QuestionActivity
import kr.huijoo.dailyinterview.databinding.ListItemBinding
import kr.huijoo.dailyinterview.model.QList
import java.util.*

/**
 * ListFragment.kt
 * 작성자 : 박희주
 * V1.0
 * 질문 목록 화면
 * 코틀린으로 작성(LastAdapter의 코틀린 지원)
 */

class ListFragment : Fragment() {
    var mDatabase = FirebaseDatabase.getInstance().reference
    var recyclerView: RecyclerView? = null
    var qListsArrayList = ArrayList<QList>()
    var adapter: LastAdapter? = null
    var listener: ValueEventListener? = null

    /**
     * Firebase DB 데이터 체인지 리스너는 백그라운드에서 돌면 안되므로, Resume에서 재할당
     */
    override fun onResume() {
        super.onResume()
        listener = mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // 질문 목록 데이터 재로딩
                qListsArrayList.clear()
                for (fileSnapshot in dataSnapshot.child("List").children) {
                    val temp = QList()
                    temp.listdate = fileSnapshot.child("ListDate").getValue(String::class.java)
                    temp.listimg = fileSnapshot.child("ListImg").getValue(String::class.java)
                    temp.listtitle = fileSnapshot.child("ListTitle").getValue(String::class.java)
                    qListsArrayList.add(0, temp)
                }
                adapter!!.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG: ", "Failed to read value", databaseError.toException())
            }
        })
    }

    /**
     * Firebase DB 데이터 체인지 리스너는 백그라운드에서 돌면 안되므로, Pause에서 제거
     */
    override fun onPause() {
        super.onPause()
        mDatabase.removeEventListener(listener!!)
    }

    /**
     * 레이아웃 파일 주입 및 리사이클러뷰 주입
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        recyclerView = view.findViewById<View>(R.id.recyclerview_list) as RecyclerView
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        // LastAdapter 사용하여 데이터 매핑하면서 동시에 클릭 리스너까지 부착
        adapter = LastAdapter(qListsArrayList, BR.listContent)
                .map<QList, ListItemBinding>(R.layout.list_item) {
                    onBind {
                        Glide.with(it.binding.iv.context)
                                .load(it.binding.listContent?.listimg)
                                .optionalCenterCrop()
                                .into(it.binding.iv)
                    }
                    onClick {
                        startActivity(
                                Intent(activity, QuestionActivity::class.java)
                                        .putExtra("title", it.binding.listContent?.listtitle)
                        )
                    }
                }
                .into(recyclerView!!)
        adapter!!.notifyDataSetChanged()
        return view
    }
}
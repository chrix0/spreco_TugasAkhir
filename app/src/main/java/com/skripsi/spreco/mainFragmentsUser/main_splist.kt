package com.skripsi.spreco.mainFragmentsUser

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.text.Layout
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.skripsi.spreco.CHANGE_TITLE
import com.skripsi.spreco.R
import com.skripsi.spreco.SHOW_PRODUCT_INFO
import com.skripsi.spreco.activityUser.user_spdetail
import com.skripsi.spreco.data
import com.skripsi.spreco.recyclerAdapters.recycler_sp_adapter
import kotlinx.android.synthetic.main.fragment_main_splist.*

class main_splist : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v = inflater.inflate(R.layout.fragment_main_splist, container, false)
        return code(v)
    }

    private fun code(v: View) : View{
        var productList = v.findViewById<RecyclerView>(R.id.productList)

        var adapter = recycler_sp_adapter(data.list_sp){
            val info = Intent(requireContext(), user_spdetail::class.java)
            info.putExtra(SHOW_PRODUCT_INFO, it as Parcelable)
            startActivity(info)
        }

        productList.layoutManager = GridLayoutManager(requireContext(), 2)
        productList.adapter = adapter
        return v
    }
}
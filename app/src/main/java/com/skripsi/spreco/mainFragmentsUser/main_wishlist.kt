package com.skripsi.spreco.mainFragmentsUser

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.spreco.R
import com.skripsi.spreco.SHOW_PRODUCT_INFO
import com.skripsi.spreco.activityUser.user_spdetail
import com.skripsi.spreco.classes.Smartphone
import com.skripsi.spreco.data
import com.skripsi.spreco.recyclerAdapters.recycler_sp_adapter
import kotlinx.android.synthetic.main.fragment_main_splist.*

class main_wishlist : Fragment() {
    private lateinit var nothing : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v = inflater.inflate(R.layout.fragment_main_wishlist, container, false)
        return code(v)
    }

    private fun code(v: View) : View{
        var productList = v.findViewById<RecyclerView>(R.id.productList)
        nothing = v.findViewById<TextView>(R.id.nothing_text)

        var db = data.getRoomHelper(requireContext())
        // Ambil semua data wishlist untuk ID akun tertentu
        var wishlist = db.daoWishlist().getAllWish(data.currentAccId)

        // Ambil data Smartphone berdasarkan idSP
        var sps : MutableList<Smartphone> = mutableListOf()
        for (i in wishlist){
            //Setiap smartphone memiliki ID unik, sehingga dapat dipastikan bahwa hasil query berjumlah satu.
            sps.add(db.daoSP().getSPDetail(i.idSP)[0])
        }

        var adapter = recycler_sp_adapter(sps, requireContext()){
            val info = Intent(requireContext(), user_spdetail::class.java)
            info.putExtra(SHOW_PRODUCT_INFO, it as Parcelable)
            startActivity(info)
        }

        productList.layoutManager = GridLayoutManager(requireContext(), 2)
        productList.adapter = adapter

        if(sps.isEmpty()){
            nothing.visibility = View.VISIBLE
        }
        return v
    }

    override fun onResume() {
        super.onResume()
        var db = data.getRoomHelper(requireContext())
        var wishlist = db.daoWishlist().getAllWish(data.currentAccId)

        var sps : MutableList<Smartphone> = mutableListOf()
        for (i in wishlist){
            //Setiap smartphone memiliki ID unik, sehingga dapat dipastikan bahwa hasil query berjumlah satu.
            sps.add(db.daoSP().getSPDetail(i.idSP)[0])
        }

        var adapter = recycler_sp_adapter(sps, requireContext()){
            val info = Intent(requireContext(), user_spdetail::class.java)
            info.putExtra(SHOW_PRODUCT_INFO, it as Parcelable)
            startActivity(info)
        }

        productList.adapter = adapter

        if(sps.isEmpty()){
            nothing.visibility = View.VISIBLE
        }
    }
}
package com.skripsi.spreco.mainFragmentsUser

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.spreco.R
import com.skripsi.spreco.SHOW_PRODUCT_INFO
import com.skripsi.spreco.SHOW_SPDATA_TO_EDIT
import com.skripsi.spreco.activityAdmin.admin_editor
import com.skripsi.spreco.activityUser.user_spdetail
import com.skripsi.spreco.classes.Account
import com.skripsi.spreco.classes.SP_rank
import com.skripsi.spreco.classes.Smartphone
import com.skripsi.spreco.data
import com.skripsi.spreco.recyclerAdapters.recycler_recshow_adapter
import com.skripsi.spreco.recyclerAdapters.recycler_sp_adapter
import kotlinx.android.synthetic.main.activity_user_recshow.*
import kotlinx.android.synthetic.main.activity_user_recshow.nothing_text
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
        var search = v.findViewById<EditText>(R.id.search)
        var sortButton = v.findViewById<ImageButton>(R.id.sortButton)
        var nothing = v.findViewById<TextView>(R.id.nothing_text)

        var db = data.getRoomHelper(requireContext())
        // Berisi kumpulan smartphone yang tersedia di dalam sistem.
        // Diubah ke tipe MutableList agar isinya bisa diubah
        var spsMutable = db.daoSP().getAllSP().toMutableList()
        var temp : MutableList<Smartphone> = mutableListOf()
        temp.addAll(spsMutable)

        var adapter : recycler_sp_adapter
        // Menambah data ke dalam recycler view
        if (data.curRole == 'C'){
            adapter = recycler_sp_adapter(spsMutable){
                val info = Intent(requireContext(), user_spdetail::class.java)
                info.putExtra(SHOW_PRODUCT_INFO, it as Parcelable)
                startActivity(info)
            }
        }
        else{
            adapter = recycler_sp_adapter(spsMutable){
                val info = Intent(requireContext(), admin_editor::class.java)
                info.putExtra(SHOW_SPDATA_TO_EDIT, it as Parcelable)
                startActivity(info)
            }
        }

        productList.layoutManager = GridLayoutManager(requireContext(), 2)
        productList.adapter = adapter

        if(spsMutable.isEmpty()){
            nothing.visibility = View.VISIBLE
        }

        fun filter(searchText: String, context: Context): MutableList<Smartphone> {
            var db = data.getRoomHelper(context)
            var like = "%${searchText}%"
            return db.daoSP().getSPLike(like).toMutableList()
        }

        // Search bar
        search.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE){
                spsMutable.clear()
                spsMutable.addAll(filter(search.text.toString(), requireContext()))
                adapter = recycler_sp_adapter(spsMutable){
                    val info = Intent(requireContext(), user_spdetail::class.java)
                    info.putExtra(SHOW_PRODUCT_INFO, it as Parcelable)
                    startActivity(info)
                }
                productList.adapter = adapter
            }
            return@setOnEditorActionListener true
        }

        // Tombol sort
        var popup = PopupMenu(requireContext(), sortButton)
        popup.menuInflater.inflate(R.menu.menu_sort_splist, popup.menu)
        sortButton.setOnClickListener{
            popup.show()
        }

        fun showModifiedRec(spsMutable : MutableList<Smartphone>){
            adapter = recycler_sp_adapter(spsMutable){
                val info = Intent(requireContext(), user_spdetail::class.java)
                info.putExtra(SHOW_PRODUCT_INFO, it as Parcelable)
                startActivity(info)
            }
            productList.adapter = adapter

            if(spsMutable.isEmpty()){
                nothing.visibility = View.VISIBLE
            }
        }

        popup.setOnMenuItemClickListener {
            return@setOnMenuItemClickListener when(it.itemId){
                R.id.harga_des->{
                    temp.sortBy { it.harga }
                    temp.reverse()
                    showModifiedRec(spsMutable)
                    true
                }
                R.id.harga_asc ->{
                    temp.sortBy { it.harga }
                    showModifiedRec(spsMutable)
                    true
                }
                R.id.nama_des->{
                    temp.sortBy { it.namaSP }
                    temp.reverse()
                    showModifiedRec(spsMutable)
                    true
                }
                R.id.nama_asc ->{
                    temp.sortBy { it.namaSP }
                    showModifiedRec(spsMutable)
                    true
                }
                else -> false
            }
        }

        return v
    }
}
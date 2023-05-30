package com.skripsi.spreco.mainFragmentsUser

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.spreco.R
import com.skripsi.spreco.SHOW_PRODUCT_INFO
import com.skripsi.spreco.SHOW_SPDATA_TO_EDIT
import com.skripsi.spreco.activityAdmin.admin_editor
import com.skripsi.spreco.activityUser.user_spdetail
import com.skripsi.spreco.classes.Smartphone
import com.skripsi.spreco.data
import com.skripsi.spreco.recyclerAdapters.recycler_sp_adapter
import com.skripsi.spreco.util.spListScroll
import kotlinx.android.synthetic.main.fragment_main_splist.*


class main_splist : Fragment() {
    lateinit var productList : RecyclerView
    lateinit var adapter : recycler_sp_adapter
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
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        productList = v.findViewById<RecyclerView>(R.id.productList)
        var search = v.findViewById<EditText>(R.id.search)
        var sortButton = v.findViewById<ImageButton>(R.id.sortButton)
        var nothing = v.findViewById<TextView>(R.id.nothing_text)
        var countDaftar = v.findViewById<TextView>(R.id.countDaftar)

        var db = data.getRoomHelper(requireContext())
        // Berisi kumpulan smartphone yang tersedia di dalam sistem.
        // Diubah ke tipe MutableList agar isinya bisa diubah
        var spsMutable = db.daoSP().getAllSP().toMutableList()

        // Menambah data ke dalam recycler view
        if (data.curRole == 'C'){
            adapter = recycler_sp_adapter(spsMutable, requireContext()){
                val info = Intent(requireContext(), user_spdetail::class.java)
                info.putExtra(SHOW_PRODUCT_INFO, it as Parcelable)
                startActivity(info)
            }
            adapter.stateRestorationPolicy=RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
        else{
            adapter = recycler_sp_adapter(spsMutable, requireContext()){
                val info = Intent(requireContext(), admin_editor::class.java)
                info.putExtra(SHOW_SPDATA_TO_EDIT, it as Parcelable)
                startActivity(info)
            }
            adapter.stateRestorationPolicy=RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }

        productList.layoutManager = GridLayoutManager(requireContext(), 2)
        productList.adapter = adapter

        if(spsMutable.isEmpty()){
            nothing.visibility = View.VISIBLE
        }
        countDaftar.text = "Jumlah data: ${adapter.myData.size}"

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
                if (data.curRole == 'C'){
                    adapter = recycler_sp_adapter(spsMutable, requireContext()){
                        val info = Intent(requireContext(), user_spdetail::class.java)
                        info.putExtra(SHOW_PRODUCT_INFO, it as Parcelable)
                        startActivity(info)
                    }
                    adapter.stateRestorationPolicy=RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                }
                else{
                    adapter = recycler_sp_adapter(spsMutable, requireContext()){
                        val info = Intent(requireContext(), admin_editor::class.java)
                        info.putExtra(SHOW_SPDATA_TO_EDIT, it as Parcelable)
                        startActivity(info)
                    }
                    adapter.stateRestorationPolicy=RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                }
                productList.adapter = adapter
                countDaftar.text = "Jumlah data: ${adapter.myData.size}"
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
            adapter = recycler_sp_adapter(spsMutable, requireContext()){
                val info = Intent(requireContext(), user_spdetail::class.java)
                info.putExtra(SHOW_PRODUCT_INFO, it as Parcelable)
                startActivity(info)
            }
            productList.adapter = adapter
            if(spsMutable.isEmpty()){
                nothing.visibility = View.VISIBLE
            }
            countDaftar.text = "Jumlah data: ${adapter.myData.size}"
        }

        popup.setOnMenuItemClickListener { menu ->
            return@setOnMenuItemClickListener when(menu.itemId){
                R.id.harga_des->{
                    var temp = (productList.adapter as recycler_sp_adapter).myData.toMutableList()
                    temp.sortBy { it.harga }
                    temp.reverse()
                    showModifiedRec(temp)
                    true
                }
                R.id.harga_asc ->{
                    var temp = (productList.adapter as recycler_sp_adapter).myData.toMutableList()
                    temp.sortBy { it.harga }
                    showModifiedRec(temp)
                    true
                }
                R.id.nama_des->{
                    var temp = (productList.adapter as recycler_sp_adapter).myData.toMutableList()
                    temp.sortBy { it.namaSP.lowercase() }
                    temp.reverse()
                    showModifiedRec(temp)
                    true
                }
                R.id.nama_asc ->{
                    var temp = (productList.adapter as recycler_sp_adapter).myData.toMutableList()
                    temp.sortBy { it.namaSP.lowercase()  }
                    showModifiedRec(temp)
                    true
                }
                else -> false
            }
        }

        return v
    }

    override fun onPause() {
        super.onPause()
        //Simpan posisi scroll di objek spListScroll sebelum berpindah activity atau menu
        spListScroll.posisiScroll = (productList.layoutManager as GridLayoutManager).findFirstCompletelyVisibleItemPosition()
        //Simpan adapter di objek spListScroll untuk menyimpan hasil sebelum berpindah activity atau menu
        spListScroll.lastShown = productList.adapter as recycler_sp_adapter
    }

    override fun onResume() {
        super.onResume()

        // Callback ini dijalakan ketika user kembali ke halaman ini atau pertama kali membuka halaman ini.
        // Jika user kembali ke halaman ini, cek apakah adapter recycler view product_list telah disimpan sebelumnya
        // jika ya, gunakan adaptor tersebut pada recycler view product_list
        if ((data.curRole == 'C') and (spListScroll.lastShown != null)) {

            // Ketika user kembali ke halaman ini, cek apakah adapter recycler view product_list telah disimpan sebelumnya
            // jika ya, gunakan adaptor tersebut pada recycler view product_list
            if ((data.curRole == 'C') and (spListScroll.lastShown != null)) {
                adapter = spListScroll.lastShown!!
            } else if ((data.curRole == 'A') and (spListScroll.lastShown != null)) {
                adapter = spListScroll.lastShown!!
            }

            productList.layoutManager = GridLayoutManager(requireContext(), 2)
            productList.adapter = adapter
            productList.adapter!!.notifyDataSetChanged()

            // Scroll ke posisi yang sama seperti sebelumnya
            (productList.layoutManager as GridLayoutManager).scrollToPosition(spListScroll.posisiScroll)
        }
    }
}


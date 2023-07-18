package com.skripsi.spreco.mainFragmentsAdmin

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.spreco.R
import com.skripsi.spreco.SHOW_PRODUCT_INFO
import com.skripsi.spreco.activityUser.user_spdetail
import com.skripsi.spreco.classes.Account
import com.skripsi.spreco.classes.Smartphone
import com.skripsi.spreco.data
import com.skripsi.spreco.recyclerAdapters.recycler_cust_adapter
import com.skripsi.spreco.recyclerAdapters.recycler_link_modify
import com.skripsi.spreco.recyclerAdapters.recycler_sp_adapter
import org.jetbrains.anko.sdk27.coroutines.onCheckedChange
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class main_manageCustomer : Fragment() {

    lateinit var custList: RecyclerView
    lateinit var adapter : recycler_cust_adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v = inflater.inflate(R.layout.fragment_main_manage_customer, container, false)
        return code(v)
    }

    fun code(v : View) : View{

        val tbSearchCust = v.findViewById<EditText>(R.id.tbSearchCust)
        var sortButton = v.findViewById<ImageButton>(R.id.sortButton)
        var nothing = v.findViewById<TextView>(R.id.nothing_text)
        custList = v.findViewById<RecyclerView>(R.id.custList)

        var db = data.getRoomHelper(requireContext())
        var accs = db.daoAccount().getAllAcc() as MutableList
        var temp = mutableListOf<Account>()

        fun filter(sort : String, searchText: String): MutableList<Account> {
            var like = "%${searchText}%"
            var found = db.daoAccount().getAccLikeUsername(like)
            if(sort == "asc"){
                found.sortBy { it.username }
            }
            else{
                found.sortByDescending { it.username }
            }
            return found
        }

        tbSearchCust.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE){
                var searchText = tbSearchCust.text
                var like = "%${searchText}%"
                var res = db.daoAccount().getAccLikeUsername(like)
                adapter = recycler_cust_adapter(res, requireContext()){
                    this.adapter.notifyDataSetChanged()
                }
                custList.adapter = adapter

                nothing.text = "Data akun customer tidak ditemukan"
                if(res.isEmpty()){
                    nothing.visibility = View.VISIBLE
                }
                else{
                    nothing.visibility = View.GONE
                }
            }
            return@setOnEditorActionListener true
        }

        var popup = PopupMenu(requireContext(), sortButton)
        popup.menuInflater.inflate(R.menu.menu_sort_customer, popup.menu)
        sortButton.setOnClickListener{
            popup.show()
        }

        fun showModifiedRec(spsMutable : MutableList<Account>){
            adapter = recycler_cust_adapter(spsMutable, requireContext()){
                this.adapter.notifyDataSetChanged()
            }
            custList.adapter = adapter

            if(spsMutable.isEmpty()){
                nothing.visibility = View.VISIBLE
                nothing.text = "Data tidak ditemukan"
            }
        }

        popup.setOnMenuItemClickListener { menu ->
            var temp = (custList.adapter as recycler_cust_adapter).myData.toMutableList()
            return@setOnMenuItemClickListener when(menu.itemId){
                R.id.id_asc->{
                    temp.sortBy { it.idAcc }
                    showModifiedRec(temp)
                    true
                }
                R.id.id_des ->{
                    temp.sortByDescending { it.idAcc }
                    showModifiedRec(temp)
                    true
                }
                R.id.username_asc ->{
                    temp.sortBy { it.username.lowercase()}
                    showModifiedRec(temp)
                    true
                }
                R.id.username_des ->{
                    temp.sortByDescending { it.username.lowercase()}
                    showModifiedRec(temp)
                    true
                }
                R.id.lastLogin_asc ->{
                    // Ascending, urutkan secara descending (Dari yang terbaru hingga terlama)
                    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
                    temp.sortWith(compareBy { LocalDateTime.parse(it.terakhirLogin, formatter) })
                    temp.reverse()
                    showModifiedRec(temp)
                    true
                }
                R.id.lastLogin_des ->{
                    // Descending, urutkan secara ascending (Dari yang terlama hingga terbaru)
                    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
                    temp.sortWith(compareBy { LocalDateTime.parse(it.terakhirLogin, formatter) })
                    showModifiedRec(temp)
                    true
                }
                else -> false
            }
        }


        adapter = recycler_cust_adapter(accs, requireContext()){
            this.adapter.notifyDataSetChanged()
        }
        custList.layoutManager = LinearLayoutManager(requireContext())
        custList.adapter = adapter

        return v
    }

    override fun onResume() {
        super.onResume()
        custList.adapter!!.notifyDataSetChanged()
    }
}
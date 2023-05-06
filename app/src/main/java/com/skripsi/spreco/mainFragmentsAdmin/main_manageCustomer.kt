package com.skripsi.spreco.mainFragmentsAdmin

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
import android.widget.RadioButton
import android.widget.Toast
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
        val byUsername = v.findViewById<RadioButton>(R.id.byUsername)
        val byID = v.findViewById<RadioButton>(R.id.byID)
        val ascend = v.findViewById<RadioButton>(R.id.ascend)
        val descend = v.findViewById<RadioButton>(R.id.descend)
        val buttonCari = v.findViewById<Button>(R.id.buttonCari)
        custList = v.findViewById<RecyclerView>(R.id.custList)

        var db = data.getRoomHelper(requireContext())
        var accs = db.daoAccount().getAllAcc() as MutableList
        var temp = mutableListOf<Account>()

        fun filter(mode: String, sort : String, searchText: String, context: Context): MutableList<Account> {
            var db = data.getRoomHelper(context)
            var found: MutableList<Account> = if (mode == "username"){
                var like = "%${searchText}%"
                var found = db.daoAccount().getAccLikeUsername(like)
                if(sort == "asc"){
                    found.sortBy { it.username }
                }
                else{
                    found.sortBy { it.username }
                    found.reverse()
                }
                found
            } else{ //ID
                var like = "%${searchText}%"
                var found = db.daoAccount().getAccLikeID(like)
                if(sort == "asc"){
                    found.sortBy { it.idAcc }
                }
                else{
                    found.sortBy { it.idAcc }
                    found.reverse()
                }
                found
            }

            return found
        }

        var mode = ""
        byUsername.onCheckedChange { _, isChecked ->
            if(isChecked)
                mode = "username"
        }
        byID.onCheckedChange { _, isChecked ->
            if(isChecked)
                mode = "id"
        }

        var sort = ""
        descend.onCheckedChange { _, isChecked ->
            if(isChecked)
                sort = "des"
        }
        ascend.onCheckedChange { _, isChecked ->
            if(isChecked)
                sort = "asc"
        }

        buttonCari.setOnClickListener {
            if((sort == "") and (mode == "")){
                Toast.makeText(requireContext(),"Jenis pencarian dan jenis urutan harus dipilih terlebih dahulu.", Toast.LENGTH_SHORT).show()
            }
            else{
                temp.clear()
                temp.addAll(filter(mode, sort, tbSearchCust.text.toString(), requireContext()))
                adapter = recycler_cust_adapter(temp, requireContext()){
                    this.adapter.notifyDataSetChanged()
                }
                custList.adapter = adapter
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
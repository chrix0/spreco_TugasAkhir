package com.skripsi.spreco.mainFragmentsUser

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.skripsi.spreco.R
import com.skripsi.spreco.accountAuth.login
import com.skripsi.spreco.activityUser.user_editprofile
import com.skripsi.spreco.classes.Account
import com.skripsi.spreco.data
import com.skripsi.spreco.data.clearStack
import com.skripsi.spreco.util.spListScroll
import kotlinx.android.synthetic.main.fragment_main_about.*

class main_about : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var layout = inflater.inflate(R.layout.fragment_main_about, container, false)
        return code(layout)
    }

    private fun code(v : View) : View{
        var logout = v.findViewById<Button>(R.id.logout)
        var edit = v.findViewById<Button>(R.id.profileEdit)
        var greet = v.findViewById<TextView>(R.id.greet)
        var tellRole = v.findViewById<TextView>(R.id.tellRole)

        var db = data.getRoomHelper(requireContext())
        var getAcc = db.daoAccount().getAccById(data.currentAccId)
        var acc : Account

        var role = ""

        if (getAcc.isEmpty()){
            role = "Admin"
            edit.visibility = View.INVISIBLE
            greet.text = "Halo."
        }
        else{
            acc = getAcc[0]
            role = "Customer"
            greet.text = "Halo, ${acc.namaAcc}."
        }

        tellRole.text = "Anda sedang login sebagai $role"

        logout.setOnClickListener {
            var intent = Intent(requireContext(), login::class.java)
            intent.clearStack()
            spListScroll.lastShown = null
            startActivity(intent)
        }
        edit.setOnClickListener {
            var intent = Intent(requireContext(), user_editprofile::class.java)
            startActivity(intent)
        }
        return v
    }
}
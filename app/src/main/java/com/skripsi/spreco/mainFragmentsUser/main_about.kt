package com.skripsi.spreco.mainFragmentsUser

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.skripsi.spreco.R
import com.skripsi.spreco.accountAuth.login

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
        logout.setOnClickListener {
            var intent = Intent(requireContext(), login::class.java)
            startActivity(intent)
        }
        return v
    }
}
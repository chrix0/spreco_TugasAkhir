package com.skripsi.spreco.mainFragmentsUser

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.skripsi.spreco.R
import com.skripsi.spreco.activityUser.user_recsettings
import com.skripsi.spreco.activityUser.user_recshow

class main_rekomendasi : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var layout = inflater.inflate(R.layout.fragment_main_rekomendasi, container, false)
        return code(layout)
    }

    private fun code(v: View) : View{
        val rec_settings = v.findViewById<Button>(R.id.rec_settings)
        val rec_show = v.findViewById<Button>(R.id.rec_show)
        rec_settings.setOnClickListener {
            var intent = Intent(requireContext(), user_recsettings::class.java)
            startActivity(intent)
        }
        rec_show.setOnClickListener {
            var intent = Intent(requireContext(), user_recshow::class.java)
            startActivity(intent)
        }
        return v
    }
}
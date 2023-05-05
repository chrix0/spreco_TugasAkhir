package com.skripsi.spreco.mainFragmentsAdmin

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.icu.number.NumberFormatter.with
import android.icu.number.NumberRangeFormatter.with
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.spreco.R
import com.skripsi.spreco.RETURN_LAST_TAB
import com.skripsi.spreco.activityUser.adapter
import com.skripsi.spreco.classes.SPSource
import com.skripsi.spreco.classes.Smartphone
import com.skripsi.spreco.classes.recHistory
import com.skripsi.spreco.data
import com.skripsi.spreco.recyclerAdapters.recycler_link_modify
import com.skripsi.spreco.recyclerAdapters.recycler_link_pembelian
import com.skripsi.spreco.recyclerAdapters.recycler_recshow_adapter
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_user_rechistory.*
import kotlinx.android.synthetic.main.recycler_rekomendasi_res.*
import java.text.SimpleDateFormat
import java.util.*

class main_tambah : Fragment() {
    lateinit var adapter : recycler_link_modify

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
//    https://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
    fun EditText.transformIntoDatePicker(context: Context, format: String, maxDate: Date? = null) {
        isFocusableInTouchMode = false
        isClickable = true
        isFocusable = false

        val myCalendar = Calendar.getInstance()
        val datePickerOnDataSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val sdf = SimpleDateFormat(format, Locale.ENGLISH)
                setText(sdf.format(myCalendar.time))
            }

        setOnClickListener {
            DatePickerDialog(
                context, datePickerOnDataSetListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).run {
                maxDate?.time?.also { datePicker.maxDate = it }
                show()
            }
        }
    }

    fun getCurrentDateTime():String{
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        return sdf.format(Date())
    }

//    https://stackoverflow.com/questions/32989851/how-can-i-use-a-color-as-placeholder-image-with-picasso
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v =  inflater.inflate(R.layout.fragment_main_tambah, container, false)
        return code(v)
    }

    fun code(v : View) : View{
        val ed_namasp = v.findViewById<EditText>(R.id.ed_namasp)
        val ed_merek = v.findViewById<EditText>(R.id.ed_merek)
        val ed_harga = v.findViewById<EditText>(R.id.ed_harga)
        val ed_ram = v.findViewById<EditText>(R.id.ed_ram)
        val ed_rom = v.findViewById<EditText>(R.id.ed_rom)
        val ed_chipset = v.findViewById<EditText>(R.id.ed_chipset)
        val ed_cpu = v.findViewById<EditText>(R.id.ed_cpu)
        val ed_gpu = v.findViewById<EditText>(R.id.ed_gpu)
        val ed_tdisplay = v.findViewById<EditText>(R.id.ed_tdisplay)
        val ed_udisplay = v.findViewById<EditText>(R.id.ed_udisplay)
        val ed_maincam = v.findViewById<EditText>(R.id.ed_maincam)
        val ed_frontcam = v.findViewById<EditText>(R.id.ed_frontcam)
        val ed_os = v.findViewById<EditText>(R.id.ed_os)
        val ed_battery = v.findViewById<EditText>(R.id.ed_battery)
        val ed_network = v.findViewById<EditText>(R.id.ed_network)
        val ed_simSlot = v.findViewById<EditText>(R.id.ed_simSlot)
        val ed_warna = v.findViewById<EditText>(R.id.ed_warna)
        val ed_tglrilis = v.findViewById<EditText>(R.id.ed_tglrilis)
        val ed_urlpic = v.findViewById<EditText>(R.id.ed_urlpic)

        val previewURL =  v.findViewById<Button>(R.id.previewURL)
        val previewPic =  v.findViewById<ImageView>(R.id.previewPic)

        val linkList =  v.findViewById<RecyclerView>(R.id.linkList)
        val addLink =  v.findViewById<Button>(R.id.addLink)

        val addnew =  v.findViewById<Button>(R.id.addnew)


        //Placeholder dengan warna abu-abu solid
        val placeHolderColor: Int = Color.rgb(20, 20, 20)
        var gradientDrawable = GradientDrawable()
        gradientDrawable.shape = GradientDrawable.RECTANGLE
        gradientDrawable.setColor(placeHolderColor)

        //https://stackoverflow.com/questions/25744344/android-picasso-load-image-failed-how-to-show-error-message?answertab=scoredesc#tab-top
        previewPic.setImageDrawable(gradientDrawable)
        previewURL.setOnClickListener {
            if(ed_urlpic.text.toString() != ""){
                Picasso.get().load(ed_urlpic.text.toString().trim()).placeholder(gradientDrawable).into(previewPic, object : Callback{
                    override fun onSuccess() {
                        Toast.makeText(requireContext(),"Gambar ditemukan.", Toast.LENGTH_SHORT).show()
                    }
                    override fun onError(e: java.lang.Exception?) {
                        Toast.makeText(requireContext(),"Gambar tidak ditemukan.\nCoba gunakan URL lain.", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            else{
                Toast.makeText(requireContext(),"Isi URL Gambar Produk terlebih dahulu.", Toast.LENGTH_SHORT).show()
            }

        }

        data.links = mutableListOf()
        adapter = recycler_link_modify(data.links, requireContext()){
            //Kalau tombol hapusnya ditekan.. update RecyclerView
            this.adapter.notifyDataSetChanged()
        }
        linkList.layoutManager = LinearLayoutManager(requireContext())
        linkList.adapter = adapter

        // Jika EditText Tanggal Rilis ditekan, munculkan dialog untuk memilih DateTime.
        ed_tglrilis.transformIntoDatePicker(requireContext(), "dd/MM/yyyy")

        addLink.setOnClickListener {
            var layout = layoutInflater.inflate(R.layout.dialog_tambah_link,null)
            var dialog = AlertDialog.Builder(requireContext()).apply{
                setView(layout)
            }
            var creator = dialog.create()

            val namaWebsite = layout.findViewById<EditText>(R.id.namaWebsite)
            var urlProduk = layout.findViewById<EditText>(R.id.urlProduk)
            val tambah_link = layout.findViewById<Button>(R.id.tambah_link)
            val batal = layout.findViewById<TextView>(R.id.batal)

            tambah_link.setOnClickListener {
                if((namaWebsite.text.toString() == "") or (urlProduk.text.toString() == "")){
                    Toast.makeText(requireContext(), "Nama website dan URL produk harus diisi.", Toast.LENGTH_SHORT).show()
                }
                else{
                    var url = urlProduk.text.toString()
                    //Wajib menggunakan protocol http
                    if (!url.startsWith("https://") && !url.startsWith("http://")) {
                        url = "http://$url"
                    }
                    //SPSource ini belum terhubung ke data smartphone apapun, sehingga idSP diberi -1.
                    var newSource = SPSource(0, -1, namaWebsite.text.toString(), url, getCurrentDateTime())
                    data.links.add(newSource)

                    Toast.makeText(requireContext(), "Link telah ditambahkan.", Toast.LENGTH_SHORT).show()
                    creator.cancel()
                    adapter = recycler_link_modify(data.links, requireContext()){
                        //Kalau tombol hapusnya ditekan.. update RecyclerView
                        this.adapter.notifyDataSetChanged()
                    }
                    linkList.adapter = adapter
                }
            }

            batal.setOnClickListener{
                creator.cancel()
            }

            creator.show()
        }


        addnew.setOnClickListener {
            var pass = true
            for (editText in listOf(ed_namasp, ed_merek, ed_harga, ed_ram, ed_rom,
                ed_chipset, ed_cpu, ed_gpu, ed_tdisplay, ed_udisplay, ed_maincam,
                ed_frontcam, ed_os, ed_battery, ed_network, ed_simSlot, ed_warna,
                ed_tglrilis, ed_urlpic)) {
                if (editText.text.isEmpty()){
                    pass = false
                    break
                }
            }
            if (pass){
                //String
                var value_pass = true
                val namasp = ed_namasp.text.toString()
                val merek = ed_merek.text.toString()
                val chipset = ed_chipset.text.toString()
                val cpu = ed_cpu.text.toString()
                val gpu = ed_gpu.text.toString()
                val tdisplay = ed_tdisplay.text.toString()
                val os = ed_os.text.toString()
                val network = ed_network.text.toString()
                val warna = ed_warna.text.toString()
                val tglrilis = ed_tglrilis.text.toString()
                val urlpic = ed_urlpic.text.toString()
                val sim = ed_simSlot.text.toString()

                var harga = 0
                var ram = 0
                var rom = 0
                var udisplay  = 0.0
                var maincam = 0.0
                var frontcam = 0.0
                var battery = 0

                //Double / Int
                try {
                    harga = ed_harga.text.toString().toInt()
                } catch (e : Exception){
                    Toast.makeText(requireContext(), "Nilai harga melewati nilai batas (${Int.MAX_VALUE}).", Toast.LENGTH_SHORT).show()
                    value_pass = false
                }

                try {
                    ram = ed_ram.text.toString().toInt()
                } catch (e : Exception){
                    Toast.makeText(requireContext(), "Nilai RAM melewati nilai batas (${Int.MAX_VALUE}).", Toast.LENGTH_SHORT).show()
                    value_pass = false
                }

                try {
                    rom = ed_rom.text.toString().toInt()
                } catch (e : Exception){
                    Toast.makeText(requireContext(), "Nilai ROM melewati nilai batas (${Int.MAX_VALUE}).", Toast.LENGTH_SHORT).show()
                    value_pass = false
                }

                try {
                    udisplay = ed_udisplay.text.toString().toDouble()
                } catch (e : Exception){
                    Toast.makeText(requireContext(), "Nilai Ukuran Display melewati nilai batas (${Double.MAX_VALUE}).", Toast.LENGTH_SHORT).show()
                    value_pass = false
                }

                try {
                    maincam = ed_maincam.text.toString().toDouble()
                } catch (e : Exception){
                    Toast.makeText(requireContext(), "Nilai Kamera Belakang melewati nilai batas (${Double.MAX_VALUE}).", Toast.LENGTH_SHORT).show()
                    value_pass = false
                }

                try {
                    frontcam = ed_frontcam.text.toString().toDouble()
                } catch (e : Exception){
                    Toast.makeText(requireContext(), "Nilai Kamera Depan melewati nilai batas (${Double.MAX_VALUE}).", Toast.LENGTH_SHORT).show()
                    value_pass = false
                }

                try {
                    battery = ed_battery.text.toString().toInt()
                } catch (e : Exception){
                    Toast.makeText(requireContext(), "Nilai Kapasitas baterai melewati nilai batas (${Int.MAX_VALUE}).", Toast.LENGTH_SHORT).show()
                    value_pass = false
                }

                if (value_pass){
                    var sp = Smartphone(
                        0,
                        namaSP = namasp,
                        merek = merek,
                        chipset = chipset,
                        cpu =  cpu,
                        gpu = gpu,
                        tDisplay = tdisplay,
                        OS = os,
                        network = network,
                        warna = warna,
                        tanggalRilis = tglrilis,
                        picURL = urlpic,
                        harga = harga,
                        ram = ram,
                        rom = rom,
                        uDisplay = udisplay,
                        mainCam = maincam,
                        selfieCam = frontcam,
                        battery = battery,
                        sim =  sim
                    )

                    var db = data.getRoomHelper(requireContext())
                    var getSPId = db.daoSP().addSP(sp)
                    for (i in data.links){
                        i.idSP = getSPId.toInt()
                    }
                    db.daoSPSource().addAllSource(data.links)

                    Toast.makeText(requireContext(),"Data smartphone berhasil ditambah.", Toast.LENGTH_LONG).show()

                    //Reset activity biar kembali ke posisi semula (akibat me malas aja), yang penting hasilnya sesuai.
                    var intent = Intent(activity!!.intent)
                    intent.putExtra(RETURN_LAST_TAB, "ADD")
                    this.activity!!.finish()
                    this.activity!!.overridePendingTransition(0, 0)
                    this.activity!!.startActivity(intent);
                    this.activity!!.overridePendingTransition(0, 0)
                }
            }
            else{
                Toast.makeText(requireContext(),"Semua atribut smartphone harus diisi.", Toast.LENGTH_LONG).show()
            }
        }
        return v
    }

}
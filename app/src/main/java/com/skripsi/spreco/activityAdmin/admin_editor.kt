package com.skripsi.spreco.activityAdmin

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.spreco.*
import com.skripsi.spreco.accountAuth.login
import com.skripsi.spreco.classes.SPSource
import com.skripsi.spreco.classes.Smartphone
import com.skripsi.spreco.recyclerAdapters.recycler_link_modify
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_admin_editor.*
import kotlinx.android.synthetic.main.activity_admin_pass_setting.*
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class admin_editor : AppCompatActivity() {
    lateinit var adapter : recycler_link_modify

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_editor)

        val produk : Smartphone = intent.getParcelableExtra<Parcelable>(SHOW_SPDATA_TO_EDIT) as Smartphone

        idCustomer.text = "ID Smarphone: ${produk.idSP}"

        val ed_namasp = findViewById<EditText>(R.id.ed_namasp)
        val ed_merek = findViewById<EditText>(R.id.ed_merek)
        val ed_harga = findViewById<EditText>(R.id.ed_harga)
        val ed_ram = findViewById<EditText>(R.id.ed_ram)
        val ed_rom = findViewById<EditText>(R.id.ed_rom)
        val ed_chipset = findViewById<EditText>(R.id.ed_chipset)
        val ed_cpu = findViewById<EditText>(R.id.ed_cpu)
        val ed_gpu = findViewById<EditText>(R.id.ed_gpu)
        val ed_tdisplay = findViewById<EditText>(R.id.ed_tdisplay)
        val ed_udisplay = findViewById<EditText>(R.id.ed_udisplay)
        val ed_maincam = findViewById<EditText>(R.id.ed_maincam)
        val ed_frontcam = findViewById<EditText>(R.id.ed_frontcam)
        val ed_os = findViewById<EditText>(R.id.ed_os)
        val ed_battery = findViewById<EditText>(R.id.ed_battery)
        val ed_network = findViewById<EditText>(R.id.ed_network)
        val ed_simSlot = findViewById<EditText>(R.id.ed_simSlot)
        val ed_warna = findViewById<EditText>(R.id.ed_warna)
        val ed_tglrilis = findViewById<EditText>(R.id.ed_tglrilis)
        val ed_urlpic = findViewById<EditText>(R.id.ed_urlpic)

        ed_namasp.setText(produk.namaSP)
        ed_merek.setText(produk.merek)
        ed_harga.setText(produk.harga.toString())
        ed_ram.setText(produk.ram.toString())
        ed_rom.setText(produk.rom.toString())
        ed_chipset.setText(produk.chipset)
        ed_cpu.setText(produk.cpu)
        ed_gpu.setText(produk.gpu)
        ed_tdisplay.setText(produk.tDisplay)
        ed_udisplay.setText(produk.uDisplay.toString())
        ed_maincam.setText(produk.mainCam.toString())
        ed_frontcam.setText(produk.selfieCam.toString())
        ed_os.setText(produk.OS)
        ed_battery.setText(produk.battery.toString())
        ed_network.setText(produk.network)
        ed_simSlot.setText(produk.sim)
        ed_warna.setText(produk.warna)
        ed_tglrilis.setText(produk.tanggalRilis)
        ed_tglrilis.transformIntoDatePicker(this, "dd/MM/yyyy")
        ed_urlpic.setText(produk.picURL)

        val previewURL =  findViewById<Button>(R.id.previewURL)
        val previewPic =  findViewById<ImageView>(R.id.previewPic)

        //Placeholder dengan warna abu-abu solid
        val placeHolderColor: Int = Color.rgb(20, 20, 20)
        var gradientDrawable = GradientDrawable()
        gradientDrawable.shape = GradientDrawable.RECTANGLE
        gradientDrawable.setColor(placeHolderColor)

        Picasso.get().load(ed_urlpic.text.toString().trim()).placeholder(gradientDrawable).into(previewPic, object :
            Callback {
            override fun onSuccess() {
                Toast.makeText(this@admin_editor,"Gambar ditemukan.", Toast.LENGTH_SHORT).show()
            }
            override fun onError(e: java.lang.Exception?) {
                Toast.makeText(this@admin_editor,"Gambar tidak ditemukan.", Toast.LENGTH_SHORT).show()
            }
        })

        //Jika tombol Preview URL ditekan..
        previewURL.setOnClickListener {
            val placeHolderColor: Int = Color.rgb(20, 20, 20)
            var gradientDrawable = GradientDrawable()
            gradientDrawable.shape = GradientDrawable.RECTANGLE
            gradientDrawable.setColor(placeHolderColor)
            if(ed_urlpic.text.toString() != ""){
                Picasso.get().load(ed_urlpic.text.toString().trim()).placeholder(gradientDrawable).into(previewPic, object :
                    Callback {
                    override fun onSuccess() {
                        Toast.makeText(this@admin_editor,"Gambar ditemukan.", Toast.LENGTH_SHORT).show()
                    }
                    override fun onError(e: java.lang.Exception?) {
                        Toast.makeText(this@admin_editor,"Gambar tidak ditemukan.", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            else{
                Toast.makeText(this,"Isi URL Gambar Produk terlebih dahulu.", Toast.LENGTH_SHORT).show()
            }
        }

        val linkList =  findViewById<RecyclerView>(R.id.linkList)
        var db = data.getRoomHelper(this@admin_editor)
        var sources = db.daoSPSource().getAllSource(produk.idSP) as MutableList
        var temp = mutableListOf<SPSource>()
        temp.addAll(sources)

        adapter = recycler_link_modify(temp, this@admin_editor){
            //Kalau tombol hapusnya ditekan.. update RecyclerView
            this.adapter.notifyDataSetChanged()
        }
        linkList.layoutManager = LinearLayoutManager(this@admin_editor)
        linkList.adapter = adapter

        val addLink =  findViewById<Button>(R.id.addLink)
        addLink.setOnClickListener{
            var layout = layoutInflater.inflate(R.layout.dialog_tambah_link,null)
            var dialog = AlertDialog.Builder(this).apply{
                setView(layout)
            }
            var creator = dialog.create()

            val namaWebsite = layout.findViewById<EditText>(R.id.namaWebsite)
            var urlProduk = layout.findViewById<EditText>(R.id.urlProduk)
            val tambah_link = layout.findViewById<Button>(R.id.tambah_link)
            val batal = layout.findViewById<TextView>(R.id.batal)

            fun getCurrentDateTime():String{
                val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
                return sdf.format(Date())
            }

            tambah_link.setOnClickListener {
                if((namaWebsite.text.toString() == "") or (urlProduk.text.toString() == "")){
                    Toast.makeText(this, "Nama website dan URL produk harus diisi.", Toast.LENGTH_SHORT).show()
                }
                else{
                    var url = urlProduk.text.toString()
                    //Wajib menggunakan protocol http
                    if (!url.startsWith("https://") && !url.startsWith("http://")) {
                        url = "http://$url"
                    }

                    var newSource = SPSource(0, produk.idSP, namaWebsite.text.toString(), url, getCurrentDateTime())
                    adapter.myData.add(newSource)
                    linkList.adapter = adapter

                    Toast.makeText(this, "Link telah ditambahkan.", Toast.LENGTH_SHORT).show()
                    creator.cancel()
//                    adapter = recycler_link_modify(data.links, this){
//                        //Kalau tombol hapusnya ditekan.. update RecyclerView
//                        this.adapter.notifyDataSetChanged()
//                    }
//                    linkList.adapter = adapter
                }
            }

            batal.setOnClickListener{
                creator.cancel()
            }

            creator.show()
        }
        update.setOnClickListener {
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
                    Toast.makeText(this@admin_editor, "Nilai harga melewati nilai batas (${Int.MAX_VALUE}).", Toast.LENGTH_SHORT).show()
                    value_pass = false
                }

                try {
                    ram = ed_ram.text.toString().toInt()
                } catch (e : Exception){
                    Toast.makeText(this@admin_editor, "Nilai RAM melewati nilai batas (${Int.MAX_VALUE}).", Toast.LENGTH_SHORT).show()
                    value_pass = false
                }

                try {
                    rom = ed_rom.text.toString().toInt()
                } catch (e : Exception){
                    Toast.makeText(this@admin_editor, "Nilai ROM melewati nilai batas (${Int.MAX_VALUE}).", Toast.LENGTH_SHORT).show()
                    value_pass = false
                }

                try {
                    udisplay = ed_udisplay.text.toString().toDouble()
                } catch (e : Exception){
                    Toast.makeText(this@admin_editor, "Nilai Ukuran Display melewati nilai batas (${Double.MAX_VALUE}).", Toast.LENGTH_SHORT).show()
                    value_pass = false
                }

                try {
                    maincam = ed_maincam.text.toString().toDouble()
                } catch (e : Exception){
                    Toast.makeText(this@admin_editor, "Nilai Kamera Belakang melewati nilai batas (${Double.MAX_VALUE}).", Toast.LENGTH_SHORT).show()
                    value_pass = false
                }

                try {
                    frontcam = ed_frontcam.text.toString().toDouble()
                } catch (e : Exception){
                    Toast.makeText(this@admin_editor, "Nilai Kamera Depan melewati nilai batas (${Double.MAX_VALUE}).", Toast.LENGTH_SHORT).show()
                    value_pass = false
                }

                try {
                    battery = ed_battery.text.toString().toInt()
                } catch (e : Exception){
                    Toast.makeText(this@admin_editor, "Nilai Kapasitas baterai melewati nilai batas (${Int.MAX_VALUE}).", Toast.LENGTH_SHORT).show()
                    value_pass = false
                }

                if (value_pass){
                    var sp = Smartphone(
                        produk.idSP,
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

                    var db = data.getRoomHelper(this@admin_editor)
                    db.daoSP().updateSP(sp)
                    db.daoSPSource().deleteAllSource(sources)
                    db.daoSPSource().addAllSource(adapter.myData)

                    Toast.makeText(this@admin_editor,"Data smartphone berhasil diupdate.", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
            else{
                Toast.makeText(this@admin_editor,"Semua atribut smartphone harus diisi.", Toast.LENGTH_LONG).show()
            }
        }

        delete.setOnClickListener{
            var dialog = AlertDialog.Builder(this)
                .setTitle("Pemberitahuan")
                .setMessage("Apakah Anda yakin ingin menghapus data ini?")
                .setPositiveButton("Ya") { _, _ ->
                    db.daoSP().deleteSP(produk)
                    finish()
                }
                .setNegativeButton("Tidak"){ _, _ -> } //Tutup dialog

            dialog.show()
        }


        val actionbar = supportActionBar
        actionbar!!.title = "Edit data smartphone"
        actionbar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        //onBackPressed() deprecated, jadi perlu menggunakan onBackPressedDispatcher.onBackPressed()
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
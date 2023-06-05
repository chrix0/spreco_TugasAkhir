package com.skripsi.spreco.mainFragmentsAdmin

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.icu.number.NumberFormatter.with
import android.icu.number.NumberRangeFormatter.with
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.spreco.R
import com.skripsi.spreco.RETURN_LAST_TAB
import com.skripsi.spreco.activityAdmin.main_tambahManual
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
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

class main_tambah : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    fun getCurrentDateTime():String{
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        return sdf.format(Date())
    }

    fun code(v : View) : View{
        val keTambahManual = v.findViewById<Button>(R.id.keTambahManual)
        val keTambahSpCSV = v.findViewById<Button>(R.id.keTambahSpCSV)
        val keTambahSpSourceCSV = v.findViewById<Button>(R.id.keTambahSpSourceCSV)
        val kePanduanCSV = v.findViewById<Button>(R.id.kePanduanCSV)

        keTambahManual.setOnClickListener {
            val intent = Intent(context, main_tambahManual::class.java)
            startActivity(intent)
        }

        fun permissionExternal() : Boolean {
            // Ini hanya dilakukan jika menggunakan Android 9 atau di bawahnya.
            // https://developer.android.com/training/data-storage/shared/documents-files
            // Method ini tidak digunakan untuk Android 10 atau lebih, karena permission ini sudah tidak digunakan lagi.
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "Akses baca penyimpanan eksternal ditolak.\nSilahkan izinkan terlebih dahulu.", Toast.LENGTH_SHORT).show()
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            } else {
                Toast.makeText(requireContext(), "Akses baca penyimpanan eksternal diizinkan", Toast.LENGTH_SHORT).show()
            }
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true
            }
            return false
        }

        // List tidak memiliki method componentN yang di atas 5.
        // Oleh karena itu kita perlu membuat method tersebut sendiri.
        operator fun <T> List<T>.component6(): T = get(5)
        operator fun <T> List<T>.component7(): T = get(6)
        operator fun <T> List<T>.component8(): T = get(7)
        operator fun <T> List<T>.component9(): T = get(8)
        operator fun <T> List<T>.component10(): T = get(9)
        operator fun <T> List<T>.component11(): T = get(10)
        operator fun <T> List<T>.component12(): T = get(11)
        operator fun <T> List<T>.component13(): T = get(12)
        operator fun <T> List<T>.component14(): T = get(13)
        operator fun <T> List<T>.component15(): T = get(14)
        operator fun <T> List<T>.component16(): T = get(15)
        operator fun <T> List<T>.component17(): T = get(16)
        operator fun <T> List<T>.component18(): T = get(17)
        operator fun <T> List<T>.component19(): T = get(18)
        operator fun <T> List<T>.component20(): T = get(19)


        //Langkah 3 - Baris dalam CSV menjadi Data Smartphone
        fun readCsvSP(inputStream: InputStream): MutableList<Smartphone> {
            val reader = inputStream.bufferedReader()
            val header = reader.readLine()
            val res = mutableListOf<Smartphone>()
            var countData = 0
            var curIdx = 0
            if (header == "idSP;namaSP;merek;harga;network;sim;tDisplay;uDisplay;OS;chipset;cpu;gpu;ram;rom;mainCam;selfieCam;battery;warna;tanggalRilis;picURL") {
                var done = false
                var idxKolom = 0
                var idxBaris = 0
                try {
                    reader.lineSequence()
                        .filter { it.isNotBlank() }
                        .forEachIndexed { indexBaris, line ->
                            val columns = line.split(';', ignoreCase = true)

                            idxBaris = indexBaris
                            idxKolom = 0
                            var idSP = columns[idxKolom].toInt(); idxKolom++
                            var namasp = columns[idxKolom]; idxKolom++
                            var merek = columns[idxKolom]; idxKolom++
                            var harga = columns[idxKolom].toInt(); idxKolom++
                            var network = columns[idxKolom];idxKolom++
                            var sim = columns[idxKolom];idxKolom++
                            var tdisplay = columns[idxKolom];idxKolom++
                            var udisplay = columns[idxKolom].toDouble();idxKolom++
                            var OS = columns[idxKolom];idxKolom++
                            var chipset = columns[idxKolom];idxKolom++
                            var cpu = columns[idxKolom];idxKolom++
                            var gpu = columns[idxKolom];idxKolom++
                            var ram = columns[idxKolom].toInt();idxKolom++
                            var rom = columns[idxKolom].toInt();idxKolom++
                            var mainCam = columns[idxKolom].toDouble();idxKolom++
                            var selfieCam = columns[idxKolom].toDouble();idxKolom++
                            var battery = columns[idxKolom].toInt();idxKolom++
                            var warna = columns[idxKolom];idxKolom++
                            var tglrilis = columns[idxKolom];idxKolom++
                            var urlpic = columns[idxKolom];idxKolom++

                            val smartphone = Smartphone(
                                idSP,
                                namasp,
                                merek,
                                harga,
                                network,
                                sim,
                                tdisplay,
                                udisplay,
                                OS,
                                chipset,
                                cpu,
                                gpu,
                                ram,
                                rom,
                                mainCam,
                                selfieCam,
                                battery,
                                warna,
                                tglrilis,
                                urlpic
                            )
                            res.add(smartphone)
                            countData++
                            done = true
                        }
                } catch (e: Exception) {
                    done = false
                    val columnNames = listOf(
                        "idSP",
                        "namaSP",
                        "merek",
                        "harga",
                        "network",
                        "sim",
                        "tDisplay",
                        "uDisplay",
                        "OS",
                        "chipset",
                        "cpu",
                        "gpu",
                        "ram",
                        "rom",
                        "mainCam",
                        "selfieCam",
                        "battery",
                        "warna",
                        "tglRilis",
                        "picURL"
                    )

                    // Pesan error yang diberikan
                    val errorDetails =
                        "Terdapat error pada baris ke-${idxBaris + 1}, kolom ${columnNames[idxKolom].uppercase()}:\nInput tidak sesuai dengan format. Silahkan baca kembali panduan CSV yang diberikan pada halaman ini. "
                    Toast.makeText(requireContext(), errorDetails, Toast.LENGTH_LONG).show()
                }

                if (done) {
                    //Tambah data
                    var db = data.getRoomHelper(requireContext())
                    db.daoSP().addAllSP(res)
                    Toast.makeText(requireContext(), "${res.size} data Smartphone telah ditambahkan.", Toast.LENGTH_LONG).show()
                }
            }
            else{
                Toast.makeText(
                    requireContext(),
                    "Header CSV harus berisi: \n'idSP;namaSP;merek;harga;network;sim;tDisplay;uDisplay;OS;chipset;\ncpu;gpu;ram;rom;mainCam;selfieCam;battery;warna;tanggalRilis;picURL'",
                    Toast.LENGTH_LONG
                ).show()
            }
            return res
        }

        fun readCsvSPSource(inputStream: InputStream): MutableList<SPSource> {
            val reader = inputStream.bufferedReader()
            val header = reader.readLine()
            var resSource = mutableListOf<SPSource>()
            var countData = 0
            if (header == "idSource;idSP;source;sourceLink;dateAdded") {
                var done = false
                var idxKolom = 0
                var idxBaris = 0

                reader.lineSequence()
                    .filter { it.isNotBlank() }
                    .forEachIndexed { indexBaris, line ->
                        try {
                            val columns = line.split(';', ignoreCase = true)

                            idxBaris = indexBaris
                            idxKolom = 0
                            var idSource = columns[idxKolom].toInt(); idxKolom++
                            var idSP = columns[idxKolom].toInt(); idxKolom++
                            var source = columns[idxKolom];idxKolom++
                            var sourceLink = columns[idxKolom];idxKolom++


                            var sourceObj = SPSource(
                                idSource, idSP, source, sourceLink,
                                getCurrentDateTime()
                            )
                            resSource.add(sourceObj)
                            countData++
                            done = true
                        } catch (e: Exception) {
                            done = false
                            val columnNames = listOf(
                                "idSource", "idSP", "source", "sourceLink", "dateAdded"
                            )
                            // Pesan error yang diberikan
                            val errorDetails =
                                "Terdapat error pada baris ke-${idxBaris + 1}, kolom ${columnNames[idxKolom].uppercase()}:\nInput tidak sesuai dengan format. Silahkan baca kembali panduan CSV yang diberikan pada halaman ini. "
                            Toast.makeText(requireContext(), errorDetails, Toast.LENGTH_LONG).show()
                        }
                    }

                if (done) {
                    var db = data.getRoomHelper(requireContext())
                    db.daoSPSource().addAllSource(resSource)
                    Toast.makeText(requireContext(), "${resSource.size} data Sumber Pembelian Smartphone telah ditambahkan.", Toast.LENGTH_LONG).show()
                }
            }
            else{
                Toast.makeText(
                    requireContext(),
                    "Header CSV harus mengandung: \n'idSource;idSP;source;sourceLink;dateAdded'",
                    Toast.LENGTH_SHORT
                ).show()
            }
            return resSource
        }


        // Langkah 2 - Pembacaan CSV
        var prosesCsvSP = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                if (data != null) {
                    data.data?.let {
                        val inputStream: InputStream? =
                            requireActivity().contentResolver.openInputStream(it)
                        if (inputStream != null) {
                            readCsvSP(inputStream)
                        } else
                            Toast.makeText(requireContext(), "File tidak ditemukan", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        var prosesCsvSPSource = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                if (data != null) {
                    data.data?.let {
                        val inputStream: InputStream? =
                            requireActivity().contentResolver.openInputStream(it)
                        if (inputStream != null) {
                            readCsvSPSource(inputStream)
                        } else
                            Toast.makeText(requireContext(), "File tidak ditemukan", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Proses Pembacaan CSV
        // Langkah 1 - Pencarian file CSV
        fun openFileManagerSP() {
            var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.type = "text/comma-separated-values"

            var check = true
            if (Build.VERSION.SDK_INT <= 28) {// Apakah menggunakan Android 9 (Pie)?
                intent = Intent("android.intent.action.GET_CONTENT")
                check = permissionExternal() // Lakukan pemeriksaan permission
            }
            if (check) { // Android 10 dan seterusnya tidak perlu permission
                prosesCsvSP.launch(intent) // Lanjut di Langkah 2
            }
        }

        fun openFileManagerSPSource() {
            var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.type = "text/comma-separated-values"

            var check = true
            if (Build.VERSION.SDK_INT < 28) {
                intent = Intent("android.intent.action.GET_CONTENT")
                check = permissionExternal()
            }
            if (check) {
                prosesCsvSPSource.launch(intent)
            }
        }




        keTambahSpCSV.setOnClickListener {
            openFileManagerSP()
        }

        keTambahSpSourceCSV.setOnClickListener {
            openFileManagerSPSource()
        }

        kePanduanCSV.setOnClickListener {
            var layout = layoutInflater.inflate(R.layout.dialog_tambah_panduancsv,null)
            var dialog = AlertDialog.Builder(requireContext()).apply{
                setView(layout)
            }
            var creator = dialog.create()

            var tutup = layout.findViewById<TextView>(R.id.tutup)
            var downloadSP = layout.findViewById<Button>(R.id.downloadSP)
            var downloadSPSource = layout.findViewById<Button>(R.id.downloadSPSource)
            var textSP = layout.findViewById<TextView>(R.id.textSP)
            var textSPSource = layout.findViewById<TextView>(R.id.textSPSource)

            tutup.setOnClickListener {
                creator.dismiss()
            }

            downloadSP.setOnClickListener {
                var url = "https://drive.google.com/file/d/1DrDu6YKUCAlfjWCUtzNu7w9coDUgVAn_/view?usp=sharing"
                val openUrlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(openUrlIntent)
            }

            downloadSPSource.setOnClickListener {
                var url = "https://drive.google.com/file/d/1RUPLq6nHhOLecI6anC7gJ8XVnZ7mIPG6/view?usp=sharing"
                val openUrlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(openUrlIntent)
            }

            textSP.text = "Anda dapat mengedit template CSV Spesifikasi Smartphone dengan Microsoft Excel atau dengan aplikasi text editor. Contoh pengisian data tersedia pada baris kedua dari file CSV tersebut.\n\n" +
                    "Keterangan setiap kolom:\n" +
                    "- idSP : Nomor ID unik data smartphone.\n" +
                    "- namaSP : Nama smartphone\n" +
                    "- merek : Nama merek smartphone\n" +
                    "- harga : Harga Smartphone dalam rupiah. \n" +
                    "- network : Jaringan seluler smartphone yang didukung\n" +
                    "- sim : Jumlah sim yang dapat dimasukkan\n" +
                    "- tDisplay: Tipe layar smartphone\n" +
                    "- uDisplay: Ukuran layar smartphone dalam satuan inci.  \n" +
                    "- OS : Sistem Operasi yang digunakan.\n" +
                    "- chipset : Chipset yang digunakan\n" +
                    "- cpu : CPU yang digunakan\n" +
                    "- gpu : GPU yang digunakan\n" +
                    "- ram : Kapasitas RAM dalam satuan GB\n" +
                    "- rom : Kapasitas ROM dalam satuan GB\n" +
                    "- mainCam : Resolusi kamera belakang dalam satuan MP.\n" +
                    "- selfieCam : Resolusi kamera depan dealam satuan MP\n" +
                    "- battery: Kapasitas baterai dalam satuan mAH\n" +
                    "- warna: Warna yang tersedia\n" +
                    "- tanggalRilis: Tanggal smartphone dirilis.\n" +
                    "- picURL: Link gambar yang akan ditampilkan dalam aplikasi. Gambar dapat berekstensi JPG, JPEG, atau PNG\n\n" +
                    "Untuk pengisian kolom yang menggunakan angka desimal, gunakan tanda titik (“.”) sebagai tanda desimal. Hindari penggunaan tanda koma (“,”) sebagai tanda desimal karena dapat menyebabkan error."

            textSPSource.text = "Anda dapat mengedit template CSV Sumber Pembelian Smartphone dengan Microsoft Excel atau dengan aplikasi text editor. Contoh pengisian data tersedia pada baris kedua dari file CSV tersebut.\n\n" +
                    "Keterangan setiap kolom:\n" +
                    "- idSource : Nomor ID unik link sumber pembelian\n" +
                    "- idSP : Nomor ID Smartphone yang akan diberi link pembelian.\n" +
                    "- source : Nama website tempat pembelian\n" +
                    "- sourceLink : URL link pembelian"

            creator.show()
        }

        return v
    }

}
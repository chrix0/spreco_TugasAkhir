package com.skripsi.spreco

import com.skripsi.spreco.classes.Smartphone
import com.skripsi.spreco.classes.SP_rank
import com.skripsi.spreco.classes.SP_rec
import com.skripsi.spreco.data.caraHitung
import com.skripsi.spreco.data.enabledCriteria
import com.skripsi.spreco.data.enabledCriteriaType
import kotlin.math.pow

object fahp_waspas {

    //FAHP
    //===========================================================================================================
    //STEP 1 - Mengubah Pairwise Comparison Matrix ke dalam skala TFN (Triangle Fuzzy Number)
    fun convertSkalaTFN(matriksPC: MutableList<MutableList<Double>>): MutableList<MutableList<MutableList<Double>>> {
        val matriksTFN = MutableList(matriksPC.size) { MutableList(matriksPC.size) { mutableListOf<Double>() } }

        val mapTFN = mapOf(
            1.0 to mutableListOf(1.0, 1.0, 1.0),
            2.0 to mutableListOf(1.0, 2.0, 3.0),
            3.0 to mutableListOf(2.0, 3.0, 4.0),
            4.0 to mutableListOf(3.0, 4.0, 5.0),
            5.0 to mutableListOf(4.0, 5.0, 6.0),
            6.0 to mutableListOf(5.0, 6.0, 7.0),
            7.0 to mutableListOf(6.0, 7.0, 8.0),
            8.0 to mutableListOf(7.0, 8.0, 9.0),
            9.0 to mutableListOf(8.0, 9.0, 10.0),
            1.0 / 2.0 to mutableListOf(1.0 / 3.0, 1.0 / 2.0, 1.0),
            1.0 / 3.0 to mutableListOf(1.0 / 4.0, 1.0 / 3.0, 2.0),
            1.0 / 4.0 to mutableListOf(1.0 / 5.0, 1.0 / 4.0, 1.0 / 3.0),
            1.0 / 5.0 to mutableListOf(1.0 / 6.0, 1.0 / 5.0, 1.0 / 4.0),
            1.0 / 6.0 to mutableListOf(1.0 / 7.0, 1.0 / 6.0, 1.0 / 5.0),
            1.0 / 7.0 to mutableListOf(1.0 / 8.0, 1.0 / 7.0, 1.0 / 6.0),
            1.0 / 8.0 to mutableListOf(1.0 / 9.0, 1.0 / 8.0, 1.0 / 7.0),
            1.0 / 9.0 to mutableListOf(1.0 / 10.0, 1.0 / 9.0, 1.0 / 8.0)

//            1.0 to mutableListOf(1.0, 1.0, 3.0),
//            2.0 to mutableListOf(1.0, 2.0, 4.0),
//            3.0 to mutableListOf(1.0, 3.0, 5.0),
//            4.0 to mutableListOf(2.0, 4.0, 6.0),
//            5.0 to mutableListOf(3.0, 5.0, 7.0),
//            6.0 to mutableListOf(4.0, 6.0, 8.0),
//            7.0 to mutableListOf(5.0, 7.0, 9.0),
//            8.0 to mutableListOf(6.0, 8.0, 9.0),
//            9.0 to mutableListOf(7.0, 9.0, 9.0),
//            1.0 / 2.0 to mutableListOf(1.0 / 4.0, 1.0 / 2.0, 1.0),
//            1.0 / 3.0 to mutableListOf(1.0 / 5.0, 1.0 / 3.0, 1.0),
//            1.0 / 4.0 to mutableListOf(1.0 / 6.0, 1.0 / 4.0, 1.0 / 2.0),
//            1.0 / 5.0 to mutableListOf(1.0 / 7.0, 1.0 / 5.0, 1.0 / 3.0),
//            1.0 / 6.0 to mutableListOf(1.0 / 8.0, 1.0 / 6.0, 1.0 / 4.0),
//            1.0 / 7.0 to mutableListOf(1.0 / 9.0, 1.0 / 7.0, 1.0 / 5.0),
//            1.0 / 8.0 to mutableListOf(1.0 / 9.0, 1.0 / 8.0, 1.0 / 6.0),
//            1.0 / 9.0 to mutableListOf(1.0 / 9.0, 1.0 / 9.0, 1.0 / 7.0)
        )

        for (i in matriksPC.indices) {
            for (j in 0..i) {
                if (i == j) {
                    matriksTFN[i][j] = mapTFN.getValue(1.0)
                } else {
                    val value = matriksPC[j][i]
                    matriksTFN[j][i] = mapTFN.getValue(value)

                    if (value == 1.0){
                        matriksTFN[i][j] = mutableListOf(1.0 / 3.0, 1.0, 1.0)
                    }
                    else{
                        matriksTFN[i][j] = mapTFN.getValue(1.0 / value)
                    }

                }
            }
        }
        return matriksTFN
    }

    // STEP 2 - Menentukan nilai rata-rata geometrik fuzzy setiap baris PCM (kriteria) (r~)
    fun fuzzyGM(matriksTFN: MutableList<MutableList<MutableList<Double>>>): MutableList<MutableList<Double>> {
        var res : MutableList<MutableList<Double>> = mutableListOf()

        // Perkalian setiap nilai l, m, dan u pada TFN
        for (i in 0 until matriksTFN.size){ //Kriteria ke-i
            var l = 1.0
            var m = 1.0
            var u = 1.0
            for(j in 0 until matriksTFN[i].size){ //TFN ke-j dari Kriteria ke-i
                l *= matriksTFN[i][j][0]
                m *= matriksTFN[i][j][1]
                u *= matriksTFN[i][j][2]
            }
            res.add(mutableListOf(l, m , u))
        }

        // Pangkat 1/n
        for (i in 0 until res.size){
            // Jumlah kriteria yang digunakan dalam rekomendasi
            val jumlahKriteria = matriksTFN.size.toDouble()
            // l
            res[i][0] =  res[i][0].pow(1.0/jumlahKriteria)
            // m
            res[i][1] =  res[i][1].pow(1.0/jumlahKriteria)
            // u
            res[i][2] =  res[i][2].pow(1.0/jumlahKriteria)
        }

        //Tampilkan res dalam Logcat

        return res
    }

    // STEP 3 - Menentukan nilai bobot fuzzy setiap kriteria
    fun fuzzyW(geoMean : MutableList<MutableList<Double>>) : MutableList<MutableList<Double>>{
        var result : MutableList<MutableList<Double>> = mutableListOf()
        // Transpos matriks agar setiap baris matriks menunjukkan semua nilai l, m, dan u dari hasil perhitungan rata-rata geometrik
        // Contoh:
//        (l1, m1, u1)
//        (l2, m2, u2)
//        (l3, m3, u3)
        // Menjadi:
//        (l1, l2, l3)
//        (m1, m2, m3)
//        (u1, u2, u3)
        var transpos = transposeMatriks(geoMean as ArrayList<ArrayList<Double>>)

        // Hitung (𝑟̃1 ⊕ ⋯⊕ 𝑟̃𝑛)^-1
        val lTotalInversed = 1.0 / transpos[0].sum()
        val mTotalInversed = 1.0 / transpos[1].sum()
        val uTotalInversed = 1.0 / transpos[2].sum()

        for(i in 0 until geoMean.size){ //Kriteria ke-i
            // Hitung 𝑟̃𝑖 ⊗ (𝑟̃1 ⊕ ⋯⊕ 𝑟̃𝑛)^-1
            var l = geoMean[i][0] * lTotalInversed
            var m = geoMean[i][1] * mTotalInversed
            var u = geoMean[i][2] * uTotalInversed

            result.add(mutableListOf(l, m, u))
        }

        //Tampilkan res dalam Logcat
        return result
    }

    //STEP 4 - Defuzzifikasi dengan metode COA
    fun coaDefuz(hasilSFE : MutableList<MutableList<Double>>) : MutableList<Double> {
        var res: MutableList<Double> = mutableListOf()
//        Dengan metode COA (Centre of Area)..
        for (i in hasilSFE) {
            // COA = l + ((u - l) + (m - l))/3
            var coa = i[0] + (((i[2] - i[0]) + (i[1] - i[0])) / 3)
            res.add(coa)
        }

//        Simpan bobot kriteria di data.bobotKriteria untuk nanti disimpan ke objek RecHistory
        data.bobotKriteria = mutableMapOf()
        for( i in 0 until enabledCriteria.size){ //enabledCriteria memiliki jumlah yang sama dengan variabel res
            data.bobotKriteria[enabledCriteria[i]] = res[i] // "Nama kriteria yang digunakan" -> nilai bobot kriteria
        }

        return res
    }

    //WASPAS
    //===========================================================================================================

    // Persiapan: Ubah setiap bagian spesifikasi yang akan diproses pada data alternatif menjadi nilai kriteria.
    // Lalu membuatnya mengisinya ke dalam objek SP_rec.
    // Objek-objek ini akan digunakan dalam pembuatan matriks keputusan nantinya.

    fun convert_ke_nilai_kriteria(list : MutableList<Smartphone>) : MutableList<SP_rec>{
        var list_converted = mutableListOf<SP_rec>()
        for (obj in list){
            var converted = SP_rec()
            // ram
            converted.c_ram = when (obj.ram) {
                in 0..1 -> 1.0
                in 2..3 -> 2.0
                in 4..5 -> 3.0
                in 6..7 -> 4.0
                else -> 5.0
            }
            // rom
            converted.c_rom = when (obj.rom) {
                in 0..16 -> 1.0
                in 17..32 -> 2.0
                in 33..64 -> 3.0
                in 65..128 -> 4.0
                else -> 5.0
            }
            // battery
            converted.c_bat = when (obj.battery) {
                in 0..2999 -> 1.0
                in 3000..3875 -> 2.0
                in 3876..4687 -> 3.0
                in 4688..5500 -> 4.0
                else -> 5.0
            }
            // main (rear) camera
            converted.c_mainCam = when (obj.mainCam) {
                in 0.0..1.0 -> 1.0
                in 2.0..8.0 -> 2.0
                in 9.0..14.0 -> 3.0
                in 15.0..20.0 -> 4.0
                else -> 5.0
            }
            // selfie camera
            converted.c_selfie = when (obj.selfieCam) {
                in 0.0..1.0 -> 1.0
                in 2.0..8.0 -> 2.0
                in 9.0..14.0 -> 3.0
                in 15.0..20.0 -> 4.0
                else -> 5.0
            }
            // ukuran layar
            converted.c_layar = when (obj.uDisplay) {
                in 0.0..5.0 -> 1.0
                in 5.1..5.5 -> 2.0
                in 5.6..6.0 -> 3.0
                in 6.1..6.5 -> 4.0
                else -> 5.0
            }
            // harga
            converted.c_harga = when (obj.harga) {
                in 0..1000000 -> 5.0
                in 1000001..2000000 -> 4.0
                in 2000001..3000000 -> 3.0
                in 3000001..4000000  -> 2.0
                else -> 1.0
            }
            list_converted.add(converted)
        }
        print(list_converted.toString())
        return list_converted
    }

    //STEP 1 - Membuat Matriks Keputusan
    fun createMatriksKeputusan(matriks : MutableList<SP_rec>, enabledCriteria : MutableList<String>) : MutableList<MutableList<Double>>{
        var newMatriks : MutableList<MutableList<Double>> = mutableListOf()
        for(i in matriks){ //i merupakan objek SP_rec yang sudah dibuat sebelumnya
            var newBaris : MutableList<Double> = mutableListOf()
            // enabledCriteria berisi kriteria yang dipilih pengguna ketika mengatur kriteria rekomendasi.
            for (criteria in enabledCriteria) {
                when (criteria) {
                    "RAM" -> newBaris.add(i.c_ram!!)
                    "ROM" -> newBaris.add(i.c_rom!!)
                    "Kapasitas baterai" -> newBaris.add(i.c_bat!!)
                    "Kamera belakang" -> newBaris.add(i.c_mainCam!!)
                    "Kamera depan" -> newBaris.add(i.c_selfie!!)
                    "Ukuran layar" -> newBaris.add(i.c_layar!!)
                    "Harga" -> newBaris.add(i.c_harga!!)
                }
            }
            newMatriks.add(newBaris)
        }
        return newMatriks
    }

    // STEP 2 - Normalisasi Matriks Keputusan
    fun normMatriksKeputusan(matriksKeputusan: MutableList<MutableList<Double>>): ArrayList<ArrayList<Double>> {
        var normMatriks: ArrayList<ArrayList<Double>> = arrayListOf()
        // Transpos agar setiap baris berisi nilai kriteria keseluruhan data untuk kriteria yang sama.
        var transposed = transposeMatriks(matriksKeputusan as ArrayList<ArrayList<Double>>)
        // Contoh:
        // [RAM1, ROM1, HARGA1, LAYAR1]
        // [RAM2, ROM2, HARGA2, LAYAR2]
        // [RAM3, ROM3, HARGA3, LAYAR3]

        // Hasil:
        // [RAM1, RAM2, RAM3]
        // [ROM1, ROM2, ROM3]
        // [HARGA1, HARGA2, HARGA3]
        // [LAYAR1, LAYAR2, LAYAR3]

        // .indices di sini sama dengan 0..(transposed.size-1),
        // Karena sudah ditranpos, transposed.size merupakan jumlah kriteria rekomendasi yang digunakan.
        for (i in transposed.indices) {
            var newBaris: ArrayList<Double> = arrayListOf()
            var max = transposed[i].maxByOrNull { it } // Ambil nilai kriteria maksimum untuk kritieria ke-i
            var min = transposed[i].minByOrNull { it } // Ambil nilai kriteria minimum untuk kritieria ke-i

            var mode = enabledCriteriaType[i] // Lihat tipe kriteria ke-i apakah benefit (B) atau cost (C)
            val transposedRow = transposed[i] // Ambil semua nilai kriteria dari kriteria ke-i

            for (j in transposedRow.indices) { // .indices di sini sama dengan 0..(transposedRow.size-1)
                var value = if (mode == 'B') { // BENEFIT
                    var norm = transposedRow[j] / max!!
                    norm
                } else { // COST
                    var norm = min!! / transposedRow[j]
                    norm
                }
                newBaris.add(value)
            }

            normMatriks.add(newBaris)
        }

        //Transpos kembali sehingga matriks berada dalam kondisi semula.
        return transposeMatriks(normMatriks)
    }

    //STEP 3 - Perhitungan Qi
    fun preferensiQi(normMatriks : ArrayList<ArrayList<Double>>, bobot : MutableList<Double>) : MutableList<Double>{
        var res : MutableList<Double> = mutableListOf()

        // Setiap data akan dicari nilai Qi
        for(i in 0 until normMatriks.size){
            var wsm = 0.0
            var wpm = 1.0 //Untuk menghindari wpm selalu bernilai 0, nilai awal adalah 1.

            //Hitung nilai WSM dan WPM untuk data tersebut
            for(j in 0 until normMatriks[i].size){
                wsm += normMatriks[i][j]*bobot[j]
                wpm *= normMatriks[i][j].pow(bobot[j])
            }

            // 0.5 * WSM + 0.5 * WPM
            var qiValue = 0.5 * wsm + 0.5 * wpm
//            wsm_wpm_waspas.add(mutableListOf(wsm, wpm, qiValue))
            res.add(qiValue)
        }
        return res
    }

    /*////////////////////////////////////////////////////////////
    * Ketiga method di bawah ini memanfaatkan semua method di atas
    */////////////////////////////////////////////////////////////

    fun FAHP(PCM: MutableList<MutableList<Double>>): MutableList<Double> {
        var TFN = convertSkalaTFN(PCM) // Langkah 1
        var geoFuzzy = fuzzyGM(TFN) // Langkah 2
        var fuzzyWeight = fuzzyW(geoFuzzy) // Langkah 3
        var defuz = coaDefuz(fuzzyWeight) // Langkah 4
        return defuz
    }

    fun WASPAS(dataAlternatif: MutableList<SP_rec>, bobotKriteria: MutableList<Double>) : MutableList<Double>{
        var matriksKeputusan = createMatriksKeputusan(dataAlternatif, enabledCriteria) // Langkah 1
        var normMatriksKeputusan = normMatriksKeputusan(matriksKeputusan) // Langkah 2
        var qi = preferensiQi(normMatriksKeputusan,bobotKriteria) // Langkah 3

        // Langkah 4 : Membuat daftar ranking smartphone berdasarkan nilai Qi.
        // Proses tersebut dapat dilihat pada method di bawah ini (makeRanking()).
        return qi
    }

    fun fahpOnly(dataAlternatif: MutableList<SP_rec>,
                 bobotKriteria: MutableList<Double>) : MutableList<Double>{
//        hitung = FAHPOnly(convert, FAHP(data.pcm))
        var matriks: ArrayList<ArrayList<Double>> = arrayListOf()
        for (i in dataAlternatif){
            var newBaris : ArrayList<Double> = arrayListOf()
            for (criteria in enabledCriteria) {
                when (criteria) {
                    "RAM" -> newBaris.add(i.c_ram!!)
                    "ROM" -> newBaris.add(i.c_rom!!)
                    "Kapasitas baterai" -> newBaris.add(i.c_bat!!)
                    "Kamera belakang" -> newBaris.add(i.c_mainCam!!)
                    "Kamera depan" -> newBaris.add(i.c_selfie!!)
                    "Ukuran layar" -> newBaris.add(i.c_layar!!)
                    "Harga" -> newBaris.add(i.c_harga!!)
                }
            }
            matriks.add(newBaris)
        }
        //transpos. Ini dilakukan agar setiap baris berisi nilai kriteria tertentu untuk semua data yang ada.
        matriks = fahp_waspas.transposeMatriks(matriks)
        //Bobot kriteria sudah diurutkan, sehingga i tidak perlu diperhatikan
        for (i in 0 until matriks.size){
            for (o in 0 until matriks[i].size){
                matriks[i][o] *= bobotKriteria[i]
            }
        }
        //transpos kembali
        matriks = transposeMatriks(matriks)

        //Jumlahkan setiap array
        var res = mutableListOf<Double>()
        for (i in matriks){
            // Setelah dilakukan percobaan dengan smartphone berspesifikasi tertinggi, ditemukan nilai maksimumnya adalah 500.
            res.add((i.sum() / 500.0) * 100) // Agar rentang hasilnya 0 dan 1.
        }
        return res
    }

    fun makeRanking(dataAlt : MutableList<Smartphone>, qiList : MutableList<Double>) : MutableList<SP_rank>{
        var res : MutableList<SP_rank> = mutableListOf()
        //Hubungkan data alternatif dengan nilai Qi. Rank akan ditentukan setelah sorting.
        for(i in 0 until qiList.size){
            res.add(SP_rank(0, dataAlt[i], qiList[i]))
        }

        //Descending Sort
        res.sortByDescending{it.score}
        //Tentukan rank
        var rank = 0
        for(i in 0 until res.size){
            when(i){
                0 ->{
                    rank++
                    // Jika elemen pertama, maka rank akan bernilai 1
                    res[i].rank = rank
                }
                else->{
                    // Jika bukan elemen pertama, maka skor akan dibandingkan dengan skor data sebelumnya.
                    // Jika tidak sama, maka rank tidak sama dengan data sebelumnya.
                    if(res[i].score != res[i-1].score){
                        rank++
                        res[i].rank = rank
                    }
                    // Jika sama, data tersebut memiliki ranking yang sama dengan data sebelumnya.
                    else{
                        res[i].rank = res[i-1].rank
                    }
                }
            }
        }

//        caraHitung += "Sorted berdasarkan nilai qi: $res"
        return res
    }

    ////////////////////
    // Method pembantu
    ////////////////////

    fun transposeMatriks(matrix: ArrayList<ArrayList<Double>>): ArrayList<ArrayList<Double>> {
        val numRows = matrix.size
        val numCols = matrix[0].size
        //Persiapan ArrayList sebagai tempat hasil transpos.
        //ArrayList memiliki performa yang lebih baik dari MutableList dalam menangani jumlah data yang banyak
        val transpose = ArrayList<ArrayList<Double>>(numCols)
        //Transpos
        for (i in 0 until numCols) {
            val column = ArrayList<Double>(numRows)
            for (j in 0 until numRows) {
                column.add(matrix[j][i])
            }
            transpose.add(column)
        }

        return transpose
    }

}
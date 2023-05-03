package com.skripsi.spreco

import android.util.Log
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
    fun convertSkalaTFN(matriksPC : MutableList<MutableList<Double>>) : MutableList<MutableList<MutableList<Double>>>{
        var matriksTFN : MutableList<MutableList<MutableList<Double>>> = mutableListOf()

        //Ukuran matriks TFN = jlhKriteria x jlhKriteria
        for(i in 0 until matriksPC.size){
            var row : MutableList<MutableList<Double>> = mutableListOf()
            for(j in 0 until matriksPC.size){
                row.add(mutableListOf())
            }
            matriksTFN.add(row)
        }

        //Setiap elemen matriks diubah ke dalam bentuk TFN, yang terdiri dari l, m, dan u.
        for(i in 0 until matriksPC.size){
            for(j in 0..i){
                //Perbandingan kriteria yang sama (diagonal)
                if (i == j){
                    matriksTFN[i][j] = mutableListOf(1.0, 1.0, 1.0)
                }
                else{
                    when(matriksPC[j][i]){
                        1.0 -> {
                            matriksTFN[j][i] = mutableListOf(1.0, 1.0, 3.0) //non-invers
                            matriksTFN[i][j] = mutableListOf(1.0/3.0, 1.0, 1.0) //invers
                        }
                        2.0 -> {
                            matriksTFN[j][i] = mutableListOf(1.0, 2.0, 4.0)
                            matriksTFN[i][j] = mutableListOf(1.0/4.0, 1.0/2.0, 1.0)
                        }
                        3.0 -> {
                            matriksTFN[j][i] = mutableListOf(1.0, 3.0, 5.0)
                            matriksTFN[i][j] = mutableListOf(1.0/5.0, 1.0/3.0, 1.0)
                        }
                        4.0 -> {
                            matriksTFN[j][i] = mutableListOf(2.0, 4.0, 6.0)
                            matriksTFN[i][j] = mutableListOf(1.0/6.0, 1.0/4.0, 1.0/2.0)
                        }
                        5.0 -> {
                            matriksTFN[j][i] = mutableListOf(3.0, 5.0, 7.0)
                            matriksTFN[i][j] = mutableListOf(1.0/7.0, 1.0/5.0, 1.0/3.0)
                        }
                        6.0 -> {
                            matriksTFN[j][i] = mutableListOf(4.0, 6.0, 8.0)
                            matriksTFN[i][j] = mutableListOf(1.0/8.0, 1.0/6.0, 1.0/4.0)
                        }
                        7.0 -> {
                            matriksTFN[j][i] = mutableListOf(5.0, 7.0, 9.0)
                            matriksTFN[i][j] = mutableListOf(1.0/9.0, 1.0/7.0, 1.0/5.0)
                        }
                        8.0 -> {
                            matriksTFN[j][i] = mutableListOf(6.0, 8.0, 9.0)
                            matriksTFN[i][j] = mutableListOf(1.0/9.0, 1.0/8.0, 1.0/6.0)
                        }
                        9.0 -> {
                            matriksTFN[j][i] = mutableListOf(7.0, 9.0, 9.0)
                            matriksTFN[i][j] = mutableListOf(1.0/9.0, 1.0/9.0, 1.0/7.0)
                        }

                        1.0/2.0 -> {
                            matriksTFN[j][i] = mutableListOf(1.0/4.0, 1.0/2.0, 1.0)
                            matriksTFN[i][j] = mutableListOf(1.0, 2.0, 4.0)
                        }
                        1.0/3.0 -> {
                            matriksTFN[j][i] = mutableListOf(1.0/5.0, 1.0/3.0, 1.0)
                            matriksTFN[i][j] = mutableListOf(1.0, 3.0, 5.0)
                        }
                        1.0/4.0 -> {
                            matriksTFN[j][i] = mutableListOf(1.0/6.0, 1.0/4.0, 1.0/2.0)
                            matriksTFN[i][j] = mutableListOf(2.0, 4.0, 6.0)
                        }
                        1.0/5.0 -> {
                            matriksTFN[j][i] = mutableListOf(1.0/7.0, 1.0/5.0, 1.0/3.0)
                            matriksTFN[i][j] = mutableListOf(3.0, 5.0, 7.0)
                        }
                        1.0/6.0 -> {
                            matriksTFN[j][i] = mutableListOf(1.0/8.0, 1.0/6.0, 1.0/4.0)
                            matriksTFN[i][j] = mutableListOf(4.0, 6.0, 8.0)
                        }
                        1.0/7.0 -> {
                            matriksTFN[j][i] = mutableListOf(1.0/9.0, 1.0/7.0, 1.0/5.0)
                            matriksTFN[i][j] = mutableListOf(5.0, 7.0, 9.0)
                        }
                        1.0/8.0 -> {
                            matriksTFN[j][i] = mutableListOf(1.0/9.0, 1.0/8.0, 1.0/6.0)
                            matriksTFN[i][j] = mutableListOf(6.0, 8.0, 9.0)
                        }
                        1.0/9.0 -> {
                            matriksTFN[j][i] = mutableListOf(1.0/9.0, 1.0/9.0, 1.0/7.0)
                            matriksTFN[i][j] = mutableListOf(7.0, 9.0, 9.0)
                        }
                    }
                }
            }
        }

        caraHitung +=
            "\n\n FAHP - STEP 1\n" +
            "Pairwise Comparison Matrix yang digunakan: \n $matriksPC \n\n" +
            "Ubah ke skala TFN: \n $matriksTFN \n"

        return matriksTFN
    }

    //Hitung total semua L, M, dan U tiap baris
    fun totalLMU_Baris(matriksTFN : MutableList<MutableList<MutableList<Double>>>) : MutableList<MutableList<Double>>{
        var all: MutableList<MutableList<Double>> = mutableListOf()
        var total_L = 0.0
        var total_M = 0.0
        var total_U = 0.0

        caraHitung += "\nMenentukan L, M, dan U setiap baris..\n"

        for(i in 0 until matriksTFN.size){
            var text_L = "Total L (Kriteria ke-${i+1}) = "
            var text_M = "Total M (Kriteria ke-${i+1}) = "
            var text_U = "Total U (Kriteria ke-${i+1}) = "

            for(j in 0 until matriksTFN[i].size){
                total_L += matriksTFN[i][j][0]
                total_M += matriksTFN[i][j][1]
                total_U += matriksTFN[i][j][2]

                text_L += "+ ${matriksTFN[i][j][0]} "
                text_M += "+ ${matriksTFN[i][j][1]} "
                text_U += "+ ${matriksTFN[i][j][2]} "
            }

            text_L += "= $total_L"
            text_M += "= $total_M"
            text_U += "= $total_U"

            caraHitung += "$text_L \n $text_M \n $text_U \n\n"

            all.add(mutableListOf(total_L, total_M, total_U))
            total_L = 0.0
            total_M = 0.0
            total_U = 0.0
        }
        return all
    }

    //Hitung total semua kolom L, M, dan U
    fun totalLMU_Kolom(matriksTFN : MutableList<MutableList<MutableList<Double>>>) : MutableList<Double>{
        var total_L : Double = 0.0
        var total_M : Double = 0.0
        var total_U : Double = 0.0

        var text_L = "Total L = "
        var text_M = "Total M = "
        var text_U = "Total U = "

        caraHitung += "\n\n Menentukan menghitung total L, M, dan U setiap kolom \n"

        for(i in 0 until matriksTFN.size){
            for(j in 0 until matriksTFN[i].size){
                total_L += matriksTFN[i][j][0]
                total_M += matriksTFN[i][j][1]
                total_U += matriksTFN[i][j][2]

                text_L += "+ ${matriksTFN[i][j][0]} "
                text_M += "+ ${matriksTFN[i][j][1]} "
                text_U += "+ ${matriksTFN[i][j][2]} "
            }
        }

        text_L += "= $total_L"
        text_M += "= $total_M"
        text_U += "= $total_U"

        caraHitung += "$text_L \n $text_M \n $text_U \n\n"

        return mutableListOf(total_L, total_M, total_U)
    }

    // STEP 2 - FUZZY GEOMETRIC MEAN & FUZZY SYNTHESIS EXTENT (Si)

    fun fuzzyGM(matriksTFN: MutableList<MutableList<MutableList<Double>>>): MutableList<MutableList<Double>> {
        caraHitung +=
            "STEP 2 [1ST PART]\n" +
                    "Menentukan nilai rata-rata geometrik setiap baris PCM (r~) \n"

        var res : MutableList<MutableList<Double>> = mutableListOf()
        for (i in 0 until matriksTFN.size){
            var part = "\nKriteria ke-${i+1}\n"
            var TFN : MutableList<Double> = mutableListOf()

            var l = 1.0
            var m = 1.0
            var u = 1.0

            var textl = "\nl_${i+1} = "
            var textm = "\nm_${i+1} = "
            var textu = "\nu_${i+1} = "

            for(j in 0 until matriksTFN[i].size){
                for (k in 0 until 3){
//                    teks += "⊗ ${matriksTFN[j][i][k]}"

                    when (k) {
                        0 -> {
                            l *= matriksTFN[i][j][k]
                            textl += "* ${matriksTFN[i][j][k]}"
                        }
                        1 -> {
                            m *= matriksTFN[i][j][k]
                            textm += "* ${matriksTFN[i][j][k]}"
                        }
                        else -> {
                            u *= matriksTFN[i][j][k]
                            textu += "* ${matriksTFN[i][j][k]}"
                        }
                    }
                }
            }
            textl += " = $l"
            textm += " = $m"
            textu += " = $u"
            part += textl + textm + textu
            TFN = mutableListOf(l, m , u)
            caraHitung += part
            res.add(TFN)
        }

        caraHitung += "\n\n---PANGKAT 1/JUMLAH KRITERIA---\n\n"
        for (i in 0 until res.size){
            var part = "Kriteria ke-${i+1}: "
            part += "((${res[i][0]})^(1/${matriksTFN.size}), (${res[i][1]})^(1/${matriksTFN.size}), (${res[i][2]})^(1/${matriksTFN.size})) = "
            for (j in 0 until res[i].size){
                res[i][j] =  res[i][j].pow(1.0/matriksTFN.size.toDouble())
            }
            part += "(${res[i][0]}, ${res[i][1]}, ${res[i][2]})\n"
            caraHitung += part
        }

        return res
    }

    fun fuzzySE(geoMean : MutableList<MutableList<Double>>) : MutableList<MutableList<Double>>{
        //lmuKolom -> [l total, m total, u total]

        caraHitung +=
            "STEP 2 [Part 2]\n" +
                    "Menentukan nilai bobot fuzzy dalam TFN\n"

        var result : MutableList<MutableList<Double>> = mutableListOf()
        var transpos = transposeMatriks(geoMean)
        for(i in 0 until geoMean.size){
            var l = 0.0
            var m = 0.0
            var u = 0.0
            var text = "\n\nKriteria ke-${i+1} \n"
            for (j in 0 until geoMean[i].size){
                when (j) {
                    0 -> {
                        l = geoMean[i][j] * 1.0 / transpos[j].sum()
                        text += "L = ${geoMean[i][j]}*/frac{1}{${transpos[j].sum()})=$l \n"
                    }
                    1 -> {
                        m = geoMean[i][j] * 1.0 / transpos[j].sum()
                        text += "M = ${geoMean[i][j]}*/frac{1}{${transpos[j].sum()})=$m \n"
                    }
                    else -> {
                        u = geoMean[i][j] * 1.0 / transpos[j].sum()
                        text += "U = ${geoMean[i][j]}*/frac{1}{${transpos[j].sum()})=$u \n"
                    }
                }
            }
            result.add(mutableListOf(l, m, u))
            caraHitung += "\n $text \n"
        }
        return result
    }

    //ALTERNATE STEP 3
    //STEP 3 - DEFUZZIFIKASI
    fun coaDefuz(hasilSFE : MutableList<MutableList<Double>>) : MutableList<Double> {
        caraHitung +=
            "ALT STEP 3\n" +
                    "DEFUZZIFIKASI -> METODE COA\n\n"

        var res: MutableList<Double> = mutableListOf()
        var textCount = 1
//        Dengan metode COA (Centre of Area) untuk mengubah nilai menjadi crisp values
        for (i in hasilSFE) {
            var coa = i[0] + (((i[2] - i[0]) + (i[1] - i[0])) / 3) // l + ((u - l) + (m - l))/3
            var text =
                "Kriteria ke-${textCount} = ${i[0]} + ((${i[1]} - ${i[0]}) + (${i[2]} - ${i[0]})) / 3 = $coa\n"
            textCount += 1
            caraHitung += "$text \n"
            res.add(coa)
        }
        return res
    }

//    ALTERNATE STEP 4 - NORMALISASI NILAI BOBOT
    fun normVF(hasilDefuz : MutableList<Double>) : MutableList<Double> {
        caraHitung +=
            "ALT STEP 4\n" +
                    "NORMALISASI NILAI BOBOT (W) \n\n"
        // tiap bobot kriteria yang telah didefuzzifikasi dibagi dgn penjumlahan total seluruh bobot kriteria tsb
        var sum : Double = (hasilDefuz.sumOf{it})
        var res : MutableList<Double> = mutableListOf()
        var text = ""
        var count = 1
        for(i in hasilDefuz){
            var hitung = i / sum
            text = "Kriteria ke-$count: /frac{$i}{$sum} = $hitung\n"
            count+=1
            caraHitung += "$text\n"
            res.add(hitung)
        }

        data.bobotKriteria = mutableMapOf<String, Double>()
        caraHitung += "\n\n Bobot kriteria akhir: $res \n"
        //Simpan bobot kriteria di data.bobotKriteria untuk nanti disimpan ke objek RecHistory
        for( i in 0 until enabledCriteria.size){ //enabledCriteria memiliki jumlah yang sama dengan variabel res
            data.bobotKriteria.put(enabledCriteria[i], res[i])
        }

        return res
    }

    //WASPAS
    //===========================================================================================================

    fun convert_ke_nilai_kriteria(list : MutableList<Smartphone>) : MutableList<SP_rec>{
        var list_converted = mutableListOf<SP_rec>()
        for (obj in list){
            var converted = SP_rec()

            //ram
            if ((obj.ram <= 1))
                converted.c_ram = 1.0
            else if ((obj.ram >= 2) and (obj.ram <= 3))
                converted.c_ram = 2.0
            else if ((obj.ram >= 4) and (obj.ram <= 5))
                converted.c_ram = 3.0
            else if ((obj.ram >= 6) and (obj.ram <= 7))
                converted.c_ram = 4.0
            else
                converted.c_ram = 5.0

            //rom
            if (obj.rom <= 16)
                converted.c_rom = 1.0
            else if ((obj.rom > 16) and (obj.rom <= 32))
                converted.c_rom = 2.0
            else if ((obj.rom >= 33) and (obj.rom <= 64))
                converted.c_rom = 3.0
            else if ((obj.rom >= 65) and (obj.rom <= 128))
                converted.c_rom = 4.0
            else
                converted.c_rom = 5.0

            //Battery
            if (obj.battery < 3000)
                converted.c_bat = 1.0
            else if ((obj.battery >= 3000) and (obj.battery <= 3875))
                converted.c_bat = 2.0
            else if ((obj.battery >= 3876) and (obj.battery <= 4687))
                converted.c_bat = 3.0
            else if ((obj.battery >= 4688) and (obj.battery <= 5500))
                converted.c_bat = 4.0
            else
                converted.c_bat = 5.0

            //Main (Rear) Camera
            if (obj.mainCam < 2)
                converted.c_mainCam = 1.0
            else if ((obj.mainCam >= 2) and (obj.mainCam <= 8))
                converted.c_mainCam = 2.0
            else if ((obj.mainCam >= 9) and (obj.mainCam <= 14))
                converted.c_mainCam = 3.0
            else if ((obj.mainCam >= 15) and (obj.mainCam <= 20))
                converted.c_mainCam = 4.0
            else
                converted.c_mainCam = 5.0

            //Selfie Camera
            if (obj.selfieCam < 2)
                converted.c_selfie = 1.0
            else if ((obj.selfieCam >= 2) and (obj.selfieCam <= 8))
                converted.c_selfie = 2.0
            else if ((obj.selfieCam >= 9) and (obj.selfieCam <= 14))
                converted.c_selfie = 3.0
            else if ((obj.selfieCam >= 15) and (obj.selfieCam <= 20))
                converted.c_selfie = 4.0
            else
                converted.c_selfie = 5.0

            //Ukuran layar
            if (obj.uDisplay <= 5.0)
                converted.c_layar = 1.0
            else if ((obj.uDisplay >= 5.1) and (obj.uDisplay <= 5.5))
                converted.c_layar = 2.0
            else if ((obj.uDisplay >= 5.6) and (obj.uDisplay <= 6.0))
                converted.c_layar = 3.0
            else if ((obj.uDisplay >= 6.1) and (obj.uDisplay <= 6.5))
                converted.c_layar = 4.0
            else
                converted.c_layar = 6.5

            //Harga
            if (obj.harga < 1000001)
                converted.c_harga = 5.0
            else if ((obj.harga >= 1000001) and (obj.harga <= 2000000))
                converted.c_harga = 4.0
            else if ((obj.harga >= 2000001) and (obj.harga <= 3000000))
                converted.c_harga = 3.0
            else if ((obj.harga >= 3000001) and (obj.harga <= 4000000))
                converted.c_harga = 2.0
            else
                converted.c_harga = 1.0

            list_converted.add(converted)
        }
        print(list_converted.toString())
        return list_converted
    }


    //STEP 1 - Matriks Keputusan
    fun createMatriksKeputusan(matriks : MutableList<SP_rec>, enabledCriteria : MutableList<String>) : MutableList<MutableList<Double>>{
        caraHitung += "\n\n WASPAS - STEP 1\n" +
                "BUAT MATRIKS KEPUTUSAN \n\n"
        var newMatriks : MutableList<MutableList<Double>> = mutableListOf()

        var criteriaType = mutableMapOf(
            "RAM" to 'B',
            "ROM" to 'B',
            "Kapasitas baterai" to 'B',
            "Kamera belakang" to 'B',
            "Kamera depan" to 'B',
            "Ukuran layar" to 'B',
            "Harga" to 'C'
        )

        for(i in matriks){
            var newBaris : MutableList<Double> = mutableListOf()
            if(enabledCriteria.contains("RAM")){
                newBaris.add(i.c_ram!!)
            }
            if(enabledCriteria.contains("ROM")){
                newBaris.add(i.c_rom!!)
            }
            if(enabledCriteria.contains("Kapasitas baterai")){
                newBaris.add(i.c_bat!!)
            }
            if(enabledCriteria.contains("Kamera belakang")){
                newBaris.add(i.c_mainCam!!)
            }
            if(enabledCriteria.contains("Kamera depan")){
                newBaris.add(i.c_selfie!!)
            }
            if(enabledCriteria.contains("Ukuran layar")){
                newBaris.add(i.c_layar!!)
            }
            if(enabledCriteria.contains("Harga")){
                newBaris.add(i.c_harga!!)
            }

            newMatriks.add(newBaris)
        }
        caraHitung += "${newMatriks.toString()}"
        return newMatriks
    }

    //STEP 2 - Normalisasi Matriks Keputusan
    fun transposeMatriks(matriks : MutableList<MutableList<Double>>):MutableList<MutableList<Double>>{

        var transpose : MutableList<MutableList<Double>> = mutableListOf()
        //Persiapan sebelum transpose..
        for(i in matriks[0]){
            var row : MutableList<Double> = mutableListOf()
            for(j in 0 until matriks.size){
                row.add(0.toDouble())
            }
            transpose.add(row)
        }
        Log.i("CEK", "prepare.. $transpose")
        //Transpose
        for(i in 0 until transpose[0].size){
            for(j in 0 until transpose.size){
                transpose[j][i] = matriks[i][j]
            }
        }

        Log.i("CEK", "transpos matriks: ${transpose.toString()}")
        caraHitung += "\n Transpos matriks: ${transpose.toString()} \n\n"
        return transpose
    }

    //ignore ini. ini hanya utk yg tanpa comment
    fun uc_transposeMatriks(matriks : MutableList<MutableList<Double>>):MutableList<MutableList<Double>>{

        var transpose : MutableList<MutableList<Double>> = mutableListOf()
        //Persiapan sebelum transpose..
        for(i in matriks[0]){
            var row : MutableList<Double> = mutableListOf()
            for(j in 0 until matriks.size){
                row.add(0.toDouble())
            }
            transpose.add(row)
        }

        //Transpose
        for(i in 0 until transpose[0].size){
            for(j in 0 until transpose.size){
                transpose[j][i] = matriks[i][j]
            }
        }

        return transpose
    }

    fun normMatriksKeputusan(transposed : MutableList<MutableList<Double>>) : MutableList<MutableList<Double>>{
        // TOLONG UBAH KODE INI PAS UDH GANTI TABEL LIST KRITERIANYA
        caraHitung += "\n Normalisasi Matriks Keputusan\n"
        var normMatriks : MutableList<MutableList<Double>> = mutableListOf()
        for(i in 0 until transposed.size){
            caraHitung += "\n\n Baris ke-$i \n"
            var newBaris : MutableList<Double> = mutableListOf()
            var max = transposed[i].maxBy{it}
            var min = transposed[i].minBy{it}
            caraHitung += "\n Max = $max \n  Min = $min \n"

            var mode = enabledCriteriaType[i]

            for(j in 0 until transposed[0].size){
                var value  = 0.0
                value = if (mode == 'B'){ //BENEFIT
                    var norm = transposed[i][j]/max
                    caraHitung += "Kriteria ke-$j (benefit) = /frac{${transposed[i][j]}}{$max}=$norm \n"
                    norm
                } else{ //COST
                    var norm = min/transposed[i][j]
                    caraHitung += "Kriteria ke-$j (cost) =  /frac{$min}{${transposed[i][j]}}=$norm \n"
                    norm
                }
                newBaris.add(value)
            }
            normMatriks.add(newBaris)
        }
        caraHitung += "\n\n Hasil normalisasi = $normMatriks \n"
        Log.i("CEK", "normalised: ${normMatriks.toString()}")
        return normMatriks
    }

    //STEP 3 - Perhitungan Qi
    fun preferensiQi(normMatriks : MutableList<MutableList<Double>>, bobot : MutableList<Double>) : MutableList<Double>{
        caraHitung += "\n\n (WASPAS) STEP 3\n" +
                "Perhitungan Nilai Qi\n\n"
        var res : MutableList<Double> = mutableListOf()
        var reTranspose : MutableList<MutableList<Double>> = uc_transposeMatriks(normMatriks)
        caraHitung += "\n\n Transpos kembali matriks yang udah dinormalisasi tadi ke posisi semula.. : \n $reTranspose "
        for(i in 0 until reTranspose.size){
            caraHitung += "\nData ke-${i+1}:\n"
            var left : Double = 0.0
            var right : Double = 1.0
            var textLeft = ""
            var textRight = ""
            for(j in 0 until reTranspose[i].size){
                left += reTranspose[i][j]*bobot[j]
                textLeft+= "+(${reTranspose[i][j]}*${bobot[j]})"
                right *= Math.pow(reTranspose[i][j],bobot[j])
                textRight+= "*(${reTranspose[i][j]}^(${bobot[j]}))"
            }
            textLeft += "= $left"
            textRight += "= $right"

            caraHitung += "\nBagian kiri: $textLeft \n Bagian kanan: $textRight"
            var qiValue = 0.5*left + 0.5*right
            caraHitung += "\nNilai Qi = $qiValue\n\n"
            res.add(qiValue)
        }
        caraHitung += "Hasil: $res"
        return res
    }
    //////////////////////////////////////
    fun viewMatriks(matriks: MutableList<MutableList<Double>>){
        for(i in matriks){
            print(i.toString() + '\n')
        }
    }

    fun viewMatriksTFN(matriks: MutableList<MutableList<MutableList<Double>>>){
        for(i in matriks){
            print(i.toString() + '\n')
        }
    }

    fun FAHP(PCM: MutableList<MutableList<Double>>): MutableList<Double> {
        var TFN = convertSkalaTFN(PCM)
//          SEMENTARA TIDAK DIGUNAKAN!!!!!!!!
//        var Vektor = prioritasVektor(fuzzySE(totalLMU_Baris(TFN), totalLMU_Kolom(TFN)))
//        var defuz = coaDefuz(fuzzySE(totalLMU_Baris(TFN), totalLMU_Kolom(TFN)))
        var defuz = coaDefuz(fuzzySE(fuzzyGM(TFN)))
        return normVF(defuz)
    }

    fun FAHP_WASPAS(dataAlternatif: MutableList<SP_rec>, pcm: MutableList<MutableList<Double>>) : MutableList<Double>{
        var matriksKeputusan : MutableList<MutableList<Double>> = createMatriksKeputusan(dataAlternatif, enabledCriteria)
        var transpose = transposeMatriks(matriksKeputusan)
        var normMatriksKeputusan : MutableList<MutableList<Double>> = normMatriksKeputusan(transpose)
        var bobotFahp : MutableList<Double> = FAHP(pcm)
        return preferensiQi(normMatriksKeputusan,bobotFahp)
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
        for(i in 1..res.size){
            res[i - 1].rank = i
        }

        caraHitung += "Sorted berdasarkan nilai qi: $res"
        return res
    }
}
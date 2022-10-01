package com.skripsi.spreco

import android.util.Log
import com.skripsi.spreco.classes.SP
import com.skripsi.spreco.classes.SP_rank
import com.skripsi.spreco.classes.SP_rec

object fahp_waspas {
    //FAHP
    //===========================================================================================================
    //STEP 1 - Mengubah Pairwise Comparison Matrix ke dalam skala TFN (Triangle Fuzzy Number)
    fun convertSkalaTFN(matriksPC : MutableList<MutableList<Double>>) : MutableList<MutableList<MutableList<Double>>>{
        var matriksTFN : MutableList<MutableList<MutableList<Double>>> = mutableListOf()

        //Ukuran matriks TFN = jlhKriteria x jlhKriteria
        for(i in 0 until matriksPC.size){
            var row : MutableList<MutableList<Double>> = mutableListOf()
            for(j in 0..(matriksPC.size - 1)){
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
        return matriksTFN
    }

    //Hitung total semua L, M, dan U tiap baris
    fun totalLMU_Baris(matriksTFN : MutableList<MutableList<MutableList<Double>>>) : MutableList<MutableList<Double>>{
        var all: MutableList<MutableList<Double>> = mutableListOf()
        var total_L : Double = 0.0
        var total_M : Double = 0.0
        var total_U : Double = 0.0
        for(i in 0 until matriksTFN.size){
            for(j in 0 until matriksTFN[i].size){
                total_L += matriksTFN[i][j][0]
                total_M += matriksTFN[i][j][1]
                total_U += matriksTFN[i][j][2]
            }
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

        for(i in 0 until matriksTFN.size){
            for(j in 0 until matriksTFN[i].size){
                total_L += matriksTFN[i][j][0]
                total_M += matriksTFN[i][j][1]
                total_U += matriksTFN[i][j][2]
            }
        }
        return mutableListOf(total_L, total_M, total_U)
    }

    // STEP 2 - FUZZY SYNTHESIS EXTENT (Si)
    fun fuzzySE(lmuBaris : MutableList<MutableList<Double>>, lmuKolom : MutableList<Double>) : MutableList<MutableList<Double>>{
        //lmuKolom -> [l total, m total, u total]
        var result : MutableList<MutableList<Double>> = mutableListOf()
        for(i in 0 until lmuBaris.size){
            //lower / l -> upper
            var l : Double = lmuBaris[i][0] * (1 / lmuKolom[2])
            //median / m -> median
            var m : Double = lmuBaris[i][1] * (1 / lmuKolom[1])
            //upper / u -> lower
            var u : Double = lmuBaris[i][2] * (1 / lmuKolom[0])
            result.add(mutableListOf(l, m, u))
        }
        return result
    }

    // STEP 3 - PRIORITAS VEKTOR (V)
    // Jika:
    // - m2 >= m1 maka 1
    // - l1 >= u2 maka 0
    // - selain dari kedua syarat di atas: (l1 - u2) / ((m2 - u2) - (m1 - l1))

    fun prioritasVektor(hasilFSE: MutableList<MutableList<Double>>) : MutableList<MutableList<Double>>{
        var resVektor : MutableList<MutableList<Double>> = mutableListOf()
        for(i in 0 until hasilFSE.size){
            var newBaris : MutableList<Double> = mutableListOf()
            for(j in 0 until hasilFSE.size){
                //m2 >= m1?
                if(hasilFSE[j][1] >= hasilFSE[i][1]){
                    newBaris.add(1.0)
                }
                //l1 >= u2?
                else if (hasilFSE[i][0] >= hasilFSE[j][2]){
                    newBaris.add(0.0)
                }
                //selain kedua syarat di atas
                else{
                    var hitung = (hasilFSE[i][0] - hasilFSE[j][2]) / ((hasilFSE[j][1] - hasilFSE[j][2]) - (hasilFSE[i][1] - hasilFSE[i][0]))
                    newBaris.add(hitung)
                }
            }
            resVektor.add(newBaris)
        }
        return resVektor
    }

    //STEP 4 - NILAI ORDINAT DEFUZZIFIKASI (d')
    fun ordDefuz(hasilVektor : MutableList<MutableList<Double>>) : MutableList<Double>{
        var res : MutableList<Double> = mutableListOf()
        //Setiap baris dicari nilai terendah
        for(i in hasilVektor){
            res.add(i.minBy{it})
        }
        return res
    }

    //STEP 5 - NORMALISASI NILAI BOBOT VEKTOR FUZZY (W)
    fun normVF(hasilDefuz : MutableList<Double>) : MutableList<Double> {
        // tiap bobot kriteria yang telah didefuzzifikasi dibagi dgn penjumlahan total seluruh bobot kriteria tsb
        var sum : Double = (hasilDefuz.sumOf{it})
        var res : MutableList<Double> = mutableListOf()
        for(i in hasilDefuz){
            res.add(i / sum)
        }

        //Kalau semua elemen array res dijumlahkan, hasilnya pasti 1.
        return res
    }

    //WASPAS
    //===========================================================================================================
    //STEP 1 - Matriks Keputusan
    fun createMatriksKeputusan(matriks : MutableList<SP_rec>) : MutableList<MutableList<Double>>{
        var newMatriks : MutableList<MutableList<Double>> = mutableListOf()
        for(i in matriks){
            var newBaris : MutableList<Double> = mutableListOf()
            newBaris.add(i.c_ram!!)
            newBaris.add(i.c_rom!!)
            newBaris.add(i.c_bat!!)
            newBaris.add(i.c_mainCam!!)
            newBaris.add(i.c_selfie!!)
            newBaris.add(i.c_harga!!)
            newMatriks.add(newBaris)
        }
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

        Log.i("CEK", "transposed: ${transpose.toString()}")

        return transpose
    }
    fun normMatriksKeputusan(transposed : MutableList<MutableList<Double>>) : MutableList<MutableList<Double>>{
        var normMatriks : MutableList<MutableList<Double>> = mutableListOf()
        for(i in 0 until transposed.size){
            var newBaris : MutableList<Double> = mutableListOf()
            for(j in 0 until transposed[0].size){
    //          anggap semua kriteria menggunakan benefit
                var max = transposed[i].maxBy{it}
                var benefit : Double = transposed[i][j]/max
                newBaris.add(benefit)
            }
            normMatriks.add(newBaris)
        }
        Log.i("CEK", "normalised: ${normMatriks.toString()}")
        return normMatriks
    }

    //STEP 3 - Perhitungan Qi
    fun preferensiQi(normMatriks : MutableList<MutableList<Double>>, bobot : MutableList<Double>) : MutableList<Double>{
        var res : MutableList<Double> = mutableListOf()
        var reTranspose : MutableList<MutableList<Double>> = transposeMatriks(normMatriks)
        for(i in 0 until reTranspose.size){
            var left : Double = 0.0
            var right : Double = 1.0
            for(j in 0 until reTranspose.size){
                left += reTranspose[i][j]*bobot[j]
                right *= Math.pow(reTranspose[i][j],bobot[j])
            }
            res.add(0.5*left + 0.5*right)
        }
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

    fun FAHP(PCM : MutableList<MutableList<Double>>) : MutableList<Double>{
        var TFN = convertSkalaTFN(PCM)
        var Vektor = prioritasVektor(fuzzySE(totalLMU_Baris(TFN), totalLMU_Kolom(TFN)))
        return normVF(ordDefuz(Vektor))
    }

    fun FAHP_WASPAS(dataAlternatif: MutableList<SP_rec>, pcm: MutableList<MutableList<Double>>) : MutableList<Double>{
        var matriksKeputusan : MutableList<MutableList<Double>> = createMatriksKeputusan(dataAlternatif)
        var transpose = transposeMatriks(matriksKeputusan)
        var normMatriksKeputusan : MutableList<MutableList<Double>> = normMatriksKeputusan(transpose)
        var bobotFahp : MutableList<Double> = FAHP(pcm)
        return preferensiQi(normMatriksKeputusan,bobotFahp)
    }

    fun makeRanking(dataAlt : MutableList<SP>, qiList : MutableList<Double>) : MutableList<SP_rank>{
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
        return res
    }
}
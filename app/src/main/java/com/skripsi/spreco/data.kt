package com.skripsi.spreco

import com.skripsi.spreco.classes.SP
import com.skripsi.spreco.classes.SP_rec
import java.text.DecimalFormat
import java.text.NumberFormat
import java.time.temporal.ValueRange

object data {
    // Account auth
    var curRole = "u"

    //List data
    var list_sp : MutableList<SP> = mutableListOf(
        SP(
            1,
            "Infinix Smart 6 Plus (3/64)",
            1139000,
            "GSM / HSPA / LTE",
            "Dual SIM (Nano-SIM, dual stand-by)",
            "IPS LCD, 440 nits (typ)",
            6.82,
            "Android 12 (Go edition)",
            "MediaTek MT6762G Helio G25 (12 nm)",
            "Octa-core (4x2.0 GHz Cortex-A53 & 4x1.5 GHz Cortex-A53)",
            "PowerVR GE8320",
            3,
            64,
            8.0,
            5.0,
            5000,
            "Tranquil Sea Blue, Miracle Black",
            3242,
            "https://fdn2.gsmarena.com/vv/bigpic/infinix-smart-6-plus.jpg"
        ),
        SP(
            2,
            "Xiaomi Redmi Note 11 Pro (6/64)",
            3449000,
            "GSM / HSPA / LTE",
            "Hybrid Dual SIM (Nano-SIM, dual stand-by)",
            "Super AMOLED, 120Hz, 700 nits, 1200 nits (peak)",
            6.67,
            "Android 11, MIUI 13",
            "Mediatek Helio G96 (12 nm)",
            "Octa-core (2x2.05 GHz Cortex-A76 & 6x2.0 GHz Cortex-A55)",
            "Mali-G57 MC2",
            6,
            64,
            108.0,
            16.0,
            5000,
            "Graphite Gray (Stealth Black), Polar White (Phantom White), Star Blue",
            8209,
            "https://fdn2.gsmarena.com/vv/bigpic/xiaomi-redmi-note-11-pro-global.jpg"
        ),
        SP(
            3,
            "Xiaomi Redmi K20 Pro (6/64)",
            4999000,
            "GSM / HSPA / LTE",
            "Dual SIM (Nano-SIM, dual stand-by)",
            "Super AMOLED, HDR10",
            6.39,
            "Android 9.0 (Pie), upgradable to Android 10, MIUI 12",
            "Qualcomm SM8150 Snapdragon 855 (7 nm)",
            "Octa-core (1x2.84 GHz Kryo 485 & 3x2.42 GHz Kryo 485 & 4x1.78 GHz Kryo 485)",
            "Adreno 640",
            6, 64,
            48.0, 20.0,
            4000,
            "Carbon black, Flame red, Glacier blue, Summer Honey, Pearl White",
            8621,
            "https://fdn2.gsmarena.com/vv/bigpic/xiaomi-redmi-k20pro-.jpg"
        ),
        SP(
            4,
            "Xiaomi Redmi Note 5 (3/32)",
            1899000,
            "GSM / HSPA / LTE",
            "Hybrid Dual SIM (Nano-SIM, dual stand-by)",
            "IPS LCD",
            5.99,
            "Android 7.1.2 (Nougat), planned upgrade to Android 10, MIUI 12",
            "Qualcomm MSM8953 Snapdragon 625 (14 nm)",
            "Octa-core 2.0 GHz Cortex-A53",
            "Adreno 506",
            3, 32,
            12.0, 5.0,
            4000,
            "Black, Gold, Blue, Rose Gold",
            4124,
            "https://fdn2.gsmarena.com/vv/bigpic/xiaomi-redmi-5-plus.jpg"
        ),
        SP(
            5,
            "Xiaomi Redmi 6A (2/16)",
            1200000,
            "GSM / CDMA / HSPA / LTE",
            "Dual SIM (Nano-SIM, dual stand-by)",
            "IPS LCD",
            5.45,
            "Android 8.1 (Oreo), planned upgrade to Android 10, MIUI 12",
            "Mediatek MT6761 Helio A22 (12 nm)",
            "Quad-core 2.0 GHz Cortex-A53",
            "PowerVR GE8320",
            2, 16,
            13.0, 5.0,
            3000,
            "Black, Gold, Blue, Rose Gold",
            1104,
            "https://fdn2.gsmarena.com/vv/bigpic/xiaomi-redmi-6a.jpg"
        )
    )

    //Methods
    fun formatHarga(harga : Int) : String{
        var formatter : DecimalFormat = NumberFormat.getInstance() as DecimalFormat
        formatter.applyPattern("#,###")
        return formatter.format(harga)
    }

    fun convert_ke_nilai_kriteria(list : MutableList<SP>) : MutableList<SP_rec>{
        var list_converted = mutableListOf<SP_rec>()
        for (obj in list){
            var converted = SP_rec()

            //RAM
            if ((obj.RAM <= 1))
                converted.c_ram = 1.0
            else if ((obj.RAM >= 2) and (obj.RAM <= 3))
                converted.c_ram = 2.0
            else if ((obj.RAM >= 4) and (obj.RAM <= 5))
                converted.c_ram = 3.0
            else if ((obj.RAM >= 6) and (obj.RAM <= 7))
                converted.c_ram = 4.0
            else
                converted.c_ram = 5.0

            //ROM
            if (obj.ROM < 16)
                converted.c_rom = 1.0
            else if ((obj.ROM >= 16) and (obj.ROM <= 32))
                converted.c_rom = 2.0
            else if ((obj.ROM >= 33) and (obj.ROM <= 64))
                converted.c_rom = 3.0
            else if ((obj.ROM >= 65) and (obj.ROM <= 128))
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

            //Harga
            if (obj.harga < 1000000)
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
}
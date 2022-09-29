package com.skripsi.spreco

import com.skripsi.spreco.classes.SP
import java.text.DecimalFormat
import java.text.NumberFormat

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
}
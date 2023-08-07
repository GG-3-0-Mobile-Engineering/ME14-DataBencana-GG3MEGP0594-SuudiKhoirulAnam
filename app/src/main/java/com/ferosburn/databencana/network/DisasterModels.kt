package com.ferosburn.databencana.network

data class DisasterModel(
    val pkey: String,
    val createdAt: String,
    val imageUrl: String?,
    val disasterType: String,
    val instanceRegionCode: String,
    val coordinates: List<Double>
)

enum class DisasterTypes(val disasterName: String, val disasterValue: String) {
    ALL("Semua", ""),
    FLOOD("Banjir", "flood"),
    EARTHQUAKE("Gempa Bumi", "earthquake"),
    FIRE("Kebakaran", "fire"),
    HAZE("Kabut Asap", "haze"),
    WIND("Angin Kencang", "wind"),
    VOLCANO("Gunung Api", "volcano")
}

enum class Provinces(val provinceName: String, val code: String) {
    ACEH("Aceh", "ID-AC"),
    BALI("Bali", "ID-BA"),
    BANGKABELITUNG("Kep. Bangka Belitung", "ID-BB"),
    BENGKULU("Bengkulu", "ID-BE"),
    BANTEN("Banten", "ID-BT"),
    GORONTALO("Gorontalo", "ID-GO"),
    JABAR("Jawa Barat", "ID-JB"),
    JAKARTA("DKI Jakarta", "ID-JK"),
    JAMBI("Jambi", "ID-JA"),
    JATENG("Jawa Tengah", "ID-JT"),
    JATIM("Jawa Timur", "ID-JI"),
    KALBAR("Kalimantan Barat", "ID-KB"),
    KALSEL("Kalimantan Selatan", "ID-KS"),
    KALTARA("Kalimantan Utara", "ID-KU"),
    KALTENG("Kalimantan Tengah", "ID-KT"),
    KALTIM("Kalimantan Timur", "ID-KI"),
    KEPRI("Kep. Riau", "ID-KR"),
    LAMPUNG("Lampung", "ID-LA"),
    MALUKU("Maluku", "ID-MA"),
    MALUT("Maluku Utara", "ID-MU"),
    NTB("Nusa Tenggara Barat", "ID-NB"),
    NTT("Nusa Tenggara Timur", "ID-NT"),
    PABAR("Papua Barat", "ID-PB"),
    PAPUA("Papua", "ID-PA"),
    RIAU("Riau", "ID-RI"),
    SULBAR("Sulawesi Barat", "ID-SR"),
    SULSEL("Sulawesi Selatan", "ID-SN"),
    SULTENG("Sulawesi Tengah", "ID-ST"),
    SULTRA("Sulawesi Tenggara", "ID-SG"),
    SULUT("Sulawesi Utara", "ID-SA"),
    SUMBAR("Sumatra Barat", "ID-SB"),
    SUMSEL("Sumatra Selatan", "ID-SS"),
    SUMUT("Sumatra Utara", "ID-SU"),
    YOGYA("DI Yogyakarta", "ID-YO")
}

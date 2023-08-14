package com.ferosburn.databencana.domain.model

enum class DisasterTypes(val disasterName: String, val disasterValue: String) {
    ALL("Semua", ""),
    FLOOD("Banjir", "flood"),
    EARTHQUAKE("Gempa Bumi", "earthquake"),
    FIRE("Kebakaran", "fire"),
    HAZE("Kabut Asap", "haze"),
    WIND("Angin Kencang", "wind"),
    VOLCANO("Gunung Api", "volcano")
}
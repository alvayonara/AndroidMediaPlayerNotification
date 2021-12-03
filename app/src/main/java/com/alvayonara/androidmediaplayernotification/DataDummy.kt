package com.alvayonara.androidmediaplayernotification

import com.alvayonara.androidmediaplayernotification.model.AudioPlayerEntity

object DataDummy {

    fun getLocalAudio(): List<AudioPlayerEntity> =
        mutableListOf<AudioPlayerEntity>().apply {
            this.add(
                AudioPlayerEntity(
                    name = "italian-film-festival-digelar-secara-daring",
                    title = "Italian Film Festival Digelar secara Daring",
                    publishedDate = "2021-12-02 22:08:11",
                    terms = "Pendidikan & Kebudayaan",
                    thumbnail = "https://assetd.kompas.id/iY3atLo8lHSDOLHcT5CUiRd1x2g=/720x540/https://silo.kompas.id/wp-content/uploads/2021/12/IMG_9373-2_1638452821.jpg",
                    event = "dashboard",
                    audio = "https://apio.kompas.id/soundwave/?blogtype=editorial&berkas=italian-film-festival-digelar-secara-daring.mp3",
                    slug = "TERBARU",
                    isBebasAkses = true,
                    subscriptionStatus = 1,
                    cursor = "1638453520",
                    id = "1216a1bca4361c39d1d77965c5d95ee3",
                    authorName = "SEKAR GANDHAWANGI",
                    contentCategory = "Pendidikan & Kebudayaan|Bebas Akses|Humaniora|Desk|Desk Humaniora|, articleTags=film|kebudayaan|Festival Film|utama|kedutaan besar italia|aktual|Italian Film Festival|festival film daring|Italian Cultural Institute|Institut Kebudayaan Italia di Jakarta|film italia|"
                )
            )
            this.add(
                AudioPlayerEntity(
                    name = "google-sediakan-beasiswa-pelatihan-teknologi-digital-2-juta-dollar-as",
                    title = "Google Sediakan Beasiswa Pelatihan Teknologi Digital 2 Juta Dollar AS",
                    publishedDate = "2021-12-02 21:53:46",
                    terms = "Ekonomi",
                    thumbnail = "https://assetd.kompas.id/HRsFht2qxvUUVjHzu5k1ppv2Uu8=/720x540/https://silo.kompas.id/wp-content/uploads/2021/09/FILES-US-TECH-REMOTE-COMPUTERS_99021891_1632354028.jpg",
                    event = "dashboard",
                    audio = "https://apio.kompas.id/soundwave/?blogtype=editorial&berkas=google-sediakan-beasiswa-pelatihan-teknologi-digital-2-juta-dollar-as.mp3",
                    slug = "TERBARU",
                    isBebasAkses = false,
                    subscriptionStatus = 1,
                    cursor = "1638453520",
                    id = "0e64a7b00c83e3d22ce6b3acf2c582b6",
                    authorName = "Mediana",
                    contentCategory = "Ekonomi|Desk|Desk Ekonomi & Bisnis|",
                    articleTags = "Google|berita|google.org|aktual|Google for Indonesia|kompetensi teknologi digital|"
                )
            )
        }
}
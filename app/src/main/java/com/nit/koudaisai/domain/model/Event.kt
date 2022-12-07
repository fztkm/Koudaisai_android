package com.nit.koudaisai.domain.model

import com.google.android.gms.maps.model.LatLng

data class Event(
    val name: String,
    val img: Int?,
    val date: String,
    val location: String,
    val locationId: LocationId? = null,
    val latLngList: Map<String, LatLng>?,
    val description: List<EventContent>,
)

data class EventContent(
    val title: String? = null,
    val subtitle: String? = null,
    val text: String? = null,
    val img: Int? = null,
    val video: String? = null,
)

enum class LocationId(val label: String) {
    FIFTYTWO("52号館"),
    FIFTYONE("51号館"),
    TWELVE("12号館"),
    NINTEEN("19号館"),
    FOUR("4号館"),
    NIT("NITech Hall"),
    STAGE("2号館前ステージ"),
    GYM("体育館"),
    JUDO("柔道場"),
    EAT("模擬店"),
    TICKET_SUN("整理券配布場所 (晴天時)"),
    TICKET_LAIN("整理券配布場所 (雨天時)")
}
package com.nit.koudaisai.graph

import com.nit.koudaisai.R

//TODO enum にする
interface HomeDestination {
    val icon: Int
    val route: String
    val title: String
}

object Info : HomeDestination {
    override val icon = R.drawable.approval
    override val route = "info"
    override val title = "第60回工大祭"
}

object Settings : HomeDestination {
    override val icon = R.drawable.settings
    override val route = "settings"
    override val title = "設定"
}

object Provisional : HomeDestination {
    override val icon = R.drawable.approval
    override val route = "provisional"
    override val title = "第60回工大祭"
}

object Overview : HomeDestination {
    override val icon = R.drawable.approval
    override val route = "overview"
    override val title = "第60回工大祭"
}

object Mapview : HomeDestination {
    override val icon = R.drawable.location_on
    override val route = "mapview"
    override val title = "マップ"
}

object Events : HomeDestination {
    override val icon = R.drawable.approval
    override val route = "events"
    override val title = "企画"
}

object Schedule : HomeDestination {
    override val icon = R.drawable.calendar_today
    override val route = "schedule"
    override val title = "タイムテーブル"
}
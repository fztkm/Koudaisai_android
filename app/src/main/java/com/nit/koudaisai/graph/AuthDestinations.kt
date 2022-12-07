package com.nit.koudaisai.graph


//TODO enumにする
interface ReserveDestination {
    val route: String
}

object ReserveMenu : ReserveDestination {
    override val route = "reserve_home"
}

object SignIn : ReserveDestination {
    override val route = "sign_in"
}

object ReserveInputUserData : ReserveDestination {
    override val route = "reserve_input_user_data"
}

object ReserveConfirm : ReserveDestination {
    override val route = "reserve_confirm"
}

object ReserveCompleted : ReserveDestination {
    override val route = "reserve_completed"
}


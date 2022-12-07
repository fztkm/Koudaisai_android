package com.nit.koudaisai.domain.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class KoudaisaiUser(
    val name: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val dayOneSelected: Boolean = false,
    val dayTwoSelected: Boolean = false,
    val dayOneVisited: Boolean = false,
    val dayTwoVisited: Boolean = false,
    var subUserIdList: List<String>? = listOf(),
    val parentId: String? = null,
    @set:PropertyName("isAdmin")
    @get:PropertyName("isAdmin")
    var isAdmin: Boolean? = null,
    val timestamps: List<Timestamp>? = listOf()
) {
    override fun equals(other: Any?): Boolean {
        if (other !is KoudaisaiUser) {
            return false
        }
        if (this.email != other.email) {
            return false
        }
        if (this.name != other.name) {
            return false
        }
        if (this.phoneNumber != other.phoneNumber) {
            return false
        }
        if (this.dayOneSelected != other.dayOneSelected) {
            return false
        }
        if (this.dayTwoSelected != other.dayTwoSelected) {
            return false
        }
        if (this.dayOneVisited != other.dayOneVisited) {
            return false
        }
        if (this.dayTwoVisited != other.dayTwoVisited) {
            return false
        }
        if (this.subUserIdList?.equals(other.subUserIdList) != true) {
            return false
        }
        if (this.parentId != other.parentId) {
            return false
        }
        if (this.isAdmin != other.isAdmin) {
            return false
        }
        if (this.timestamps?.equals(other.timestamps) != true) {
            return false
        }
        return true
    }
}

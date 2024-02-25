package com.yapp.bol.domain.model.core

sealed class Role(val string: String) {
    object OWNER : Role("OWNER")
    object HOST : Role("HOST")
    object GUEST : Role("GUEST")

    companion object {
        fun toRole(rawRole: String): Role {
            return when(rawRole) {
                OWNER.string -> OWNER
                HOST.string -> HOST
                GUEST.string -> GUEST
                else -> throw IllegalArgumentException("there's no matching role")
            }
        }
    }
}

package com.cochipcho.conchip.Helpers

val signupDate: Int
    get() {
        val signupDateLong = System.currentTimeMillis()/1000
        val signupDate = signupDateLong.toInt()
        return signupDate
    }
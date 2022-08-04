package com.example.covid_19tracker

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request

object client {
    private val okhttps=OkHttpClient()
    private val request=Request.Builder()
        .url("https://api.covid19india.org/data.json")
        .build()
    val api= okhttps.newCall(request)

}
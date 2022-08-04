package com.example.covid_19tracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    lateinit var stateadapter: stateadapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        list.addHeaderView(LayoutInflater.from(this).inflate(R.layout.item_header,list,false))
        fetchResult()
    }

    private fun fetchResult() {
        GlobalScope.launch {
            val response = withContext(Dispatchers.IO) { client.api.execute() }
            if(response.isSuccessful)
            {
                val data=Gson().fromJson(response.body?.string(),Response::class.java)
                launch(Dispatchers.Main) {
                    bindCombinedata(data.statewise.get(0))
                    bindStatewise(data.statewise.subList(1,data.statewise.size))
                }
             }
    }
    }

    private fun bindStatewise(subList: List<StatewiseItem>) {

        stateadapter= stateadapter(subList)
        list.adapter=stateadapter

    }

    private fun bindCombinedata(data: StatewiseItem) {
        val lasttime=data.lastupdatedtime
        val simpleDateFormat=SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        lastupdated.text="Last Updated \n ${getTimeago(simpleDateFormat.parse(lasttime))}"
        confirm.text=data.confirmed
        active.text=data.active
        recovered.text=data.recovered
        deceased.text=data.deaths
    }
    fun getTimeago(past:Date):String{
        val now=Date()
        val seconds=TimeUnit.MILLISECONDS.toSeconds(now.time-past.time)
        val minutes=TimeUnit.MILLISECONDS.toMinutes(now.time-past.time)
        val hours=TimeUnit.MILLISECONDS.toHours(now.time-past.time)
        return when{
            seconds < 60 ->{
                    "Few Seconds Ago"
            }
            minutes<60 ->{
                "$minutes minutes ago"
            }
            hours<24 ->{
                "$hours hours ${minutes % 60} min ago"
            }
            else
                ->{
                    SimpleDateFormat("dd/MM/yy,hh:mm:a").format(past).toString()
                }

        }

    }

}
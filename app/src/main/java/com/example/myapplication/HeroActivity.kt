package com.example.myapplication

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Adapter.MyMovieAdapter
import com.example.myapplication.Common.Common
import com.example.myapplication.Interface.RetrofitServices
import com.example.myapplication.Model.Movie
import dmax.dialog.SpotsDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HeroActivity : AppCompatActivity() {
    lateinit var mService: RetrofitServices
    lateinit var linerLayout: LinearLayoutManager
    lateinit var adapter: MyMovieAdapter
    lateinit var dialog: AlertDialog
    lateinit var recList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hero)

        recList =  findViewById(R.id.recyclerMovieList)
        mService =  Common.retrofitService
        recList.setHasFixedSize(true)
        linerLayout = LinearLayoutManager(this)
        recList.layoutManager = linerLayout
        dialog = SpotsDialog.Builder().setCancelable(true).setContext(this).build()

        getAllMovieList()
    }
    private fun getAllMovieList() {
        dialog.show()
        mService.getMovieList().enqueue(object : Callback<MutableList<Movie>> {
            override fun onFailure(call: Call<MutableList<Movie>>, t: Throwable) {

            }

            override fun onResponse(call: Call<MutableList<Movie>>, response: Response<MutableList<Movie>>) {
                adapter = MyMovieAdapter(baseContext, response.body() as MutableList<Movie>)
                adapter.notifyDataSetChanged()
                recList.adapter = adapter

                dialog.dismiss()
            }
        })
    }
}
package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.db.AdapterOfElement
import com.example.myapplication.db.MyDbManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    val myDbManager = MyDbManager(this)
    val myAdapter = AdapterOfElement(ArrayList(), this)
    private var job: Job? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        searchView()

    }

    fun goToApi(view: View) {
        var i = Intent(this, HeroActivity::class.java)
        startActivity(i)
    }

    fun toPlus(view: View) {
        var i = Intent(this, EditActivity::class.java)
        startActivity(i)
    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
        updateAdapter("")
    }

    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }

    fun init() {

        var element = findViewById<RecyclerView>(R.id.recyclerView)
        element.layoutManager = LinearLayoutManager(this)
        val swap = getSwap()
        swap.attachToRecyclerView(element)
        element.adapter = myAdapter

    }

    private fun updateAdapter(text: String) {
        job?.cancel()
        job = CoroutineScope(Dispatchers.Main).launch {
            val list = myDbManager.readDbData(text)
            myAdapter.update(list)

            if (list.size > 0) {
                findViewById<TextView>(R.id.textView).visibility = View.GONE
            } else {
                findViewById<TextView>(R.id.textView).visibility = View.VISIBLE
            }
        }
    }

    private fun searchView() {
        var searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                updateAdapter(newText!!)

                return true
            }
        })
    }

    private fun getSwap(): ItemTouchHelper {
        return ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                myAdapter.toDelete(viewHolder.adapterPosition, myDbManager)
            }
        })
    }

}

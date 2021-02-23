package com.example.todoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.database.DbContract
import com.example.todoapp.database.RenderDbHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), DataAdapter.Callback {

    lateinit var dbHelper : RenderDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = RenderDbHelper(this)

        initListener()
    }

    private fun getData() {
        val db = dbHelper.readableDatabase

        val projection = arrayOf(BaseColumns._ID, DbContract.DataEntry.COLUMN_TITLE, DbContract.DataEntry.COLUMN_DESC)
        val sortOrder = "${DbContract.DataEntry.COLUMN_DESC} DESC"
        val cursor = db.query(
                DbContract.DataEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        )
        val dataList = mutableListOf<DataModel>()
        with(cursor){
            while(moveToNext()){
                val id = getLong(getColumnIndex(BaseColumns._ID))
                val title = getString(getColumnIndexOrThrow(DbContract.DataEntry.COLUMN_TITLE))
                val desc = getString(getColumnIndexOrThrow(DbContract.DataEntry.COLUMN_DESC))
                dataList.add(DataModel(id, title, desc))
            }
        }

        val dataAdapter = DataAdapter(this)
        rv_plans.apply {
            val linearLayoutManager = LinearLayoutManager(context)
            linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
            layoutManager = linearLayoutManager
            adapter = dataAdapter
        }

        dataAdapter.setData(dataList)
        dataAdapter.notifyDataSetChanged()
    }

    private fun initListener(){
        fab_add.setOnClickListener {
            startActivity(Intent(this, DetailActivity::class.java).putExtra("status", true))
        }
    }

    private fun getDataDummy(){
        var dataList = ArrayList<DataModel>()
        dataList.add(DataModel(1, "Belajar Java", "Belajar Java dan Android"))
        dataList.add(DataModel(2, "Belajar Java", "Belajar Java dan Android"))
        dataList.add(DataModel(3, "Belajar Java", "Belajar Java dan Android"))

        var dataAdapter = DataAdapter(this)
        rv_plans.apply {
            var linearLayoutManager = LinearLayoutManager(context)
            linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
            layoutManager = linearLayoutManager
            adapter = dataAdapter
        }

        dataAdapter.setData(dataList)
        dataAdapter.notifyDataSetChanged()
    }

    override fun onClick(data: DataModel) {
        startActivity(Intent(this, DetailActivity::class.java)
                .putExtra("status", false)
                .putExtra("data", data)
        )
    }

    override fun onResume() {
        super.onResume()
        getData()

    }
}
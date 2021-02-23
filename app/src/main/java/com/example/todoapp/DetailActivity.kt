package com.example.todoapp

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.view.View
import android.widget.Toast
import com.example.todoapp.database.DbContract
import com.example.todoapp.database.RenderDbHelper
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    lateinit var dbHelper: RenderDbHelper
    lateinit var db: SQLiteDatabase
    lateinit var data: DataModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        dbHelper = RenderDbHelper(this)
        db = dbHelper.writableDatabase

        var status = intent.getBooleanExtra("status", false)
        if(!status){
            data = intent.getParcelableExtra("data")!!
            et_title_plan.setText(data.title)
            et_description_plan.setText(data.desc)
            btn_delete.visibility = View.VISIBLE
            btn_save.text = "Edit Plan"
            initListenerEdit()
        }else{
            initListener()
        }
    }

    private fun initListener(){
        btn_save.setOnClickListener {
            var title = et_title_plan.text.toString();
            var desc = et_description_plan.text.toString();

            if(title.isNullOrEmpty()){
                et_title_plan.error = "Silahkan masukan title"
                et_title_plan.requestFocus()
            }else if(desc.isNullOrEmpty()){
                et_description_plan.error = "Silahkan masukan description"
                et_description_plan.requestFocus()
            }else{
                val value = ContentValues().apply {
                    put(DbContract.DataEntry.COLUMN_TITLE, title)
                    put(DbContract.DataEntry.COLUMN_DESC, desc)
                }

                val newRowId = db.insert(DbContract.DataEntry.TABLE_NAME, null, value)
                if(newRowId==-1L){
                    Toast.makeText(this, "Data gagal disimpan", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Data telah disimpan", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun initListenerEdit(){
        btn_delete.setOnClickListener {
            val selection = "${BaseColumns._ID} LIKE ?"
            val selectionArg = arrayOf(data.id.toString())
            val deleteRows = db.delete(DbContract.DataEntry.TABLE_NAME, selection, selectionArg)
            if(deleteRows==-1){
                Toast.makeText(this, "Data gagal didelete", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Data telah didelete", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        btn_save.setOnClickListener {
            var title = et_title_plan.text.toString();
            var desc = et_description_plan.text.toString();

            if(title.isNullOrEmpty()){
                et_title_plan.error = "Silahkan masukan title"
                et_title_plan.requestFocus()
            }else if(desc.isNullOrEmpty()){
                et_description_plan.error = "Silahkan masukan description"
                et_description_plan.requestFocus()
            }else{
                val value = ContentValues().apply {
                    put(DbContract.DataEntry.COLUMN_TITLE, title)
                    put(DbContract.DataEntry.COLUMN_DESC, desc)
                }
                val selection = "${BaseColumns._ID} LIKE ?"
                val selectionArg = arrayOf(data.id.toString())
                val editRowId = db.update(DbContract.DataEntry.TABLE_NAME, value, selection, selectionArg)
                if(editRowId==-1){
                    Toast.makeText(this, "Data gagal diupdate", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Data telah diupdate", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

    }
}
package com.example.todoapp.database

import android.provider.BaseColumns

object DbContract {
    object DataEntry : BaseColumns{
        const val TABLE_NAME = "entry"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DESC = "desc"
    }
}
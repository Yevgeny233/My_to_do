package com.example.myapplication.db

import android.provider.BaseColumns

object MyDbNameClass : BaseColumns {
    const val TABLE_NAME = "name"
    const val NAME_TITLE = "title"
    const val NAME_SUBTITLE = "subtitle"
    const val NAME_IMAGE_URI = "uri"

    const val DATA_BASE_VERSION = 1
    const val DATA_BASE_NAME = "MyDb.db"

    const val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME(" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY,$NAME_TITLE TEXT, $NAME_SUBTITLE TEXT," +
            "$NAME_IMAGE_URI TEXT)"

    const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS ${TABLE_NAME}"

}
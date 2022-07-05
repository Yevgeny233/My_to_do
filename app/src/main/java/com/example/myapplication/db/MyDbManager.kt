package com.example.myapplication.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MyDbManager(val context: Context) {
    val myDbHelper = MyDbHelper(context)
    var db: SQLiteDatabase? = null

    fun openDb() {
        db = myDbHelper.writableDatabase
    }

    suspend fun insertToDb(title: String, subtitle: String, uri: String) =
        withContext(Dispatchers.IO) {
            val values = ContentValues().apply {
                put(MyDbNameClass.NAME_TITLE, title)
                put(MyDbNameClass.NAME_SUBTITLE, subtitle)
                put(MyDbNameClass.NAME_IMAGE_URI, uri)
            }
            db?.insert(MyDbNameClass.TABLE_NAME, null, values)
        }

    suspend fun updateToItem(title: String, subtitle: String, uri: String, id: Int) =
        withContext(Dispatchers.IO) {
            val selection = BaseColumns._ID + "=$id"
            val values = ContentValues().apply {
                put(MyDbNameClass.NAME_TITLE, title)
                put(MyDbNameClass.NAME_SUBTITLE, subtitle)
                put(MyDbNameClass.NAME_IMAGE_URI, uri)
            }
            db?.update(MyDbNameClass.TABLE_NAME, values, selection, null)
        }

    fun removeToDb(id: String) {
        val selection = BaseColumns._ID + "=$id"
        db?.delete(MyDbNameClass.TABLE_NAME, selection, null)
    }

    @SuppressLint("Range")
    suspend fun readDbData(searchText: String): ArrayList<ListItem> = withContext(Dispatchers.IO) {
        val dataList = ArrayList<ListItem>()
        var selection = "${MyDbNameClass.NAME_TITLE} like ?"
        val cursor = db?.query(
            MyDbNameClass.TABLE_NAME, null, selection,
            arrayOf("%$searchText%"), null, null, null
        )

        while (cursor?.moveToNext()!!) {
            val dataTitle = cursor.getString(cursor.getColumnIndexOrThrow(MyDbNameClass.NAME_TITLE))
            val dataSubtitle =
                cursor.getString(cursor.getColumnIndexOrThrow(MyDbNameClass.NAME_SUBTITLE))
            val dataUri =
                cursor.getString(cursor.getColumnIndexOrThrow(MyDbNameClass.NAME_IMAGE_URI))
            val dataId = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID))
            val item = ListItem()
            item.title = dataTitle
            item.description = dataSubtitle
            item.uri = dataUri
            item.id = dataId
            dataList.add(item)
        }
        cursor.close()

        return@withContext dataList
    }

    fun closeDb() {
        myDbHelper.close()
    }
}
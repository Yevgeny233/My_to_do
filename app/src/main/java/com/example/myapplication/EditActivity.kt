package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.db.IntentConst
import com.example.myapplication.db.MyDbManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class EditActivity : AppCompatActivity() {
    var isState = false
    val KEY_FOR_RESULT = 10
    var id = 0
    var imageUri = "empty"
    val myDbManager = MyDbManager(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        getIntends()
    }


    override fun onResume() {
        super.onResume()
        myDbManager.openDb()

    }

    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == KEY_FOR_RESULT) {
            findViewById<ImageView>(R.id.imageViwe).setImageURI(data?.data)
            imageUri = data?.data.toString()
            contentResolver.takePersistableUriPermission(
                data?.data!!,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

        }
    }

    fun onClickAddImage(view: View) {
        findViewById<View>(R.id.ImageLayout).visibility = View.VISIBLE
        findViewById<View>(R.id.addImage).visibility = View.GONE
    }

    fun onClickToDelete(view: View) {
        findViewById<View>(R.id.ImageLayout).visibility = View.GONE
        findViewById<View>(R.id.addImage).visibility = View.VISIBLE
        imageUri = "empty"
    }

    fun onClickToImage(view: View) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        startActivityForResult(intent, KEY_FOR_RESULT)
    }

    fun toSave(view: View) {
        val head = findViewById<EditText>(R.id.heading).text.toString()
        val descriptor = findViewById<EditText>(R.id.description).text.toString()
        if (head != "" && descriptor != "") {
            CoroutineScope(Dispatchers.Main).launch {
                if (isState == true) {
                    myDbManager.updateToItem(head, descriptor, imageUri, id)
                } else {
                    myDbManager.insertToDb(head, descriptor, imageUri)
                }
                finish()
            }

        }
    }

    fun getIntends() {
        val editButton = findViewById<FloatingActionButton>(R.id.editButton)
        editButton.visibility = View.GONE
        val heading = findViewById<EditText>(R.id.heading)
        val desc = findViewById<EditText>(R.id.description)
        val i = intent
        if (i != null) {
            if (i.getStringExtra(IntentConst.INT_TITLE_KEY) != null) {
                findViewById<View>(R.id.addImage).visibility = View.GONE
                heading.setText(i.getStringExtra(IntentConst.INT_TITLE_KEY))
                desc.setText(i.getStringExtra(IntentConst.INT_DESC_KEY))
                id = i.getIntExtra(IntentConst.INT_ID_KEY, 0)
                isState = true
                heading.isEnabled = false
                desc.isEnabled = false
                editButton.visibility = View.VISIBLE

                if (i.getStringExtra(IntentConst.INT_URI_KEY) != "empty") {
                    findViewById<View>(R.id.ImageLayout).visibility = View.VISIBLE
                    imageUri = i.getStringExtra(IntentConst.INT_URI_KEY)!!
                    findViewById<ImageView>(R.id.imageViwe).setImageURI(Uri.parse(imageUri))
                    findViewById<View>(R.id.imageButtonDelete).visibility = View.GONE
                    findViewById<View>(R.id.imageButtonToAdd).visibility = View.GONE
                }
            }
        }

    }

    fun editItem(view: View) {
        val heading = findViewById<EditText>(R.id.heading)
        val desc = findViewById<EditText>(R.id.description)
        heading.isEnabled = true
        desc.isEnabled = true
        findViewById<ImageButton>(R.id.editButton).visibility = View.VISIBLE
        findViewById<ImageButton>(R.id.addImage).visibility = View.VISIBLE
        if (imageUri == "empty") return
        findViewById<ImageButton>(R.id.imageButtonToAdd).visibility = View.VISIBLE
        findViewById<ImageButton>(R.id.imageButtonDelete).visibility = View.VISIBLE
    }
}
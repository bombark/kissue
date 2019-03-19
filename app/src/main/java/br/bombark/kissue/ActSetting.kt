package br.bombark.kissue

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.act_setting.*
import android.content.pm.PackageManager
import android.Manifest
import android.view.View
import android.view.Menu
import android.view.MenuItem

import java.net.HttpURLConnection
import java.net.URL


class ActSetting : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.act_setting)
		setSupportActionBar(toolbar)
		ask_permission()
	}


	fun ask_permission() {
		val permission = ContextCompat.checkSelfPermission(
			this, Manifest.permission.INTERNET
		)
		if (permission != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.INTERNET), 1);
		}
	}


	fun btn_save_onclick(view: View){
		//val queue = Volley.newRequestQueue(this)
		//val url = "http://www.google.com"
		val connection = URL("http://www.android.com/").openConnection() as HttpURLConnection
		val data = connection.inputStream.bufferedReader().readText()
	}


	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.menu_act_issue, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		val id = item.itemId
		if (id == R.id.action_settings) {
			this.finish()
			return true
		}
		return super.onOptionsItemSelected(item)
	}
}

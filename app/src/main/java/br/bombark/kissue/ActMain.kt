package br.bombark.kissue

import android.support.v4.content.ContextCompat
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity

import android.util.Log
import android.os.Bundle

import android.view.View
import android.view.Menu
import android.view.MenuItem

import android.Manifest
import android.content.pm.PackageManager
import android.content.Intent


class ActMain : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.act_main)
		//setSupportActionBar(toolbar)
		ask_permission()

		val db = FsDatabase()
		db.mkdir()
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


	fun btn_start_onclick(view: View) {
		val changePage = Intent(this, ActIssue::class.java)
		startActivity(changePage)
	}

	fun btn_setting_onclick(view: View) {
		val changePage = Intent(this, ActSetting::class.java)
		startActivity(changePage)
	}

	fun ask_permission() {
		val permission = ContextCompat.checkSelfPermission(
			this, Manifest.permission.READ_EXTERNAL_STORAGE
		)
		if (permission != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1);
		}
	}
}

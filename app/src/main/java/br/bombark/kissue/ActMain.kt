package br.bombark.kissue

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.util.Log
import android.content.pm.PackageManager
import android.Manifest
import android.support.v4.app.ActivityCompat


class ActMain : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.act_main)
		//setSupportActionBar(toolbar)
		ask_permission()
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

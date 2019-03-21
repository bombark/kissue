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
import android.widget.RadioGroup
import android.widget.RadioButton

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log

import java.net.HttpURLConnection
import java.net.URL




class ActSetting : AppCompatActivity() {
	var form_name = ""
	var text_formatting = ""

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.act_setting)
		setSupportActionBar(toolbar)
		//ask_permission()

		val sharedPref = this.getSharedPreferences("preferences",Context.MODE_PRIVATE);
		this.form_name = sharedPref.getString("form_name", "");


		val radiogroup_form = this.findViewById(R.id.radiogroup_forms) as RadioGroup
		val formpkg = FsDatabase().listForms()
		for ( form in formpkg ){
			Log.i("opa","form")
			var radio = RadioButton(this)
			radio.text = form
			if (form == this.form_name ){
				radio.setChecked(true)
			}
			radiogroup_form.addView(radio)
		}

		radiogroup_form.setOnCheckedChangeListener(
			RadioGroup.OnCheckedChangeListener { group, checkedId ->
				val radio:RadioButton = this.findViewById(checkedId)
				val form = radio.text.toString()
				val editor = sharedPref.edit();
				editor.putString("form_name", form);
				editor.commit();
			}
		)



		this.text_formatting = sharedPref.getString( "text_formatting", "UTF-8" );
		if ( this.text_formatting == "UTF-8" ){
			var radio = this.findViewById(R.id.radio_utf) as RadioButton
			radio.setChecked(true)
		} else {
			var radio = this.findViewById(R.id.radio_iso) as RadioButton
			radio.setChecked(true)
		}
		val radiogroup_text = this.findViewById(R.id.radiogroup_text) as RadioGroup
		radiogroup_text.setOnCheckedChangeListener(
			RadioGroup.OnCheckedChangeListener { group, checkedId ->
				val radio:RadioButton = this.findViewById(checkedId)
				val textformatting = radio.text.toString()
				val editor     = sharedPref.edit();
				editor.putString("text_formatting", textformatting);
				editor.commit();
			}
		)



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

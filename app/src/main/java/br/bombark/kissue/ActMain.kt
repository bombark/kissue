package br.bombark.kissue

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.Intent



class ActMain : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_main)
    }

    fun btn_start_onclick(view: View) {
        val changePage = Intent(this, ActIssue::class.java)
        startActivity(changePage)
    }
}

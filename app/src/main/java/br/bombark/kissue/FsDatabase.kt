package br.bombark.kissue

import java.io.File
import java.math.BigInteger
import java.security.MessageDigest



class FsDatabase {
	var results_path = "/sdcard/br.fgbombardelli.ktissue/results"
	var forms_path   = "/sdcard/br.fgbombardelli.ktissue/forms"

	fun mkdir(){
		val results = File(this.results_path)
		results.mkdirs()
		val forms = File(this.forms_path)
		forms.mkdirs()
	}

	fun addLine( result_id:String, line:String ){
		val file = File(this.results_path+"/form-"+result_id+".csv")
		file.appendText(line+"\n")
	}

	fun existsResult( result_id:String ) : Boolean {
		val file = File( this.results_path+"/"+result_id )
		return file.exists()
	}

	fun calcMd5( raw:String ) : String {
		val md = MessageDigest.getInstance("MD5")
		return BigInteger(1, md.digest(raw.toByteArray())).toString(16).padStart(32, '0')
	}
}

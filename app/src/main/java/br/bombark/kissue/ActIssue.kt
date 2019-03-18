package br.bombark.kissue

import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup

import kotlinx.android.synthetic.main.act_issue.*
import java.io.File
import org.yaml.snakeyaml.Yaml
import android.widget.EditText
import android.widget.TextView
import android.text.InputType
import android.widget.RadioGroup
import android.widget.RadioButton
import android.widget.CheckBox
import android.widget.LinearLayout
import android.content.res.AssetManager


import java.io.IOException
import java.io.StringReader
import java.io.InputStream
import java.io.ByteArrayInputStream



import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.math.BigInteger
import java.security.MessageDigest



class ActIssue : AppCompatActivity() {

	/**
	 * The [android.support.v4.view.PagerAdapter] that will provide
	 * fragments for each of the sections. We use a
	 * {@link FragmentPagerAdapter} derivative, which will keep every
	 * loaded fragment in memory. If this becomes too memory intensive, it
	 * may be best to switch to a
	 * [android.support.v4.app.FragmentStatePagerAdapter].
	 */
	private lateinit var section: SectionsPagerAdapter
	private var form : ArrayList< Any > = ArrayList()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.act_issue)

		setSupportActionBar(toolbar)
		this.section = SectionsPagerAdapter(supportFragmentManager)
		container.adapter = this.section

		fab.setOnClickListener { //view ->
			//this.section.setCurrentItem(0);
			if ( this.section.check() )
				this.section.save()
			//Snackbar.make(view, "Obrigado!", Snackbar.LENGTH_LONG).setAction("Action", null).show()
		}

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



	/**
	 * A [FragmentPagerAdapter] that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

		private var form : ArrayList< Any > = ArrayList()
		private var issuepkg : ArrayList< Issue? > = ArrayList()
		private var form_md5 = ""

		init {
			val manager = getAssets();
			val ims = manager.open("form.yml");
			val raw = ims.readBytes().toString(Charsets.UTF_8)
			//var raw = File("/sdcard/form_cini.yml").readBytes().toString(Charsets.UTF_8)
			val yaml = Yaml()
			this.form = yaml.load(raw)

			this.form_md5 = calcMd5(raw)
			Log.i("opa",this.form_md5)

			for (i in 0..this.form.size){
				issuepkg.add(null)
			}
		}

		override fun getItem(id_issue: Int): Fragment {
			//IssueText.newInstance(position + 1)
			val question = this.getQuestion(id_issue)
			val klass = question["class"].toString().toLowerCase()

			/*val args1 = Bundle()
			args1.putInt   ( "id", position )
			args1.putString( "class", klass )
			args1.putString( "title", question["title"].toString() )*/

			if ( klass == "text" || klass == "int" || klass == "date" ) {
				val f1 = IssueText(id_issue, question); //f1.setArguments(args1)
				issuepkg[id_issue] = f1
				return f1

			} else if ( klass == "boolean" ){
				val f1 = IssueBool(id_issue, question); //f1.setArguments(args1)
				issuepkg[id_issue] = f1
				return f1

			} else if ( klass == "enum" ){
				val f1 = IssueEnum(id_issue, question); //f1.setArguments(args1)
				issuepkg[id_issue] = f1
				return f1

			} else if ( klass == "checkbox" ){
				val f1 = IssueCheckbox(id_issue, question); //f1.setArguments(args1)
				issuepkg[id_issue] = f1
				return f1

			} else {
				val f1 = IssueError(id_issue, question); //f1.setArguments(args1)
				issuepkg[id_issue] = f1
				return f1
			}
		}

		override fun getCount(): Int {
			return this.form.size
		}

		fun getQuestion(i:Int) : LinkedHashMap<String,Any> {
			return this.form[i] as LinkedHashMap<String,Any>
		}

		fun check() : Boolean {
			Log.i("opa", this.issuepkg.size.toString())
			for ( issue in this.issuepkg ){
				val answer = issue?.getAnswer() ?: ""
				Log.i("opa", answer)
			}
			Log.i("opa","fim")
			return true
		}

		fun getAnswer() : String {
			var res = ""
			var is_first = true
			for ( issue in this.issuepkg ){
				val answer = issue?.getAnswer() ?: ""
				if ( is_first ){
					is_first = false
				} else {
					res += ';'
				}
				res += answer
			}
			return res
		}

		fun save() {
			val linha = this.getAnswer()
			Log.i("opa",linha)
			Log.i("opa","salvando!!!!!")

			val results = File("/sdcard/br.fgbombardelli.ktissue/results")
			results.mkdirs()

			File(results, "form-"+this.form_md5+".csv" ).appendText(linha)
		}

		/*public CharSequence getPageTitle(int position) {
        	return "Page " + position;
        }*/
	}


	abstract class Issue(var issue_id:Int, var form:LinkedHashMap<String,Any>) : Fragment() {
		var klass     = ""
		var title     = ""
		var is_started = false
		lateinit var v_root:ViewGroup
		//lateinit var v_title:TextView


		init {
			//this.issue_id  = arguments?.getInt("id")  ?: 0
			//val fm         = getFragmentManager() as SectionsPagerAdapter
			//this.form      = fm.getQuestion( this.issue_id )
			this.klass   = this.form["class"].toString().toLowerCase()
			this.title   = this.form["title"].toString()
		}

		open fun getAnswer():String {
			return ""
		}

		override fun onSaveInstanceState(outState: Bundle) {
			super.onSaveInstanceState(outState)
			var answer = this.getAnswer()
			outState.putString( "value", answer )
		}


	}



	class IssueText(issue_id:Int, form:LinkedHashMap<String,Any>) : Issue(issue_id, form) {
		lateinit var v_input:EditText


		override fun onCreateView(
			inflater: LayoutInflater, container: ViewGroup?, save: Bundle?
		): View? {
			val rootView = inflater.inflate(R.layout.issue_text, container, false) as ViewGroup
			val v_title:TextView = rootView.findViewById(R.id.title);
			v_title.text = this.id.toString() + ". " + title

			this.v_input = rootView.findViewById(R.id.input);
			if ( this.klass == "int" ){
				this.v_input?.setInputType(InputType.TYPE_CLASS_NUMBER)
			} else if ( this.klass == "date" ){
				this.v_input?.setInputType(InputType.TYPE_CLASS_DATETIME)
			} else if ( this.klass == "phone" ){
				this.v_input?.setInputType(InputType.TYPE_CLASS_PHONE)
			} else if ( this.klass == "email" ){
				this.v_input?.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
			}

			return rootView
		}

		override fun getAnswer():String {
			return this.v_input.text.toString()
		}
	}





	class IssueBool(issue_id:Int, form:LinkedHashMap<String,Any>) : Issue(issue_id, form) {

		override fun onCreateView(
			inflater: LayoutInflater, container: ViewGroup?,
			savedInstanceState: Bundle?
		): View? {
			if ( this.is_started ){
				return this.v_root
			}

			this.v_root = inflater.inflate(R.layout.issue_bool, container, false) as ViewGroup
			val v_title:TextView = this.v_root.findViewById(R.id.title);
			v_title.text = this.issue_id.toString() + ". " + title

			this.is_started = true
			return this.v_root
		}

		override fun getAnswer():String {
			val radio_sim = this.v_root.findViewById(R.id.radio_sim) as RadioButton;
			if ( radio_sim.isChecked() )
				return "sim"
			val radio_nao = this.v_root.findViewById(R.id.radio_nao) as RadioButton;
			if ( radio_nao.isChecked() )
				return "nao"
			return ""
		}
	}



	class IssueEnum(issue_id:Int, form:LinkedHashMap<String,Any>) : Issue(issue_id, form) {
		var answer = ArrayList<RadioButton>()

		override fun onCreateView(
			inflater: LayoutInflater, container: ViewGroup?, save: Bundle?
		): View? {
			if ( this.is_started == true ){
				return this.v_root
			}

			this.v_root = inflater.inflate(R.layout.issue_enum, container, false) as ViewGroup
			val v_title:TextView   = this.v_root.findViewById(R.id.title);
			val v_group:RadioGroup = this.v_root.findViewById(R.id.radiogroup);

			val box = this.form["box"] as ArrayList<Any>
			for (_item in box){
				val item = _item as LinkedHashMap<String,Any>
				val radio = RadioButton( getActivity() );
				radio.setText( item["title"].toString() );
				answer.add(radio)
				v_group.addView(radio);
			}

			v_title.text = this.issue_id.toString() + ". " + title

			this.is_started = true
			return this.v_root
		}

		override fun getAnswer():String {
			for (radio in this.answer){
				if ( radio.isChecked() ){
					return radio.text.toString()
				}
			}
			return ""
		}
	}



	class IssueCheckbox(issue_id:Int, form:LinkedHashMap<String,Any>) : Issue(issue_id, form) {
		lateinit var rootView:ViewGroup
		//lateinit var v_title:TextView
		var answer = ArrayList<CheckBox>()

		override fun onCreateView(
			inflater: LayoutInflater, container: ViewGroup?, save: Bundle?
		): View? {
			if ( this.is_started == true ){
				return this.rootView
			}

			this.rootView = inflater.inflate(R.layout.issue_checkbox, container, false) as ViewGroup

			val v_title  = rootView.findViewById(R.id.title) as TextView;
			v_title.text = issue_id.toString() + ". " + title

			val v_group:LinearLayout = rootView?.findViewById(R.id.group)
			val box = this.form["box"] as ArrayList<Any>
			for (_item in box){
				val item = _item as LinkedHashMap<String,Any>
				val check = CheckBox( getActivity() );
				check.setText( item["title"].toString() );
				answer.add(check)
				v_group.addView(check);
			}
			this.is_started = true

			return this.rootView
		}


		override fun getAnswer():String {
			var is_first = true
			var res = ""
			for (check in this.answer){
				if ( check.isChecked() ){
					if ( is_first ){
						is_first = false
					} else {
						res += ","
					}
					res += check.text.toString()
				}
			}
			return res
		}
	}



	class IssueError(issue_id:Int, form:LinkedHashMap<String,Any>) : Issue(issue_id, form) {

		override fun onCreateView(
			inflater: LayoutInflater, container: ViewGroup?, save: Bundle?
		): View? {
			val rootView = inflater.inflate(R.layout.issue_error, container, false) as ViewGroup
			val v_title:TextView = rootView.findViewById(R.id.title);
			v_title.text = this.id.toString() + ". " + title
			return rootView
		}

	}



	fun calcMd5(data:String): String {
		val md = MessageDigest.getInstance("MD5")
		return BigInteger(1, md.digest(data.toByteArray())).toString(16).padStart(32, '0')
	}
}





/*companion object {
	private val ARG_SECTION_NUMBER = "section_number"
	fun newInstance(sectionNumber: Int): Issue {
		val fragment = IssueText()
		val args = Bundle()
		args.putInt(ARG_SECTION_NUMBER, sectionNumber)
		fragment.arguments = args
		return fragment
	}
}*/

package br.bombark.kissue

import android.content.Context
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup

import kotlinx.android.synthetic.main.act_issue.*
import kotlinx.android.synthetic.main.formulario.view.*
import java.io.File
import org.yaml.snakeyaml.Yaml
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView


class ActIssue : AppCompatActivity() {

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var form : ArrayList< Any > = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_issue)

        setSupportActionBar(toolbar)
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        container.adapter = mSectionsPagerAdapter

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

    }

    override fun onPause() {
        super.onPause();
        Log.i("opa", "oaaaappaaaaa")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.i("opa", "oaaaappaaaaa")
        super.onSaveInstanceState(outState)
        outState.putInt("value", 10)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_act_issue, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_settings) {
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

        init {
            var raw = File("/sdcard/form_cini.yml").readBytes().toString(Charsets.UTF_8)
            val yaml = Yaml()
            this.form = yaml.load(raw)
        }

        override fun getItem(position: Int): Fragment {
            val f1 = PlaceholderFragment.newInstance(position + 1)
            val question = this.getQuestion(position)
            val args1 = Bundle()
            args1.putInt   ( "page_id", position+1 )
            args1.putString( "class", question["class"].toString() )
            args1.putString( "title", question["title"].toString() )
            f1.setArguments(args1)
            return f1
        }




        override fun getCount(): Int {
            return this.form.size
        }

        fun getQuestion(i:Int) : LinkedHashMap<String,Any> {
            return this.form[i] as LinkedHashMap<String,Any>
        }

    }



    /**
     * A placeholder fragment containing a simple view.
     */
    class PlaceholderFragment : Fragment() {

        var json_raw = ""

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val rootView = inflater.inflate(R.layout.formulario, container, false) as ViewGroup

            val act = this.activity as ActIssue;
            val page_id = arguments?.getInt("page_id")
            val klass   = arguments?.getString("class")
            val title   = arguments?.getString("title")

            //val text = page_id.toString() + ". " + title
            //rootView.section_label.text = text//getString(R.string.section_format, arguments?.getInt(ARG_SECTION_NUMBER))


            val lbl_title = TextView(act)
            lbl_title.text = page_id.toString() + ". " + title
            //lbl_title.setPadding(0,0,0,0)
            val edit_answer = EditText( act )


            val ll = LinearLayout( act )
            ll.setPadding(10,10,10,10)
            ll.orientation = LinearLayout.VERTICAL

            ll.addView(lbl_title)
            ll.addView(edit_answer)
            rootView.addView(ll)

            return rootView


        }





        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private val ARG_SECTION_NUMBER = "section_number"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }
}

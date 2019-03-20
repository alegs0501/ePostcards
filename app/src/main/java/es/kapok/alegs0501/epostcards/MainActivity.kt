package es.kapok.alegs0501.epostcards

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.WindowManager
import es.kapok.alegs0501.epostcards.dao.PostcardDBAdapter
import es.kapok.alegs0501.epostcards.data.BackListAdapter
import es.kapok.alegs0501.epostcards.data.PostcardListAdapter
import es.kapok.alegs0501.epostcards.models.Postcard
import es.kapok.alegs0501.epostcards.models.PostcardReference

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private val mDBAdapter = PostcardDBAdapter(this)
    private var postcardList = ArrayList<Postcard>()

    private var adapter: PostcardListAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        //Hiding status bar
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        postcardList = mDBAdapter.selectAllPostcards()

        layoutManager = LinearLayoutManager(this)
        adapter = PostcardListAdapter(postcardList, this)

        mainRecyclerView.layoutManager = LinearLayoutManager(this, OrientationHelper.VERTICAL, false)
        mainRecyclerView.adapter = adapter

        fab.setOnClickListener { view ->
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }

        //Card view listener
        adapter!!.setOnclickListener(object: PostcardListAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(this@MainActivity, ViewPostcardActivity::class.java)
                PostcardReference.front = postcardList[position].front
                PostcardReference.back = postcardList[position].back
                startActivity(intent)
            }
        })


    }

}

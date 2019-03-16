package es.kapok.alegs0501.epostcards

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.WindowManager
import es.kapok.alegs0501.epostcards.data.BackListAdapter
import kotlinx.android.synthetic.main.activity_back_list.*

class BackListActivity : AppCompatActivity() {

    private var adapter: BackListAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    private val backList = arrayListOf(R.drawable.post_1, R.drawable.post_2,
                                        R.drawable.post_3, R.drawable.post_4)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_back_list)

        //Hiding status bar
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        layoutManager = LinearLayoutManager(this)
        adapter = BackListAdapter(backList, this)

        backRecyclerView.layoutManager = LinearLayoutManager(this, OrientationHelper.VERTICAL, false)
        backRecyclerView.adapter = adapter
    }
}

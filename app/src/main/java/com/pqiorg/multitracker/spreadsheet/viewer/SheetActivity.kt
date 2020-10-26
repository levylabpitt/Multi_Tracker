package com.pqiorg.multitracker.spreadsheet.viewer
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.pqiorg.multitracker.R
import com.synapse.Utility
import com.unwrappedapps.android.spreadsheet.ui.sheet.SheetFragment
import com.unwrappedapps.android.spreadsheet.ui.sheet.SheetLayoutManager
import com.unwrappedapps.android.spreadsheet.ui.sheet.SheetViewModel
import com.unwrappedapps.android.spreadsheet.ui.sheet.SheetViewPager
import kotlinx.android.synthetic.main.tab_activity.*


class SheetActivity : AppCompatActivity() {

    lateinit var mPager: SheetViewPager
    var mSpreadsheetStatePagerAdapter: SpreadsheetStatePagerAdapter? = null

    var tabCount = 1

    var selectedTab = 0

    val MY_REQ_READ_EXTERNAL_STORAGE : Int = 93 // random magic number



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.tab_activity)

        tabSetup()

        val viewModel = ViewModelProviders.of(this).get(SheetViewModel::class.java)

        viewModel.sheetLoadState.observe(this, Observer {
            postSpreadsheetLoad()
        })
        checkViewIntents2()
      //  checkViewIntents(intent)

    }
    fun checkViewIntents2(){
        var uri:Uri=  Uri.fromFile(Utility.getPathWorkbook(this))
        handleViewIntent(uri)
    }
    fun checkViewIntents(intent: Intent?) {
        if (intent == null) return
        val action = intent.action
        if (Intent.ACTION_VIEW.equals(action)) {
            val type = intent.type
            if (type != null) {
                val uri = intent.data
                if (uri != null) {
                    val csvMime = "text/comma-separated-values"
                    val odsMime = "application/vnd.oasis.opendocument.spreadsheet"
                    val xlsMime = "application/vnd.ms-excel"
                    val xlsxMime = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"

                    if (type.equals(csvMime)) {
                        handleViewIntent(uri)
                    } else if (type.equals(odsMime)) {
                        handleViewIntent(uri)
                    } else if (type.equals(xlsMime) || type.equals(xlsxMime)) {
                        handleViewIntent(uri)
                    }
                }
            }
        }
        // else if (Intent.ACTION_SEARCH.equals(action))
    }

    fun handleViewIntent(uri: Uri) {

        val viewModel = ViewModelProviders.of(this).get(SheetViewModel::class.java)
        viewModel.processUri(uri, contentResolver)
        postSpreadsheetLoad()
    }

    private fun tabSetup() {

        val numberOfTabs = tabCount

        val tabLayout = findViewById<View>(R.id.tab_layout) as TabLayout
        val tab = arrayOfNulls<TabLayout.Tab>(numberOfTabs)

        for (i in 0 until numberOfTabs) {
            tab[i] = tabLayout.newTab()
        }

        for (i in 0 until numberOfTabs) {
            tabLayout.addTab(tab[i]!!)
        }

        // create Fragment Pager Adapter
        //FragmentManager.enableDebugLogging(true);

        mSpreadsheetStatePagerAdapter = SpreadsheetStatePagerAdapter(
            supportFragmentManager
        )

        mPager = view_pager

        // attach adapter to viewpager
        mPager.setAdapter(mSpreadsheetStatePagerAdapter)

        // attach viewpager to tab layout
        tabLayout.setupWithViewPager(mPager)

        // action to do when page changed
        setViewPagerListener(mPager)
    }

    fun postSpreadsheetLoad() {
        val viewModel = ViewModelProviders.of(this).get(SheetViewModel::class.java)

        val spreadsheet = viewModel.spreadsheet.value

        if (spreadsheet != null) {

            val tableCount = spreadsheet.workbook.sheetList.size

            val list = mutableListOf<String>()

            for (i in 0 until tableCount) {
                val name = spreadsheet.workbook.sheetList[i].name
                list.add(name)
            }

            val numberOfTabs = tableCount

            val tabLayout = findViewById<View>(R.id.tab_layout) as TabLayout
            val tab = arrayOfNulls<TabLayout.Tab>(numberOfTabs)

            mSpreadsheetStatePagerAdapter?.setNames(list)

            mSpreadsheetStatePagerAdapter?.notifyDataSetChanged()

            tabLayout.removeAllTabs()

            for (i in 0 until numberOfTabs) {
                val name = spreadsheet.workbook.sheetList[i].name
                val newTab = tabLayout.newTab()
                newTab.setText(name)
                tab[i] = newTab
            }

            for (i in 0 until numberOfTabs) {
                tabLayout.addTab(tab[i]!!)
            }

            tabCount = numberOfTabs


            mSpreadsheetStatePagerAdapter?.notifyDataSetChanged()
        }


    }

    private fun setViewPagerListener(mPager: SheetViewPager) {

        mPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageSelected(position: Int) {
                tabSelected(position)
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun tabSelected(position: Int) {
        selectedTab = position

        val viewModel = ViewModelProviders.of(this).get(SheetViewModel::class.java)
        viewModel.topRow = 1
        viewModel.leftColumn = 1
        SheetLayoutManager.topRow = 1
        SheetLayoutManager.leftColumn = 1

        val spreadsheet = viewModel.spreadsheet.value

        spreadsheet?.workbook?.currentSheet = selectedTab

        val frag = mSpreadsheetStatePagerAdapter?.instantiateItem(mPager, mPager.currentItem) as SheetFragment

        val sheetAdapter = frag.fragmentRecyclerView.adapter as SheetAdapter


        sheetAdapter.notifyDataSetChanged()

    }






    fun load() {

        val viewModel = ViewModelProviders.of(this).get(SheetViewModel::class.java)
        viewModel.topRow = 1
        viewModel.leftColumn = 1
        SheetLayoutManager.topRow = 1
        SheetLayoutManager.leftColumn = 1

        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }
        startActivityForResult(intent, MY_REQ_READ_EXTERNAL_STORAGE)
    }

    fun clearSelectBox() {
        select.text = ""
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == MY_REQ_READ_EXTERNAL_STORAGE) {
                clearSelectBox()
                data?.data?.also { uri ->

                    val frag = mSpreadsheetStatePagerAdapter?.instantiateItem(
                        mPager,
                        mPager.currentItem
                    ) as SheetFragment
                    frag.processUri()

                    val viewModel = ViewModelProviders.of(this).get(SheetViewModel::class.java)
                    viewModel.processUri(uri, contentResolver)
                }
            }
        }
    }




    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        checkViewIntents(intent)
    }










    fun getSheetString() : String {
        return getString(R.string.sheet)
    }




    inner class SpreadsheetStatePagerAdapter(fragmentManager: FragmentManager) :
            FragmentStatePagerAdapter(fragmentManager) {

        var nameList : MutableList<String> = mutableListOf()

        fun setNames(list: MutableList<String>) {
            nameList = list
        }

        override fun getItem(position: Int): Fragment {

            val fragment = SheetFragment.newInstance()

            /*
            fragment.arguments = Bundle().apply {
                putInt("num", position)
            }
            */

            return fragment
        }

        override fun getPageTitle(position: Int): CharSequence? {

            val titlePos = position + 1

            if (nameList.size > position) {

                val n = nameList[position]
                return n
            } else {
                val sheetString = getSheetString()
                return "$sheetString $titlePos"
            }
        }

        override fun getCount(): Int {
            return tabCount
        }

    }

    fun showFromInternal(){
        val frag = mSpreadsheetStatePagerAdapter?.instantiateItem(mPager, mPager.currentItem) as SheetFragment
        frag.processUri()

        val viewModel = ViewModelProviders.of(this).get(SheetViewModel::class.java)

        var uri:Uri= Utility.getPathWorkbook(this).absolutePath.toUri()     /////nks
        viewModel.processUri(uri, contentResolver)
    }

}
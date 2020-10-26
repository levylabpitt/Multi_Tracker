package com.pqiorg.multitracker.spreadsheet.viewer

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.pqiorg.multitracker.drive.FullscreenImageViewDrive2
import com.google.android.material.snackbar.Snackbar
import com.pqiorg.multitracker.R
import com.pqiorg.multitracker.qr_scanner.intent_service.UpdateWebRequestService
import com.pqiorg.multitracker.spreadsheet.viewer.spreadsheet.Spreadsheet
class SheetAdapter(val density: Int, var select: TextView?, var context: Context?) :
    RecyclerView.Adapter<SheetAdapter.ViewHolder>() {


    companion object {
        private var spreadsheet: Spreadsheet? = null

        private const val CELL = 3
        private const val COLUMN_MARKER = 2
        private const val ROW_MARKER = 1

        //30 can do 3 digits
        //36 can do 4 digits
        //private val ROW_MARKER_WIDTH = 45
        private const val ROW_MARKER_WIDTH = 70
        // private const val ROW_MARKER_WIDTH = 50

        private const val ROW_HEIGHT = 60
    }

    init {
        select?.text = ""
    }

    fun assignSpreadsheet(ss: Spreadsheet?) {
        spreadsheet = ss
    }

    override fun getItemViewType(position: Int): Int {

        val coordinate = posToMarkers(position)

        return if (coordinate.r == 0) {
            COLUMN_MARKER
        } else if (coordinate.c == 0) {
            ROW_MARKER
        } else {
            CELL
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        // Create a new view.
        // could change markers by viewType here if necessary
        val view: View

        view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.graph_cell, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        var items: Int

        items = 2147483647 // biggest int I can make
        items--
        items--
        return items
    }

    // what to do when position is bound to viewholder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (getItemViewType(position) == CELL) {
            makeCell(holder, position)
        } else if (getItemViewType(position) == COLUMN_MARKER) {
            makeColumnMarker(holder, position)
        } else {
            makeRowMarker(holder, position)
        }

    }


    private fun makeCell(viewHolder: ViewHolder, position: Int) {

        val workbook = spreadsheet?.workbook
        val sheet = workbook?.sheetList?.get(workbook.currentSheet)

        val numColumns = sheet?.getNumberOfColumns()

        val coordinate = posToMarkers(position)
        var r = coordinate.r
        var c = coordinate.c

        r--
        c--

        val cellsValue: String?

        if (numColumns == null) return

        if (numColumns < 1) {
            cellsValue = " "
        } else {
            cellsValue = sheet.getRow(r).getCell(c).cellValue
        }



        viewHolder.textView.text = cellsValue

        if (sheet.columnWidths.size > c) {
            val baseWidth = sheet.columnWidths[c]
            val denseWidth = baseWidth * density
            viewHolder.textView.width = denseWidth
        } else {
            viewHolder.textView.width = ROW_MARKER_WIDTH * density * 2
        }

        val denseHeight = density * sheet.getRow(r).height

        viewHolder.textView.height = denseHeight

        //val alpha = 0;
        val alpha = 255

        val sdk = android.os.Build.VERSION.SDK_INT
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            viewHolder.textView.setBackgroundDrawable(
                ColorDrawable(
                    Color.argb(
                        alpha,
                        255,
                        255,
                        255
                    )
                )
            )

        } else {
            viewHolder.textView.setBackground(ColorDrawable(Color.argb(alpha, 255, 255, 255)))
        }

        viewHolder.textView.setOnClickListener {
            select?.text = cellsValue

            if (cellsValue.isNullOrBlank()) return@setOnClickListener
            var fullImageLink: String = cellsValue

            if (fullImageLink.contains("https://lh3.googleusercontent.com")) {  // thumb

                if (fullImageLink.contains(",")) {
                    var s = fullImageLink.split(",")
                    var driveParentId = s[1];
                    var driveFileId = s[2];
                    val newIntent = Intent(context, FullscreenImageViewDrive2::class.java)
                    newIntent.putExtra("driveParentId", driveParentId)
                    newIntent.putExtra("driveFileId", driveFileId)
                    newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context?.startActivity(newIntent)
                }

            }else if(cellsValue.contains("Parent_Id")){

                if (cellsValue.contains(",")) {
                    var s = cellsValue.split(",")

                    var driveParentId = s[0];
                    if( driveParentId.contains("=")){
                        var a= driveParentId.split("=");
                        driveParentId=a[1];
                    }


                    var driveFileId = s[1];
                    if( driveFileId.contains("=")){
                        var a=  driveFileId.split("=");
                        driveFileId=a[1];
                    }


                    val newIntent = Intent(context, FullscreenImageViewDrive2::class.java)
                    newIntent.putExtra("driveParentId", driveParentId)
                    newIntent.putExtra("driveFileId", driveFileId)
                    newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context?.startActivity(newIntent)
                }
            }


            else if (cellsValue.equals("In Progress") || cellsValue.equals("Completed")) {
                val timestamp= sheet.getRow(r).getCell(1).cellValue;
                showAppCompatDialog(timestamp)
            } else {



                val snackbar = Snackbar
                    .make(viewHolder.textView, cellsValue, Snackbar.LENGTH_LONG)
                    .setAction("OK") {


                    }
                    .show()
            }


        }
    }
     fun showAppCompatDialog(timestamp:String) {



        val alertDialog = AlertDialog.Builder(context)
            .setMessage("Do you want to resume uploading this data?")
            .setTitle("Notice")
            .setPositiveButton("Continue") { arg0, arg1 ->
                Toast.makeText(context, "Uploading started in background...", Toast.LENGTH_SHORT).show()
                val msgIntent = Intent(context, UpdateWebRequestService::class.java)
                msgIntent.putExtra(UpdateWebRequestService.QRTimestamp, timestamp)
                context?.startService(msgIntent)
            }
            .setNegativeButton("Cancel") { arg0, arg1 -> }
            .show()
    }

    private fun makeRowMarker(viewHolder: ViewHolder, position: Int) {

        var (row, _) = posToMarkers(position)

        row--
        val workbook = spreadsheet?.workbook
        val sheet = workbook?.sheetList?.get(workbook.currentSheet)
        val height = sheet?.getRow(row)?.height ?: ROW_HEIGHT
        val denseHeight = density * height

        viewHolder.textView.height = denseHeight
        setBackground(viewHolder)

        val rm = Integer.toString(row + 1)

        val width = ROW_MARKER_WIDTH * density

        viewHolder.textView.height = density * height


        viewHolder.textView.setWidth(width)
        //viewHolder.textView.setWidth(width)
        viewHolder.textView.setText(rm)
        viewHolder.textView.setGravity(Gravity.CENTER)
    }


    // must be wide enough for 2-3 letter column markers
    private fun makeColumnMarker(viewHolder: ViewHolder, position: Int) {

        val (_, c) = posToMarkers(position)

        val workbook = spreadsheet?.workbook
        val sheet = workbook?.sheetList?.get(workbook.currentSheet)

        val size = sheet?.columnWidths?.size

        if (position == 0) {
            viewHolder.textView.width = ROW_MARKER_WIDTH * density

        } else {
            if (size != null) {
                if (size >= c) {
                    val baseWidth = sheet.columnWidths[c - 1]
                    val denseWidth = baseWidth * density
                    viewHolder.textView.width = denseWidth
                } else {
                    viewHolder.textView.width = ROW_MARKER_WIDTH * density * 2
                }
            } else {
                viewHolder.textView.width = ROW_MARKER_WIDTH * density * 2
            }
        }

        setBackground(viewHolder)

        val lastPosition = (c - 1) % 26

        var text = getLetter(lastPosition)

        if (c > 26) {
            var before = (c - 1) / 26
            before--

            if (before >= 26) {
                before = before % 26
            }

            val beforeLast = getLetter(before)
            text = beforeLast + text

        } else if (position == 0) {
            text = " "
        }


        if (c > 26 * (26 + 1)) {
            var ch = c
            ch = ch - 26
            ch = ch - 1
            ch = ch / 26
            ch = ch / 26
            ch = ch - 1

            val beforebeforeLast = getLetter(ch)
            text = beforebeforeLast + text

        } else if (position == 0) {
            text = " "
        }

        viewHolder.textView.setGravity(Gravity.CENTER)
        viewHolder.textView.setText(text)
    }


    private fun setBackground(viewHolder: ViewHolder) {

        //val gray = 84
        val gray = 193

        val sdk = android.os.Build.VERSION.SDK_INT
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            //setBackgroundDrawable();
            viewHolder.textView.setBackgroundDrawable(
                ColorDrawable(
                    Color.argb(
                        255,
                        gray,
                        gray,
                        gray
                    )
                )
            )

        } else {
            viewHolder.textView.setBackground(ColorDrawable(Color.argb(255, gray, gray, gray)))

        }
    }

    private fun getLetter(lastPosition: Int): String {

        val text: String

        when (lastPosition) {
            0 -> text = "A"
            1 -> text = "B"
            2 -> text = "C"
            3 -> text = "D"
            4 -> text = "E"
            5 -> text = "F"
            6 -> text = "G"
            7 -> text = "H"
            8 -> text = "I"
            9 -> text = "J"
            10 -> text = "K"
            11 -> text = "L"
            12 -> text = "M"
            13 -> text = "N"
            14 -> text = "O"
            15 -> text = "P"
            16 -> text = "Q"
            17 -> text = "R"
            18 -> text = "S"
            19 -> text = "T"
            20 -> text = "U"
            21 -> text = "V"
            22 -> text = "W"
            23 -> text = "X"
            24 -> text = "Y"
            25 -> text = "Z"
            else -> text = " "
        }
        return text
    }


    // translate position to column and row
    private fun posToMarkers(pos: Int): Coordinate {
        val diff: Int
        val column: Int

        var p = pos
        p++
        p = p * 2
        val r = Math.floor(Math.sqrt(p.toDouble()))
        val w = r.toInt()

        val tryit = (w + 1) * w / 2

        if (tryit > pos) {
            val low = (w - 1) * w / 2
            diff = low - pos
            column = w - 1 + diff
        } else {
            diff = tryit - pos
            column = w + diff
        }

        val row = diff * -1

        return Coordinate(row, column)
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView

        init {
            textView = view.findViewById(R.id.textView)
        }
    }

    private data class Coordinate(val r: Int, val c: Int)

}

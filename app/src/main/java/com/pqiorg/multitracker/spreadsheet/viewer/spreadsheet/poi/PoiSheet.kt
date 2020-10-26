package com.unwrappedapps.android.spreadsheet.spreadsheet.poi

import com.synapse.Utility
import com.unwrappedapps.android.spreadsheet.spreadsheet.Cell
import com.unwrappedapps.android.spreadsheet.spreadsheet.Row
import com.unwrappedapps.android.spreadsheet.spreadsheet.Sheet as MySheet

class PoiSheet() : com.unwrappedapps.android.spreadsheet.spreadsheet.Sheet() {

    lateinit var pSheet: org.apache.poi.ss.usermodel.Sheet

    constructor(sheet: org.apache.poi.ss.usermodel.Sheet) : this() {
        pSheet = sheet

        name = pSheet.sheetName

        val numberOfRows = sheet.physicalNumberOfRows

        // XXX: should this not match the row height divisor?
        //val magicWidthDiv = 10
        val magicWidthDiv = 15

        var max: Short = 0
        var currentMax: Short

        for (i in 0 until numberOfRows) {
            val pRow = pSheet.getRow(i)

            // TODO: decide if should be skipped or a blank one
            if (pRow != null) {
                val row = PoiRow(pRow)
                rowList.add(row)
                currentMax = pRow.lastCellNum
                if (currentMax > max) max = currentMax

                /********************/
                for (j in 0 until pRow.physicalNumberOfCells) {
                    var cellCharLength = pRow.getCell(j).stringCellValue.length
                    if (i == 0) {
                        columnWidths.add(cellCharLength)
                    } else {
                        try {
                            if (cellCharLength > columnWidths[j]) columnWidths[j] = cellCharLength
                        }catch (e: Exception){
                            Utility.ReportNonFatalError("PoiSheet",e.message);
                        }
                    }
                }
                /********************/

            }
        }


        /* for (i in 0 until max) {
             columnWidths.add(pSheet.getColumnWidth(i) / magicWidthDiv)
         }*/

        for (i in 0 until columnWidths.size) {
            columnWidths[i] = ((columnWidths[i] * 1.14388)).toInt() * 20;
        }


    }
}

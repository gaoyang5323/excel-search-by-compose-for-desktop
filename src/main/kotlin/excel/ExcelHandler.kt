package excel

import com.alibaba.excel.EasyExcel
import com.alibaba.excel.context.AnalysisContext
import com.alibaba.excel.metadata.data.ReadCellData
import com.alibaba.excel.read.listener.ReadListener
import sqlite.ExcelDataPo
import sqlite.SqliteUtil
import java.io.File

class ExcelHandler : ReadListener<HashMap<Any, Any>> {

    //表格索引
    var sheetIndex = 1
    var rowIndex = 1;

    lateinit var fileName: String


    fun readExcel(filePath: String) {
        fileName = filePath
        val file = File(filePath)
        if (!file.isFile) {
            return
        }

        EasyExcel.read(file, this).doReadAll()
    }

    override fun invokeHead(headMap: MutableMap<Int, ReadCellData<*>>?, context: AnalysisContext?) {
        if (headMap == null) {
            return
        }
        rowIndex++
        headMap.values.forEach({
            //  print("列号" + it.columnIndex)
            //  print("行号" + it.rowIndex)
            //  println("标题名" + it.stringValue)
            val po = ExcelDataPo()
            po.sheet = sheetIndex
            po.rowIndex = it.rowIndex
            po.columnIndex = it.columnIndex
            po.data = it.stringValue
            po.fileName = fileName
            SqliteUtil().insert(po)
        })
    }

    override fun invoke(data: HashMap<Any, Any>?, context: AnalysisContext?) {
        if (data == null) {
            return
        }

        data.entries.forEach {
            it.key
            val po = ExcelDataPo()
            po.sheet = sheetIndex
            po.rowIndex = rowIndex
            it.key.let {
                po.columnIndex = it.toString().toInt()
            }
            it.value.let {
                po.data = it.toString()
            }
            po.fileName = fileName
            SqliteUtil().insert(po)
        }
        rowIndex++;
    }

    override fun doAfterAllAnalysed(context: AnalysisContext?) {
        sheetIndex++
        rowIndex = 1
    }
}

fun main() {
    ExcelHandler().readExcel("C:\\Users\\78517\\Desktop\\典籍里的中国(统计).xlsx")
}
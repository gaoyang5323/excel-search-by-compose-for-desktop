package excel

import com.alibaba.excel.EasyExcel
import com.alibaba.excel.context.AnalysisContext
import com.alibaba.excel.metadata.data.ReadCellData
import com.alibaba.excel.read.listener.ReadListener
import dataSource.ExcelDataPo
import dataSource.MemoryUtil
import dataSource.SqliteUtil
import java.io.File

class ExcelHandler : ReadListener<HashMap<Any, Any>> {

    //表格索引
    var sheetIndex = 1
    var rowIndex = 1

    //1 sql写入  2内存写入
    var insertType = 1

    lateinit var fileName: String


    fun readExcel(filePath: String, insertType: Int) {
        this.insertType = insertType
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

            insertByType(po)
        })
    }

    override fun invoke(data: HashMap<Any, Any>, context: AnalysisContext?) {
        if (data == null) {
            return
        }

        data.entries.forEach {
            try {
                if (it.value == null || it.key == null) {
                    return@forEach
                }
                val po = ExcelDataPo()
                po.sheet = sheetIndex
                po.rowIndex = rowIndex
                po.columnIndex = it.key.toString().toInt()
                po.data = it.value.toString()
                po.fileName = fileName

                insertByType(po)
            } catch (e: Exception) {
                print(e)
            }
        }
        rowIndex++;
    }

    override fun doAfterAllAnalysed(context: AnalysisContext?) {
        sheetIndex++
        rowIndex = 1
    }

    private fun insertByType(po: ExcelDataPo) {
        when (insertType) {
            1 -> {
                SqliteUtil().insert(po)
            }
            2 -> {
                MemoryUtil().insert(po)
            }
            else -> {
                TODO()
            }
        }
    }
}

fun main() {
    ExcelHandler().readExcel("C:\\Users\\78517\\Desktop\\典籍里的中国(统计).xlsx", 2)
}
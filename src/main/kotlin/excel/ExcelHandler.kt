package excel

import androidx.compose.runtime.MutableState
import com.alibaba.excel.EasyExcel
import com.alibaba.excel.context.AnalysisContext
import com.alibaba.excel.metadata.data.ReadCellData
import com.alibaba.excel.read.listener.ReadListener
import dataSource.ExcelDataPo
import dataSource.MemoryUtil
import dataSource.SqliteUtil
import java.io.File
import java.util.*

class ExcelHandler : ReadListener<HashMap<Any, Any>> {

    //表格索引
    var sheetIndex = 1
    var rowIndex = 1

    //1 sql写入  2内存写入
    var insertType = 1

    lateinit var fileName: String

    companion object {
        fun readFilesByDir(dirPath: String): List<String> {
            val files = listFilesRecursively(File(dirPath))
            return files.filter { it.path.endsWith(".xlsx") || it.path.endsWith(".xls") }.map { it.absolutePath }
                .toList()
        }

        fun listFilesRecursively(folder: File): List<File> {
            val fileList = mutableListOf<File>()
            if (folder.isDirectory) {
                val files = folder.listFiles()
                if (files != null) {
                    for (file in files) {
                        if (file.isDirectory) {
                            fileList.addAll(listFilesRecursively(file))
                        } else {
                            fileList.add(file)
                        }
                    }
                }
            }
            return fileList
        }
    }

    fun readExcel(
        filePath: String,
        insertType: Int,
        showImportFileErrorAlert: MutableState<Boolean>,
        showImportFileErrorFileName: MutableState<String>,
    ) {
        this.insertType = insertType
        fileName = filePath
        val file = File(filePath)
        if (!file.isFile) {
            return
        }

        try {
            EasyExcel.read(file, this).doReadAll()
        } catch (e: Exception) {
            showImportFileErrorAlert.value = true
            if (e.message?.contains("Can't open the specified file input stream from file") == true) {
                showImportFileErrorFileName.value =
                    ExcelDataPo.getFileName(fileName).replace("~$", "") + "  正在打开导致无法读取,请关闭重试"
            } else {
                showImportFileErrorFileName.value = "发生未知错误,请联系管理员"
            }
            throw  RuntimeException()
        }
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
        po.id = UUID.randomUUID().toString()
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
    //ExcelHandler().readExcel("C:\\Users\\78517\\Desktop\\典籍里的中国(统计).xlsx", 2)
    println(UUID.randomUUID().toString())
}
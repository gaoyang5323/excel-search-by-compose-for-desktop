package dataSource

import java.io.File
import java.util.concurrent.atomic.LongAdder

class ExcelDataPo {
    var data: String? = null
    var fileName: String? = null
    var columnIndex: Int? = null
    var rowIndex: Int? = null
    var sheet: Int? = null
    var id: Int? = null

    companion object instance {

        var searchCount: LongAdder = LongAdder()

        fun sheetConvert(index: Int?): String {
            if (index == null) {
                return ""
            }
            var colCode = "";
            var key = 'a';
            var loop = index / 26;
            if (loop > 0) {
                colCode += sheetConvert(loop - 1);
            }
            key = (key + index % 26);
            colCode += key;
            return colCode.uppercase();
        }

        fun getFileName(filePath: String): String {
            val split = filePath.split(File.separator)
            return split[split.size - 1]
        }
    }
}

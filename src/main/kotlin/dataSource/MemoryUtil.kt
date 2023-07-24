package dataSource


class MemoryUtil {

    companion object {
        var allData = mutableListOf<ExcelDataPo>()
    }

    fun insert(data: ExcelDataPo) {
        allData.add(data)
    }

    fun selectByData(data: String): List<ExcelDataPo> {

        val findList = allData.filter {
            it.data?.contains(data) ?: false
        }

        return findList
    }
}


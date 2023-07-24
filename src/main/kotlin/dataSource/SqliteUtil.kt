package dataSource

import java.sql.Connection
import java.sql.DriverManager


class SqliteUtil {
    fun connect(): Connection {
        val url = "jdbc:sqlite:" + System.getProperty("user.dir") + "/kotlin-test.db"
        return DriverManager.getConnection(url)
    }

    fun insert(data: ExcelDataPo) {
        val sql = "INSERT INTO excel_data (data, file_name, column_index, row_index, sheet) VALUES (?,?,?,?,?)"

        val connect = connect()
        connect.use {
            val prepareStatement = connect.prepareStatement(sql)
            prepareStatement.setString(1, data.data)
            prepareStatement.setString(2, data.fileName)
            data.columnIndex?.let { prepareStatement.setInt(3, it) }
            data.rowIndex?.let { prepareStatement.setInt(4, it) }
            data.sheet?.let { prepareStatement.setInt(5, it) }

            prepareStatement.execute()
        }
    }

    fun selectByData(data: String): MutableList<ExcelDataPo> {
        val dataList: MutableList<ExcelDataPo> = mutableListOf()

        val sql = """
            SELECT
            	* 
            FROM
            	excel_data 
            WHERE
            	data LIKE '%'||?||'%'
        """

        val connect = connect()
        connect.use {
            val prepareStatement = connect.prepareStatement(sql)
            prepareStatement.setString(1, data)

            val executeQuery = prepareStatement.executeQuery()
            while (executeQuery.next()) {
                val po = ExcelDataPo()
                po.id = executeQuery.getInt(1)
                po.data = executeQuery.getString(2)
                po.fileName = executeQuery.getString(3)
                po.columnIndex = executeQuery.getInt(4)
                po.rowIndex = executeQuery.getInt(5)
                po.sheet = executeQuery.getInt(6)

                dataList.add(po)
            }
            return dataList
        }
    }
}


fun main() {
    val connect = SqliteUtil().connect()

    // var data = ExcelDataPo()
    // data.data = "123"
    // data.columnIndex = 1
//
    // SqliteUtil().insert(data, connect)

    val selectByData = SqliteUtil().selectByData("1")
    selectByData.forEach({
        println(it.id)
        println(it.data)
        println(it.sheet)
    })

}
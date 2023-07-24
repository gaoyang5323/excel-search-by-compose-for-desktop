import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import sqlite.ExcelDataPo

@Preview
@Composable
fun searchResult(searchResult: SnapshotStateList<ExcelDataPo>, searchText: MutableState<String>) {

    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(5.dp)
            .border(border = BorderStroke(1.dp, Color.Black))
            .background(Color.White)
    ) {
        if (searchResult.isNotEmpty()) {
            //进行了点击查询
            searchResult.forEach({
                Row {
                    it.rowIndex?.let { it ->

                        Text(
                            text = "所在行" + it.toString(),
                            Modifier.padding(start = 3.dp, bottom = 3.dp).width(80.dp)
                                .border(BorderStroke(1.dp, Color.Black)),
                            maxLines = 1,
                            textAlign = TextAlign.Center
                        )
                    }
                    it.columnIndex?.let { it ->
                        Text(
                            text = "所在列" + it.toString(),
                            Modifier.padding(start = 3.dp, bottom = 3.dp).width(80.dp)
                                .border(BorderStroke(1.dp, Color.Black)),
                            maxLines = 1,
                            textAlign = TextAlign.Center
                        )
                    }
                    it.data?.let { it ->
                        Text(
                            text = it,
                            Modifier.padding(start = 3.dp, bottom = 3.dp).width(300.dp)
                                .border(BorderStroke(1.dp, Color.Black)),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            })
        } else {
            Text("暂无搜索内容~")
        }
    }
}
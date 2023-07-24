import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import sqlite.ExcelDataPo

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun searchResult(searchResult: SnapshotStateList<ExcelDataPo>, searchText: MutableState<String>) {

    val state = rememberLazyListState()

    Box(
        modifier = Modifier.fillMaxSize()
            .background(color = Color.White)
    ) {

        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(5.dp)
                .border(border = BorderStroke(1.dp, Color.Black))
                .background(Color.White).padding(end = 12.dp), state
        ) {
            items(searchResult) { result ->
                run {
                    showResult(result)
                }
            }
        }

        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).width(35.dp).fillMaxHeight(0.9f).padding(end = 20.dp),
            adapter = rememberScrollbarAdapter(scrollState = state)
        )
    }
}

@Composable
fun showResult(result: ExcelDataPo) {
    //进行了点击查询
    Row {
        result.rowIndex?.let { it ->
            Text(
                text = "所在行" + it.toString(),
                Modifier.padding(start = 3.dp, bottom = 3.dp).width(80.dp)
                    .border(BorderStroke(1.dp, Color.Black)),
                maxLines = 1,
                textAlign = TextAlign.Center
            )
        }
        result.columnIndex?.let { it ->
            Text(
                text = "所在列" + it.toString(),
                Modifier.padding(start = 3.dp, bottom = 3.dp).width(80.dp)
                    .border(BorderStroke(1.dp, Color.Black)),
                maxLines = 1,
                textAlign = TextAlign.Center
            )
        }
        result.data?.let { it ->
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
}
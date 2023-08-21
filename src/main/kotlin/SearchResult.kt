import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dataSource.ExcelDataPo

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun searchResult(
    searchResult: SnapshotStateList<ExcelDataPo>,
    searchText: MutableState<String>,
) {

    val state = rememberLazyListState()

    Box(
        modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(top = 5.dp)
    ) {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .border(border = BorderStroke(1.dp, Color(0xFFF0F0F0)))
                .background(Color.White), state
        ) {
            item {
                showTitle()
            }

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
fun showTitle() {
    Row {
        Box(
            modifier = Modifier.fillMaxWidth().height(30.dp).padding(start = 5.dp, top = 5.dp)
                .border(BorderStroke(1.dp, Color(0x7F000000))).weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "文件磁盘位置",
                color = Color.Black,
                maxLines = 1,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
        }

        Box(
            modifier = Modifier.fillMaxWidth().height(30.dp).padding(start = 2.dp, top = 5.dp)
                .border(BorderStroke(1.dp,  Color(0x7F000000))).weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "搜索结果所在位置",
                maxLines = 1,
                textAlign = TextAlign.Center
            )
        }

        Box(
            modifier = Modifier.fillMaxWidth().height(30.dp).padding(start = 2.dp, top = 5.dp, end = 5.dp)
                .border(BorderStroke(1.dp, Color(0x7F000000))).weight(4f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "搜索结果",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun showResult(result: ExcelDataPo) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    var showAlert by remember { mutableStateOf(false) }

    if (showAlert) {
        AlertDialog(onDismissRequest = {
            showAlert = false;
        }, title = {
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "选中内容", fontSize = 15.sp)
            }
        }, text = {
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "已成功复制~", fontSize = 15.sp)
            }
        }, buttons = {
            /*Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = {
                    showAlert = false;
                }, content = {
                    Text(text = "取消", fontSize = 10.sp)
                }, modifier = Modifier.width(60.dp))
            }*/
        }, modifier = Modifier.width(200.dp).height(100.dp))
    }

    //进行了点击查询
    Row {
        Box(
            modifier = Modifier.fillMaxWidth().height(30.dp).padding(start = 5.dp, top = 5.dp)
                .border(BorderStroke(1.dp,  Color(0x7F000000))).weight(1f).clickable {
                    //点击复制内容
                    clipboardManager.setText(AnnotatedString((result.fileName.toString())))
                    showAlert = true
                },
            contentAlignment = Alignment.Center
        ) {
            result.fileName?.let { it ->
                Text(
                    text = ExcelDataPo.getFileName(it),
                    color = Color.Black,
                    fontSize = 10.sp,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        Box(
            modifier = Modifier.fillMaxWidth().height(30.dp).padding(start = 2.dp, top = 5.dp)
                .border(BorderStroke(1.dp, Color(0x7F000000))).weight(1f).clickable {
                    //点击复制内容
                    clipboardManager.setText(AnnotatedString((ExcelDataPo.sheetConvert(result.columnIndex) + result.rowIndex.toString() + "-sheet" + result.sheet)))
                    showAlert = true
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = ExcelDataPo.sheetConvert(result.columnIndex) + result.rowIndex.toString() + "-sheet" + result.sheet,
                maxLines = 1,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
        }

        Box(
            modifier = Modifier.fillMaxWidth().height(30.dp).padding(start = 2.dp, top = 5.dp, end = 5.dp)
                .border(BorderStroke(1.dp,  Color(0x7F000000))).weight(4f).clickable {
                    //点击复制内容
                    clipboardManager.setText(AnnotatedString((result.data.toString())))
                    showAlert = true
                },
            contentAlignment = Alignment.Center
        ) {
            result.data?.let { it ->
                Text(
                    text = it,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

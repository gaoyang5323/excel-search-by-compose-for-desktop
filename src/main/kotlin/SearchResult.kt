import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Desktop
import java.io.File
import java.io.IOException

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class, DelicateCoroutinesApi::class)
@Preview
@Composable
fun searchResult(
    searchResult: SnapshotStateList<ExcelDataPo>,
    searchText: MutableState<String>,
) {

    val state = rememberLazyListState()
    val showAlert = remember { mutableStateOf(false) }
    val showAlertPath = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(top = 5.dp)
    ) {
        Column {
            showTitle()

            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .border(border = BorderStroke(1.dp, Color(0xFFF0F0F0)))
                    .background(Color.White), state
            ) {
                items(items = searchResult, key = {
                    it.id!!
                }) { result ->
                    run {
                        showResult(result, showAlert, showAlertPath)
                    }
                }
            }

            VerticalScrollbar(
                //  modifier = Modifier.align(Alignment.CenterEnd).width(35.dp).fillMaxHeight(0.9f).padding(end = 20.dp),
                adapter = rememberScrollbarAdapter(scrollState = state)
            )
        }
    }

    //复制弹窗
    if (showAlert.value || showAlertPath.value) {
        if (showAlertPath.value) {
            GlobalScope.launch {
                delay(3000)
                showAlert.value = false
                showAlertPath.value = false
            }
        } else {
            GlobalScope.launch {
                delay(1000)
                showAlert.value = false
                showAlertPath.value = false
            }
        }

        AlertDialog(
            onDismissRequest = {
                showAlert.value = false;
                showAlertPath.value = false
            },
            title = {
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "复制", fontSize = 15.sp)
                }
            },
            text = {
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (showAlertPath.value) {
                        Text(text = "系统不存在支持打开该文件的程序,已复制该文件磁盘路径", fontSize = 15.sp)
                    } else {
                        Text(text = "已成功复制搜索结果内容", fontSize = 15.sp)
                    }
                }
            },
            buttons = {
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
            },
            modifier = Modifier.width(300.dp).height(100.dp)
                .border(border = BorderStroke(0.dp, Color.White), shape = RoundedCornerShape(4.dp))
        )
    }
}

@Composable
fun showTitle() {
    Row {
        Box(
            modifier = Modifier.fillMaxWidth().height(25.dp).padding(start = 5.dp)
                .border(BorderStroke(1.dp, Color(0x7F000000))).weight(1f).background(Color(176, 196, 222)),
            contentAlignment = Alignment.Center
        ) {
            Row {
                Text(
                    text = "文件",
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "(单击打开文件)",
                    Modifier.padding(top = 3.dp),
                    color = Color.White,
                    // textAlign = TextAlign.Center,
                    fontSize = 10.sp,
                )
            }
        }

        Box(
            modifier = Modifier.fillMaxWidth().height(25.dp).padding(start = 2.dp)
                .border(BorderStroke(1.dp, Color(0x7F000000)))
                .weight(0.8f)
                .background(Color(176, 196, 222)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "所在位置",
                maxLines = 1,
                textAlign = TextAlign.Center
            )
        }

        Box(
            modifier = Modifier.fillMaxWidth().height(25.dp).padding(start = 2.dp, end = 5.dp)
                .border(BorderStroke(1.dp, Color(0x7F000000))).weight(4f).background(Color(176, 196, 222)),
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


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class, DelicateCoroutinesApi::class)
@Composable
fun showResult(result: ExcelDataPo, showAlert: MutableState<Boolean>, showAlertPath: MutableState<Boolean>) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    //进行了点击查询
    Row {
        Box(
            modifier = Modifier.fillMaxWidth().height(30.dp).padding(start = 5.dp, top = 5.dp)
                .border(BorderStroke(1.dp, Color(0x7F000000))).weight(1f)
                .background(Color(33, 150, 70))
                .clickable {
                    //点击复制内容
                    // clipboardManager.setText(AnnotatedString((result.fileName.toString())))
                    // showAlertPath.value = true

                    //打开文件
                    val excelFile = File(result.fileName!!)
                    if (Desktop.isDesktopSupported() && excelFile.exists()) {
                        val desktop = Desktop.getDesktop()
                        try {
                            desktop.open(excelFile) // 使用默认程序打开文件
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    } else {
                        //点击复制内容
                        clipboardManager.setText(AnnotatedString((result.fileName.toString())))
                        showAlertPath.value = true
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            result.fileName?.let { it ->
                var tempPath = it
                Text(
                    text = ExcelDataPo.getFileName(tempPath),
                    color = Color.White,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 15.sp
                )
            }
        }

        var resultRowSre by remember { mutableStateOf(ExcelDataPo.sheetConvert(result.columnIndex) + result.rowIndex.toString() + "-sheet" + result.sheet) }
        Box(
            modifier = Modifier.fillMaxWidth().height(30.dp).padding(start = 2.dp, top = 5.dp)
                .border(BorderStroke(1.dp, Color(0x7F000000))).weight(0.8f)
                .background(Color(33, 180, 70))
                .clickable {
                    //点击复制内容
                    clipboardManager.setText(AnnotatedString((ExcelDataPo.sheetConvert(result.columnIndex) + result.rowIndex.toString())))
                    // showAlert.value = true
                    resultRowSre = "已复制"
                    GlobalScope.launch {
                        delay(800)
                        resultRowSre =
                            ExcelDataPo.sheetConvert(result.columnIndex) + result.rowIndex.toString() + "-sheet" + result.sheet
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = resultRowSre,
                maxLines = 1,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                color = Color.White
            )
        }

        var resultDataSre by remember { mutableStateOf(result.data!!) }
        Box(
            modifier = Modifier.fillMaxWidth().height(30.dp).padding(start = 2.dp, top = 5.dp, end = 5.dp)
                .border(BorderStroke(1.dp, Color(0x7F000000))).weight(4f).clickable {
                    //点击复制内容
                    clipboardManager.setText(AnnotatedString((result.data.toString())))
                    //showAlert.value = true
                    resultDataSre = "已复制"
                    GlobalScope.launch {
                        delay(800)
                        resultDataSre = result.data!!
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = resultDataSre,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
            )
        }
    }
}

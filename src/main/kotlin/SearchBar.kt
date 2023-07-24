import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import sqlite.ExcelDataPo
import sqlite.SqliteUtil

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview
fun searchBar(searchText: MutableState<String>, searchResult: SnapshotStateList<ExcelDataPo>) {
    var text by remember { searchText }
    var showPlaceHolder by remember { mutableStateOf(true) }
    var showAlert by remember { mutableStateOf(false) }
    var inputEnable by remember { mutableStateOf(true) }


    Row() {
        BasicTextField(
            value = text,
            onValueChange = {
                text = it

                //最大字符串数量
                if (text.isNotBlank() && text.length > 20) {
                    text = text.substring(0, 20)
                }

                showPlaceHolder = text.isEmpty()
            },
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "搜索",
                    )
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (showPlaceHolder) {
                            Text(
                                text = "输入点东西看看吧~",
                                color = Color(0x7F000000),
                                modifier = Modifier.clickable { showPlaceHolder = false }
                            )
                        }
                        innerTextField()
                    }
                    if (text.isNotBlank()) {
                        IconButton(
                            onClick = {
                                text = ""
                                showPlaceHolder = true

                                //释放搜索框
                                //inputEnable = true
                            },
                            modifier = Modifier.size(16.dp)
                        ) {
                            Icon(imageVector = Icons.Filled.Close, contentDescription = "清除")
                        }
                    }
                }
            },
            modifier = Modifier
                .padding(start = 5.dp)
                .background(Color.White, CircleShape)
                .height(40.dp)
                .weight(2f)
                .fillMaxWidth()
                .border(border = BorderStroke(1.dp, Color.Black)),
            enabled = inputEnable,
            singleLine = true
        )

        Button(
            onClick = {
                //弹窗
                if (text.isBlank()) {
                    showAlert = true
                    return@Button
                }
                //进行搜索
                searchResult.clear()
                val dataList = SqliteUtil().selectByData(searchText.value)
                searchResult.addAll(dataList)
                //锁定搜索框
                //inputEnable = false
            },
            Modifier
                .weight(1f)
                .height(40.dp).padding(end = 5.dp)
                .background(Color.White)
                .padding(start = 5.dp)
                .wrapContentWidth(Alignment.End)
                .fillMaxWidth(),
            enabled = true,
            content = {
                Text("查询")
            })
    }

    if (showAlert) {
        AlertDialog(onDismissRequest = {
            showAlert = false;
        }, title = {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("进行查询操作")
            }
        }, text = {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("查询的关键词不能为空")
            }
        }, buttons = {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = {
                    showAlert = false;
                }, content = {
                    Text(text = "取消", fontSize = 10.sp)
                }, modifier = Modifier.width(60.dp))
            }
        }, modifier = Modifier.width(250.dp))
    }
}


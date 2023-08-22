import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dataSource.ExcelDataPo
import dataSource.MemoryUtil
import dataSource.SqliteUtil
import excel.ExcelHandler
import java.awt.FileDialog


@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview
fun searchBar(
    searchText: MutableState<String>,
    searchResult: SnapshotStateList<ExcelDataPo>,
) {
    var text by remember { searchText }
    var showPlaceHolder by remember { mutableStateOf(true) }
    var showAlert by remember { mutableStateOf(false) }
    var showAlertReward by remember { mutableStateOf(false) }
    //输入框开关
    var inputEnable by remember { mutableStateOf(false) }
    //1 sql查询  2内存查询
    var searchSourceType by remember { mutableStateOf(2) }
    //搜索框颜色
    var searchButtonColor by remember { mutableStateOf(Color.Gray) }

    //fuxuankuang
    var checkedState1 by remember { mutableStateOf(true) }
    var checkedState2 by remember { mutableStateOf(false) }


    Row(
        Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(end = 5.dp, top = 5.dp)
    ) {
        //搜索栏
        Column(Modifier.fillMaxWidth().weight(2.5f)) {
            BasicTextField(
                value = text,
                onValueChange = {
                    text = it
                    if (text.isNotBlank() && text.length > 40) {
                        text = text.substring(0, 40)
                    }

                    showPlaceHolder = text.isEmpty()
                },
                decorationBox = { innerTextField ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        //文件名筛选
                        /*  Column(
                              Modifier
                                  .fillMaxHeight()
                                  .width(150.dp)
                                  .border(1.dp, Color(0x7F000000), shape = RoundedCornerShape(4.dp)),
                              verticalArrangement = Arrangement.Center
                          ) {
                              Text(
                                  text = "请点击右侧选择文件", textAlign = TextAlign.Center,
                                  modifier = Modifier.fillMaxWidth()
                              )
                          }*/
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "搜索图标",
                            modifier = Modifier.padding(3.dp)
                        )

                        //搜索框
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .weight(1f),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (showPlaceHolder) {
                                Text(
                                    text = if (inputEnable) "请输入搜索内容~" else "搜索前请点击右侧上传图标,选择需要搜索的文件所在目录=>",
                                    color = Color(0x7F000000),
                                    modifier = Modifier.clickable {
                                        //showPlaceHolder = false
                                    }
                                )
                            }
                            innerTextField()
                        }

                        //清除
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
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "清除"
                                )
                            }
                        }
                    }
                },
                modifier = Modifier
                    .padding(start = 5.dp)
                    .background(Color.White, CircleShape)
                    .fillMaxHeight()
                    .border(border = BorderStroke(1.dp, Color(0x7F000000)), shape = RoundedCornerShape(4.dp)),
                enabled = inputEnable,
                singleLine = true
            )
        }

        //按钮栏
        Row(Modifier.fillMaxWidth().weight(1f).height(40.dp)) {
            //搜索按钮
            Box(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
                    .padding(start = 5.dp)
                    .wrapContentWidth(Alignment.End)
                    .border(
                        border = BorderStroke(1.dp, Color(0x7F000000)),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .clip(RoundedCornerShape(4.dp))
                    .fillMaxWidth()
                    .background(searchButtonColor)
                    .clickable {
                        //判断输入框是否开启
                        if (!inputEnable) {
                            return@clickable
                        }

                        //没有搜索内容的弹窗
                        if (text.isBlank()) {
                            showAlert = true
                            return@clickable
                        }
                        //进行搜索
                        when (searchSourceType) {
                            1 -> {
                                searchResult.clear()
                                val dataList = SqliteUtil().selectByData(searchText.value)
                                searchResult.addAll(dataList)
                            }
                            2 -> {
                                searchResult.clear()
                                val dataList = MemoryUtil().selectByData(searchText.value)
                                searchResult.addAll(dataList)
                            }
                            else -> {
                                TODO()
                            }
                        }

                        //锁定搜索框
                        //inputEnable = false

                        //判断是否弹出奖励
                        if (ExcelDataPo.searchCount.toLong() % 20 == 0L) {
                            showAlertReward = true
                        }
                        ExcelDataPo.searchCount.add(1);
                    },
                contentAlignment = Center
            ) {
                Text(
                    text = "搜索",
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            }

            //导入按钮
            Image(
                painter = painterResource("上传.png"),
                contentDescription = "My Image",
                modifier = Modifier.fillMaxHeight()
                    .width(30.dp)
                    .padding(start = 5.dp)
                    .background(Color(176, 196, 222))
                    .border(border = BorderStroke(1.dp, Color(0x7F000000)), shape = RoundedCornerShape(4.dp))
                    .clickable {
                        val fileDialog = FileDialog(ComposeWindow())
                        fileDialog.setMultipleMode(true)
                        fileDialog.setVisible(true)
                        var files = fileDialog.getFiles()

                        files = files.filter {
                            it.path.endsWith(".xlsx") || it.path.endsWith(".xls")
                        }.toTypedArray()
                        if (files.isEmpty()) {
                            return@clickable
                        }
                        when (searchSourceType) {
                            1 -> {
                                searchResult.clear()
                                files.forEach {
                                    ExcelHandler().readExcel(it.path, 1)
                                }

                            }
                            2 -> {
                                MemoryUtil.allData.clear()
                                searchResult.clear()
                                files.forEach {
                                    ExcelHandler().readExcel(it.path, 2)
                                }
                            }
                            else -> {
                                TODO()
                            }
                        }


                        // JFileChooser().apply {
                        //     fileSelectionMode = JFileChooser.FILES_AND_DIRECTORIES
                        //     showOpenDialog(ComposeWindow())
                        //     var path = selectedFile?.absolutePath ?: ""
                        //     println(path)
                        // }

                        inputEnable = true
                        searchButtonColor = Color.Magenta
                    }
            )
        }
    }

    //喝茶弹窗
    if (showAlertReward) {
        AlertDialog(
            onDismissRequest = {
            },
            title = {
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "请喝个饮料呗~", fontSize = 15.sp, color = Color.Gray)
                }
            },
            text = {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource("wxpay.png"),
                            contentDescription = "My Image",
                            Modifier.width(200.dp).height(200.dp).weight(1f)
                        )

                        Image(
                            painter = painterResource("alipay.png"),
                            contentDescription = "My Image",
                            Modifier.width(200.dp).height(200.dp).weight(1f)
                        )
                    }


                }
            },
            buttons = {
                Column {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "不会影响你使用的,每搜索20次弹出来一次~", fontSize = 13.sp, color = Color.Magenta)
                    }

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(onClick = {
                            showAlertReward = false;
                        }, content = {
                            Text(text = "取消", fontSize = 15.sp)
                        }, modifier = Modifier.width(80.dp))
                    }
                }

            },
            modifier = Modifier.width(500.dp).height(400.dp)
                .border(border = BorderStroke(0.dp, Color.White), shape = RoundedCornerShape(4.dp))
        )
    }

    //查询提示
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


// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dataSource.ExcelDataPo

@Composable
@Preview
fun App() {
    //搜索关键字
    val searchText = remember { mutableStateOf("") }
    val searchResult = remember { mutableStateListOf<ExcelDataPo>() }

    Column(
        Modifier
            .background(Color(0xfff7f7f7))
            .fillMaxWidth()
            .fillMaxHeight()
    ) {

        //搜索框
        searchBar(searchText, searchResult)

        //搜索结果
        searchResult(searchResult, searchText)
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "测试",
        icon = painterResource("logo.png")
    ) {
        App()
    }
}

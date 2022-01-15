package io.github.devriesl.refreshratetester.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import io.github.devriesl.refreshratetester.TesterViewModel

@Composable
fun Information(
    modifier: Modifier = Modifier,
    testerViewModel: TesterViewModel
) {
    val refreshRatesList = testerViewModel.getRefreshRates()
    val refreshRate by testerViewModel.refreshRate.collectAsState()
    val supportSeamlessVrr = testerViewModel.supportSeamlessVrr()

    Column(modifier = modifier) {
        Text(text = buildAnnotatedString {
            append("系统信息\n")
            append("动态刷新率：")
            if (supportSeamlessVrr) {
                append("支持")
            } else {
                append("不支持")
            }
        })
        Spacer(modifier = Modifier.fillMaxWidth())
        Text(text = "支持的系统刷新率：")
        LazyRow {
            items(refreshRatesList) { refreshRate ->
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color.Magenta)) {
                            append(refreshRate.toInt().toString())
                        }
                        append("Hz")
                    }
                )
                Spacer(Modifier.width(4.dp))
            }
        }
        Text(
            buildAnnotatedString {
                append("当前系统刷新率：")
                withStyle(style = SpanStyle(color = Color.Magenta)) {
                    append(refreshRate?.toInt().toString())
                }
                append("Hz")
            }
        )
    }
}
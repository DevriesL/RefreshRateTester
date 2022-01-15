package io.github.devriesl.refreshratetester.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.viewinterop.AndroidView
import io.github.devriesl.refreshratetester.TesterViewModel
import io.github.devriesl.refreshratetester.VariableRefreshRate

@Composable
fun RefreshRateMenu(
    modifier: Modifier = Modifier,
    testerViewModel: TesterViewModel
) {
    var expandedState by remember { mutableStateOf(false) }
    var animationRefreshRate by remember { mutableStateOf(0f) }

    var surfaceViewWidth by remember { mutableStateOf(Size.Zero.width) }
    val surfaceViewHeight = with(LocalDensity.current) {
        (surfaceViewWidth * 1.2f).toDp()
    }

    Column(modifier = modifier) {
        Text(text = "可变刷新率动画")
        Row {
            AndroidView(
                modifier = Modifier
                    .height(surfaceViewHeight)
                    .weight(3f)
                    .onGloballyPositioned { coordinates ->
                        surfaceViewWidth = coordinates.size.toSize().width
                    },
                factory = { context ->
                    AnimationSurfaceView(context).also {
                        testerViewModel.registerDisplayListener()
                    }
                },
                update = {
                    it.changeRefreshRate(animationRefreshRate)
                }
            )
            Box(modifier = Modifier
                .wrapContentHeight()
                .weight(1f)
            ) {
                Text(modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = { expandedState = true }),
                    text = buildAnnotatedString {
                        if (animationRefreshRate > 0) {
                            withStyle(style = SpanStyle(color = Color.Magenta)) {
                                append(animationRefreshRate.toInt().toString())
                            }
                            append("Hz")
                        } else {
                            append("选择动画帧率")
                        }
                    }
                )
                DropdownMenu(
                    expanded = expandedState,
                    onDismissRequest = { expandedState = false }
                ) {
                    VariableRefreshRate.values().forEach {
                        DropdownMenuItem(
                            onClick = {
                                animationRefreshRate = it.refreshRate
                                expandedState = false
                            }
                        ) {
                            Text(text = it.name)
                        }
                    }
                }
            }
        }
    }
}
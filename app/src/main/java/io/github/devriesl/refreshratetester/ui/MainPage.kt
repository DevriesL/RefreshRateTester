package io.github.devriesl.refreshratetester.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.devriesl.refreshratetester.TesterViewModel

@Composable
fun MainPage(
    testerViewModel: TesterViewModel
) {
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Information(testerViewModel = testerViewModel)
        RefreshRateMenu(testerViewModel = testerViewModel)
    }
}

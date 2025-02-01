package org.book.ui.components.pags

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.book.ui.screen.rune.RuneViewModel
import org.book.utils.enum.RunesEnum

@Composable
fun Controller(
    swipeLeft: () -> Boolean,
    swipeRight: () -> Boolean,
    runeActual: RunesEnum,
    viewModel: RuneViewModel,
    navController: NavHostController
) {
    val density = LocalDensity.current
    var totalDragHorizontal by remember { mutableStateOf(0f) }
    val thresholdHorizontal = with(density) { 100.dp.toPx() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragStart = { totalDragHorizontal = 0f },
                    onHorizontalDrag = { change, dragAmount ->
                        change.consume()
                        totalDragHorizontal += dragAmount
                    },
                    onDragEnd = {
                        when {
                            totalDragHorizontal < thresholdHorizontal -> {
                                if (swipeLeft()) {
                                    viewModel.update { copy(indexActual = this.indexActual + 1) }
                                    navController.popBackStack()
                                    navController.navigate(runeActual.dataRuneNavigation.routeRuneNext)
                                }
                            }

                            totalDragHorizontal > thresholdHorizontal -> {
                                if (swipeRight()) {
                                    viewModel.update { copy(indexActual = this.indexActual - 1) }
                                    navController.popBackStack()
                                    navController.navigate(runeActual.dataRuneNavigation.routeRunePrevious)
                                }
                            }
                        }
                        totalDragHorizontal = 0f
                    }
                )
            }
    )
}
package org.book.ui.screen.rune

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.book.ui.components.pags.Controller
import org.book.ui.components.pags.Inventory
import org.book.ui.components.pags.TextRune
import org.book.ui.components.pags.Tutorial
import org.book.ui.navigation.NavControllerRunes
import org.book.utils.enum.RunesEnum
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RuneScreen() = Screen()

@Composable
private fun Screen(viewModel: RuneViewModel = koinViewModel()) {
    val navController = rememberNavController()
    val state by viewModel.state.collectAsState()

    Controller(
        viewModel = viewModel,
        navController = navController,
        runeActual = state.rune[state.indexActual],
        swipeLeft = {
            viewModel.update { copy(directionNavigation = true) }
            state.indexActual < state.rune.size - 1
        },
        swipeRight = {
            viewModel.update { copy(directionNavigation = false) }
            state.indexActual > 0
        }
    )
    Content(
        navController = navController,
        rune = state.rune,
        indexActual = state.indexActual,
        directionNavigation = state.directionNavigation
    )
    if (state.rune[state.indexActual] == RunesEnum.entries[0]) Tutorial()
    Inventory(
        runeSelection = {
            viewModel.update { copy(runeSelection = it) }
        },
        runeSelectionActual = state.runeSelection
    )
    TextRune(
        rune = state.rune,
        indexActual = state.indexActual
    )
}

@Composable
private fun Content(
    navController: NavHostController,
    rune: List<RunesEnum>,
    indexActual: Int,
    directionNavigation: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background),
        contentAlignment = Alignment.Center,
        content = {
            NavControllerRunes(
                navController = navController,
                listRune = rune,
                indexActual = indexActual,
                navigationDirection = directionNavigation
            )
        }
    )
}
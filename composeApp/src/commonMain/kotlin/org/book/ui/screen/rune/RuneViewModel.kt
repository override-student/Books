package org.book.ui.screen.rune

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.book.utils.data.RuneState
import org.book.utils.enum.ComparisonOperator
import org.book.utils.enum.InventoryObject
import org.book.utils.enum.RunesEnum

class RuneViewModel : ViewModel() {
    private val _state: MutableStateFlow<RuneState> = MutableStateFlow(RuneState())
    val state: StateFlow<RuneState> = _state.asStateFlow()

    fun update(update: RuneState.() -> RuneState) {
        _state.value = update(_state.value)
    }

    fun toggleSelection(item: InventoryObject) {
        _state.update { current ->
            val newSelection = when {
                item in current.selectedItems -> current.selectedItems - item
                current.selectedItems.size < 2 -> current.selectedItems + item
                else -> current.selectedItems.drop(1) + item
            }
            current.copy(selectedItems = newSelection)
        }
    }

    fun performComparison() {
        val (operator, items) = _state.value.run { comparisonOperator to selectedItems }
        if (operator == null || items.size != 2) return update { copy(isPagComplete = false) }

        val (first, second) = items.first() to items.last()
        _state.update { current ->
            current.copy(
                isPagComplete = when (operator) {
                    ComparisonOperator.EQUAL -> first.value == second.value
                    ComparisonOperator.NOT_EQUAL -> first.value != second.value
                    ComparisonOperator.LESS_THAN -> first.value < second.value
                    ComparisonOperator.GREATER_THAN -> first.value > second.value
                    ComparisonOperator.LESS_EQUAL -> first.value <= second.value
                    ComparisonOperator.GREATER_EQUAL -> first.value >= second.value
                    ComparisonOperator.OR -> first.form == second.form || first.value == second.value
                    ComparisonOperator.AND -> first.form == second.form && first.value == second.value
                }
            )
        }
    }

    fun preloadImages(platformContext: PlatformContext) {
        val imageLoader = ImageLoader(context = platformContext)
        viewModelScope.launch(Dispatchers.IO) {
            RunesEnum.entries.forEach {
                RunesEnum.entries.forEach { rune ->
                    val request = ImageRequest.Builder(context = platformContext)
                        .data(rune.imageUrl)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .build()
                    imageLoader.execute(request)
                }
            }
        }
    }
}
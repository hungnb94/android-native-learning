package com.tekome.androidnativelearning.ui.savedstate

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

private const val KEY_SAVED_COUNTER = "saved_counter"
private const val KEY_SAVED_NAME = "saved_name"

/**
 * Demo ViewModel với SavedStateHandle.
 *
 * SavedStateHandle hoạt động như Bundle — được Android tự động lưu
 * vào onSaveInstanceState, nên data persist qua CẢ process kill!
 *
 * ✅ savedCounter & savedName sống qua: rotation + process kill
 * ❌ regularCounter chỉ sống qua: rotation (mất khi process kill)
 *
 * Giới hạn: chỉ lưu được primitive, String, Parcelable — không lưu được
 * List lớn hay object phức tạp chưa Parcelable.
 *
 * [savedStateHandle] được inject tự động khi dùng viewModel() hoặc hiltViewModel().
 * KHÔNG cần tạo Factory thủ công.
 */
class SavedStateDemoViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // ✅ PERSIST qua process kill — đọc/ghi qua SavedStateHandle (Bundle)
    val savedCounter: StateFlow<Int> =
        savedStateHandle.getStateFlow(KEY_SAVED_COUNTER, initialValue = 0)

    val savedName: StateFlow<String> =
        savedStateHandle.getStateFlow(KEY_SAVED_NAME, initialValue = "")

    // ❌ KHÔNG persist qua process kill — chỉ là biến trong memory
    private val _regularCounter = MutableStateFlow(0)
    val regularCounter: StateFlow<Int> = _regularCounter.asStateFlow()

    // --- SavedStateHandle counter ---
    fun incrementSaved() {
        savedStateHandle[KEY_SAVED_COUNTER] = (savedCounter.value) + 1
    }

    fun decrementSaved() {
        savedStateHandle[KEY_SAVED_COUNTER] = maxOf(0, savedCounter.value - 1)
    }

    fun updateSavedName(name: String) {
        savedStateHandle[KEY_SAVED_NAME] = name
    }

    // --- Regular counter ---
    fun incrementRegular() = _regularCounter.update { it + 1 }
    fun decrementRegular() = _regularCounter.update { if (it > 0) it - 1 else 0 }
}

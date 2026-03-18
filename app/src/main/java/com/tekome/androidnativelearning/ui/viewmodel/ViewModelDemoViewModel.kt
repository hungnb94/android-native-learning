package com.tekome.androidnativelearning.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class UserProfile(
    val name: String = "Hung Nguyen",
    val age: Int = 25,
    val score: Double = 9.5
)

/**
 * Demo ViewModel — lưu data phức tạp KHÔNG cần serialize.
 *
 * ✅ Data sống qua: screen rotation, navigate back/forward, config change
 * ❌ Data mất khi: system kill process (low memory), app bị swipe đi
 *
 * ViewModel bị destroy khi Activity/Fragment thật sự finish (back button).
 */
class ViewModelDemoViewModel : ViewModel() {

    // Counter đơn giản
    private val _counter = MutableStateFlow(0)
    val counter: StateFlow<Int> = _counter.asStateFlow()

    // List of strings — không cần serialize
    private val _items = MutableStateFlow<List<String>>(emptyList())
    val items: StateFlow<List<String>> = _items.asStateFlow()

    // Complex object — data class không cần Parcelable
    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    fun increment() = _counter.update { it + 1 }
    fun decrement() = _counter.update { if (it > 0) it - 1 else 0 }

    fun addItem() {
        val itemNumber = _items.value.size + 1
        _items.update { it + "Item #$itemNumber" }
    }

    fun clearItems() = _items.update { emptyList() }

    fun updateProfile() {
        _userProfile.update {
            it.copy(
                score = it.score + 0.1,
                age = it.age + 1
            )
        }
    }
}

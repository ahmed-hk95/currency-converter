package ahmed.hk.currencyconverter.ui.main

import ahmed.hk.currencyconverter.ui.base.BaseViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): BaseViewModel() {

    private val _enabled = MutableStateFlow(false)
    val enabled: StateFlow<Boolean> = _enabled


    val text = MutableStateFlow("")

    init {
        viewModelScope.launch {
            text.collect{
                _enabled.value = it.lines().size>1
            }
        }
    }

}
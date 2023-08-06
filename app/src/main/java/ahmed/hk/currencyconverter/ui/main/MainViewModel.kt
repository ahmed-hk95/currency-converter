package ahmed.hk.currencyconverter.ui.main

import ahmed.hk.currencyconverter.data.models.Currency
import ahmed.hk.currencyconverter.ui.base.BaseViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : BaseViewModel() {

    private val _enabled = MutableStateFlow(false)
    val enabled: StateFlow<Boolean> = _enabled

    private val _currencies = mutableListOf<Currency>()
    private val _currenciesSymbols = MutableStateFlow(emptyList<String>())
    val currencies: StateFlow<List<String>> = _currenciesSymbols

    private val _fromCurrencyPosition = MutableStateFlow(-1)
    val fromCurrencyPosition:StateFlow<Int> = _fromCurrencyPosition

    private val _toCurrencyPosition = MutableStateFlow(-1)
    val toCurrencyPosition:StateFlow<Int> = _toCurrencyPosition

    val fromCurrencyListener: (Int) -> Unit = {
        _fromCurrencyPosition.value = it
        convert()
    }
    val toCurrencyListener: (Int) -> Unit = {
        _toCurrencyPosition.value = it
        convert()
    }
    val fromAmount = MutableStateFlow("1")

    private val _convertedAmount = MutableStateFlow("0")
    val convertedAmount = _convertedAmount

    fun onSwapCurrencyClicked() {
        val fromValue = fromCurrencyPosition.value
        val toValue = toCurrencyPosition.value
        _fromCurrencyPosition.value = toValue
        _toCurrencyPosition.value = fromValue
        convert()
    }

    fun onDetailsButtonClicked() {

    }

    init {
        api(
            func = { dataHelper.getCurrencies() },
            error = { showToast(it.toString()) }
        ) {
            _currencies.addAll(it)
            this._currenciesSymbols.value = _currencies.map { currency -> currency.symbol }
        }

        viewModelScope.launch {
            fromAmount.collect {
                convert(it.toDoubleOrNull())
            }

        }
    }


    private fun convert(amount: Double? = fromAmount.value.toDoubleOrNull()) {
        val from = _currencies.getOrNull(fromCurrencyPosition.value)
        val to = _currencies.getOrNull(toCurrencyPosition.value)
        if (from == null || to == null || amount == null) return
        val result = (to.value * amount / from.value).toString()
        _convertedAmount.value = result
    }
}
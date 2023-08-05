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

    private var fromCurrency: Currency? = null
    private var toCurrency: Currency? = null

    val fromCurrencyListener: (Int) -> Unit = {
        fromCurrency = _currencies[it]
        convert()
    }
    val toCurrencyListener: (Int) -> Unit = {
        toCurrency = _currencies[it]
        convert()
    }
    val fromAmount = MutableStateFlow("1")

    private val _convertedAmount = MutableStateFlow("0")
    val convertedAmount = _convertedAmount

    fun onSwapCurrencyClicked() {

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
        viewModelScope.launch {
            convertedAmount.collect {
                convert(it.toDoubleOrNull(), isReversed = true)
            }
        }
    }


    private fun convert(amount: Double? = fromAmount.value.toDoubleOrNull(),isReversed:Boolean = false) {
        val from = if (isReversed) toCurrency else fromCurrency
        val to = if (isReversed) fromCurrency else toCurrency
        if (from == null || to == null || amount == null) return
        val result = (from.value * amount / to.value).toString()
        if (isReversed)
            fromAmount.value = result
        else
            _convertedAmount.value = result
    }
}
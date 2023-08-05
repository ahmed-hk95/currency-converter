package ahmed.hk.currencyconverter.utils

import ahmed.hk.currencyconverter.data.models.Currency
import ahmed.hk.currencyconverter.data.remote.CurrenciesResponse

fun CurrenciesResponse.mapToModel(): List<Currency> {
    return rates?.map { Currency(symbol = it.key, value = it.value) } ?: emptyList()
}
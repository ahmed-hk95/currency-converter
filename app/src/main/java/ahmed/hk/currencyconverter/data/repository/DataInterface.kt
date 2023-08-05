package ahmed.hk.currencyconverter.data.repository

import ahmed.hk.currencyconverter.data.models.Currency

interface DataInterface {

    suspend fun getCurrencies(): List<Currency>
}
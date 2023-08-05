package ahmed.hk.currencyconverter.data.remote

interface ApiInterface {

    suspend fun getCurrencies(): CurrenciesResponse
}
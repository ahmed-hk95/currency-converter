package ahmed.hk.currencyconverter.data.repository

import ahmed.hk.currencyconverter.data.models.Currency
import ahmed.hk.currencyconverter.data.remote.ApiInterface
import ahmed.hk.currencyconverter.utils.mapToModel
import javax.inject.Inject

class DataRepository @Inject constructor(private val apiInterface: ApiInterface) : DataInterface {
    override suspend fun getCurrencies(): List<Currency> =
        api { apiInterface.getCurrencies().mapToModel() }
}

private suspend fun <T> api(operation: suspend ()->T) : T{
    return try {
        operation()
    }catch (e:Exception) {
        throw e
    }
}

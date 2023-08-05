package ahmed.hk.currencyconverter.data.remote

import ahmed.hk.currencyconverter.BuildConfig
import javax.inject.Inject

class ApiRepository @Inject constructor(private val apiService: APIs) : ApiInterface {

    private val accessKey = BuildConfig.AccessKey

    override suspend fun getCurrencies(): CurrenciesResponse =
        api { apiService.getCurrencies(accessKey) }

    private suspend fun <T : ApiResponse> api(operation: (suspend () -> T)): T {
        try {
            val result = operation()
            if (!result.success) {
                val error = result.error
                throw Exception(error?.info ?: "Something went wrong")
            }
            return result
        }catch (e:Exception){
            throw Exception("Something went wrong")
        }

    }
}
package ahmed.hk.currencyconverter.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface APIs {

    @GET("/api/latest")
    suspend fun getCurrencies(@Query("access_key") key: String): CurrenciesResponse

}
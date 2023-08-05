package ahmed.hk.currencyconverter.data.remote

import kotlinx.serialization.Serializable

@Serializable
sealed class ApiResponse{
    abstract val success: Boolean
    abstract val error: Error?
}

@Serializable
data class CurrenciesResponse(
    override val success: Boolean,
    override val error: Error? = null,
    val rates: Map<String, Double>? = null
):ApiResponse()


@Serializable
data class Error(
    val code: Int,
    val type: String,
    val info: String
)
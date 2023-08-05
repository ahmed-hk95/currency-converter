package ahmed.hk.currencyconverter.data.repository

import ahmed.hk.currencyconverter.data.remote.APIs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DataRepository  @Inject constructor(private val apiService: APIs) : DataInterface {

}

private fun <T> api(operation: suspend ()->T) : Flow<T>{
    return flow {
        try {
            emit(operation())
        }catch (e:Exception) {
            throw e
        }
    }
}

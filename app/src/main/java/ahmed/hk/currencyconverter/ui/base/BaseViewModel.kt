package ahmed.hk.currencyconverter.ui.base

import ahmed.hk.currencyconverter.data.repository.DataInterface
import ahmed.hk.currencyconverter.utils.Event
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseViewModel : ViewModel() {


    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    @Inject internal lateinit var dataHelper: DataInterface
    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    @Inject internal lateinit var eventChannel:Channel<Event>

    val eventFlow :Flow<Event> get() = eventChannel.receiveAsFlow()

    protected val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    protected fun showLoadingDialog(){
        sendEvent(Event.ShowLoadingDialog)
    }

    protected fun navigateTo(direction: NavDirections){
        sendEvent(Event.Navigate(direction))
    }

    protected fun showToast(text:String,isLong:Boolean=false){
        sendEvent(
            when(isLong){
                true -> Event.ShowToast(text,Toast.LENGTH_LONG)
                false -> Event.ShowToast(text)
            }
        )
    }

    protected fun showToast(textResource:Int,isLong:Boolean=false){
//        showToast(Utils.getStringFromResources(textResource),isLong)
    }


    protected fun sendEvent(event: Event){
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }

    protected fun <T> coroutine(
        func: suspend CoroutineScope.()-> Flow<T>,
        loading:()-> Unit = {},
        error:(Throwable) -> Unit = {},
        success:(T) -> Unit = {},
    ){
        viewModelScope.launch {
            func()
                .flowOn(Dispatchers.IO)
                .onStart {
                    _loading.value = true
                    loading()
                }
                .catch {
                    _loading.value = false
                    error(it)
                }
                .collect {
                    _loading.value = false
                    success(it)
                }
        }
    }

}
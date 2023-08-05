package ahmed.hk.currencyconverter.utils

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavDirections
import com.google.android.material.snackbar.Snackbar

sealed class Event {
    data class ShowSnackBar(val text: String = "",val duration: Int = Snackbar.LENGTH_SHORT): Event()
    data class ShowToast(val text: String = "",val duration:Int = Toast.LENGTH_SHORT): Event()
    data class OpenActivity<T : AppCompatActivity>(
        val activity:Class<T>,
        val extras:Bundle= bundleOf(),
        val requestCode:Int = -1,
        val finishCurrent:Boolean = false,
        val clearStack:Boolean = false
    )
    data class FinishTo<T : AppCompatActivity>(val activity:Class<T>) :Event()
    data class SetActivityResult(val result : Bundle = bundleOf())
    object FinishActivity: Event()
    object FinishApp: Event()
    object ShowLoadingDialog: Event()
    object HideLoadingDialog: Event()
    data class Navigate(val direction:NavDirections):Event()
}
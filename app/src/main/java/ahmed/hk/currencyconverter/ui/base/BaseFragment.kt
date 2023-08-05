package ahmed.hk.currencyconverter.ui.base

import ahmed.hk.currencyconverter.BR
import ahmed.hk.currencyconverter.utils.Event
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch

abstract class BaseFragment: Fragment() {

    abstract val viewModel:BaseViewModel
    abstract val layoutId:Int
    lateinit var viewDataBinding: ViewDataBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        observeBaseEvents()
        observeEvents()
        performDataBinding(inflater,container)
        return viewDataBinding.root
    }

    private fun performDataBinding(inflater: LayoutInflater, container: ViewGroup?) {
        viewDataBinding = DataBindingUtil.inflate(inflater,layoutId,container,false)
        with(viewDataBinding){
            setVariable(BR.viewModel,viewModel)
            lifecycleOwner = viewLifecycleOwner
            executePendingBindings()
        }

    }

    protected open fun observeEvents() {
    }

    private fun observeBaseEvents() {
        //New safer way of handling UI related actions needed to be observed by the view
        //Reference:
        //https://medium.com/androiddevelopers/a-safer-way-to-collect-flows-from-android-uis-23080b1f8bda
        lifecycleScope.launch {
            viewModel.eventFlow
                .flowWithLifecycle(lifecycle)
                .collect { event ->
                    when(event){
                        is Event.ShowToast -> showToast(event.text,event.duration)
                        is Event.Navigate -> navigateTo(event.direction)
                        else -> {}
                    }
                }
        }
    }

    protected fun navigateTo(direction: NavDirections){
        findNavController().navigate(direction)
    }

    protected fun showToast(message:String, duration: Int=Toast.LENGTH_SHORT){
        Toast.makeText(context,message,duration).show()
    }
}
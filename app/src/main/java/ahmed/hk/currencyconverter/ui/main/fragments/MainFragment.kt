package ahmed.hk.currencyconverter.ui.main.fragments

import ahmed.hk.currencyconverter.R
import ahmed.hk.currencyconverter.ui.base.BaseFragment
import ahmed.hk.currencyconverter.ui.main.MainViewModel
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment: BaseFragment() {
    override val viewModel by activityViewModels<MainViewModel>()
    override val layoutId = R.layout.fragment_main

}
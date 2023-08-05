package ahmed.hk.currencyconverter.ui.base.recyclerview

import androidx.annotation.LayoutRes

interface ItemViewModel {
    val bindableProperties: Map<Int, Any>
        get() = mapOf()

    @get:LayoutRes
    val layoutId: Int
    val viewType: Int
        get() = 0
}

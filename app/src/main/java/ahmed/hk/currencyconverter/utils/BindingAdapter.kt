package ahmed.hk.currencyconverter.utils

import ahmed.hk.currencyconverter.ui.base.recyclerview.ItemViewModel
import ahmed.hk.currencyconverter.ui.base.recyclerview.RecyclerViewAdapter
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("dropdownMenuItems","dropdownMenuListener",requireAll = false)
fun AutoCompleteTextView.bindDropdownMenu(list: List<String>,listener: ((position: Int) -> Unit)?) {
    if (list.isEmpty()) return
    val adapter = ArrayAdapter<String>(context,android.R.layout.simple_spinner_item)
    adapter.clear()
    adapter.addAll(list)
    setAdapter(adapter)
    setOnItemClickListener { _, _, position, _ ->
        listener?.invoke(position)
    }
    listSelection = 0
}

@BindingAdapter("itemViewModels","itemClickListener",requireAll = false)
fun bindItemViewModels(
    recyclerView: RecyclerView,
    itemViewModels: List<ItemViewModel>?,
    listener: ((position: Int, id: Int) -> Unit)?
) {
    val adapter = getOrCreateAdapter(recyclerView, listener = listener)
    adapter.updateItems(itemViewModels)
}

@BindingAdapter("visibleIf")
fun changeVisibility(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("invisibleIf")
fun changeInvisibility(view: View, invisible: Boolean) {
    view.visibility = if (invisible) View.INVISIBLE else View.VISIBLE
}

private fun getOrCreateAdapter(
    recyclerView: RecyclerView,
    listener: ((position: Int, id: Int) -> Unit)?
): RecyclerViewAdapter {
    return if (recyclerView.adapter is RecyclerViewAdapter) {
        recyclerView.adapter as RecyclerViewAdapter
    } else {
        val adapter = if (listener==null) RecyclerViewAdapter() else RecyclerViewAdapter(listener)
        recyclerView.adapter = adapter
        adapter
    }
}
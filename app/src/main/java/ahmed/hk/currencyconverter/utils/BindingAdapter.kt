package ahmed.hk.currencyconverter.utils

import ahmed.hk.currencyconverter.ui.base.recyclerview.ItemViewModel
import ahmed.hk.currencyconverter.ui.base.recyclerview.RecyclerViewAdapter
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("dropdownMenuItems","dropdownMenuListener")
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

@BindingAdapter("dropdownMenuSelection")
fun AutoCompleteTextView.setSelectedIndex(position: Int){
    if (position==-1 || adapter==null || position>= adapter.count) return
    val value = adapter.getItem(position) as? String
    if (value!=null){
        setText(value,false)
    }
}

@BindingAdapter("itemViewModels","itemClickListener",requireAll = false)
fun RecyclerView.bindItemViewModels(
    itemViewModels: List<ItemViewModel>?,
    listener: ((position: Int, id: Int) -> Unit)?
) {
    val adapter = getOrCreateAdapter(listener = listener)
    adapter.updateItems(itemViewModels)
}

@BindingAdapter("visibleIf")
fun View.changeVisibility(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("invisibleIf")
fun View.changeInvisibility(invisible: Boolean) {
    visibility = if (invisible) View.INVISIBLE else View.VISIBLE
}

private fun RecyclerView.getOrCreateAdapter(
    listener: ((position: Int, id: Int) -> Unit)?
): RecyclerViewAdapter {
    return if (adapter is RecyclerViewAdapter) {
        adapter as RecyclerViewAdapter
    } else {
        val adapter = if (listener==null) RecyclerViewAdapter() else RecyclerViewAdapter(listener)
        this.adapter = adapter
        adapter
    }
}
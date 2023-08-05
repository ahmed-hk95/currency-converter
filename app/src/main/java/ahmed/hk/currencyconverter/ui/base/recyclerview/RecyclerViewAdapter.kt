package ahmed.hk.currencyconverter.ui.base.recyclerview

import ahmed.hk.currencyconverter.BR
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import android.view.View


class RecyclerViewAdapter(private val clickListener:(position:Int, id:Int) -> Unit =  { _, _ ->  }) : RecyclerView.Adapter<ItemViewHolder>() {

    private var itemViewModels: List<ItemViewModel> = emptyList()
    private val viewTypeToLayoutId: MutableMap<Int, Int> = mutableMapOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding: ViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            viewTypeToLayoutId[viewType] ?: 0,
            parent,
            false)
        val holder= ItemViewHolder(binding)
        val listener = {view : View->
            val position: Int = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) clickListener(position, view.id)
        }
        binding.root.setOnClickListener(listener)
        if (binding.root is ViewGroup) addClickListeners((binding.root as ViewGroup), listener)

        return holder
    }

    private fun addClickListeners(parent: ViewGroup, listener: View.OnClickListener) {
        for (i in 0 until parent.childCount) {
            parent.getChildAt(i).setOnClickListener(listener)
            if (parent.getChildAt(i) is ViewGroup)
                addClickListeners(parent.getChildAt(i) as ViewGroup, listener)
        }
    }


    override fun getItemViewType(position: Int): Int {
        val item = itemViewModels[position]
        if (!viewTypeToLayoutId.containsKey(item.viewType)) {
            viewTypeToLayoutId[item.viewType] = item.layoutId
        }
        return item.viewType
    }

    override fun getItemCount(): Int = itemViewModels.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(itemViewModels[position])
    }

    fun updateItems(items: List<ItemViewModel>?) {
        itemViewModels = items ?: emptyList()
        notifyDataSetChanged()
    }
}

class ItemViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(itemViewModel: ItemViewModel) {
        itemViewModel.bindableProperties.forEach {
            binding.setVariable(it.key,it.value)
        }
//        binding.setVariable(BR.itemViewModel, itemViewModel)
    }
}
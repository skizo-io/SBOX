package io.comico.library.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter<T : ViewDataBinding, D>(
    val context: Context,
    @LayoutRes val cell: Int,
    val bindingVariableId: Int? = null,
    val bindingListener: (Pair<Int, Any>)? = null
) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder<T>>() {

    init {
        setHasStableIds(true)
    }

    var items: ArrayList<D>? = null
    var listener: (() -> Unit)? = null

    open class ViewHolder<T : ViewDataBinding>(
        @LayoutRes cell: Int,
        parent: ViewGroup,
        val bindingVariableId: Int? = null,
        val bindingListener: (Pair<Int, Any>)? = null
    ) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(cell, parent, false)) {
        val binding: T? = DataBindingUtil.bind(itemView)
        fun onBindViewHolder(item: Any?) {
            try {
                bindingVariableId?.let {
                    binding?.setVariable(it, item)
                }
                bindingListener?.let {
                    bindingListener.first?.let {
                        binding?.setVariable(it, bindingListener.second)
                    }
                }
            } catch (e: Exception) {
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        object : ViewHolder<T>(cell, parent, bindingVariableId, bindingListener) {}

    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) {
        items?.let {
            holder.onBindViewHolder(it.get(position))
        }
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun addItem(item: ObservableArrayList<D>) {
        items = item
    }
}

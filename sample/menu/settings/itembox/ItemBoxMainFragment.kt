package com.smackjeeves.ui.menu.settings.itembox

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smackjeeves.R
import com.smackjeeves.databinding.CellItemboxBinding
import com.smackjeeves.model.*
import com.smackjeeves.network.Api
import com.smackjeeves.network.send
import com.smackjeeves.ui.base.BaseFragment
import skizo.library.extensions.delayed
import kotlinx.android.synthetic.main.item_appbar.*
import kotlinx.android.synthetic.main.item_recyclerview.*


class ItemBoxMainFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle? = null) =
            ItemBoxMainFragment().apply {
                arguments = bundle
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.common_recyclerview, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val linearLayoutManager = LinearLayoutManager(context)
        recyclerview.setLayoutManager(linearLayoutManager)
        recyclerview.addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        )

        swipelayout.setOnRefreshListener {
            delayed(this::getContent)
        }

        appbar_title.setText(R.string.item_box)
        appbar.setVisible(back = true)

        getContent()
    }


    fun getContent() {
        swipelayout.isRefreshing = false
        Api.service.getItemBox().send(this::setContent)

    }

    fun setContent(vo: ItemBox) {

        context?.let {
            var adapter = RecyclerAdapter(it)
            recyclerview.setAdapter(adapter)
            adapter.addItem(vo.data.items)
            adapter.notifyDataSetChanged()
        }

    }

    internal class RecyclerAdapter(val context: Context) : RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder>() {
        internal inner class ItemViewHolder(val binding: CellItemboxBinding) : RecyclerView.ViewHolder(binding.root)

        private var listData: List<ItemBoxItem>? = null

        @NonNull
        override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ItemViewHolder {
            val binding: CellItemboxBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.cell_itembox, parent, false)
            return ItemViewHolder(binding)
        }

        override fun onBindViewHolder(@NonNull holder: ItemViewHolder, position: Int) {
            listData?.let {  holder.binding.data = it.get(position) }
        }

        override fun getItemCount(): Int {
            return listData?.size ?: 0
        }

        internal fun addItem(data: List<ItemBoxItem>) {
            listData = data
        }
    }


}
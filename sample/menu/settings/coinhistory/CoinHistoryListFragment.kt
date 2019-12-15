package com.smackjeeves.ui.menu.settings.coinhistory

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
import com.smackjeeves.databinding.CellCoinHistoryBinding
import com.smackjeeves.model.CoinHistory
import com.smackjeeves.model.CoinHistoryItem
import com.smackjeeves.network.Api
import com.smackjeeves.network.send
import com.smackjeeves.ui.base.BaseFragment
import com.smackjeeves.utils.bundleOf
import kotlinx.android.synthetic.main.item_recyclerview.*


class CoinHistoryListFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(isUsage: Boolean) =
            CoinHistoryListFragment().apply {
                arguments = bundleOf("isUsage".to(isUsage))
            }
    }

    var isUsage: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            isUsage = it.getBoolean("isUsage")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.item_recyclerview, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val linearLayoutManager = LinearLayoutManager(context)
        recyclerview.setLayoutManager(linearLayoutManager)
        recyclerview.addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        )

        swipelayout.isEnabled = false


        getContent()
    }

    fun getContent() {

        if(isUsage)
            Api.service.getCoinUsageHistory().send(this::setContent)
        else
            Api.service.getCoinHistory().send(this::setContent)

    }

    fun setContent(vo: CoinHistory) {

        context?.let {
            var adapter = RecyclerAdapter(it)
            recyclerview.setAdapter(adapter)

            adapter.addItem( if(isUsage) vo.data.usage else vo.data.purchase )
            adapter.notifyDataSetChanged()
        }

    }


    internal class RecyclerAdapter(val context: Context) : RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder>() {
        internal inner class ItemViewHolder(val binding: CellCoinHistoryBinding) : RecyclerView.ViewHolder(binding.root)

        private var listData: List<CoinHistoryItem>? = null

        @NonNull
        override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ItemViewHolder {
            val binding: CellCoinHistoryBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.cell_coin_history, parent, false)
            return ItemViewHolder(binding)
        }

        override fun onBindViewHolder(@NonNull holder: ItemViewHolder, position: Int) {
            listData?.let {  holder.binding.data = it.get(position) }
        }

        override fun getItemCount(): Int {
            return listData?.size ?: 0
        }

        internal fun addItem(data: List<CoinHistoryItem>) {
            listData = data
        }
    }
}


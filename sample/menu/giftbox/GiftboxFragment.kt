package com.smackjeeves.ui.menu.giftbox

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
import com.smackjeeves.ui.base.BaseFragment
import com.smackjeeves.R
import com.smackjeeves.databinding.CellGiftboxBinding
import com.smackjeeves.model.Giftbox
import com.smackjeeves.model.GiftboxItem
import com.smackjeeves.network.Api
import com.smackjeeves.network.send
import kotlinx.android.synthetic.main.cell_giftbox.view.*
import kotlinx.android.synthetic.main.fragment_menu_giftbox.*
import kotlinx.android.synthetic.main.item_appbar.*
import kotlinx.android.synthetic.main.item_recyclerview.*
import skizo.library.extensions.click
import skizo.library.extensions.delayed
import skizo.library.extensions.visible


class GiftboxFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle?) =
            GiftboxFragment().apply {
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
        return inflater.inflate(R.layout.fragment_menu_giftbox, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val linearLayoutManager = LinearLayoutManager(context)
        recyclerview.setLayoutManager(linearLayoutManager)
        recyclerview.addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        )

        appbar_title.setText(R.string.giftbox)
        appbar.setVisible(back = true)

        swipelayout.setOnRefreshListener {
            delayed(this::getContent)
        }


        giftbox_receive_all.visible = false
        giftbox_receive_all.click {
            Api.service.receiveGiftbox().send({
                getContent()
            })


            getContent()
        }

        getContent()
    }

    fun getContent() {
        swipelayout.isRefreshing = false
        Api.service.getGiftboxList().send(this::setContent)

    }

    fun setContent(vo: Giftbox) {

        context?.let {
            var adapter = RecyclerAdapter(it)
            recyclerview.setAdapter(adapter)
            adapter.addItem(vo.data.messages)
            adapter.notifyDataSetChanged()
        }

        giftbox_receive_all.visible = false
        vo.data.messages.forEach {
            if(!it.isReceived) {
                giftbox_receive_all.visible = true
                return
            }
        }

    }

    internal class RecyclerAdapter(val context: Context) : RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder>() {
        internal inner class ItemViewHolder(val binding: CellGiftboxBinding) : RecyclerView.ViewHolder(binding.root)

        private var listData: List<GiftboxItem>? = null

        @NonNull
        override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ItemViewHolder {
            val binding: CellGiftboxBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.cell_giftbox, parent, false)
            return ItemViewHolder(binding)
        }

        override fun onBindViewHolder(@NonNull holder: ItemViewHolder, position: Int) {
            listData?.let {
                var item: GiftboxItem = it.get(position)
                holder.binding.data = item

                holder.binding.root.giftbox_recieve.click {

                    /*
                    Api.service.receiveGiftbox(item.id).send({
                        item.isReceived = true
                        holder.binding.root.giftbox_recieve.visible = false
                        holder.binding.root.giftbox_read.visible = true
                    })
                    */
                    item.isReceived = true
                    holder.binding.root.giftbox_recieve.visible = false
                    holder.binding.root.giftbox_read.visible = true
                }
            }

        }

        override fun getItemCount(): Int {
            return listData?.size ?: 0
        }

        internal fun addItem(data: List<GiftboxItem>) {
            listData = data
        }
    }


}
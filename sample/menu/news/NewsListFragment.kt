package com.smackjeeves.ui.menu.news

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smackjeeves.R
import com.smackjeeves.databinding.CellNewsBinding
import com.smackjeeves.model.News
import com.smackjeeves.model.NewsContent
import com.smackjeeves.network.Api
import com.smackjeeves.network.base.ApiCallBack
import com.smackjeeves.network.send
import com.smackjeeves.ui.base.BaseFragment
import com.smackjeeves.utils.bundleOf
import kotlinx.android.synthetic.main.item_recyclerview.*


class NewsContentFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(key: String) =
            NewsContentFragment().apply {
                arguments = bundleOf("KEY".to(key))
            }
    }

    lateinit var key: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            key = it.getString("KEY")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.item_recyclerview, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        swipelayout.isEnabled = false


        getContent()
    }

    fun getContent() {

        Api.service.getNews(key).send(this::setContent)

    }

    fun setContent(vo: News) {

        val linearLayoutManager = LinearLayoutManager(context)
        recyclerview.setLayoutManager(linearLayoutManager)

        context?.let {
            var adapter = RecyclerAdapter(it)
            recyclerview.setAdapter(adapter)

            adapter.addItem(vo.data.news)
            adapter.notifyDataSetChanged()
        }

    }


    internal class RecyclerAdapter(val context: Context) : RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder>() {
        internal inner class ItemViewHolder(val binding: CellNewsBinding) : RecyclerView.ViewHolder(binding.root)

        private var listData: List<NewsContent>? = null

        @NonNull
        override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ItemViewHolder {
            val binding: CellNewsBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.cell_news, parent, false)
            return ItemViewHolder(binding)
        }

        override fun onBindViewHolder(@NonNull holder: ItemViewHolder, position: Int) {
            listData?.let {  holder.binding.data = it.get(position) }
        }

        override fun getItemCount(): Int {
            return listData?.size ?: 0
        }

        internal fun addItem(data: List<NewsContent>) {
            listData = data
        }
    }
}


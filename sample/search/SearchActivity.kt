package com.smackjeeves.ui.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smackjeeves.R
import com.smackjeeves.databinding.CellTitleListBinding
import com.smackjeeves.databinding.CellTrendwordBinding
import com.smackjeeves.model.Titles
import com.smackjeeves.model.TrendWords
import com.smackjeeves.model.TrendWordsData
import com.smackjeeves.model.base.Result
import com.smackjeeves.model.item.Title
import com.smackjeeves.network.Api
import com.smackjeeves.network.send
import com.smackjeeves.ui.base.BaseActivity
import com.smackjeeves.ui.component.appbar.SearchAppBar
import skizo.library.extensions.getStringFromRes
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.item_appbar.*
import kotlinx.android.synthetic.main.item_recyclerview.*

class SearchActivity: BaseActivity(), SearchAppBar.OnSearchActionListener {


    companion object {
        val INTENT_KEYWORD = "intentKeyword"

    }


    interface OnTrendSearchListener {
        fun onClickTrend(trendword: String)
    }



    lateinit var serachAppbar: SearchAppBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)

        setContentView(R.layout.activity_search)


        swipelayout.isEnabled = false


        val linearLayoutManager = LinearLayoutManager(applicationContext)
        recyclerview.setLayoutManager(linearLayoutManager)
        recyclerview.addItemDecoration(
            DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        )

        var keyword: String = ""

        try {
            val bundle = intent.extras
            keyword = intent.getStringExtra(INTENT_KEYWORD)
        } catch (e: Exception) {

        }


        appbar.setVisible(back = true)
        serachAppbar = SearchAppBar(this)
        appbar.setCustomAppbar(serachAppbar)


        if(keyword.isNullOrEmpty())
            getTrendword()
        else
            getContent(keyword)
    }




    fun getTrendword() {
        search_header_text.textSize = 16f
        search_header_text.text = getStringFromRes(R.string.trending_searches)
        setTrendword(TrendWords(Result(200), TrendWordsData(listOf("Romance","Coin","Ranking","Hosting","Author","#Action"))))
    }

    fun setTrendword(vo: TrendWords) {
        recyclerview.adapter = TrendAdapter(this, vo.data.trendWords, object : OnTrendSearchListener {
            override fun onClickTrend(trendword: String) {
                getContent(trendword)
            }
        })
    }


    fun getContent(keyword: String) {
        search_header_text.textSize = 21f
        search_header_text.text = keyword

        swipelayout.isRefreshing = false
        Api.service.getDaily().send(this::setContent)

    }

    fun setContent(vo: Titles) {
        recyclerview.adapter = TitlesAdapter(this, vo.data.titles)
    }


    override fun onSearch(keyword: String) {
        getContent(keyword)
    }





    internal class TrendAdapter(val context: Context, var listData: List<String>, var listener: OnTrendSearchListener) : RecyclerView.Adapter<TrendAdapter.ItemViewHolder>() {
        internal inner class ItemViewHolder(val binding: CellTrendwordBinding) : RecyclerView.ViewHolder(binding.root)

        @NonNull
        override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ItemViewHolder {
            val binding: CellTrendwordBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.cell_trendword, parent, false)
            return ItemViewHolder(binding)
        }

        override fun onBindViewHolder(@NonNull holder: ItemViewHolder, position: Int) {
            listData?.let {
                holder.binding.data = it.get(position)
                holder.binding.listener = listener
            }
        }

        override fun getItemCount(): Int {
            return listData?.size
        }

    }



    internal class TitlesAdapter(val context: Context, val listData: List<Title>) : RecyclerView.Adapter<TitlesAdapter.ItemViewHolder>() {
        internal inner class ItemViewHolder(val binding: CellTitleListBinding) : RecyclerView.ViewHolder(binding.root)


        @NonNull
        override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ItemViewHolder {
            val binding: CellTitleListBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.cell_title_list, parent, false)
            return ItemViewHolder(binding)
        }

        override fun onBindViewHolder(@NonNull holder: ItemViewHolder, position: Int) {
            listData?.let {  holder.binding.data = it.get(position) }
        }

        override fun getItemCount(): Int {
            return listData?.size ?: 0
        }

    }

}


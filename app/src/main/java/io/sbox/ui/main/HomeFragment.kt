package io.comico.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import io.comico.library.extensions.observeNotNull
import io.comico.library.extensions.trace
import kotlinx.android.synthetic.main.item_recyclerview.*
import io.sbox.R
import io.sbox.ui.base.BaseFragment

class HomeFragment : BaseFragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        return inflater.inflate(R.layout.fragment_recyclerview_refresh, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        homeViewModel.text.observeNotNull(this, {

            trace("@@@@@@@@@@@@@@@@@@@@@@ observe ", it)

//            setContent(it)
        })

        val linearLayoutManager = LinearLayoutManager(context)
        recyclerview.setLayoutManager(linearLayoutManager)

        /*
        swipe_refresh_recyclerview.setOnRefreshListener {
            delayed({
                homeViewModel.getContent()                                                                          
            })
        }
*/


/*
        homeViewModel.content.observe(this, Observer {
            it?.let { setContent(it) }
        })
*/

//        homeViewModel.getContent()

    }

/*

    fun setContent(model: HomeModel) {
        trace("@@@@@@@@@@@@@@@@@@@ home model ", model)

        swipe_refresh_recyclerview.isRefreshing = false

        context?.let {
            var adapter = RecyclerAdapter(it)
            recyclerview.setAdapter(adapter)
            adapter.addItem(model)
            adapter.notifyDataSetChanged()
        }


    }

    internal class RecyclerAdapter(var context: Context) : RecyclerView.Adapter<BaseHomeViewHolder>() {

        private val TYPE_ROTATION = 0
        private val TYPE_HORIZONTAL_LIST = 1
        private val TYPE_HORIZONTAL_RANKING = 2
        private val TYPE_NEWS = 3
        private val TYPE_TAGS = 4

        private var listData: HomeModel? = null

        @NonNull
        override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): BaseHomeViewHolder {
            var holder: BaseHomeViewHolder
            holder = when (viewType) {
                TYPE_ROTATION -> HomeRotationView(
                    LayoutInflater.from(context).inflate(
                        R.layout.view_home_rotation,
                        null
                    )
                )
                TYPE_HORIZONTAL_LIST -> HomeHorizontalView(
                    LayoutInflater.from(context).inflate(
                        R.layout.view_home_horizontal,
                        null
                    )
                )
                TYPE_HORIZONTAL_RANKING -> HomeHorizontalView(
                    LayoutInflater.from(context).inflate(
                        R.layout.view_home_horizontal,
                        null
                    ), true
                )
//                TYPE_NEWS -> HomeNewsView(LayoutInflater.from(context).inflate(R.layout.view_home_news, null))
//                TYPE_TAGS -> HomeTagsView(LayoutInflater.from(context).inflate(R.layout.view_home_tags, null))
                else -> HomeRotationView(LayoutInflater.from(context).inflate(R.layout.view_home_rotation, null))
            }
            return holder
        }

        override fun onBindViewHolder(@NonNull holder: BaseHomeViewHolder, position: Int) {
            listData?.let {
                when (holder) {
                    is HomeRotationView -> holder.setContent(it.data.rotation)
//                    is HomeNewsView -> holder.setContent(it.data.news)
//                    is HomeTagsView -> holder.setContent(it.data.tags)
                    is HomeHorizontalView -> if(position > 0) holder.setContent(it.data.contents.get(position - 1))
                }
            }
        }

        override fun getItemViewType(position: Int): Int {

            var type = when (position) {
                0 -> TYPE_ROTATION
                itemCount - 2 -> TYPE_NEWS
                itemCount - 1 -> TYPE_TAGS
                else -> {
                    listData?.let {
                        if (it.data.contents.get(position - 1).type.equals("ranking"))
                            TYPE_HORIZONTAL_RANKING
                        else
                            TYPE_HORIZONTAL_LIST
                    }
                }
            }
            return type ?: TYPE_ROTATION
        }

        override fun getItemCount(): Int {
            listData?.let {
                return it.count
            }
            return 0
        }

        internal fun addItem(data: HomeModel) {
            listData = data
        }
    }
*/



}
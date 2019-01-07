package com.dnkilic.carousellnews.ui.news

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.dnkilic.carousellnews.binding.FragmentDataBindingComponent
import com.dnkilic.carousellnews.R
import com.dnkilic.carousellnews.databinding.NewsListFragmentBinding
import com.dnkilic.carousellnews.extension.visible
import com.dnkilic.carousellnews.repository.model.Command
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.news_list_fragment.error
import kotlinx.android.synthetic.main.news_list_fragment.newsList
import javax.inject.Inject

class NewsListFragment : Fragment() {

    @Inject
    lateinit var viewModeFactory: ViewModelProvider.Factory

    private lateinit var viewModel: NewsListViewModel


    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    lateinit var binding : NewsListFragmentBinding
    private lateinit var adapter: NewsAdapter

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<NewsListFragmentBinding>(
                inflater,
                R.layout.news_list_fragment,
                container,
                false
        )
        binding = dataBinding
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this,
                viewModeFactory).get(NewsListViewModel::class.java)
        binding.setLifecycleOwner(viewLifecycleOwner)
        adapter = NewsAdapter(dataBindingComponent) { news ->
            // TODO navigate to detail
        }
        binding.newsList.adapter = adapter
        binding.command = viewModel.news
        viewModel.news.observe(this, Observer {
            status ->
            when (status) {
                is Command.Success -> {
                    newsList.visible()
                    adapter.submitList(status.news)
                    binding.newsList.smoothScrollToPosition(0)
                }
                is Command.Error -> {
                    showErrorMessage(status.code)
                }
                is Command.Loading -> {
                    adapter.submitList(emptyList())
                }
            }

        })

        viewModel.getNews()
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item!!.itemId) {
        R.id.recent -> {
            viewModel.sortNewsByDate()
            true
        }
        R.id.popular -> {
            viewModel.sortNewsByRankAndDate()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun showErrorMessage(code: Int?) {
        error.visible()
        error.text = getText(R.string.network_error)
    }

}

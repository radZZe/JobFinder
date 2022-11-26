package com.example.jobfinder.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobfinder.R
import com.example.jobfinder.data.models.Project
import com.example.jobfinder.databinding.FragmentMainScreenBinding
import com.example.jobfinder.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainScreenFragment : Fragment() {

    private var _binding: FragmentMainScreenBinding? = null
    private val mBinding get() = _binding!!

    private lateinit var itemsArrayList: ArrayList<Project>
    private lateinit var adapter: MainListAdapter
    private val mViewModel: MainScreenViewModel by viewModels()
    private lateinit var rvProjectList: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainScreenBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        initialization()
        mViewModel.liveProjects.observe(this, Observer { list ->
            list?.let {
                adapter.updateList(it)
            }
        })
        if (IS_FILTERED) {
            filter()
            IS_FILTERED = false
        } else {
            mViewModel.getProjects { mBinding.progressBar.visibility = View.GONE }
        }
    }

    private fun initialization() {
        setupRecyclerView()
        setupSearchView()
        mBinding.btnFilter.setOnClickListener {
            APP_ACTIVITY.navController.navigate(R.id.action_mainScreenFragment2_to_filterProjectsFragment)
        }
    }

    private fun setupRecyclerView() {
        rvProjectList = mBinding.rvProjectList
        with(rvProjectList) {
            recycledViewPool.setMaxRecycledViews(
                MainListAdapter.VIEW_TYPE,
                MainListAdapter.MAX_POOL_SIZE
            )
            layoutManager = LinearLayoutManager(APP_ACTIVITY)
            setHasFixedSize(true)
        }

        itemsArrayList = arrayListOf()
        adapter = MainListAdapter(itemsArrayList, object : ProjectListener {
            override fun onProjectClicked(project: Project) {
                val bundle = Bundle()
                bundle.putSerializable(KEY_CLICKED_PROJECT, project)
                APP_ACTIVITY.navController.navigate(
                    R.id.action_mainScreenFragment2_to_projectFragment,
                    bundle
                )
            }
        })
        rvProjectList.adapter = adapter
    }

    private fun setupSearchView() {
        mBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.getFilter().filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.getFilter().filter(newText)
                return true
            }

        })
    }

    private fun filter() {
        val type = arguments?.get(KEY_FILTER)
        mViewModel.getFilteredProjects(type.toString()) { mBinding.progressBar.visibility = View.GONE }
    }

}
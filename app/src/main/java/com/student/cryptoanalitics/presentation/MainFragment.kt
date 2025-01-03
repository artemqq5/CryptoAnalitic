package com.student.cryptoanalitics.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.student.cryptoanalitics.R
import com.student.cryptoanalitics.databinding.FragmentMainBinding
import com.student.cryptoanalitics.domain.models.CryptoCoinModel
import com.student.cryptoanalitics.presentation.adapters.my_coins.CryptoCoinsPrivateAdapter
import com.student.cryptoanalitics.presentation.adapters.my_coins.CryptoCoinsPrivateClick
import com.student.cryptoanalitics.presentation.vm.LoadCryptoCoinsViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class MainFragment : Fragment(), CryptoCoinsPrivateClick {

    private lateinit var binding: FragmentMainBinding

    private val loadCryptoCoinsViewModel: LoadCryptoCoinsViewModel by activityViewModel()

    private val cryptoAdapter by lazy {
        CryptoCoinsPrivateAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerCrypto.adapter = cryptoAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            loadCryptoCoinsViewModel.isLoading.collect { isLoading ->
                binding.progressBar.visibility =
                    if (isLoading) View.VISIBLE else View.GONE
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            loadCryptoCoinsViewModel.isRefreshing.collect { isRefreshing ->
                binding.swipeRefresh.isRefreshing = isRefreshing
            }
        }

        lifecycleScope.launch {
            loadCryptoCoinsViewModel.coinsFlow.collectLatest {
                cryptoAdapter.submitList(it)
            }
        }

        binding.swipeRefresh.setOnRefreshListener {
            if(!loadCryptoCoinsViewModel.isLoading.value) {
                loadCryptoCoinsViewModel.loadCoinsData(true)
            }else {
                binding.swipeRefresh.isRefreshing = false
            }
        }

        binding.recyclerCrypto.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                if (layoutManager.findLastCompletelyVisibleItemPosition() == cryptoAdapter.itemCount - 1) {
                    loadCryptoCoinsViewModel.loadCoinsData()
                }
            }
        })


        binding.addCrypto.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_addCryptoFragment)
        }


    }

    override fun clickCoin(cryptoCoinModel: CryptoCoinModel) {
        val navController = findNavController()
        if (navController.currentDestination?.id == R.id.mainFragment) {
            navController.navigate(
                R.id.action_mainFragment_to_detailCryptoFragment,
                bundleOf("coin" to cryptoCoinModel)
            )
        }
    }
}
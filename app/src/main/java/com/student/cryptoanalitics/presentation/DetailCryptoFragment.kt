package com.student.cryptoanalitics.presentation

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.student.cryptoanalitics.R
import com.student.cryptoanalitics.databinding.FragmentDetailCryptoBinding
import com.student.cryptoanalitics.domain.models.CryptoCoinModel
import com.student.cryptoanalitics.presentation.vm.DeleteCoinsViewModel
import com.student.cryptoanalitics.presentation.vm.GeminiViewModel
import com.student.cryptoanalitics.presentation.vm.LoadCryptoCoinsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailCryptoFragment : Fragment() {

    private lateinit var binding: FragmentDetailCryptoBinding
    private val deleteCoinsViewModel: DeleteCoinsViewModel by viewModel()
    private val geminiViewModel: GeminiViewModel by viewModel()
    private val loadCryptoCoinsViewModel: LoadCryptoCoinsViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailCryptoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: CryptoCoinModel? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("coin", CryptoCoinModel::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable("coin")
        }

        args?.let { coin ->
            Picasso.get().load(coin.coinImg).into(binding.coinIcon)

            binding.coinName.text = coin.coinName
            binding.marketPrice.text = coin.marketPrice
            binding.priceChange.text = coin.volMktCap

            binding.circulatingSupplyValue.text =
                resources.getString(R.string.circulating_supply, coin.circulatingSupply)
            binding.marketCapValue.text = resources.getString(R.string.market_cap, coin.marketCap)
            binding.volume24hValue.text = resources.getString(R.string.volume_24h, coin.volume24h)

            binding.delete.setOnClickListener {
                deleteCoinsViewModel.deleteCoin(coin.coinName)

                // update main coins list UI after deleted coin
                loadCryptoCoinsViewModel.loadCoinsData(isRefresh = true, immediate = true)
                findNavController().navigate(R.id.action_detailCryptoFragment_to_mainFragment)
            }

            lifecycleScope.launch {
                geminiViewModel.analiseCoinGemini.collect {
                    it?.let {
                        binding.analiseText.text = it
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.analiseText.visibility = View.VISIBLE
                    }
                }
            }

            geminiViewModel.requestAnaliseCoin(coin)

        }



        binding.back.setOnClickListener {
            findNavController().navigate(R.id.action_detailCryptoFragment_to_mainFragment)
        }

    }
}
package com.student.cryptoanalitics.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.student.cryptoanalitics.App.Companion.mylog
import com.student.cryptoanalitics.R
import com.student.cryptoanalitics.databinding.FragmentAddCryptoBinding
import com.student.cryptoanalitics.domain.models.currencies.CryptoCurrencyModel
import com.student.cryptoanalitics.presentation.adapters.public_coins.CryptoCurrencyPublicAdapter
import com.student.cryptoanalitics.presentation.adapters.public_coins.CryptoCurrencyPublicClick
import com.student.cryptoanalitics.presentation.vm.InsertCoinsViewModel
import com.student.cryptoanalitics.presentation.vm.LoadCryptoCoinsViewModel
import com.student.cryptoanalitics.presentation.vm.PublicCoinsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddCryptoFragment : Fragment(), CryptoCurrencyPublicClick {

    private lateinit var binding: FragmentAddCryptoBinding

    private val publicCoinsViewModel: PublicCoinsViewModel by viewModel()
    private val insertCoinsViewModel: InsertCoinsViewModel by viewModel()
    private val loadCryptoCoinsViewModel: LoadCryptoCoinsViewModel by activityViewModel()

    private val cryptoAdapter by lazy {
        CryptoCurrencyPublicAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddCryptoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerCrypto.adapter = cryptoAdapter

        binding.recyclerCrypto.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                if (layoutManager.findLastCompletelyVisibleItemPosition() == cryptoAdapter.itemCount - 1) {
                    publicCoinsViewModel.loadCoins()
                }
            }
        })

        viewLifecycleOwner.lifecycleScope.launch {
            publicCoinsViewModel.coins.collect { newsList ->
                cryptoAdapter.submitList(newsList?.coinList)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            publicCoinsViewModel.isLoading.collect { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }

        binding.back.setOnClickListener {
            findNavController().navigate(R.id.action_addCryptoFragment_to_mainFragment)
        }
    }

    override fun addCrypto(button: MaterialButton, cryptoCurrencyModel: CryptoCurrencyModel) {
        publicCoinsViewModel.coinExist(cryptoCurrencyModel.coinName) {
            button.visibility = if (it) View.INVISIBLE else View.VISIBLE
        }

        button.setOnClickListener {
            insertCoinsViewModel.addNewCoin(cryptoCurrencyModel) {
                if (it) {
                    button.visibility = View.INVISIBLE

                    // update main coins list UI
                    loadCryptoCoinsViewModel.loadCoinsData(isRefresh = true, immediate = true)
                } else {
                    Snackbar.make(
                        binding.root,
                        "Крипто-токен не додано, виникла помилка",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
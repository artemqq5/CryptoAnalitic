package com.student.cryptoanalitics.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.student.cryptoanalitics.databinding.FragmentMainBinding
import com.student.cryptoanalitics.domain.usecases.GetCoinHTMLUseCase
import com.student.cryptoanalitics.domain.usecases.GetCryptoCurrenciesHTMLUseCase
import org.koin.android.ext.android.inject

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val test: GetCoinHTMLUseCase by inject()
    private val test2: GetCryptoCurrenciesHTMLUseCase by inject()

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
//        test.getCoinData("solana")
//        test2.getCoinsList(2)
    }
}
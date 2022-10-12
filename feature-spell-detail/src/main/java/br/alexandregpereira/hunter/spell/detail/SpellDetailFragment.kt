package br.alexandregpereira.hunter.spell.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import br.alexandregpereira.hunter.spell.detail.ui.SpellDetailScreen
import br.alexandregpereira.hunter.ui.util.createComposeView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SpellDetailFragment : Fragment() {

    private val viewModel: SpellDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return requireContext().createComposeView { contentPadding ->
            val state by viewModel.state.collectAsState()
            SpellDetailScreen(
                state = state,
                contentPadding = contentPadding,
                onClose = viewModel::onClose
            )
        }
    }
}

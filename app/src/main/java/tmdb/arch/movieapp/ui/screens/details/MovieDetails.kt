package tmdb.arch.movieapp.ui.screens.details

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.arch.utils.UiState
import com.example.arch.utils.delegates.viewBinding
import com.example.arch.utils.extensions.collectRepeatOnStart
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import tmdb.arch.movieapp.BuildConfig
import tmdb.arch.movieapp.R
import tmdb.arch.movieapp.databinding.MovieDetailsBinding

class MovieDetails : Fragment(R.layout.movie_details) {

    private val binding by viewBinding(MovieDetailsBinding::bind)
    private val args by navArgs<MovieDetailsArgs>()
    private val viewModel by viewModel<MoviesDetailsViewModel> { parametersOf(args.id) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        subscribeUi()
    }

    private fun initView() {
        binding.toWatchButton.setOnClickListener {
            viewModel.onToWatchButtonClicked()
        }

        binding.saveButton.setOnClickListener {
            viewModel.onFavoritesButtonClicked()
        }
    }

    private fun subscribeUi() {
        viewModel.movie.collectRepeatOnStart(viewLifecycleOwner) { state ->
            when (state) {
                UiState.Loading -> {
                    binding.contentLayout.isVisible = false
                    binding.loadingIndicator.isVisible = true
                }

                UiState.Error -> {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Error")
                        .setMessage("Could not retrieve information about the movie!")
                        .setPositiveButton("Retry") { _, _ ->
                            viewModel.onRetryClicked()
                        }
                        .setNegativeButton("Go back") { _, _ ->
                            findNavController().navigateUp()
                        }
                        .show()
                }

                is UiState.Result -> {
                    val item = state.item

                    binding.contentLayout.isVisible = true
                    binding.loadingIndicator.isVisible = false

                    binding.originalTitle.text = item.originalTitle
                    binding.title.text = item.title
                    binding.overview.text = item.overview
                    binding.genres.text = item.genres?.joinToString()
                    binding.poster.load(BuildConfig.IMAGE_URL + item.posterPath)

                    binding.toWatchIcon.setColorFilter(
                        if (item.isToWatch) {
                            Color.RED
                        } else {
                            Color.BLACK
                        }
                    )
                    binding.saveIcon.setColorFilter(
                        if (item.isFavored) {
                            Color.RED
                        } else {
                            Color.BLACK
                        }
                    )
                }
            }
        }
    }
}

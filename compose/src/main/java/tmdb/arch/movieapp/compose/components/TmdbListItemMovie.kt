package tmdb.arch.movieapp.compose.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import tmdb.arch.movieapp.compose.R
import tmdb.arch.movieapp.compose.theme.TmdbTheme

@Composable
fun TmdbListItemMovie(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    language: String? = null,
    rating: String? = null,
    playTime: Int? = null,
    model: Any? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        SubcomposeAsyncImage(
            model = model,
            contentDescription = "Movie Poster",
            contentScale = ContentScale.Crop,
            loading = { ImageLoading() },
            error = { TmdbImageLoadingError() },
            modifier = Modifier
                .size(width = 90.dp, height = 135.dp)
                .clip(RoundedCornerShape(6.dp))
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (language != null) {
                TmdbIconText(
                    painter = painterResource(R.drawable.ic_globe),
                    text = "Language: $language"
                )
            }

            if (rating != null) {
                Spacer(modifier = Modifier.height(10.dp))
                TmdbIconText(
                    painter = painterResource(R.drawable.ic_star),
                    text = "Rating: $rating / 10 TMDB"
                )
            }

            if (playTime != null) {
                val hours = playTime / 60
                val mins = playTime % 60

                Spacer(modifier = Modifier.height(10.dp))

                TmdbIconText(
                    painter = painterResource(R.drawable.ic_clock),
                    text = "Duration: $hours h $mins m"
                )
            }
        }
    }
}

@Composable
private fun ImageLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant
            )
    )
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TmdbListItemMoviePreview() {
    TmdbTheme {
        TmdbListItemMovie(
            title = "Terminator",
            language = "French",
            rating = "8.8",
            playTime = 136,
            model = null,
            onClick = {}
        )
    }
}

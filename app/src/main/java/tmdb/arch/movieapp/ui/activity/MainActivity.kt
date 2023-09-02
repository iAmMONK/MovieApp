package tmdb.arch.movieapp.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import tmdb.arch.movieapp.databinding.ActivityMainBinding
import tmdb.arch.movieapp.utils.delegates.viewBinding

class MainActivity : AppCompatActivity() {

    val binding: ActivityMainBinding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}

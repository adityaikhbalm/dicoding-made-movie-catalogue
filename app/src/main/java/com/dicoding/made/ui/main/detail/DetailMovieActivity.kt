package com.dicoding.made.ui.main.detail

import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.made.BuildConfig
import com.dicoding.made.ui.base.BaseActivity
import com.dicoding.made.utils.ConvertAny
import com.dicoding.made.utils.ConvertAny.convertColor
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.activity_detail_movie.*
import kotlinx.android.synthetic.main.detail_movie_cast.*
import kotlinx.android.synthetic.main.detail_movie_overview.*
import kotlinx.android.synthetic.main.detail_movie_recommendation.*
import kotlinx.android.synthetic.main.detail_movie_title.*
import kotlinx.android.synthetic.main.detail_movie_trailer.*
import com.dicoding.made.R
import com.dicoding.made.data.local.model.*
import com.dicoding.made.data.remote.network.Status
import com.dicoding.made.ui.widget.FavoriteWidget
import com.dicoding.made.utils.AppPreferences
import com.dicoding.made.utils.Constant
import kotlinx.android.synthetic.main.detail_movie_rating.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class DetailMovieActivity : BaseActivity(), DetailMovieCallback {

    private var data: Movie? = null
    private var favorite: Boolean? = null
    private var update: Boolean = true
    private val detailViewModel: DetailViewModel by viewModel()
    private var titleBar: String = ""
    private val key by lazy { intent.getLongExtra("detail",0) }
    private val type by lazy { intent.getStringExtra("type") }
    private val arrow by lazy { arrayListOf(detail_overview_arrow,detail_cast_arrow,detail_trailer_arrow,detail_recommendation_arrow) }
    private val card by lazy { arrayListOf(detail_overview,detail_cast,detail_trailer,detail_recommendation) }

    override fun setView(): Int = R.layout.activity_detail_movie

    override fun activityCreated(savedInstance: Bundle?) {
        AppPreferences.init(this)
        if (savedInstance == null) detailViewModel.detailMovie(arrayOf(key.toString(),AppPreferences.language,type))

        setupToolbar()
        favoriteControl()
        buttonShowControl()
        contentMovieControl()
    }

    private fun setupToolbar() {
        setSupportActionBar(detail_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_circle)

        detail_appbar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = true
            var scrollRange = -1

            override fun onOffsetChanged(p0: AppBarLayout?, p1: Int) {
                if (scrollRange == -1) {
                    scrollRange = detail_appbar.totalScrollRange
                }
                if (scrollRange + p1 == 0) {
                    detail_collapsing_toolbar.title = titleBar
                    detail_collapsing_toolbar.setCollapsedTitleTextColor(this@DetailMovieActivity.convertColor(R.color.high_white))
                    isShow = true
                }
                else if(isShow) {
                    detail_collapsing_toolbar.title = " "
                    isShow = false
                }
            }
        })
    }

    private fun favoriteControl() {
        btn_favorite.setOnClickListener {
            if (favorite == true) {
                favorite = false
                detailViewModel.deleteMovie(data)
            }
            else {
                update = false
                favorite = true
                detailViewModel.saveMovie(data,type)
            }
            changeButtonFavorite()
            refreshWidget()
        }
    }

    private fun changeButtonFavorite() {
        if (favorite == true) btn_favorite.setText(R.string.remove_favorite)
        else btn_favorite.setText(R.string.add_favorite)
    }

    private fun buttonShowControl() {
        for (i in 0 until arrow.size) {
            arrow[i].setOnClickListener {
                val rotationAngle: Float
                if (card[i].isVisible) {
                    rotationAngle = 0f
                    card[i].isVisible = false
                }
                else {
                    rotationAngle = 180f
                    card[i].isVisible = true
                }
                arrow[i].animate().rotation(rotationAngle).setDuration(300).start()
            }
        }
    }

    private fun contentMovieControl() {
        detailViewModel.fetchDetailMovie.observe(this, Observer { movie ->
            if (update) {
                when (movie.status) {
                    Status.SUCCESS -> {
                        showLoading(false)
                        data = movie.data?.detail
                        setMovieContent(movie.data?.detail)
                        setRecyclerCast(movie.data?.castList)
                        setRecyclerTrailer(movie.data?.trailerList)
                        setRecyclerSimiliar(movie.data?.similarList)
                        favorite = movie.data?.detail?.isFavorite ?: false
                        changeButtonFavorite()
                    }
                    Status.ERROR -> {
                        showError(movie.message)
                        showLoading(true)
                    }
                    Status.LOADING -> {
                        showLoading(true)
                    }
                }
            }
        })
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    @SuppressLint("SimpleDateFormat", "DefaultLocale")
    private fun setMovieContent(data: Movie?) {
        data?.let {
            val title: String
            val realDate: String?
            val duration: String
            val dateMovie =
                if (it.releaseDate?.isNotEmpty() == true)
                    " (" + (it.releaseDate?.substring(0,4)) + ")"
                else ""
            val dateTv =
                if (it.firstAirDate?.isNotEmpty() == true)
                    " (" + (it.firstAirDate?.substring(0,4)) + ")"
                else ""
            if (type == "movie") {
                title = it.title + dateMovie
                realDate = it.releaseDate
                duration = it.runtime + "m"
            }
            else {
                title = it.name + dateTv
                realDate = it.firstAirDate
                duration = if (it.episodeRunTime.isNullOrEmpty()) "m"
                else it.episodeRunTime?.get(0) + "m"
            }
            titleBar = title
            if (it.originalLanguage?.isNotEmpty() == true) {
                val locale = Locale(it.originalLanguage)
                val language = locale.displayLanguage
                detail_language.text = language.capitalize()
            }
            if (realDate?.isNotEmpty() == true) {
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val d = sdf.parse(realDate)
                sdf.applyPattern("dd-MM-yyyy")
                val date = sdf.format(d)
                detail_release.text = date
            }
            detail_title.text = title
            Glide.with(this)
                .load(BuildConfig.IMAGE_URL + Constant.image_size_500 + it.backdropPath)
                .centerCrop()
                .into(detail_image)
            Glide.with(this)
                .load(BuildConfig.IMAGE_URL + Constant.image_size_185 + it.posterPath)
                .centerCrop()
                .into(detail_image_poster)
            detail_user.text = it.voteCount.toString()
            detail_rating.text = it.voteAverage.toString()
            detail_duration.text = duration
            var overview = it.overview.toString()
            if (overview.isEmpty()) overview = getString(R.string.no_translations)

            detail_text_overview.text = overview

            it.genres?.let { genreList ->
                chip_group.removeAllViews()
                for (i in genreList.indices) {
                    if (i < 3) {
                        val chip = Chip(chip_group.context)
                        chip.id = i
                        val genre = genreList[i].name
                        with(chip) {
                            text = genre
                            chipStrokeWidth = ConvertAny.convertDpToPixel(1f).toFloat()
                            chipStrokeColor = AppCompatResources.getColorStateList(this@DetailMovieActivity, R.color.low_white)
                            chipBackgroundColor = AppCompatResources.getColorStateList(this@DetailMovieActivity, R.color.high_dark)
                            setTextColor(ContextCompat.getColor(this@DetailMovieActivity,R.color.medium_white))
                            textSize = 13f
                            chipStartPadding = 0f
                            chipEndPadding = 0f

                            val layoutParams = ChipGroup.LayoutParams(
                                ChipGroup.LayoutParams.WRAP_CONTENT,
                                ConvertAny.convertDpToPixel(22f)
                            )
                            this.layoutParams = layoutParams
                            chip_group.addView(chip)
                        }
                    }
                }
            }
        }
    }

    private fun setRecyclerCast(data: List<Cast>?) {
        data?.let {
            with(detail_recycler_cast) {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(this@DetailMovieActivity, RecyclerView.HORIZONTAL, false)
                adapter = CastAdapter(it)
            }
        }
    }

    private fun setRecyclerTrailer(data: List<Trailer>?) {
        data?.let {
            with(detail_recycler_trailer) {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(this@DetailMovieActivity, RecyclerView.HORIZONTAL, false)
                adapter = TrailerAdapter(this@DetailMovieActivity,it)
            }
        }
    }

    private fun setRecyclerSimiliar(data: List<Similar>?) {
        data?.let {
            with(detail_recycler_recommendation) {
                setHasFixedSize(true)
                layoutManager =
                    LinearLayoutManager(this@DetailMovieActivity, RecyclerView.HORIZONTAL, false)
                adapter = SimiliarAdapter(type, this@DetailMovieActivity, it)
            }
        }
    }

    private fun showError(message: String?) {
        showLoading(false)
        if (message != null) showSnackBar(message.toInt())
    }

    private fun showLoading(loading: Boolean) {
        detail_loading.isVisible = loading
        if (!loading) {
            detail_layout.isVisible = true
            detail_appbar.isVisible = true
        }
        else {
            detail_layout.isVisible = false
            detail_appbar.isVisible = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.refresh_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.menu_refresh -> {
                refreshContent(key)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun refreshContent(id: Long) {
        showLoading(true)
        update = true
        chip_group.removeAllViews()
        detailViewModel.detailMovie(arrayOf(id.toString(),AppPreferences.language,type))
        detail_nestedScrollView.scrollTo(0,0)
        detail_appbar.setExpanded(true)
        card[0].isVisible = true
        arrow[0].rotation = 180f
        for (i in 1 until arrow.size) {
            card[i].isVisible = false
            arrow[i].rotation = 0f
        }
    }

    private fun refreshWidget() {
        val intent = Intent(this, FavoriteWidget::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val ids = AppWidgetManager.getInstance(application).getAppWidgetIds(
            ComponentName(
                application,
                FavoriteWidget::class.java
            )
        )
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        sendBroadcast(intent)
    }

    override fun getMovie(id: Long) {
        refreshContent(id)
    }
}

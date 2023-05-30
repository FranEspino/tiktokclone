package com.fraporitmos.tiktok


import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.util.Pools
import com.fraporitmos.Video
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.FileDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSink
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.exoplayer2.util.Util
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@SuppressLint("UnrememberedMutableState")

@Composable
fun VideoList(
    modifier: Modifier,
    videos: List<Video>
) {
    val lazyListState = rememberLazyListState()
    val currentVideoIndex = remember { mutableStateOf(0) }

    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect { index ->
                currentVideoIndex.value = index
            }
    }

    LaunchedEffect(currentVideoIndex.value) {
        lazyListState.animateScrollToItem(currentVideoIndex.value)
    }

    LazyColumn(
        modifier = modifier,
        state = lazyListState,
    ) {
        items(count = Int.MAX_VALUE) { index ->
            val videoIndex = index % videos.size
            VideoItem(
                video = videos[videoIndex],
                focusedVideo = index == currentVideoIndex.value
            )
        }
    }
}


@Composable
fun VideoItem(video: Video, focusedVideo: Boolean) {
    val configuration = LocalConfiguration.current

    Box(

            Modifier.fillMaxSize().background(Color.Black)

        ) {
            Player(
                modifier = Modifier.height(configuration.screenHeightDp.dp),
                video = video,
                focusedVideo = focusedVideo
            )

            androidx.compose.animation.AnimatedVisibility(
                visible = focusedVideo,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                ConstraintLayout(
                    modifier = Modifier
                      //  .fillMaxSize()
                       // .background(Color.Black.copy(alpha = 0.5f))
                ) {
                    val (header, videoplayer, actions) = createRefs()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .background(Color.Black)
                            .constrainAs(header) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        Icon(
                            imageVector = Icons.Filled.LiveTv,
                            contentDescription = "Live",
                            tint = Color.White,
                        )

                        BadgedBox(badge = { Badge { Text("8") } }) {
                            Text(
                                text = "Friends",
                                color = Color.White,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }

                        BadgedBox(badge = { Badge { Text("LIVE") } }) {
                            Text(
                                text = "Following",
                                color = Color.White,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }

                        Text(
                            text = "For you",
                            color = Color.White,
                            modifier = Modifier.padding(start = 8.dp)
                        )

                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Live",
                            tint = Color.White,
                        )

                    }

                    Column(
                        modifier = Modifier
                            .height(configuration.screenHeightDp.dp)
                            .constrainAs(actions) {
                                end.linkTo(parent.end)
                                top.linkTo(parent.top)
                            },
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {

                        Card(
                            modifier = Modifier.size(40.dp),
                            shape = CircleShape,
                            elevation = 2.dp

                        ) {
                            Box(Modifier.fillMaxSize()) {
                                Image(
                                    painterResource(R.drawable.frapo),
                                    contentDescription = "",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()

                                )
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(Color.Red)
                                        .align(Alignment.BottomCenter)

                                ) {
                                    Icon(
                                        Icons.Filled.Add,
                                        contentDescription = "",
                                        tint = Color.White,
                                        modifier = Modifier
                                            .size(20.dp)
                                            .padding(2.dp)
                                    )
                                }

                            }
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Favorite,
                                contentDescription = "Live",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(46.dp)
                                    .padding(4.dp)
                            )
                            Text(
                                text = "360 k",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,

                            modifier = Modifier.padding(6.dp)
                        ) {
                            Icon(
                                //Image vector from drawable
                                painter = painterResource(id = R.drawable.comment),
                                contentDescription = "Live",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(46.dp)
                                    .padding(4.dp)
                            )
                            Text(
                                text = "1587",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Bookmark,
                                contentDescription = "Live",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(46.dp)
                                    .padding(4.dp)
                            )
                            Text(
                                text = "2485",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }


                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(6.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.share),
                                contentDescription = "Live",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(46.dp)
                                    .padding(4.dp)
                            )
                            Text(
                                text = "4785",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }


                    }

                }
          }
    }
}


@Composable
private fun Player(modifier: Modifier = Modifier, video: Video, focusedVideo: Boolean) {
    val context = LocalContext.current
    val exoPlayer = remember { SimpleExoPlayerHolder.get(context) }
    var playerView: PlayerView? = null

    if (focusedVideo) {
        LaunchedEffect(video.url) {
            val videoUri = Uri.parse(video.url)
            val dataSourceFactory = DataSourceHolder.getCacheFactory(context)
            val type = Util.inferContentType(videoUri)
            val source = when (type) {
                C.TYPE_DASH -> DashMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(videoUri))
                C.TYPE_HLS -> HlsMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(videoUri))
                else -> ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(videoUri))
            }
            exoPlayer.setMediaSource(source)
            exoPlayer.prepare()
        }
    }

    AndroidView(

        modifier = modifier,
        factory = { ctx ->
            val frameLayout = FrameLayout(ctx)
            frameLayout
        }, update = { frameLayout ->

            frameLayout.removeAllViews()
            if (focusedVideo) {
                playerView = PlayerViewPool.get(frameLayout.context)
                //no controls
                playerView?.useController = false
                PlayerView.switchTargetView(
                    exoPlayer, PlayerViewPool.currentPlayerView, playerView
                )
                PlayerViewPool.currentPlayerView = playerView
                playerView?.apply {
                    player?.playWhenReady = true
                }

                playerView?.apply {
                    (parent as? ViewGroup)?.removeView(this)
                }
                frameLayout.addView(
                    playerView,
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            } else if (playerView != null) {
                playerView?.apply {
                    (parent as? ViewGroup)?.removeView(this)
                    PlayerViewPool.release(this)
                }
                playerView = null
            }

        })

    DisposableEffect(video.url) {

        onDispose {
            if (focusedVideo) {
                playerView?.apply {
                    (parent as? ViewGroup)?.removeView(this)
                }
                exoPlayer.stop()
                playerView?.let {
                    PlayerViewPool.release(it)
                }
                playerView = null
            }
        }
    }
}


object SimpleExoPlayerHolder {
    private var exoplayer: ExoPlayer? = null

    fun get(context: Context): ExoPlayer {
        if (exoplayer == null) {
            exoplayer = createExoPlayer(context)
        }
        return exoplayer!!
    }

    private fun createExoPlayer(context: Context): ExoPlayer {
        return ExoPlayer.Builder(context).setLoadControl(
            DefaultLoadControl.Builder().setBufferDurationsMs(
                DefaultLoadControl.DEFAULT_MIN_BUFFER_MS,
                DefaultLoadControl.DEFAULT_MAX_BUFFER_MS,
                DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS / 10,
                DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS / 10
            ).build()
        ).build().apply {
            repeatMode = Player.REPEAT_MODE_ONE
        }
    }
}


object DataSourceHolder {
    private var cacheDataSourceFactory: CacheDataSource.Factory? = null
    private var defaultDataSourceFactory: com.google.android.exoplayer2.upstream.DataSource.Factory? =
        null

    fun getCacheFactory(context: Context): CacheDataSource.Factory {
        if (cacheDataSourceFactory == null) {
            val simpleCache = CacheHolder.get(context)
            val defaultFactory = getDefaultFactory(context)
            cacheDataSourceFactory = CacheDataSource.Factory().setCache(simpleCache)
                .setUpstreamDataSourceFactory(defaultFactory)
                .setCacheReadDataSourceFactory(FileDataSource.Factory())
                .setCacheWriteDataSinkFactory(
                    CacheDataSink.Factory().setCache(simpleCache)
                        .setFragmentSize(CacheDataSink.DEFAULT_FRAGMENT_SIZE)
                )
        }

        return cacheDataSourceFactory!!
    }

    private fun getDefaultFactory(context: Context): com.google.android.exoplayer2.upstream.DataSource.Factory {
        if (defaultDataSourceFactory == null) {
            defaultDataSourceFactory = DefaultDataSource.Factory(context)
        }
        return defaultDataSourceFactory!!
    }
}


object CacheHolder {
    private var cache: SimpleCache? = null
    private val lock = Object()

    fun get(context: Context): SimpleCache {
        synchronized(lock) {
            if (cache == null) {
                val cacheSize = 20L * 1024 * 1024
                val exoDatabaseProvider = StandaloneDatabaseProvider(context)

                cache = SimpleCache(
                    context.cacheDir, LeastRecentlyUsedCacheEvictor(cacheSize), exoDatabaseProvider
                )
            }
        }
        return cache!!
    }
}


object PlayerViewPool {
    @SuppressLint("StaticFieldLeak")
    var currentPlayerView: PlayerView? = null

    private val playerViewPool = Pools.SimplePool<PlayerView>(2)

    fun get(context: Context): PlayerView {
        return playerViewPool.acquire() ?: createPlayerView(context)
    }

    fun release(player: PlayerView) {
        playerViewPool.release(player)
    }

    @SuppressLint("InflateParams")
    private fun createPlayerView(context: Context): PlayerView {
        return (LayoutInflater.from(context)
            .inflate(R.layout.exoplayer_texture_view, null, false) as PlayerView)
    }
}


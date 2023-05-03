import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.kondranton.InstantAppFeature.HardcodedContent
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreen() {
    val urls = HardcodedContent.urls
    val pagerState = rememberPagerState(pageCount = urls.size)
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color.Red)
            ) {
                HorizontalPager(state = pagerState, dragEnabled = false) { page ->
                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = { context ->
                            WebView(context).apply {
                                webViewClient = WebViewClient()
                                loadUrl(urls[page])
                            }
                        }
                    )
                }
            }
            Box(
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth()
                    .background(Color.Blue)
                    .draggable(
                        state = rememberDraggableState { _ -> },
                        orientation = Orientation.Horizontal,
                        onDragStopped = { delta ->
                            if (delta > 0) {
                                coroutineScope.launch {
                                    pagerState.scrollToPage(
                                        (pagerState.currentPage - 1).mod(pagerState.pageCount)
                                    )
                                }
                            } else if (delta < 0) {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(
                                        (pagerState.currentPage + 1).mod(pagerState.pageCount)
                                    )
                                }
                            }
                        }
                    )
            ) {
                Text("Swipe Here")
            }
        }
    }
}

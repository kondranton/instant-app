import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.kondranton.InstantAppFeature.HardcodedContent
import com.kondranton.instantappchallenge.Pager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.sign

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
                Pager(state = pagerState, urls = urls)
            }
            Box(
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth()
                    .background(Color.Yellow)
                    .draggable(
                        state = rememberDraggableState { _ -> },
                        orientation = Orientation.Horizontal,
                        onDragStopped = { delta ->
                            onSwipe(
                                -sign(delta).toInt(),
                                pagerState,
                                coroutineScope
                            )
                        }
                    )
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
fun onSwipe(pageIndexDiff: Int, pagerState: PagerState, coroutineScope: CoroutineScope) {
    coroutineScope.launch {
        pagerState.animateScrollToPage(
            (pagerState.currentPage + pageIndexDiff).mod(pagerState.pageCount)
        )
    }
}

package sharif.feryal.composeanimation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sharif.feryal.composeanimation.ui.theme.ComposeAnimationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeAnimationTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    BeautifulAnimations()
                }
            }
        }
    }
}

@Composable
fun BeautifulAnimations() {
    Column(Modifier.padding(16.dp)) {
        Ticker()
        Counter()
        Voter()
        Boxer()
    }
}

@Composable
@OptIn(ExperimentalAnimationApi::class)
fun Ticker() {
    var visisble by remember { mutableStateOf(true) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(onClick = { visisble = !visisble }) {
            Text(text = "Click")
        }
        AnimatedVisibility(
            visisble,
            enter = fadeIn() + slideInHorizontally(),
            exit = fadeOut() + slideOutHorizontally()
        ) {
            TickIcon()
        }
    }
}

@Composable
fun TickIcon() {
    Icon(
        Icons.Filled.CheckCircle,
        contentDescription = "CheckCircle",
        Modifier.padding(start = 8.dp)
    )
}

@Composable
@OptIn(ExperimentalAnimationApi::class)
fun Counter() {
    var count by remember { mutableStateOf(0) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(onClick = { count++ }) {
            Text(text = "Count")
        }
        AnimatedContent(
            targetState = count,
            transitionSpec = {
                fadeIn() + slideIntoContainer(AnimatedContentScope.SlideDirection.Start) with
                        fadeOut() + slideOutOfContainer(AnimatedContentScope.SlideDirection.End)
            }
        ) {
            Text(
                text = "Total is $count",
                Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
@OptIn(ExperimentalAnimationApi::class)
fun Voter() {
    var isOn by remember {
        mutableStateOf(false)
    }
    val offset by animateDpAsState(
        if (isOn) 200.dp else 0.dp,
        animationSpec = spring(dampingRatio = 0.7f)
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(onClick = { isOn = !isOn }) {
            Text(text = "Move")
        }
        Icon(
            Icons.Filled.ThumbUp,
            contentDescription = "ThumbUp",
            Modifier
                .offset(x = offset)
                .padding(start = 8.dp)
        )
    }
}

@Composable
@OptIn(ExperimentalAnimationApi::class)
fun Boxer() {

    var boxState by remember {
        mutableStateOf(BoxState.Small)
    }
    val transition = updateTransition(
        targetState = boxState,
        label = "Box animation"
    )
    val color by transition.animateColor(
        label = "Color",
        transitionSpec = {
            tween(durationMillis = 1000)
        }
    ) {
        when (it) {
            BoxState.Large -> Color.Yellow
            BoxState.Small -> Color.Cyan
        }
    }
    val size by transition.animateDp(
        label = "Size",
        transitionSpec = {
            tween(durationMillis = 1000)
        }
    ) {
        when (it) {
            BoxState.Large -> 100.dp
            BoxState.Small -> 32.dp
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Button(onClick = { boxState = switchState(boxState) }) {
            Text(text = "Toggle")
        }
        Box(
            modifier = Modifier
                .padding(start = 8.dp)
                .background(color)
                .size(size)
        )
    }
}

private fun switchState(state: BoxState): BoxState {
    return when (state) {
        BoxState.Large -> BoxState.Small
        BoxState.Small -> BoxState.Large
    }
}

private enum class BoxState {
    Large,
    Small
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeAnimationTheme {
        BeautifulAnimations()
    }
}
package com.example.koshi.ui.navhost

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween

// -------- Common Specs --------
private val easing = FastOutSlowInEasing
private const val short = 300
private const val medium = 400
private const val long = 500

val splashExit = slideOutVertically(
    targetOffsetY = { -it * 2 },
    animationSpec = tween(
        durationMillis = 650,
        easing = {
            // custom bounce/jump easing
            val overshoot = 1.4f
            val t2 = it - 1f
            1f + overshoot * (t2 * t2 * ((overshoot + 1) * t2 + overshoot))
        }
    )
) + fadeOut(
    animationSpec = tween(
        durationMillis = 400
    )
)

val homeEnter = slideInVertically(
    initialOffsetY = { it },
    animationSpec = tween(
        durationMillis = 800,
        easing = FastOutSlowInEasing
    )
) + fadeIn(
    animationSpec = tween(
        durationMillis = 500,
        easing = FastOutSlowInEasing
    )
)

val authEnter = slideInVertically(
    initialOffsetY = { it },
    animationSpec = tween(
        durationMillis = 750,
        easing = FastOutSlowInEasing
    )
) + fadeIn(
    animationSpec = tween(
        durationMillis = 450,
        easing = FastOutSlowInEasing
    )
)


val authExit = slideOutHorizontally(
    targetOffsetX = { -it / 3 },
    animationSpec = tween(medium, easing = easing)
) + fadeOut(animationSpec = tween(medium))

// -------- Home Screen --------

val homeExit = slideOutHorizontally(
    targetOffsetX = { -it / 2 },
    animationSpec = tween(medium, easing = easing)
) + fadeOut(animationSpec = tween(short))

val homePopEnter = slideInHorizontally(
    initialOffsetX = { -it / 2 },
    animationSpec = tween(medium, easing = easing)
) + fadeIn(animationSpec = tween(short))

val detailEnter = slideInHorizontally(
    initialOffsetX = { it / 2 },
    animationSpec = tween(medium, easing = easing)
) + fadeIn(animationSpec = tween(medium))

val detailPopExit = slideOutHorizontally(
    targetOffsetX = { it / 2 },
    animationSpec = tween(medium, easing = easing)
) + fadeOut(animationSpec = tween(short))

val profileEnter = slideInHorizontally(
    initialOffsetX = { it },
    animationSpec = tween(medium, easing = easing)
) + fadeIn(animationSpec = tween(medium))

val profileExit = slideOutHorizontally(
    targetOffsetX = { it },
    animationSpec = tween(medium, easing = easing)
) + fadeOut(animationSpec = tween(short))

val cartEnter = slideInHorizontally(
    initialOffsetX = { it / 2 },
    animationSpec = tween(medium, easing = easing)
) + fadeIn(animationSpec = tween(medium))

val cartExit = slideOutHorizontally(
    targetOffsetX = { it / 2 },
    animationSpec = tween(medium, easing = easing)
) + fadeOut(animationSpec = tween(short))

val bookingEnter = slideInVertically(
    initialOffsetY = { it / 2 },
    animationSpec = tween(medium, easing = easing)
) + fadeIn(animationSpec = tween(medium))

val bookingExit = slideOutVertically(
    targetOffsetY = { it / 2 },
    animationSpec = tween(medium, easing = easing)
) + fadeOut(animationSpec = tween(short))

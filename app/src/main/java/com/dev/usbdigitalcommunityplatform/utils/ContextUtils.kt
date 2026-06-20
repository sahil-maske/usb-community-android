package com.dev.usbdigitalcommunityplatform.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

/**
 * Extension function to find the activity from a context.
 * It recursively traverses the Context hierarchy.
 * Returns null if the activity is not found (e.g., in Compose Preview).
 */
fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}

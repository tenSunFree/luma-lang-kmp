/*
 *
 *  *
 *  *  * Copyright (c) 2026
 *  *  *
 *  *  * Author: Athar Gul
 *  *  * GitHub: https://github.com/DevAtrii/Kmp-Starter-Template
 *  *  * YouTube: https://www.youtube.com/@devatrii/videos
 *  *  *
 *  *  * All rights reserved.
 *  *
 *  *
 *
 */

package com.sun.kmpstartertemplaterefined

import androidx.compose.ui.window.ComposeUIViewController
import com.sun.kmpstartertemplaterefined.app.initKmpApp


fun mainViewController() = ComposeUIViewController(
    configure = {
        initKmpApp()
    }
) {
    App()
}
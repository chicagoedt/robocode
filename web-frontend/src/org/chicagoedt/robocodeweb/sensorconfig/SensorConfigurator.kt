package org.chicagoedt.robocodeweb.sensorconfig

import kotlin.browser.document
import kotlin.dom.addClass

class SensorConfigurator {
    val element = document.createElement("div")

    init{
        element.addClass("sensorConfigurator")
    }
}
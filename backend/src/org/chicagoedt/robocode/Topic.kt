package org.chicagoedt.robocode

/**
 * A place to broadcast and store values
 * @property value The value currently stored in the topic
 * @property topicListener The function to be called when the topic is changed
 */
class Topic {
    var value : Any = 0
        set(value){
            field = value
            topicListener(value)
        }

    var topicListener = {value : Any -> }
}
package org.chicagoedt.robocode

/**
 * A place to broadcast and store values
 * @property value The value currently stored in the topic
 * @property topicListeners The functions to be called when the topic is changed
 */
class Topic {
    var value : Any = 0
        set(value){
            field = value
            for (topicListener in topicListeners){
                topicListener(value)
            }
        }

    val topicListeners = arrayListOf<(Any) -> Unit>()

    /**
     * Restores the topic to its original value
     */
    fun reset(){
        value = 0
    }
}
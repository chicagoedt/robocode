package org.chicagoedt.robocode.actions.operations

import org.chicagoedt.robocode.Topic

/**
 * Compares the value in a topic to a value for equality
 * @param first The topic to compare the value from
 * @param second The value to compare the value from the topic to
 */
class TopicEqualsComparison<U>(first: Topic, second: U) : Comparison<Topic,U>(first, second) {
    
    override fun result(): Boolean {
        return first.value == second
    }
}
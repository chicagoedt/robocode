package org.chicagoedt.rosette.actions.operations

/**
 * Compares two objects
 * @param first The first object to compare
 * @param second The second object to compare
 */
abstract class Comparison<T, U>(var first: T, var second: U) {
    /**
     * @return The result of the comparison
     */
    internal abstract fun result() : Boolean
}
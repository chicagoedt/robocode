package org.chicagoedt.rosette.Instructions.Operations

abstract class Comparison<T, U>(var first: T, var second: U) {
    abstract fun result() : Boolean
}
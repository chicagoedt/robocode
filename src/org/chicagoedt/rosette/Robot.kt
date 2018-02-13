package org.chicagoedt.rosette

class Robot(val name: String,
            val graphic: String,
            val width: Int,
            val height: Int) {
    val instructions = arrayListOf<Instruction>()
}
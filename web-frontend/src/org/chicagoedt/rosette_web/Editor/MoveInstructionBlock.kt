package org.chicagoedt.rosette_web.Editor

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.*
import org.chicagoedt.rosette.*
import org.chicagoedt.rosette.Instructions.*

/**
 * The instruction block that represents the Move instruction
 */
class MoveInstructionBlock() : InstructionBlock<MoveInstruction>(){
	override var height = 50.0
    override var width = 100.0
	override var instruction = MoveInstruction()
	override var color = "#E64A19" //orange
	override var x = 0.0
	override var y = 0.0
	override lateinit var context : CanvasRenderingContext2D
	override var name = "Move"
}
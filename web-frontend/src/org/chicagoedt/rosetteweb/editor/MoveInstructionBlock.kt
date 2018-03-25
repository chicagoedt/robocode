package org.chicagoedt.rosetteweb.editor

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.*
import org.chicagoedt.rosette.*
import org.chicagoedt.rosette.actions.*
import org.chicagoedt.rosette.actions.robotActions.*
import org.chicagoedt.rosetteweb.canvas.InteractionManager

/**
 * The instruction block that represents the Move instruction
 */
class MoveInstructionBlock(manager : InteractionManager, context : CanvasRenderingContext2D) : InstructionBlock<MoveAction>(manager, context){
	override var height = 50.0
    override var width = 100.0
	override var action = MoveAction()
	override var color = "#E64A19" //orange
	override var x = 0.0
	override var y = 0.0
	override var name = "Move"
	
}
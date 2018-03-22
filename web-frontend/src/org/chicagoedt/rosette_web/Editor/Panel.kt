package org.chicagoedt.rosette_web.Editor

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.*
import org.chicagoedt.rosette.*
import org.chicagoedt.rosette.Robots.*

/**
 * A space to contain all of the instructions for each robot
 * @param player The player that this panel will correspond to
 * @property screenHeight The height of this panel
 * @property screenWidth The width of this panel
 * @property screenX The X position of the top left corner of this panel
 * @property screenY The Y postiion of the top left corner of this panel
 */
class Panel(val context: CanvasRenderingContext2D, 
			var player: RobotPlayer){
	var screenHeight = 0.0
	var screenWidth = 0.0
	var screenX = 0.0
	var screenY = 0.0
	private var instructions = arrayListOf<InstructionBlock<*>>()

	fun addInstruction(block : InstructionBlock<*>){
		instructions.add(block)
	}

	fun draw(){
		context.fillStyle = "#E1BEE7" //purple
        context.fillRect(screenX, screenY, screenWidth, screenHeight)

        for (instruction in instructions){
        	instruction.draw()
        }
	}
}
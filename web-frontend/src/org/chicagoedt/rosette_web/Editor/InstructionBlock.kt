package org.chicagoedt.rosette_web.Editor

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.*
import org.chicagoedt.rosette.*
import org.chicagoedt.rosette.Instructions.*

abstract class InstructionBlock<Instruction>(){
	abstract var x : Double
	abstract var y : Double
	abstract var height : Double
	abstract var width : Double
	abstract var instruction : Instruction
	abstract var color : String
	abstract var context : CanvasRenderingContext2D

	fun mouseWithin(mouseX : Double, mouseY : Double) : Boolean{
		if (mouseX > x && mouseX < (x + width)){
			if (mouseY > y && mouseY < (y + height)){
				return true
			}
		}
		return false
	}

	fun draw(){
		context.fillStyle = color
        context.fillRect(x, y, width, height)
	}
}
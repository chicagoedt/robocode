package org.chicagoedt.rosette

class LevelProperties(val name: String,
                      val difficulty : Int,
                      val players: ArrayList<RobotPlayer>){
    internal val grid = ArrayList<ArrayList<Tile>>()

}

class RobotPlayer(val name: String,
                  var x: Int,
                  var y: Int,
                  var direction: Int){

}
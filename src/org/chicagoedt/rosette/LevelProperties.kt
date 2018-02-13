package main

class LevelProperties(val name: String,
                      val difficulty : Int,
                      val players: ArrayList<RobotPlayer>){
    internal val grid = ArrayList<ArrayList<Tile>>()

}

class RobotPlayer(val name: String,
                  val x: Int,
                  val y: Int){

}
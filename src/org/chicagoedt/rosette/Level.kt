package org.chicagoedt.rosette

class Level(var properties: LevelProperties, val players: HashMap<String, RobotPlayer>) {
}

class RobotPlayer(val name: String,
                  var x: Int,
                  var y: Int,
                  var direction: Int){

}
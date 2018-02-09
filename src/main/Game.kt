package main

class Game{
    var levels = ArrayList<Level>()
    lateinit var currentLevel : Level

    fun start(){
        currentLevel = levels[0]
    }
}
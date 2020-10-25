package csapat.app.tradingGame.dataClasses.dataClasses

import csapat.app.tradingGame.dataClasses.dataClasses.characters.Character

class Player(ID: String, name: String, val character: Character) : Participant(ID, name) {

    val goods : Map<Resource, Int> = TODO()
    var money : Double = 500.0
    var inventoryLimit : Int = 20
}
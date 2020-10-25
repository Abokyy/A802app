package csapat.app.tradingGame.dataClasses.dataClasses

class Game (private val ID : String,
            val name: String,
            val password: String,
            val creatorID: String,
            val participants: MutableMap<String, MutableMap<String, out Participant>>,
            val cities : MutableMap<String, City>,
            val resources : List<Resource>)
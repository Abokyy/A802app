package csapat.app.tradingGame.dataClasses.dataClasses

class City (
        val name : String,
        var major: CityMajor? = null,
        val prices: MutableMap<Resource, Int> = mutableMapOf<Resource,Int>(),
        val items: MutableList<Item> = mutableListOf<Item>()
)
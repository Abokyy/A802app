package csapat.app.tradingGame.dataClasses.activities

import csapat.app.R
import csapat.app.tradingGame.dataClasses.dataClasses.Resource

class ExpandebleListDataPump {

    companion object {
        fun getData(): HashMap<String, List<String>> {
            val map = HashMap<String, List<String>>()

            val resources = Resource.values().toMutableList()
            val strings = mutableListOf<String>()
            resources.forEach { strings.add(it.toString()) }
            strings.sort()


            map["Nyersanyagok"] = strings
            map["Városok"] = listOf("Budapest", "Debrecen", "Kecskemét")

            return map
        }
    }

}
package csapat.app.tradingGame.dataClasses.activities

import android.os.Bundle
import android.widget.ExpandableListView
import android.widget.Toast
import csapat.app.BaseCompat
import csapat.app.R
import csapat.app.tradingGame.dataClasses.dataClasses.*
import kotlinx.android.synthetic.main.activity_create_new_game.*
import kotlinx.android.synthetic.main.expendable_list_item.view.*
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import kotlin.collections.HashMap
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.fold
import kotlin.collections.forEach
import kotlin.collections.joinToString
import kotlin.collections.map
import kotlin.collections.mutableListOf
import kotlin.collections.plus
import kotlin.collections.set
import kotlin.collections.sortBy
import kotlin.collections.toList
import kotlin.random.Random

class CreateNewGameActivity : BaseCompat() {

    lateinit var expandableListView: ExpandableListView
    lateinit var listAdapter: ExpandableListAdapter
    lateinit var listTitles: List<String>
    lateinit var listDetails: HashMap<String, List<String>>
    private val checkBoxMap = HashMap<String, MutableList<Boolean>>()
    var cities = mutableMapOf<String, City>()
    var resources = mutableListOf<Resource>()

    private val gameIDLength= 5
    private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new_game)


        expandableListView = findViewById(R.id.expandableListView)
        listDetails = ExpandebleListDataPump.getData()
        listDetails.forEach {
            val checkBoxPump = mutableListOf<Boolean>()
            repeat(listDetails[it.key]!!.size) { checkBoxPump.add(false) }
            checkBoxMap[it.key] = checkBoxPump
        }
        listTitles = listDetails.keys.toList()
        listAdapter = ExpandableListAdapter(this, listTitles, listDetails, checkBoxMap)
        expandableListView.setAdapter(listAdapter)


        expandableListView.setOnGroupExpandListener { groupPosition ->
            Toast.makeText(applicationContext, (listTitles as List<String>)[groupPosition] + " List Expanded.", Toast.LENGTH_SHORT).show()
        }

        expandableListView.setOnGroupClickListener { parent, v, groupPosition, it ->
            if (parent.isGroupExpanded(groupPosition))
                parent.collapseGroup(groupPosition)
            else {
                expandableListView.expandGroup(groupPosition)
            }
            true
        }
        expandableListView.setOnGroupCollapseListener { groupPosition ->
            Toast.makeText(applicationContext, (listTitles as List<String>)[groupPosition] + " List Collapsed.", Toast.LENGTH_SHORT).show()
        }

        expandableListView.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            val key = groupPosition.let {
                when (it) {
                    0 -> "Városok"
                    1 -> "Nyersanyagok"
                    else -> "none"
                }
            }
            if(checkBoxMap[key]!![childPosition]) {
                checkBoxMap[key]!![childPosition] = false
                v.item_check_box.isChecked = false
                listAdapter.notifyDataSetChanged()
            } else {
                checkBoxMap[key]!![childPosition] = true
                v.item_check_box.isChecked = true
                listAdapter.notifyDataSetChanged()
            }
            false
        }


        creatingGameBtn.setOnClickListener {
            createGame()
        }


    }


    private fun createGame() {

        listDetails.forEach {detailsEntry ->
            when(detailsEntry.key) {
                "Városok" -> detailsEntry.value.let { stringListEntry ->
                    stringListEntry.forEach {
                       if(checkBoxMap[detailsEntry.key]!![stringListEntry.indexOf(it)]) {
                           val city = City(it,null)
                           cities[it] = city
                       }
                    }
                }
                "Nyersanyagok" -> detailsEntry.value.let { stringListEntry ->
                    stringListEntry.forEach{
                        if(checkBoxMap[detailsEntry.key]!![stringListEntry.indexOf(it)]) {
                            resources.add(Resource.values()[stringListEntry.indexOf(it)])
                        }
                    }

                }
            }
        }

        cities = cities.toSortedMap()
        resources.sortBy { it.name }

        val creator = CityMajor(appUser.userID, appUser.fullName)
        val participants: MutableMap<String, MutableMap<String, out Participant>> =
                mutableMapOf("CityMajors" to mutableMapOf(appUser.userID to creator), "Players" to mutableMapOf<String, Player>())

        val gameID = generateGameID()
        val gamePassword = hashPassword(gamePasswordEditText.text.toString())

        val game = Game(gameID, gameNameEditText.text.toString(), gamePassword, creator.ID, participants , cities, resources)

        realtimeDB.child("TradingGames").child(gameID).setValue(game)

    }

    private fun hashPassword(pass: String): String {
        val digest = MessageDigest.getInstance("SHA512")
        val hashBytes = digest.digest(pass.toByteArray(StandardCharsets.UTF_8))
        return hashBytes.fold("", { str, it -> str + "%02x".format(it) })
    }


    private fun generateGameID(): String {

        return (1..gameIDLength)
                .map { i -> Random.nextInt(0, charPool.size) }
                .map(charPool::get)
                .joinToString("")
    }

}


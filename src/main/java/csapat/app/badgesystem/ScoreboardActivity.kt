package csapat.app.badgesystem

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import csapat.app.BaseCompat
import csapat.app.R
import kotlinx.android.synthetic.main.activity_scoreboard.*

class ScoreboardActivity : BaseCompat() {

    private val userTriples = mutableListOf<Triple<String, String, Long>>()
    private val patrolTriples = mutableListOf<Triple<String, String, Long>>()
    private var userListReady = false
    private var patrolListReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scoreboard)

        //showProgressDialog()

        db.collection("users")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val userFullName: String = document.getString("fullName")!!
                        val patrol: String = document.getString("patrol")!!
                        val score = document.get("score") as Long
                        userTriples.add(Triple(userFullName, patrol, score))
                    }
                    userListReady = true
                    initRecyclerView()
                }

        db.collection("patrols")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val patrolName: String = document.id
                        val nullString = ""
                        val score = document.get("score") as Long
                        if (score == 0L) continue
                        patrolTriples.add(Triple(patrolName, nullString, score))
                    }
                    patrolListReady = true
                    initRecyclerView()
                }


    }

    private fun initRecyclerView() {
        if (!userListReady || !patrolListReady) return

        personalScoreBoardRecyclerView.layoutManager = LinearLayoutManager(this)
        val sortedUserList: MutableList<Triple<String, String, Long>> = sortScoreBoard(userTriples)
        val adapterUser = ScoreboardItemAdapter(sortedUserList, this)
        personalScoreBoardRecyclerView.adapter = adapterUser

        patrolScoreBoardRecyclerView.layoutManager = LinearLayoutManager(this)
        val sortedPatrolList : MutableList<Triple<String, String, Long>> = sortScoreBoard(patrolTriples)
        val adapterPatrol = ScoreboardItemAdapter(sortedPatrolList, this)
        patrolScoreBoardRecyclerView.adapter = adapterPatrol
    }

    private fun sortScoreBoard(userTriples: MutableList<Triple<String, String, Long>>): MutableList<Triple<String, String, Long>> {

        val sorted = userTriples.sortedByDescending { it.third }

        val returnList = mutableListOf<Triple<String, String, Long>>()
        for (item in sorted)
            returnList.add(item)
        return returnList
    }
}

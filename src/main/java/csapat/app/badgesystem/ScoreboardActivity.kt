package csapat.app.badgesystem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import csapat.app.BaseCompat
import csapat.app.R
import kotlinx.android.synthetic.main.activity_scoreboard.*

class ScoreboardActivity : BaseCompat() {

    private val userTriples = mutableListOf<Triple<String, String, Long>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scoreboard)


        db.collection("users")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val userFullName : String = document.getString("fullName")!!
                        val patrol : String = document.getString("patrol")!!
                        val score  = document.get("score") as Long
                        val add = userTriples.add(Triple(userFullName, patrol, score))
                        initRecyclerView()
                    }
                }

    }

    private fun initRecyclerView() {
        scoreBoardRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = ScoreboardItemAdapter(userTriples, this)
        scoreBoardRecyclerView.adapter = adapter
    }
}

package csapat.app.tradingGame.dataClasses.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import csapat.app.BaseCompat
import csapat.app.R
import csapat.app.tradingGame.dataClasses.dataClasses.Game
import kotlinx.android.synthetic.main.activity_create_new_game.*
import kotlinx.android.synthetic.main.activity_join_game.*
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

class JoinGameActivity : BaseCompat() {

    private val Tag = "Join game activity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_game)


        joinGameButton.setOnClickListener {
            val gameID = joinGameIDET.text.toString()
            val gamePsw = hashPassword(joinGamePswET.text.toString())
            joinGame(gameID, gamePsw)
        }

    }

    private fun hashPassword(pass: String): String {
        val digest = MessageDigest.getInstance("SHA512")
        val hashBytes = digest.digest(pass.toByteArray(StandardCharsets.UTF_8))
        return hashBytes.fold("", { str, it -> str + "%02x".format(it) })
    }

    private fun joinGame(gameID: String, gamePsw: String) {

        val gameListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val game: Game = dataSnapshot.value as Game
                if (gamePsw == game.password) {
                    val intent = Intent(this@JoinGameActivity, CharacterOverviewActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@JoinGameActivity, "Wrong password", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@JoinGameActivity, "Game ID not found", Toast.LENGTH_LONG).show()
                Log.w(Tag, "getGame:onCancelled", databaseError.toException())
            }
        }

        realtimeDB.child("TradingGames").child(gameID).addListenerForSingleValueEvent(gameListener)
    }
}

package csapat.app.tradingGame.dataClasses.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import csapat.app.BaseCompat
import csapat.app.R
import kotlinx.android.synthetic.main.activity_create_or_join_game.*

class CreateOrJoinGameActivity : BaseCompat() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_or_join_game)

        createGameBtn.setOnClickListener {
            val intent = Intent(this, CreateNewGameActivity::class.java)
            startActivity(intent)
        }

        joinGameBtn.setOnClickListener {
            val intent = Intent(this, JoinGameActivity::class.java)
            startActivity(intent)
        }


    }
}

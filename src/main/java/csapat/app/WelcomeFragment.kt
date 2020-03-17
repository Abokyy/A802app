package csapat.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import csapat.app.badgesystem.BadgesActivity
import csapat.app.badgesystem.ScoreboardActivity
import csapat.app.badgesystem.TaskListActivity
import csapat.app.teamstructure.TeamStructureFragment
import kotlinx.android.synthetic.main.activity_send_task.*
import kotlinx.android.synthetic.main.fragment_welcome.*
import kotlinx.android.synthetic.main.fragment_welcome.view.*


class WelcomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_welcome, container, false)



        root.showTeamStructureBtn.setOnClickListener {
            val view = activity!!.findViewById<FrameLayout>(R.id.welcomeFragcontainer).id
            val transaction = activity!!.supportFragmentManager.beginTransaction()
            transaction.replace(view , TeamStructureFragment())
            transaction.commit()
        }

        root.showBadgesBtn.setOnClickListener {
            val intent = Intent (activity, BadgesActivity::class.java)
            startActivity(intent)
        }

        root.showMyTaskSolutionsBtn.setOnClickListener {
            val intent = Intent (activity, TaskListActivity::class.java)
            intent.putExtra("taskListingMode", 1)
            startActivity(intent)
        }

        root.showScoreboardBtn.setOnClickListener {
            val intent = Intent(activity, ScoreboardActivity::class.java)
            startActivity(intent)
        }

        return root
    }



}

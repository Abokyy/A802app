package csapat.app.badgesystem

import java.io.Serializable

class TaskSolution(
        val solutionDescription: String,
        val taskSubmitterUserID: String,
        val badgeID: Int,
        val imagePath: String = "noimage") : Serializable {

    constructor() : this("", "", 0, "")

}
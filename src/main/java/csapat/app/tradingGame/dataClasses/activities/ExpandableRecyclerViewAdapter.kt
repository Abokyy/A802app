package csapat.app.tradingGame.dataClasses.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Checkable
import android.widget.CheckedTextView
import android.widget.TextView
import com.thoughtbot.expandablecheckrecyclerview.models.CheckedExpandableGroup
import com.thoughtbot.expandablecheckrecyclerview.viewholders.CheckableChildViewHolder
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import csapat.app.R

class GameAttribute(
        title : String,
        items : MutableList<String>
) : CheckedExpandableGroup(title, items){


    override fun onChildClicked(childIndex: Int, checked: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

class GameAttributeViewHolder(view : View) : GroupViewHolder(view) {

    var groupTitle : TextView = view.findViewById(R.id.listTitle)

}

class OptionsViewHolder(view: View) : CheckableChildViewHolder(view) {

    var itemName : CheckedTextView = view.findViewById(R.id.list_item_multicheck_name)

    fun onBind(str : String) {
        itemName.text = str
    }

    override fun getCheckable(): Checkable {
        return itemName
    }

}

class ExpandableRecyclerViewAdapter(groups: MutableList<GameAttribute>) : ExpandableRecyclerViewAdapter<GameAttributeViewHolder, OptionsViewHolder>(groups) {
    override fun onCreateGroupViewHolder(parent: ViewGroup?, viewType: Int): GameAttributeViewHolder {
        val view = LayoutInflater.from(parent!!.context).
                inflate(R.layout.list_group, parent, false)
        return GameAttributeViewHolder(view)
    }

    override fun onCreateChildViewHolder(parent: ViewGroup?, viewType: Int): OptionsViewHolder {
        val view = LayoutInflater.from(parent!!.context).
                inflate(R.layout.game_attribute_option_list_item, parent, false)
        return OptionsViewHolder(view)
    }

    override fun onBindChildViewHolder(holder: OptionsViewHolder?, flatPosition: Int, group: ExpandableGroup<*>?, childIndex: Int) {
        val itemName = group!!.items[childIndex].toString()

        holder!!.onBind(itemName)
    }

    override fun onBindGroupViewHolder(holder: GameAttributeViewHolder?, flatPosition: Int, group: ExpandableGroup<*>?) {
        holder!!.groupTitle.text = group!!.title
    }

}
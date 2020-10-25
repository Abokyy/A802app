package csapat.app.tradingGame.dataClasses.activities

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import csapat.app.R
import kotlinx.android.synthetic.main.expendable_list_item.view.*
import kotlinx.android.synthetic.main.list_group.view.*

class ExpandableListAdapter(
        val context: Context,
        val titles: List<String>,
        val details: HashMap<String, List<String>>,
        val checkBoxMap: HashMap<String, MutableList<Boolean>>
) : BaseExpandableListAdapter() {


    override fun getGroup(groupPosition: Int): Any {
        return this.titles[groupPosition]
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView

        val headerTitle = getGroup(groupPosition) as String

        if (convertView == null) {
            val layoutInflater = this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.list_group, null)
        }

        convertView!!.listTitle.text = headerTitle



        return convertView
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return this.details[this.titles[groupPosition]]!!.size
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return this.details[this.titles[groupPosition]]?.get(childPosition)!!
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }


    @SuppressLint("InflateParams", "ShowToast")
    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        val childText = getChild(groupPosition, childPosition) as String
        if (convertView == null) {
            val layoutInflater = this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.expendable_list_item, null)
        }

        convertView!!.item_check_box.text = childText
        convertView.item_check_box.isChecked = checkBoxMap[titles[groupPosition]]!![childPosition]




        return  convertView
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return this.titles.size
    }
}
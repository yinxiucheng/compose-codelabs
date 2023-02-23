package com.yxc.customercomposeview.calander.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.yxc.customercomposeview.R
import com.yxc.customercomposeview.calander.view.DayCalendarView
import com.yxc.customercomposeview.calander.view.OnDayCalendarItemSelectListener
import com.yxc.customercomposeview.utils.AppUtil
import com.yxc.customercomposeview.utils.TimeDateUtil
import org.joda.time.LocalDate
import java.util.*

/**
 * @author yxc
 * @since 2019-05-31
 */
class CalendarDayAdapter(
    var mDataList: LinkedList<Long>,
    var mRecyclerView: RecyclerView,
    var mSelectLocalDate: LocalDate?,
    private val dots: HashMap<Long, HashMap<Long, Int>>
) : RecyclerView.Adapter<CalendarDayAdapter.CalendarDayViewHolder>(),
    OnDayCalendarItemSelectListener {
    var mLayoutInflater: LayoutInflater
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarDayViewHolder {
        val contentView: View =
            mLayoutInflater.inflate(R.layout.layout_calendar_day_item, parent, false)
        return CalendarDayViewHolder(contentView)
    }

    override fun onBindViewHolder(viewHolder: CalendarDayViewHolder, position: Int) {
        val timestamp = mDataList[position]
        viewHolder.bindData(timestamp)
        viewHolder.calendarView.setOnDayCalendarItemSelectListener(this)
        if (mSelectLocalDate != null) {
            viewHolder.calendarView.setSelectDate(mSelectLocalDate)
        }
    }

    fun scrollToLocalDate(localDate: LocalDate?) {
        val firstDayOfMonth: LocalDate = TimeDateUtil.getFirstDayOfNextMonth(localDate)
        val firstDayOfMothTime: Long = TimeDateUtil.changZeroOfTheDay(firstDayOfMonth)
        val position = Collections.binarySearch(mDataList, firstDayOfMothTime)
        if (position > 1) {
            mRecyclerView.scrollToPosition(position - 1)
        } else {
            mRecyclerView.scrollToPosition(position)
        }
    }

    fun getTime(pos: Int): Long {
        return mDataList[pos]
    }

    fun selectLocalDate(localDate: LocalDate?) {
        mSelectLocalDate = localDate
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    override fun onDayItemSelect(localDate: LocalDate) {
        selectLocalDate(localDate)
        if (onDaySelectListener != null) {
            onDaySelectListener!!.onDaySelected(localDate)
        }
    }

    inner class CalendarDayViewHolder(itemView: View) : ViewHolder(itemView) {
        var calendarView: DayCalendarView
        var txtMonth: TextView

        init {
            txtMonth = itemView.findViewById<TextView>(R.id.txt_month)
            calendarView = itemView.findViewById(R.id.monthCalendar)
        }

        fun bindData(timestamp: Long) {
            val states = dots[timestamp]!!
            val localDate: LocalDate = TimeDateUtil.timestampToLocalDate(timestamp)
            calendarView.setLocalData(localDate, states)
            txtMonth.setText(TimeDateUtil.getDateMMFormat(localDate))
        }
    }

    fun setOnDaySelectListener(onDaySelectListener: OnDaySelectListener?) {
        this.onDaySelectListener = onDaySelectListener
    }

    private var onDaySelectListener: OnDaySelectListener? = null

    init {
        mLayoutInflater = LayoutInflater.from(mRecyclerView.context)
    }
}
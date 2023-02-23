package com.yxc.customercomposeview.calander.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yxc.customercomposeview.R
import com.yxc.customercomposeview.calander.view.WeekCalendarView
import com.yxc.customercomposeview.utils.TimeDateUtil
import org.joda.time.LocalDate
import java.util.*

/**
 * @author yxc
 * @since 2019-05-31
 */
class CalendarWeekAdapter(
    var mContext: Context,
    var mDataList: LinkedList<Long>,
    recyclerView: RecyclerView,
    selectedDate: LocalDate?,
    private val dots: HashMap<Long, HashMap<Long, Int>>
) : RecyclerView.Adapter<CalendarWeekAdapter.CalendarDayViewHolder>(),
    WeekCalendarView.OnWeekCalendarItemSelectListener {
    var mLayoutInflater: LayoutInflater
    var mRecyclerView: RecyclerView
    var mSelectLocalDate: LocalDate?
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarDayViewHolder {
        val contentView: View =
            mLayoutInflater.inflate(R.layout.layout_calendar_week_item, parent, false)
        return CalendarDayViewHolder(contentView)
    }

    override fun getItemCount(): Int {
       return mDataList.size
    }

    override fun onBindViewHolder(viewHolder: CalendarDayViewHolder, position: Int) {
        val timestamp = mDataList[position]
        viewHolder.bindData(timestamp)
        viewHolder.calendarView.setOnWeekCalendarItemSelectListener(this)
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

    fun selectLocalDate(localDate: LocalDate?) {
        mSelectLocalDate = localDate
        notifyDataSetChanged()
    }

    override fun onWeekItemSelect(localDate: LocalDate?) {
        selectLocalDate(localDate)
        if (onWeekSelectListener != null) {
            onWeekSelectListener!!.onWeekSelected(localDate)
        }
    }

    inner class CalendarDayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var calendarView: WeekCalendarView
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

    interface OnWeekSelectListener {
        fun onWeekSelected(localDate: LocalDate?)
    }

    fun setOnWeekSelectListener(onDaySelectListener: OnWeekSelectListener?) {
        onWeekSelectListener = onDaySelectListener
    }

    private var onWeekSelectListener: OnWeekSelectListener? = null

    init {
        mRecyclerView = recyclerView
        mSelectLocalDate = selectedDate
        mLayoutInflater = LayoutInflater.from(mContext)
    }
}
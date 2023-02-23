package com.yxc.customercomposeview.calander.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yxc.customercomposeview.R
import com.yxc.customercomposeview.calander.view.MonthCalendarView
import com.yxc.customercomposeview.utils.TimeDateUtil
import org.joda.time.LocalDate
import java.util.*

/**
 * @author yxc
 * @since 2019-05-31
 */
class CalendarMonthAdapter(
    var mContext: Context,
    var mDataList: LinkedList<Long>,
    recyclerView: RecyclerView,
    selectedDate: LocalDate,
    private val dots: HashMap<Long, HashMap<Long, Int>>
) : RecyclerView.Adapter<CalendarMonthAdapter.CalendarDayViewHolder>(),
    MonthCalendarView.OnYearCalendarItemClickListener {
    var mLayoutInflater: LayoutInflater
    var mRecyclerView: RecyclerView
    var mSelectLocalDate: LocalDate
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarDayViewHolder {
        val contentView: View =
            mLayoutInflater.inflate(R.layout.layout_calendar_month_item, parent, false)
        return CalendarDayViewHolder(contentView)
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    override fun onBindViewHolder(viewHolder: CalendarDayViewHolder, position: Int) {
        val timestamp = mDataList[position]
        viewHolder.bindData(
            timestamp,
            TimeDateUtil.getFirstDayOfMonth(mSelectLocalDate)
        ) //需要转成每月的1号，便于计算
        viewHolder.calendarView.setOnYearCalendarItemClickListener(this)
    }

    fun scrollToLocalDate(localDate: LocalDate) {
        val firstMonthOfTheYear: LocalDate = TimeDateUtil.getFirstMonthOfTheYear(localDate)
        val firstMothTime: Long = TimeDateUtil.changZeroOfTheDay(firstMonthOfTheYear)
        val position = Collections.binarySearch(mDataList, firstMothTime)
        if (position > 1) {
            mRecyclerView.scrollToPosition(position - 1)
        } else {
            mRecyclerView.scrollToPosition(position)
        }
    }

    fun selectLocalDate(localDate: LocalDate) {
        mSelectLocalDate = localDate
        notifyDataSetChanged()
    }

    override fun onYearItemClick(localDate: LocalDate) {
        selectLocalDate(localDate)
        if (onMonthSelectListener != null) {
            onMonthSelectListener!!.onMonthSelected(localDate)
        }
    }

    inner class CalendarDayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var calendarView: MonthCalendarView
        var txtYear: TextView

        init {
            txtYear = itemView.findViewById<TextView>(R.id.txt_year)
            calendarView = itemView.findViewById(R.id.monthCalendar)
        }

        fun bindData(timestamp: Long, mSelectLocalDate: LocalDate) {
            val states = dots[timestamp]!!
            val localDate: LocalDate = TimeDateUtil.timestampToLocalDate(timestamp)
            calendarView.bindData(localDate, mSelectLocalDate, states)
            txtYear.setText(localDate.year.toString() + "")
        }
    }

    interface OnMonthSelectListener {
        fun onMonthSelected(localDate: LocalDate?)
    }

    fun setOnMonthSelectListener(onMonthSelectListener: OnMonthSelectListener?) {
        this.onMonthSelectListener = onMonthSelectListener
    }

    private var onMonthSelectListener: OnMonthSelectListener? = null

    init {
        mRecyclerView = recyclerView
        mSelectLocalDate = selectedDate
        mLayoutInflater = LayoutInflater.from(mContext)
    }
}
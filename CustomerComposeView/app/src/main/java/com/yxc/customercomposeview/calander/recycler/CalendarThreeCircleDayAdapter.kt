package com.yxc.customercomposeview.calander.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yxc.customercomposeview.R
import com.yxc.customercomposeview.calander.model.ThreeCircleModel
import com.yxc.customercomposeview.calander.view.DayThreeCircleFiveLineCalendarView
import com.yxc.customercomposeview.calander.view.DayThreeCircleFourLineCalendarView
import com.yxc.customercomposeview.calander.view.DayThreeCircleSixLineCalendarView
import com.yxc.customercomposeview.calander.view.OnDayCalendarItemSelectListener
import com.yxc.customercomposeview.utils.TimeDateUtil
import org.joda.time.LocalDate
import java.util.*

/**
 * @author yxc
 * @since 2019-05-31
 */
class CalendarThreeCircleDayAdapter(
    var mDataList: LinkedList<Long>, recyclerView: RecyclerView,
    selectedDate: LocalDate?,
    circleModelMap: Map<Long, Map<Long, ThreeCircleModel>>,
    userInfoCircleModel: ThreeCircleModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), OnDayCalendarItemSelectListener {
    private val circleModelMap: Map<Long, Map<Long, ThreeCircleModel>>
    var mLayoutInflater: LayoutInflater
    var mRecyclerView: RecyclerView
    var mSelectLocalDate: LocalDate?
    private var onDaySelectListener: OnDaySelectListener? = null
    private val userInfoCircleModel: ThreeCircleModel

    init {
        mRecyclerView = recyclerView
        mSelectLocalDate = selectedDate
        this.circleModelMap = circleModelMap
        this.userInfoCircleModel = userInfoCircleModel
        mLayoutInflater = LayoutInflater.from(mRecyclerView.context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_LINE_FOUR) {
            val contentView: View =
                mLayoutInflater.inflate(R.layout.layout_calendar_day_four_item, parent, false)
            CalendarDayViewHolder(
                contentView
            )
        } else if (viewType == TYPE_LINE_SIX) {
            val contentView: View =
                mLayoutInflater.inflate(R.layout.layout_calendar_day_six_item, parent, false)
            CalendarDayViewHolder2(contentView)
        } else {
            val contentView: View =
                mLayoutInflater.inflate(R.layout.layout_calendar_day_five_item, parent, false)
            CalendarDayViewHolder1(contentView)
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val timestamp = mDataList[position]
        val viewType = getItemViewType(position)
        if (viewType == TYPE_LINE_FOUR) {
            val viewHolder1 = viewHolder as CalendarDayViewHolder
            viewHolder1.bindData(timestamp)
            viewHolder1.calendarView.setOnDayCalendarItemSelectListener(this)
            if (mSelectLocalDate != null) {
                viewHolder1.calendarView.setSelectDate(mSelectLocalDate)
            }
        } else if (viewType == TYPE_LINE_SIX) {
            val viewHolder2 = viewHolder as CalendarDayViewHolder2
            viewHolder2.bindData(timestamp)
            viewHolder2.calendarView.setOnDayCalendarItemSelectListener(this)
            if (mSelectLocalDate != null) {
                viewHolder2.calendarView.setSelectDate(mSelectLocalDate)
            }
        } else {
            val viewHolder3 = viewHolder as CalendarDayViewHolder1
            viewHolder3.bindData(timestamp)
            viewHolder3.calendarView.setOnDayCalendarItemSelectListener(this)
            if (mSelectLocalDate != null) {
                viewHolder3.calendarView.setSelectDate(mSelectLocalDate)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val timestamp = mDataList[position]
        val localDate: LocalDate = TimeDateUtil.timestampToLocalDate(timestamp)
        val dateList: List<LocalDate> = TimeDateUtil.getMonthLocalDateCalendar(localDate)
        val lineNum = dateList.size / 7
        return if (lineNum == 4) {
            TYPE_LINE_FOUR
        } else if (lineNum == 6) {
            TYPE_LINE_SIX
        } else {
            TYPE_LINE_FIVE
        }
    }

    override fun getItemCount(): Int {
        return mDataList.size
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

    override fun onDayItemSelect(localDate: LocalDate) {
        selectLocalDate(localDate)
        if (onDaySelectListener != null) {
            onDaySelectListener!!.onDaySelected(localDate)
        }
    }

    fun setOnDaySelectListener(onDaySelectListener: OnDaySelectListener?) {
        this.onDaySelectListener = onDaySelectListener
    }

    // 4 行
    inner class CalendarDayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var calendarView: DayThreeCircleFourLineCalendarView
        var txtMonth: TextView

        init {
            txtMonth = itemView.findViewById<TextView>(R.id.txt_month)
            calendarView = itemView.findViewById(R.id.monthCalendar)
        }

        fun bindData(timestamp: Long) {
            val reportMap: Map<Long, ThreeCircleModel> = circleModelMap[timestamp]!!
            val localDate: LocalDate = TimeDateUtil.timestampToLocalDate(timestamp)
            calendarView.setLocalData(localDate, reportMap, userInfoCircleModel)
            txtMonth.setText(TimeDateUtil.getDateMMFormat(localDate))
        }
    }

    //5 行
    inner class CalendarDayViewHolder1(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var calendarView: DayThreeCircleFiveLineCalendarView
        var txtMonth: TextView

        init {
            txtMonth = itemView.findViewById<TextView>(R.id.txt_month)
            calendarView = itemView.findViewById(R.id.monthCalendar)
        }

        fun bindData(timestamp: Long) {
            val reportMap: Map<Long, ThreeCircleModel> = circleModelMap[timestamp]!!
            val localDate: LocalDate = TimeDateUtil.timestampToLocalDate(timestamp)
            calendarView.setLocalData(localDate, reportMap, userInfoCircleModel)
            txtMonth.setText(TimeDateUtil.getDateMMFormat(localDate))
        }
    }

    //6行
    inner class CalendarDayViewHolder2(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var calendarView: DayThreeCircleSixLineCalendarView
        var txtMonth: TextView

        init {
            txtMonth = itemView.findViewById<TextView>(R.id.txt_month)
            calendarView = itemView.findViewById(R.id.monthCalendar)
        }

        fun bindData(timestamp: Long) {
            val reportMap: Map<Long, ThreeCircleModel> = circleModelMap[timestamp]!!
            val localDate: LocalDate = TimeDateUtil.timestampToLocalDate(timestamp)
            calendarView.setLocalData(localDate, reportMap, userInfoCircleModel)
            txtMonth.setText(TimeDateUtil.getDateMMFormat(localDate))
        }
    }

    companion object {
        private const val TYPE_LINE_FOUR = 4
        private const val TYPE_LINE_FIVE = 5
        private const val TYPE_LINE_SIX = 6
    }
}
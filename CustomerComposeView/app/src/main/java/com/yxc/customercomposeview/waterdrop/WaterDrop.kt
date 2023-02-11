package com.yxc.customercomposeview.waterdrop

import android.animation.Animator
import android.widget.FrameLayout
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.animation.LinearInterpolator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import com.yxc.customercomposeview.R
import com.yxc.customercomposeview.common.dip2px
import java.util.ArrayList

/**
 * @author yxc
 * @since 2019/4/28
 */
class WaterDrop : FrameLayout {
    private lateinit var water1: BezierCircle
    private lateinit var water2: BezierCircle
    private lateinit var water3: BezierCircle
    private lateinit var water4: BezierCircle
    private lateinit var water5: BezierCircle
    private lateinit var water6: BezierCircle
    private lateinit var water7: BezierCircle
    private lateinit var water8: BezierCircle

    private lateinit var waterScan1: BezierCircle
    private lateinit var waterScan2: BezierCircle
    private lateinit var waterScan3: BezierCircle
    private lateinit var waterScan4: BezierCircle
    private lateinit var waterScan5: BezierCircle
    private lateinit var waterScan6: BezierCircle
    private lateinit var waterScan7: BezierCircle
    private lateinit var waterScan8: BezierCircle

    private lateinit var mAnimatorLevelSet: AnimatorSet
    private lateinit var mAnimatorScanSet: AnimatorSet
    var radius: Int = dip2px(24f)
    var mContext: Context

    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                0 -> {
                    // 移除所有的msg.what为0等消息，保证只有一个循环消息队列再跑
                    this.removeMessages(0)
                    mAnimatorScanSet.start()
                    // app的功能逻辑处理
                    // 再次发出msg，循环更新
                    sendEmptyMessageDelayed(0, 2000)
                }
                1 ->                     // 直接移除，定时器停止
                    this.removeMessages(0)
                else -> {}
            }
        }
    }

    constructor(context: Context) : super(context) {
        mContext = context
        initWaterScanView()
        initAnimationScanSet()
        startScanAnimator()
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        mContext = context
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        mContext = context
        initView()
    }

    private fun initView() {
        initBgView()
        initWaterDropView()
        initAnimationLevelSet()
        startLevelAnimator()
        initWaterScanView()
        initAnimationScanSet()
        startScanAnimator()
    }

    private fun initBgView() {
        addWaterColorful(8, R.color.water_n_drop8)
        addWaterColorful(7, R.color.water_n_drop7)
        addWaterColorful(6, R.color.water_n_drop6)
        addWaterColorful(5, R.color.water_n_drop5)
        addWaterColorful(4, R.color.water_n_drop4)
        addWaterColorful(3, R.color.water_n_drop3)
        addWaterColorful(2, R.color.water_n_drop2)
        addWaterColorful(1, R.color.water_n_drop1)
    }

    private fun initWaterDropView() {
        water1 = createWaterDrop(1, R.color.water_drop1)
        water2 = createWaterDrop(2, R.color.water_drop2)
        water3 = createWaterDrop(3, R.color.water_drop3)
        water4 = createWaterDrop(4, R.color.water_drop4)
        water5 = createWaterDrop(5, R.color.water_drop5)
        water6 = createWaterDrop(6, R.color.water_drop6)
        water7 = createWaterDrop(7, R.color.water_drop7)
        water8 = createWaterDrop(8, R.color.water_drop8)
    }

    fun resetWaterDrop() {
        removeView(water1)
        removeView(water2)
        removeView(water3)
        removeView(water4)
        removeView(water5)
        removeView(water6)
        removeView(water7)
        removeView(water8)
    }

    //层变动画
    private fun startLevelAnimator() {
//        water1.setAlpha(0.f);
        addView(water1)
        water2.alpha = 0f
        addView(water2)
        water3.alpha = 0f
        addView(water3)
        water4.alpha = 0f
        addView(water4)
        water5.alpha = 0f
        addView(water5)
        water6.alpha = 0f
        addView(water6)
        water7.alpha = 0f
        addView(water7)
        water8.alpha = 0f
        addView(water8)
        mAnimatorLevelSet.start()
    }

    private fun createLevelAnimator(water1: BezierCircle?, delay: Long): ObjectAnimator {
        val timeInterpolator = LinearInterpolator()
        val waterAlpha = ObjectAnimator.ofFloat(water1, "alpha", 0f, 0.5f, 0.2f, 1f)
        waterAlpha.duration = 3000
        waterAlpha.startDelay = delay
        waterAlpha.interpolator = timeInterpolator
        return waterAlpha
    }

    private fun initWaterScanView() {
        waterScan1 = createWaterDrop(1, R.color.water_drop1)
        waterScan2 = createWaterDrop(2, R.color.water_drop2)
        waterScan3 = createWaterDrop(3, R.color.water_drop3)
        waterScan4 = createWaterDrop(4, R.color.water_drop4)
        waterScan5 = createWaterDrop(5, R.color.water_drop5)
        waterScan6 = createWaterDrop(6, R.color.water_drop6)
        waterScan7 = createWaterDrop(7, R.color.water_drop7)
        waterScan8 = createWaterDrop(8, R.color.water_drop8)
    }

    fun resetWaterScan() {
        removeView(waterScan1)
        removeView(waterScan2)
        removeView(waterScan3)
        removeView(waterScan4)
        removeView(waterScan5)
        removeView(waterScan6)
        removeView(waterScan7)
        removeView(waterScan8)
    }

    //层变动画
    private fun startScanAnimator() {
        waterScan1.alpha = 0f
        addView(waterScan1)
        waterScan2.alpha = 0f
        addView(waterScan2)
        waterScan3.alpha = 0f
        addView(waterScan3)
        waterScan4.alpha = 0f
        addView(waterScan4)
        waterScan5.alpha = 0f
        addView(waterScan5)
        waterScan6.alpha = 0f
        addView(waterScan6)
        waterScan7.alpha = 0f
        addView(waterScan7)
        waterScan8.alpha = 0f
        addView(waterScan8)
        handler.sendEmptyMessageDelayed(0, 2000)
    }

    //层变动画
    private fun initAnimationLevelSet() {
        mAnimatorLevelSet = AnimatorSet()
        //        ObjectAnimator water1Alpha = createLevelAnimator(water1, 0);
        val water2Alpha = createLevelAnimator(water2, (0 * 2000).toLong())
        val water3Alpha = createLevelAnimator(water3, (1 * 2000).toLong())
        val water4Alpha = createLevelAnimator(water4, (2 * 2000).toLong())
        val water5Alpha = createLevelAnimator(water5, (3 * 2000).toLong())
        val water6Alpha = createLevelAnimator(water6, (4 * 2000).toLong())
        val water7Alpha = createLevelAnimator(water7, (5 * 2000).toLong())
        val water8Alpha = createLevelAnimator(water8, (6 * 2000).toLong())
        val list: MutableList<Animator> = ArrayList()
        //        list.add(water1Alpha);
        list.add(water2Alpha)
        list.add(water3Alpha)
        list.add(water4Alpha)
        list.add(water5Alpha)
        list.add(water6Alpha)
        list.add(water7Alpha)
        list.add(water8Alpha)
        mAnimatorLevelSet.playTogether(list)
        mAnimatorLevelSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                mHandler.sendEmptyMessage(1)
            }
        })
    }

    //层变动画
    private fun createScanAnimator(
        water1: BezierCircle?,
        delay: Long,
        duration: Long,
        start: Float,
        middle: Float,
        end: Float
    ): ObjectAnimator {
        val timeInterpolator = LinearInterpolator()
        val water1Alpha = ObjectAnimator.ofFloat(water1, "alpha", start, middle, end)
        water1Alpha.duration = duration
        water1Alpha.startDelay = delay
        water1Alpha.interpolator = timeInterpolator
        return water1Alpha
    }

    //扫光动画
    private fun initAnimationScanSet() {
        mAnimatorScanSet = AnimatorSet()
        val water1Alpha = createScanAnimator(waterScan2, 0, 700, 0f, 1.0f, 0f)
        val water2Alpha = createScanAnimator(waterScan3, 233, 630, 0f, 0.8f, 0f)
        val water3Alpha = createScanAnimator(waterScan4, 383, 630, 0f, 0.55f, 0f)
        val water4Alpha = createScanAnimator(waterScan5, 533, 650, 0f, 0.5f, 0f)
        val water5Alpha = createScanAnimator(waterScan6, 667, 650, 0f, 0.45f, 0f)
        val water6Alpha = createScanAnimator(waterScan7, 816, 567, 0f, 0.35f, 0f)
        val water7Alpha = createScanAnimator(waterScan8, 983, 433, 0f, 0.30f, 0f)
        val list: MutableList<Animator> = ArrayList()
        list.add(water1Alpha)
        list.add(water2Alpha)
        list.add(water3Alpha)
        list.add(water4Alpha)
        list.add(water5Alpha)
        list.add(water6Alpha)
        list.add(water7Alpha)
        mAnimatorScanSet.playTogether(list)
    }

    //扫光动画
    private fun addWaterColorful(number: Int, colorResource: Int) {
        addView(createWaterDrop(number, colorResource))
    }

    private fun createWaterDrop(number: Int, colorResource: Int): BezierCircle {
        return BezierCircle(mContext, radius, number, colorResource)
    }

//    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
//        super.onLayout(changed, l, t, r, b)
//    }
//
//    override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas)
//    }
}
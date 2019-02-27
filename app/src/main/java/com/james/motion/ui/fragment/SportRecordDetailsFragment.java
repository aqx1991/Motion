package com.james.motion.ui.fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.james.motion.R;
import com.james.motion.commmon.bean.PathRecord;
import com.james.motion.sport_motion.MotionUtils;
import com.james.motion.ui.BaseFragment;
import com.james.motion.ui.activity.SportRecordDetailsActivity;

import java.text.DecimalFormat;

import butterknife.BindView;

/**
 * 描述: 运动记录详情-详情
 * 作者: james
 * 日期: 2019/2/27 15:25
 * 类名: SportRecordDetailsFragment
 */
public class SportRecordDetailsFragment extends BaseFragment {

    @BindView(R.id.tvDistance)
    TextView tvDistance;
    @BindView(R.id.tvDuration)
    TextView tvDuration;
    @BindView(R.id.tvSpeed)
    TextView tvSpeed;
    @BindView(R.id.tvDistribution)
    TextView tvDistribution;
    @BindView(R.id.tvCalorie)
    TextView tvCalorie;

    private PathRecord pathRecord = null;

    private DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private DecimalFormat intFormat = new DecimalFormat("#");

    @Override
    public int getLayoutId() {
        return R.layout.fragment_sportrecorddetails;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            pathRecord = bundle.getParcelable(SportRecordDetailsActivity.SPORT_DATA);
        }

        if (null != pathRecord) {
            tvDistance.setText(decimalFormat.format(pathRecord.getDistance() / 1000d));
            tvDuration.setText(MotionUtils.formatseconds(pathRecord.getDuration()));
            tvSpeed.setText(decimalFormat.format(pathRecord.getSpeed()));
            tvDistribution.setText(decimalFormat.format(pathRecord.getDistribution()));
            tvCalorie.setText(intFormat.format(pathRecord.getCalorie()));
        }

    }

    @Override
    public void initListener() {

    }
}

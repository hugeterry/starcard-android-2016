package com.xingcard.address.Utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by Administrator on 2015/8/6 0006.
 */
public class Utils {

    public static int dp2px(Context context, int dp) {
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics()));
    }

}

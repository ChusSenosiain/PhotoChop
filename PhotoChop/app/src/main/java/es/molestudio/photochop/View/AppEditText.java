package es.molestudio.photochop.View;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.EditText;

import es.molestudio.photochop.R;
import es.molestudio.photochop.model.enumerator.FontStyleType;

/**
 * Created by Chus on 02/01/15.
 */
public class AppEditText extends EditText {


    public AppEditText(Context context) {
        super(context);
        init(context, null);
    }

    public AppEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AppEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AppEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        if (!this.isInEditMode()) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AppTextFont);

            String fontAsset =  FontStyleType.valueOf(a.getInt(R.styleable.AppTextFont_ats_textFont, FontStyleType.REGULAR.getFontStyleTypeId()))
                    .getFontStyleTypeAsset();

            Typeface typeface = Typeface.createFromAsset(context.getAssets(), fontAsset);

            this.setTypeface(typeface);
        }

    }


}

package epicara.UI;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by nishant on 16-05-09.
 */
public class CardImageView extends ImageView {
        private float mAspectRatio;

        public CardImageView(Context context) {
            super(context);
        }

        public CardImageView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public CardImageView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        public void setAspectRatio(float aspectRatio) {
            mAspectRatio = aspectRatio;
            requestLayout();
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = (int)((float)width / mAspectRatio);
            setMeasuredDimension(width, height);
        }
    }

package droidsonroids.pl.planningpoker;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.greenrobot.eventbus.EventBus;

public class CardFragment extends Fragment {

    private static final String ARG_CARD_TYPE = "arg_card_type";
    private String mCardType;
    private View mCardFaceLayout;
    private View mCardBackLayout;
    boolean isBackVisible = false; // Boolean variable to check if the back image is visible currently
    private AnimatorSet mSetRightOut;
    private AnimatorSet mSetLeftIn;

    public CardFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(String cardType) {
        Bundle args = new Bundle();
        args.putString(ARG_CARD_TYPE, cardType);
        Fragment fragment = new CardFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mCardType = getArguments().getString(ARG_CARD_TYPE);
        View v =  inflater.inflate(R.layout.fragment_card_fargment, container, false);
        mCardFaceLayout = v.findViewById(R.id.card_front);
        mCardBackLayout = v.findViewById(R.id.card_back);

        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.flight_right_out);
        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.flight_left_in);
        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                if (!isBackVisible) {
                    EventBus.getDefault().post(new CardClickedEvent());
                }
                return true;
            }
        });

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                flipCard();

            }
        });
        TextView cardTypeTextView = (TextView) v.findViewById(R.id.card_type_text_view);
        cardTypeTextView.setText(mCardType);
        return v;
    }

    private void flipCard()
    {
        if(!isBackVisible){
            mSetRightOut.setTarget(mCardFaceLayout);
            mSetLeftIn.setTarget(mCardBackLayout);
            mSetRightOut.start();
            mSetLeftIn.start();
            isBackVisible = true;
        }
        else{
            mSetRightOut.setTarget(mCardBackLayout);
            mSetLeftIn.setTarget(mCardFaceLayout);
            mSetRightOut.start();
            mSetLeftIn.start();
            isBackVisible = false;
        }
    }


    public String getCardType() {
        return mCardType;
    }
}

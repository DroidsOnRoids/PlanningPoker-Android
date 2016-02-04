package droidsonroids.pl.planningpoker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CardFragment extends Fragment {

    private static final String ARG_CARD_TYPE = "arg_card_type";
    private String mCardType;

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
        TextView cardTypeTextView = (TextView) v.findViewById(R.id.card_type_text_view);
        cardTypeTextView.setText(mCardType);
        return v;
    }

    public String getCardType() {
        return mCardType;
    }
}

package droidsonroids.pl.planningpoker;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.List;

public class CardPagerAdapter extends FragmentPagerAdapter {

    String[] cardTypes = {"0", "1", "2", "3", "5", "8", "13", "21","40", "100", "?"};

    private static final int CARD_NUMBER = 11;


    public CardPagerAdapter(final FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(final int position) {
        return CardFragment.newInstance(cardTypes[position]);
    }

    @Override
    public int getCount() {
        return CARD_NUMBER;
    }
}

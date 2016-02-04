package droidsonroids.pl.planningpoker;

public class CardClickedEvent {
    private final String mCardText;

    public CardClickedEvent(final String cardText) {
        mCardText = cardText;
    }

    public String getCardText() {
        return mCardText;
    }
}

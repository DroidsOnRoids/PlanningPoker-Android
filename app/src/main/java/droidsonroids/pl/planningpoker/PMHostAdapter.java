package droidsonroids.pl.planningpoker;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

public class PMHostAdapter extends RecyclerView.Adapter<PMHostAdapter.ViewHolder> {

    public ArrayList<Client> mClients;

    public PMHostAdapter() {
        mClients = new ArrayList<>();
    }

    private boolean mShowStoryPoints = false;

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_host_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.bindClient(mClients.get(position));
    }

    @Override
    public int getItemCount() {
        return mClients.size();
    }

    public void addClient(final Client client) {
        mClients.add(client);
        notifyItemInserted(getItemCount()-1);
    }

    public void removeClient(final String endpointId) {
        for (int i = 0; i < getItemCount() - 1; i++) {
            final Client client = mClients.get(i);
            if(client.getEndpointId().equals(endpointId)) {
                mClients.remove(i);
                notifyItemRemoved(i);
            }
        }
    }

    public void updateClientStoryPoints(final String clientId, final String cardText) {
        for (int i = 0; i < mClients.size(); i++) {
            final Client client = mClients.get(i);
            if (client.getEndpointId().equals(clientId)) {
                client.setStoryPoints(cardText);
                notifyItemChanged(i);
                break;
            }
        }

        for (Client client : mClients) {
            if (client.getStoryPoints() == null) {
                return;
            }
        }

        mShowStoryPoints = true;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextClient;
        private TextView mTextStoryPoints;

        private View mCardFaceLayout;
        private View mCardBackLayout;
        boolean isBackVisible = true; // Boolean variable to check if the back image is visible currently
        private AnimatorSet mSetRightOut;
        private AnimatorSet mSetLeftIn;

        public ViewHolder(final View itemView) {
            super(itemView);
            mTextClient = (TextView) itemView.findViewById(R.id.text_client_name);
            mTextStoryPoints = (TextView) itemView.findViewById(R.id.card_type_text_view);

            mCardFaceLayout = itemView.findViewById(R.id.card_front);
            mCardBackLayout = itemView.findViewById(R.id.card_back);

            mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(itemView.getContext(), R.animator.flight_right_out);
            mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(itemView.getContext(), R.animator.flight_left_in);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View v) {
                    mShowStoryPoints = false;
                    for (Client client : mClients) {
                        client.setStoryPoints(null);
                    }
                    notifyDataSetChanged();
                    return true;
                }
            });
        }

        public void bindClient(final Client client) {
            mTextClient.setText(client.getName());
            mTextStoryPoints.setText(client.getStoryPoints());

            if (isBackVisible && mShowStoryPoints) {
                flipCard();
            } else if (!isBackVisible && !mShowStoryPoints) {
                flipCard();
            }
        }

        private void flipCard() {
            if (!isBackVisible) {
                mSetRightOut.setTarget(mCardFaceLayout);
                mSetLeftIn.setTarget(mCardBackLayout);
                mSetRightOut.start();
                mSetLeftIn.start();
                isBackVisible = true;
            } else {
                mSetRightOut.setTarget(mCardBackLayout);
                mSetLeftIn.setTarget(mCardFaceLayout);
                mSetRightOut.start();
                mSetLeftIn.start();
                isBackVisible = false;
            }
        }
    }
}

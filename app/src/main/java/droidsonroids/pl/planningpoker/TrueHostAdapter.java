package droidsonroids.pl.planningpoker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

public class TrueHostAdapter extends RecyclerView.Adapter<TrueHostAdapter.ViewHolder> {

    public ArrayList<Client> mClients;

    public TrueHostAdapter() {
        mClients = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_host_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mTextClient.setText(mClients.get(position).getName());
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTextClient;
        TextView mTextStoryPoints;

        public ViewHolder(final View itemView) {
            super(itemView);
            mTextClient = (TextView) itemView.findViewById(R.id.text_client_name);
            mTextStoryPoints = (TextView) itemView.findViewById(R.id.card_type_text_view);

        }
    }
}

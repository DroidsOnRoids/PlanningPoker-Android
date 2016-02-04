package droidsonroids.pl.planningpoker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class DiscoverHostAdapter extends RecyclerView.Adapter<DiscoverHostAdapter.ViewHolder> {

    private final OnHostClickListener mOnHostClickListener;
    private List<Host> mHostList = new ArrayList<>();

    public DiscoverHostAdapter(final OnHostClickListener onHostClickListener) {
        mOnHostClickListener = onHostClickListener;
    }

    @Override
    public DiscoverHostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.host_name_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mTextView.setText(mHostList.get(position).getEndpointName());
        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mOnHostClickListener.onHostClick(mHostList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mHostList.size();
    }

    public void addHost(final String endpointId, final String endpointName) {
        for (Host host : mHostList) {
            if (host.getEndpointId().equals(endpointId)) {
                return;
            }
        }

        mHostList.add(0, new Host(endpointId, endpointName));
        notifyItemInserted(0);
    }
    public void removeHost(String endpointId) {
        for (int i = 0; i < mHostList.size(); i++) {
            final Host host = mHostList.get(i);

            if(host.getEndpointId().equals(endpointId)) {
                mHostList.remove(host);
                notifyItemRemoved(i);
                return;
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.host_name_text_view);
        }
    }

    public interface OnHostClickListener {
        void onHostClick(final Host host);
    }
}
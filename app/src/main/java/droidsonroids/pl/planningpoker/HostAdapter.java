package droidsonroids.pl.planningpoker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class HostAdapter  extends RecyclerView.Adapter<HostAdapter.ViewHolder> {

    List<String> hostList = new ArrayList();

    public void addHost(String host) {
        hostList.add(host);
        notifyDataSetChanged();
    }


    @Override
    public HostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.host_name_item, parent, false);
       return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(hostList.get(position));

    }

    @Override
    public int getItemCount() {
        return hostList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.host_name_text_view);
        }
    }
}
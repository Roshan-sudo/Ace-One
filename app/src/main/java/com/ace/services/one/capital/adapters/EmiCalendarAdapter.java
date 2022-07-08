package com.ace.services.one.capital.adapters;

import static com.ace.services.one.capital.DefaultValues.DECIMAL_FORMAT;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ace.services.one.capital.LoanDetailsActivity;
import com.ace.services.one.capital.R;
import com.ace.services.one.capital.models.EmiCalendarModel;

import java.util.List;

public class EmiCalendarAdapter extends RecyclerView.Adapter<EmiCalendarAdapter.MyViewHolder> {
    private final Context context;
    private final List<EmiCalendarModel> emiCalendarModels = LoanDetailsActivity.emiCalendarModels;

    public EmiCalendarAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.emi_calendar_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        EmiCalendarModel emiCalendarModel = emiCalendarModels.get(position);
        if (emiCalendarModel.isPaid()){
            holder.txtEmiStatus.setText("PAID");
            holder.txtEmiStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_active_loan));
        }else {
            holder.txtEmiStatus.setText("SCHEDULED");
            holder.txtEmiStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_pay_now));
        }
        int emiNo = emiCalendarModel.getEmiNos();

        holder.emi_no.setText(HtmlCompat.fromHtml(emiNo + "<sup>"+ getSupString(emiNo) +"</sup> EMI", HtmlCompat.FROM_HTML_MODE_LEGACY));
        holder.emi_amount.setText(DECIMAL_FORMAT.format(emiCalendarModel.getEmiAmount()));
    }

    @Override
    public int getItemCount() {
        return emiCalendarModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // XML Components
        private final TextView txtEmiStatus, emi_no, emi_amount;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            // Link XML Components
            txtEmiStatus = itemView.findViewById(R.id.txtEmiStatus);
            emi_no = itemView.findViewById(R.id.emi_no);
            emi_amount = itemView.findViewById(R.id.emi_amount);
        }
    }

    /**
     * Function to return the super script ordinals of the given number
     * for example - if the given number is 1, then the function will return "st" i.e, 1st
     * for the given number 2, the function will return "nd", i.e, 2nd and so on
     * @param num number to get its super script ordinals
     * @return returns the "super script ordinals" of given number
     */
    private String getSupString(int num){
        if (num == 1) return  "st";
        else if (num == 2) return  "nd";
        else if (num == 3) return "rd";
        else return "th";
    }
}

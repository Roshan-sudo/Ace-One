package com.ace.services.one.capital.adapters;

import static com.ace.services.one.capital.DefaultValues.DECIMAL_FORMAT;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ace.services.one.capital.PassbookActivity;
import com.ace.services.one.capital.R;
import com.ace.services.one.capital.models.PassbookModel;
import com.ace.services.one.capital.models.UpcomingEmiModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class PassbookAdapter extends RecyclerView.Adapter<PassbookAdapter.MyViewHolder> {
    private final Context context;
    private OnDetailsClickListener mListener;

    public PassbookAdapter(Context context) {
        this.context = context;
    }

    // Interface to detect click listener
    public interface OnDetailsClickListener{
        void OnDetailsBtnClick(int position);
    }

    public void setOnDetailsClickListener(OnDetailsClickListener listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public PassbookAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.passbook_adapter, parent, false);
        return new MyViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PassbookAdapter.MyViewHolder holder, int position) {
        PassbookModel passbookModel = PassbookActivity.passbookModels.get(position);

        if (passbookModel.isActive()){
            holder.txtCreditSummary.setText("ACTIVE");
            holder.txtCreditSummary.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_active_loan));
            holder.btn_pay_now.setVisibility(View.VISIBLE);
            holder.img_check_it.setVisibility(View.GONE);
        }else {
            holder.txtCreditSummary.setText("CLOSED");
            holder.txtCreditSummary.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_pay_now));
            holder.btn_pay_now.setVisibility(View.GONE);
            holder.img_check_it.setVisibility(View.VISIBLE);
        }
        holder.emi_remaining.setText(String.valueOf(passbookModel.getEmiRemaining()));

        UpcomingEmiModel upcomingEmiModel = getEmiModel(passbookModel.getLoanId());
        if (upcomingEmiModel != null){
            holder.due_in.setText(String.format("Due in %s Days", getRemainingDays(upcomingEmiModel.getDueDate())));
            holder.due_date.setText(upcomingEmiModel.getDueDate());
            holder.txt_emi_due_amount.setText(String.format("INR %s", DECIMAL_FORMAT.format(upcomingEmiModel.getDueAmount())));
        }
    }

    @Override
    public int getItemCount() {
        return PassbookActivity.passbookModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // XML Components
        private final TextView due_in, txt_emi_due_amount, due_date, emi_remaining, txtCreditSummary;
        private final Button btn_pay_now;
        private final ImageView img_check_it;

        public MyViewHolder(@NonNull View itemView, OnDetailsClickListener listener) {
            super(itemView);

            // Link XML Components
            Button btn_details = itemView.findViewById(R.id.btn_details);
            due_in = itemView.findViewById(R.id.due_in);
            due_date = itemView.findViewById(R.id.due_date);
            emi_remaining = itemView.findViewById(R.id.emi_remaining);
            txt_emi_due_amount = itemView.findViewById(R.id.txt_emi_due_amount);
            txtCreditSummary = itemView.findViewById(R.id.txtCreditSummary);
            btn_pay_now = itemView.findViewById(R.id.btn_pay_now);
            img_check_it = itemView.findViewById(R.id.img_check_it);

            btn_details.setOnClickListener(v -> {
                if (listener != null){
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listener.OnDetailsBtnClick(position);
                    }
                }
            });
        }
    }

    /**
     * Function to calculate "days left" from the current date to the given date
     * @param dueDate the upcoming due date
     * @return returns the total days left
     */
    private String getRemainingDays(String dueDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        LocalDate date = LocalDate.parse(dueDate, formatter);
        LocalDate now = LocalDate.now();
        return String.valueOf(ChronoUnit.DAYS.between(now, date));
    }

    private UpcomingEmiModel getEmiModel(String loanId){
        UpcomingEmiModel upcomingEmiModel = null;
        for (UpcomingEmiModel model : PassbookActivity.upcomingEmiModels){
            if (Objects.equals(model.getLoanId(), loanId)){
                upcomingEmiModel = model;
                break;
            }
        }
        return upcomingEmiModel;
    }
}

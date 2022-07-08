package com.ace.services.one.capital.adapters;

import static com.ace.services.one.capital.DefaultValues.DECIMAL_FORMAT;
import static com.ace.services.one.capital.DefaultValues.TRANSACTION_FAILED;
import static com.ace.services.one.capital.DefaultValues.TRANSACTION_PENDING;
import static com.ace.services.one.capital.DefaultValues.TRANSACTION_SUCCESS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ace.services.one.capital.LoanDetailsActivity;
import com.ace.services.one.capital.R;
import com.ace.services.one.capital.models.TransactionModel;

import java.util.List;

public class LoanTransactionAdapter extends RecyclerView.Adapter<LoanTransactionAdapter.MyViewHolder> {
    private final Context context;
    private final List<TransactionModel> transactionModels = LoanDetailsActivity.transactionModels;

    public LoanTransactionAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public LoanTransactionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loan_transaction_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoanTransactionAdapter.MyViewHolder holder, int position) {
        TransactionModel transactionModel = transactionModels.get(position);
        if (transactionModel.getStatus() == TRANSACTION_SUCCESS){
            holder.txtTransactionStatus.setText("SUCCESS");
            holder.txtTransactionStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_active_loan));
        }else if (transactionModel.getStatus() == TRANSACTION_FAILED){
            holder.txtTransactionStatus.setText("FAILED");
            holder.txtTransactionStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_failed));
        }else if (transactionModel.getStatus() == TRANSACTION_PENDING){
            holder.txtTransactionStatus.setText("PENDING");
            holder.txtTransactionStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_pending));
        }

        holder.transaction_amount.setText(String.format("INR %s", DECIMAL_FORMAT.format(transactionModel.getAmount())));
        holder.transaction_date.setText(transactionModel.getDate());
        holder.transaction_time.setText(transactionModel.getTime());
        holder.transaction_id.setText(String.valueOf(transactionModel.getTransactionId()));
        holder.payment_mode.setText(transactionModel.getPaymentMode());
    }

    @Override
    public int getItemCount() {
        return transactionModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // XML Components
        private final TextView txtTransactionStatus, transaction_amount, transaction_date, transaction_time, transaction_id, payment_mode;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            // Link XML Components
            txtTransactionStatus = itemView.findViewById(R.id.txtTransactionStatus);
            transaction_amount = itemView.findViewById(R.id.transaction_amount);
            transaction_date = itemView.findViewById(R.id.transaction_date);
            transaction_time = itemView.findViewById(R.id.transaction_time);
            transaction_id = itemView.findViewById(R.id.transaction_id);
            payment_mode = itemView.findViewById(R.id.payment_mode);
        }
    }
}

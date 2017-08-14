package packi.day.ui.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.journeemondialelib.share.AnalyticsTracker;

import java.io.IOException;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import packi.day.R;
import packi.day.databinding.FragmentRequestBinding;
import packi.day.network.Network;
import packi.day.ui.ActivityMain;

import static packi.day.R.string.loading;

public class RequestFragment extends Fragment {

    private String description;
    private Date selectedDate = new Date();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        init(savedInstanceState);
        FragmentRequestBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_request, container, false);
        binding.calendarView.setDateAsSelected(selectedDate);
        binding.calendarView.setOnDateSelectedListener(date -> selectedDate = date);
        binding.setHandler(this);
        binding.setDescription(description);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        AnalyticsTracker.getInstance(getActivity()).sendTracker("/request");
        ((ActivityMain) getActivity()).setScreenTitle(R.string.report_an_error);
    }

    private void init(@Nullable Bundle savedInstanceState) {
        if(savedInstanceState == null) {
            return;
        }

        if (savedInstanceState.containsKey("desc")) {
            description = savedInstanceState.getString("desc");
        }

        if (savedInstanceState.containsKey("date")) {
            selectedDate = (Date) savedInstanceState.getSerializable("date");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("desc", description);
        outState.putSerializable("date", selectedDate);
        super.onSaveInstanceState(outState);
    }

    public void send(String desc) {
        if (selectedDate == null) {
            displayDialog(R.string.please_pic_date);
            return;
        }

        if (TextUtils.isEmpty(desc)) {
            displayDialog(R.string.put_more_info);
            return;
        }

        AlertDialog dialog = new AlertDialog
                .Builder(getActivity(), R.style.MyAlertDialogStyle)
                .setTitle(loading)
                .setMessage(R.string.please_wait)
                .show();

        String date = selectedDate.toString();

        Network.post(desc, date, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(() -> fail(dialog));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                getActivity().runOnUiThread(() -> success(dialog));
            }
        });
    }

    private void success(AlertDialog loading) {
        dismiss(loading);
        resetView();
        displayDialog(R.string.thanks_for_report);
    }

    private void fail(AlertDialog loading) {
        dismiss(loading);
        displayDialog(R.string.an_error_occurred);
    }

    private void dismiss(AlertDialog loading) {
        if (loading != null) {
            loading.dismiss();
        }
    }

    private void resetView() {
        View view = getView();
        if (view != null) {
            FragmentRequestBinding binding = DataBindingUtil.getBinding(view);
            binding.setDescription(null);
        }
    }

    private void displayDialog(int string) {
        new AlertDialog
                .Builder(getActivity(), R.style.MyAlertDialogStyle)
                .setTitle(R.string.app_name)
                .setMessage(string)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

}

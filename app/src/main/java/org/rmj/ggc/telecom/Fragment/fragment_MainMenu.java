package org.rmj.ggc.telecom.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rmj.ggc.telecom.R;

import org.rmj.g3cm.android.g3cashmanager.Activity_CashCounter;
import org.rmj.ggc.samsung_knox.Activity.Activity_Knox;
import org.rmj.ggc.telecom.JavaExtras.ExitDialog;
import org.rmj.ggc.telecom.JavaExtras.SessionHandler;
import org.rmj.guanzongroup.promotions.Activity_RaffleEntry;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_MainMenu extends Fragment {
    private static final String TAG = fragment_MainMenu.class.getSimpleName();

    public fragment_MainMenu() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);
        SessionHandler sessionHandler = new SessionHandler(Objects.requireNonNull(getActivity()));
        ProgressDialog pDialog = new ProgressDialog(getActivity());
        Handler handler = new Handler();
        setupWigets(view);
        return view;
    }

    private void setupWigets(View v){
        CardView btnCreditApp = v.findViewById(R.id.btn_tcm_creditApp);
        CardView btnKnoxRegxx = v.findViewById(R.id.btn_tcm_KnoxRegxx);
        CardView btnCashCount = v.findViewById(R.id.btn_tcm_CashCount);
        CardView btnApplListx = v.findViewById(R.id.btn_tcm_ApplList);
        CardView btnCshCntLst = v.findViewById(R.id.btn_tcm_CshCntList);
        CardView btnAppHelpxx = v.findViewById(R.id.btn_tcm_AppHelpxx);

        btnCreditApp.setOnClickListener(new OnButtonClickListener());
        btnKnoxRegxx.setOnClickListener(new OnButtonClickListener());
        btnCashCount.setOnClickListener(new OnButtonClickListener());
        btnApplListx.setOnClickListener(new OnButtonClickListener());
        btnCshCntLst.setOnClickListener(new OnButtonClickListener());
        btnAppHelpxx.setOnClickListener(new OnButtonClickListener());
    }

    private void OpenExitDialog(){
        ExitDialog exitDialog = new ExitDialog();
        exitDialog.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(),"Application Exit");
    }

    class OnButtonClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id){
                case R.id.btn_tcm_creditApp:
                    //startActivity(new Intent(getActivity(), Activity_IntroductoryQuestion.class));
                    break;
                case R.id.btn_tcm_KnoxRegxx:
                    startActivity(new Intent(getActivity(), Activity_Knox.class));
                    break;
                case R.id.btn_tcm_CashCount:
                    startActivity(new Intent(getActivity(), Activity_RaffleEntry.class));
                    break;
                case R.id.btn_tcm_ApplList:
                    //startActivity(new Intent(getActivity(), Activity_ApplicationLog.class));
                    break;
                case R.id.btn_tcm_CshCntList:
                    startActivity(new Intent(getActivity(), Activity_CashCounter.class));
                    break;
                case R.id.btn_tcm_AppHelpxx:
                    //startActivity(new Intent(getActivity(), WelcomeLauncherActivity.class));
                    break;
            }
        }
    }
}

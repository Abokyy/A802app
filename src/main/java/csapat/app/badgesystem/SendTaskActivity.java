package csapat.app.badgesystem;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import csapat.app.BaseCompat;
import csapat.app.R;

public class SendTaskActivity extends BaseCompat {

    private EditText taskSolution;
    private Button submitBtn;
    private Button choosePicBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_task);
    }
}

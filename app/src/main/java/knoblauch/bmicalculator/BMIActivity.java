package knoblauch.bmicalculator;

import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class BMIActivity extends Activity {

    private TextView weight;
    private TextView height;

    private RadioGroup unit;

    private Button calculate;
    private Button reset;

    private TextView bmi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Retrieve local views.
        weight = findViewById(R.id.weight_value);
        height = findViewById(R.id.height_value);

        unit = findViewById(R.id.unit_group);

        calculate = findViewById(R.id.bmi_calculate);
        reset = findViewById(R.id.bmi_reset);

        bmi = findViewById(R.id.bmi_value);

        // Connect signals to local slots.
        weight.addTextChangedListener(propsChangedWatcher);
        height.addTextChangedListener(propsChangedWatcher);

        calculate.setOnClickListener(computeListener);

        reset.setOnClickListener(resetListener);
    }

    private View.OnClickListener resetListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // Reset other elements.
            weight.setText("");
            height.setText("");
            bmi.setText(R.string.bmi_default_msg);
        }
    };

    private View.OnClickListener computeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Retrieve local properties.
            String wStr = weight.getText().toString();
            String hStr = height.getText().toString();

            if (wStr.isEmpty()) {
                Toast.makeText(BMIActivity.this, "Please enter a valid weight", Toast.LENGTH_SHORT).show();
                return;
            }
            if (hStr.isEmpty()) {
                Toast.makeText(BMIActivity.this, "Please enter a valid height", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean meters = (unit.getCheckedRadioButtonId() == R.id.meters_checkbox);

            float w, h;
            try {
                w = Float.valueOf(wStr);
            }
            catch (NumberFormatException e) {
                Toast.makeText(BMIActivity.this, "Invalid value for weight \"" + wStr + "\"", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                h = Float.valueOf(hStr);
                if (!meters) {
                    h /= 100.0f;
                }
            }
            catch (NumberFormatException e) {
                Toast.makeText(BMIActivity.this, "Invalid value for height\"" + hStr+ "\"", Toast.LENGTH_SHORT).show();
                return;
            }

            float epsilon = Float.parseFloat(getString(R.string.weight_epsilon));

            if (h < epsilon) {
                Toast.makeText(BMIActivity.this, "Your height is too low: please eat some more soup (" + h + " seriously ??)", Toast.LENGTH_SHORT).show();
                return;
            }

            if (w < 0.0f) {
                Toast.makeText(BMIActivity.this, "You seem like a very lightperson (" + w + " seriously ??", Toast.LENGTH_SHORT).show();
                return;
            }

            float bmiVal = w / (h * h);

            bmi.setText(String.valueOf(bmiVal));
        }
    };

    private TextWatcher propsChangedWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Nothing to do.
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            bmi.setText(R.string.bmi_default_msg);
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Nothing to do.
        }
    };
}

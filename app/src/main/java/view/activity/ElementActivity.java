package view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import processor.OpenCV.Element;
import superbiayang.imageprocessing.R;

public class ElementActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_element);

        Intent intent = getIntent();
        EditText editText = (EditText) findViewById(R.id.element_size_editText);
        editText.setText(String.valueOf(intent.getIntExtra("size", Element.DEFAULT_SIZE)));
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.element_type_radioGroup);
        switch (intent.getIntExtra("type", Element.DEFAULT_TYPE)) {
            case Element.MORPH_RECT:
                radioGroup.check(R.id.rect_radioButton);
                break;
            case Element.MORPH_CROSS:
                radioGroup.check(R.id.cross_radioButton);
                break;
            case Element.MORPH_ELLIPSE:
                radioGroup.check(R.id.ellipse_radioButton);
                break;
        }

    }

    public void setElement(View v) {
        EditText editText = (EditText) findViewById(R.id.element_size_editText);
        int size = Integer.parseInt(editText.getText().toString());
        if (size % 2 != 1) {
            TextView textView = (TextView) findViewById(R.id.element_warning_textView);
            textView.setText("Size must be odd number!");
            return;
        }

        int type = Element.MORPH_RECT;
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.element_type_radioGroup);
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.rect_radioButton:
                type = Element.MORPH_RECT;
                break;
            case R.id.cross_radioButton:
                type = Element.MORPH_CROSS;
                break;
            case R.id.ellipse_radioButton:
                type = Element.MORPH_ELLIPSE;
                break;
        }

        Intent intent = new Intent();
        intent.putExtra("size", size);
        intent.putExtra("type", type);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void cancelSet(View v) {
        finish();
    }
}

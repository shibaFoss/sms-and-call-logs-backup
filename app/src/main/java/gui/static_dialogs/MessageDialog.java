package gui.static_dialogs;

import android.app.Dialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import gui.BaseActivity;
import in.softc.app.R;
import utils.DialogUtility;
import utils.Font;

/**
 * The class displays a message dialog to the user.
 */
public class MessageDialog implements View.OnClickListener {

    public Dialog dialog;
    private TextView title, message, yesBnt, noBnt;
    private LinearLayout titleLayout;
    private View actionButtonBorder;
    private OnClickButton onClickButtonListener;

    /**
     * Public constructor.
     * @param activity the activity is needed for building the dialog.
     */
    public MessageDialog(BaseActivity activity) {
        this.dialog = DialogUtility.generateNewDialog(activity, R.layout.dialog_message);

        this.title = (TextView) dialog.findViewById(R.id.txt_title);
        this.message = (TextView) dialog.findViewById(R.id.txt_recent_message);
        this.yesBnt = (TextView) dialog.findViewById(R.id.bntYes);
        this.noBnt = (TextView) dialog.findViewById(R.id.bntNo);
        this.titleLayout = (LinearLayout) dialog.findViewById(R.id.title_layout);
        this.actionButtonBorder = dialog.findViewById(R.id.border_action);

        title.setTypeface(Font.LatoMedium);

        yesBnt.setTypeface(Font.LatoMedium);
        noBnt.setTypeface(Font.LatoMedium);
        message.setTypeface(Font.LatoRegular);


        yesBnt.setOnClickListener(this);
        noBnt.setOnClickListener(this);

        //set the visibility of title layout, because we don't need the title and the no button.
        titleLayout.setVisibility(View.GONE);
        noBnt.setVisibility(View.GONE);
        actionButtonBorder.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == yesBnt.getId()) {
            if (onClickButtonListener != null)
                onClickButtonListener.onYesClick(this);
            else
                dialog.dismiss();

        } else if (view.getId() == noBnt.getId()) {
            if (onClickButtonListener != null)
                onClickButtonListener.onNoClick(this);
            else
                dialog.dismiss();
        }
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public void show() {
        dialog.show();
    }

    /**
     * The function sets the given string to the dialog's title.
     * @param titleText the string to be set on dialog's title.
     */
    public MessageDialog setTitle(String titleText) {
        if (titleText != null && titleText.length() > 1) {
            if (titleLayout.getVisibility() != View.VISIBLE)
                titleLayout.setVisibility(View.VISIBLE);
            title.setText(titleText);
        }
        return this;
    }

    /**
     * The function sets the given string to the dialog's message field.
     * @param messageText the string to be set on dialog's message field.
     */
    public MessageDialog setMessage(String messageText) {
        message.setText(messageText);
        return this;
    }

    /**
     * The function sets the string to the dialog's action buttons.
     * @param yesBntText the string of yes button.
     * @param noBntText  the string of no button.
     */
    public MessageDialog setButtonName(String yesBntText, String noBntText) {
        yesBnt.setText(yesBntText);
        if (noBntText != null && noBntText.length() > 1) {
            actionButtonBorder.setVisibility(View.VISIBLE);
            noBnt.setVisibility(View.VISIBLE);
            noBnt.setText(noBntText);
        }
        return this;
    }

    public MessageDialog setCallback(OnClickButton callback) {
        this.onClickButtonListener = callback;
        return this;
    }

    /**
     * Th interface is used for callback mechanism of button click event.
     */
    public interface OnClickButton {
        void onYesClick(MessageDialog messageDialog);

        void onNoClick(MessageDialog messageDialog);
    }
}

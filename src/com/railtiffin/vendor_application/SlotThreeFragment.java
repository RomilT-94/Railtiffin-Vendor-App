package com.railtiffin.vendor_application;

import java.util.Calendar;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * @author Romil
 * 
 */
public class SlotThreeFragment extends Fragment {

	View rootView;
	TextView tvStartMonday, tvStartTuesday, tvStartWednesday, tvStartThursday,
			tvStartFriday, tvStartSaturday, tvStartSunday;
	TextView tvEndMonday, tvEndTuesday, tvEndWednesday, tvEndThursday,
			tvEndFriday, tvEndSaturday, tvEndSunday;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		rootView = inflater.inflate(R.layout.fragment_time_slot, container,
				false);

		// Start Time TextViews
		tvStartMonday = (TextView) rootView.findViewById(R.id.tvStartMonday);
		tvStartTuesday = (TextView) rootView.findViewById(R.id.tvStartTuesday);
		tvStartWednesday = (TextView) rootView
				.findViewById(R.id.tvStartWednesday);
		tvStartThursday = (TextView) rootView
				.findViewById(R.id.tvStartThursday);
		tvStartFriday = (TextView) rootView.findViewById(R.id.tvStartFriday);
		tvStartSaturday = (TextView) rootView
				.findViewById(R.id.tvStartSaturday);
		tvStartSunday = (TextView) rootView.findViewById(R.id.tvStartSunday);

		// End Time TextViews
		tvEndMonday = (TextView) rootView.findViewById(R.id.tvEndMonday);
		tvEndTuesday = (TextView) rootView.findViewById(R.id.tvEndTuesday);
		tvEndWednesday = (TextView) rootView.findViewById(R.id.tvEndWednesday);
		tvEndThursday = (TextView) rootView.findViewById(R.id.tvEndThursday);
		tvEndFriday = (TextView) rootView.findViewById(R.id.tvEndFriday);
		tvEndSaturday = (TextView) rootView.findViewById(R.id.tvEndSaturday);
		tvEndSunday = (TextView) rootView.findViewById(R.id.tvEndSunday);

		CheckBox cbTuesday = (CheckBox) rootView.findViewById(R.id.cbTuesday);
		cbTuesday.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					tvStartTuesday.setText(tvStartMonday.getText().toString());
					tvEndTuesday.setText(tvEndMonday.getText().toString());
				} else {
					tvStartTuesday.setText("Start");
					tvEndTuesday.setText("End");
				}

			}
		});
		CheckBox cbWednesday = (CheckBox) rootView
				.findViewById(R.id.cbWednesday);
		cbWednesday.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					tvStartWednesday
							.setText(tvStartMonday.getText().toString());
					tvEndWednesday.setText(tvEndMonday.getText().toString());
				} else {
					tvStartWednesday.setText("Start");
					tvEndWednesday.setText("End");
				}

			}
		});
		CheckBox cbThursday = (CheckBox) rootView.findViewById(R.id.cbThursday);
		cbThursday.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					tvStartThursday.setText(tvStartMonday.getText().toString());
					tvEndThursday.setText(tvEndMonday.getText().toString());
				} else {
					tvStartThursday.setText("Start");
					tvEndThursday.setText("End");
				}

			}
		});
		CheckBox cbFriday = (CheckBox) rootView.findViewById(R.id.cbFriday);
		cbFriday.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					tvStartFriday.setText(tvStartMonday.getText().toString());
					tvEndFriday.setText(tvEndMonday.getText().toString());
				} else {
					tvStartFriday.setText("Start");
					tvEndFriday.setText("End");
				}

			}
		});
		CheckBox cbSaturday = (CheckBox) rootView.findViewById(R.id.cbSaturday);
		cbSaturday.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					tvStartSaturday.setText(tvStartMonday.getText().toString());
					tvEndSaturday.setText(tvEndMonday.getText().toString());
				} else {
					tvStartSaturday.setText("Start");
					tvEndSaturday.setText("End");
				}

			}
		});
		CheckBox cbSunday = (CheckBox) rootView.findViewById(R.id.cbSunday);
		cbSunday.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					tvStartSunday.setText(tvStartMonday.getText().toString());
					tvEndSunday.setText(tvEndMonday.getText().toString());
				} else {
					tvStartSunday.setText("Start");
					tvEndSunday.setText("End");
				}

			}
		});

		final Button startMonday = (Button) rootView
				.findViewById(R.id.bStartMonday);
		final Button endMonday = (Button) rootView
				.findViewById(R.id.bEndMonday);

		final Button startTuesday = (Button) rootView
				.findViewById(R.id.bStartTuesday);
		final Button endTuesday = (Button) rootView
				.findViewById(R.id.bEndTuesday);

		final Button startWednesday = (Button) rootView
				.findViewById(R.id.bStartWednesday);
		final Button endWednesday = (Button) rootView
				.findViewById(R.id.bEndWednesday);

		final Button startThursday = (Button) rootView
				.findViewById(R.id.bStartThursday);
		final Button endThursday = (Button) rootView
				.findViewById(R.id.bEndThursday);

		final Button startFriday = (Button) rootView
				.findViewById(R.id.bStartFriday);
		final Button endFriday = (Button) rootView
				.findViewById(R.id.bEndFriday);

		final Button startSaturday = (Button) rootView
				.findViewById(R.id.bStartSaturday);
		final Button endSaturday = (Button) rootView
				.findViewById(R.id.bEndSaturday);

		final Button startSunday = (Button) rootView
				.findViewById(R.id.bStartSunday);
		final Button endSunday = (Button) rootView
				.findViewById(R.id.bEndSunday);
		startMonday.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				DialogFragment newFragment = new TimePickerFragment(
						tvStartMonday);
				newFragment.show(getActivity().getSupportFragmentManager(),
						"timePicker");

			}
		});
		endMonday.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				DialogFragment newFragment = new TimePickerFragment(tvEndMonday);
				newFragment.show(getActivity().getSupportFragmentManager(),
						"timePicker");

			}
		});

		startTuesday.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				DialogFragment newFragment = new TimePickerFragment(
						tvStartTuesday);
				newFragment.show(getActivity().getSupportFragmentManager(),
						"timePicker");

			}
		});
		endTuesday.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				DialogFragment newFragment = new TimePickerFragment(
						tvEndTuesday);
				newFragment.show(getActivity().getSupportFragmentManager(),
						"timePicker");

			}
		});

		startWednesday.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				DialogFragment newFragment = new TimePickerFragment(
						tvStartWednesday);
				newFragment.show(getActivity().getSupportFragmentManager(),
						"timePicker");

			}
		});
		endWednesday.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				DialogFragment newFragment = new TimePickerFragment(
						tvEndWednesday);
				newFragment.show(getActivity().getSupportFragmentManager(),
						"timePicker");

			}
		});

		startThursday.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				DialogFragment newFragment = new TimePickerFragment(
						tvStartThursday);
				newFragment.show(getActivity().getSupportFragmentManager(),
						"timePicker");

			}
		});
		endThursday.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				DialogFragment newFragment = new TimePickerFragment(
						tvEndThursday);
				newFragment.show(getActivity().getSupportFragmentManager(),
						"timePicker");

			}
		});

		startFriday.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				DialogFragment newFragment = new TimePickerFragment(
						tvStartFriday);
				newFragment.show(getActivity().getSupportFragmentManager(),
						"timePicker");

			}
		});
		endFriday.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				DialogFragment newFragment = new TimePickerFragment(tvEndFriday);
				newFragment.show(getActivity().getSupportFragmentManager(),
						"timePicker");

			}
		});

		startSaturday.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				DialogFragment newFragment = new TimePickerFragment(
						tvStartSaturday);
				newFragment.show(getActivity().getSupportFragmentManager(),
						"timePicker");

			}
		});
		endSaturday.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				DialogFragment newFragment = new TimePickerFragment(
						tvEndSaturday);
				newFragment.show(getActivity().getSupportFragmentManager(),
						"timePicker");

			}
		});

		startSunday.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				DialogFragment newFragment = new TimePickerFragment(
						tvStartSunday);
				newFragment.show(getActivity().getSupportFragmentManager(),
						"timePicker");

			}
		});
		endSunday.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				DialogFragment newFragment = new TimePickerFragment(tvEndSunday);
				newFragment.show(getActivity().getSupportFragmentManager(),
						"timePicker");

			}
		});

		return rootView;
	}

	public static class TimePickerFragment extends DialogFragment implements
			TimePickerDialog.OnTimeSetListener {
		TextView mResultText;

		public TimePickerFragment(TextView startMonday) {
			// TODO Auto-generated constructor stub
			mResultText = startMonday;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current time as the default values for the picker
			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);

			// Create a new instance of TimePickerDialog and return it
			return new TimePickerDialog(getActivity(), this, hour, minute, true);
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// Do something with the time chosen by the user

			if (hourOfDay < 10) {
				if (minute < 10) {
					mResultText.setText("0" + String.valueOf(hourOfDay) + ":0"
							+ String.valueOf(minute));
				} else {
					mResultText.setText("0" + String.valueOf(hourOfDay) + ":"
							+ String.valueOf(minute));
				}

			} else if (minute < 10) {
				if (hourOfDay < 10) {
					mResultText.setText("0" + String.valueOf(hourOfDay) + ":0"
							+ String.valueOf(minute));
				} else {
					mResultText.setText(String.valueOf(hourOfDay) + ":0"
							+ String.valueOf(minute));
				}

			} else {

				mResultText.setText(String.valueOf(hourOfDay) + ":"
						+ String.valueOf(minute));
			}

		}
	}

}

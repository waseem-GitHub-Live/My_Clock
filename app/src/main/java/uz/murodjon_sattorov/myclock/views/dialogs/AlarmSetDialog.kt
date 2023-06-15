package uz.murodjon_sattorov.myclock.views.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import ca.antonious.materialdaypicker.MaterialDayPicker
import uz.murodjon_sattorov.myclock.core.helpers.WeeklyAlarm
import uz.murodjon_sattorov.myclock.core.helpers.WeeklyAlarms
import uz.murodjon_sattorov.myclock.databinding.AddNewAlarmDialogBinding
import java.util.*


class AlarmSetDialog(context: Context) : AlertDialog(context) {

    private var dialogBinding: AddNewAlarmDialogBinding =
        AddNewAlarmDialogBinding.inflate(LayoutInflater.from(context))

    private var selectedDays: MutableSet<MaterialDayPicker.Weekday> = mutableSetOf()
    private var weeklyAlarms: WeeklyAlarms = WeeklyAlarms()

    private var getCheckVibrateAndLabel: GetCheckVibrateAndLabel? = null

    fun setOnCheckVibrateAndLabel(getCheckVibrateAndLabel: GetCheckVibrateAndLabel) {
        this.getCheckVibrateAndLabel = getCheckVibrateAndLabel
    }

    init {
        dialogBinding.alarmTimePickerHour.value = Calendar.getInstance().time.hours
        dialogBinding.alarmTimePickerMinute.value = Calendar.getInstance().time.minutes

        dialogBinding.dayPicker.setDayPressedListener { weekday, isSelected ->
            if (isSelected) {
                selectedDays.add(weekday)
            } else {
                selectedDays.remove(weekday)
            }
        }

        // Restore the selected days if they were previously set
        val previouslySelectedDays = weeklyAlarms.getSelectedDays()
        selectedDays.addAll(previouslySelectedDays)

        if (previouslySelectedDays.isNotEmpty()) {
            dialogBinding.dayPicker.setSelectedDays(previouslySelectedDays.toList())
        }

        dialogBinding.okText.setOnClickListener {
            getCheckVibrateAndLabel?.getVibrateAndLabel(
                0,
                String.format("%02d", dialogBinding.alarmTimePickerHour.value),
                String.format("%02d", dialogBinding.alarmTimePickerMinute.value),
                dialogBinding.isVibrate.isChecked,
                true,
                dialogBinding.inputLabel.text.toString(),
                selectedDays.contains(MaterialDayPicker.Weekday.MONDAY),
                selectedDays.contains(MaterialDayPicker.Weekday.TUESDAY),
                selectedDays.contains(MaterialDayPicker.Weekday.WEDNESDAY),
                selectedDays.contains(MaterialDayPicker.Weekday.THURSDAY),
                selectedDays.contains(MaterialDayPicker.Weekday.FRIDAY),
                selectedDays.contains(MaterialDayPicker.Weekday.SATURDAY),
                selectedDays.contains(MaterialDayPicker.Weekday.SUNDAY)
            )

            // Update the selected days in the WeeklyAlarms object
            weeklyAlarms.setSelectedDays(selectedDays)

            dismiss()
            cancel()
        }

        dialogBinding.cancelText.setOnClickListener {
            dismiss()
            cancel()
        }

        setView(dialogBinding.root)
    }
}

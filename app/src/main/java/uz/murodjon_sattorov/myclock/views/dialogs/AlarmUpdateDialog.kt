package uz.murodjon_sattorov.myclock.views.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import ca.antonious.materialdaypicker.MaterialDayPicker
import uz.murodjon_sattorov.myclock.core.models.AlarmModel
import uz.murodjon_sattorov.myclock.databinding.AddNewAlarmDialogBinding


class AlarmUpdateDialog(context: Context, model: AlarmModel) : AlertDialog(context) {


    private var dialogBinding: AddNewAlarmDialogBinding =
        AddNewAlarmDialogBinding.inflate(LayoutInflater.from(context))
    private var selectedDays: MutableList<MaterialDayPicker.Weekday> = mutableListOf()
    var getCheckVibrateAndLabel: GetCheckVibrateAndLabel? = null

    fun setOnCheckVibrateAndLabel(getCheckVibrateAndLabel: GetCheckVibrateAndLabel) {
        this.getCheckVibrateAndLabel = getCheckVibrateAndLabel
    }


    init {
        dialogBinding.alarmTimePickerHour.value = model.hour
        dialogBinding.alarmTimePickerMinute.value = model.minute
        dialogBinding.isVibrate.isChecked = model.vibrate
        dialogBinding.inputLabel.setText(model.title)

        val list = ArrayList<MaterialDayPicker.Weekday>()

        if (model.monday) {
            selectedDays.add(MaterialDayPicker.Weekday.MONDAY)
        }
        if (model.tuesday) {
            selectedDays.add(MaterialDayPicker.Weekday.TUESDAY)
        }
        if (model.wednesday) {
            selectedDays.add(MaterialDayPicker.Weekday.WEDNESDAY)
        }
        if (model.thurday) {
            selectedDays.add(MaterialDayPicker.Weekday.THURSDAY)
        }
        if (model.friday) {
            selectedDays.add(MaterialDayPicker.Weekday.FRIDAY)
        }
        if (model.saturday) {
            selectedDays.add(MaterialDayPicker.Weekday.SATURDAY)
        }
        if (model.sunday) {
            selectedDays.add(MaterialDayPicker.Weekday.SUNDAY)
        }
        dialogBinding.dayPicker.setSelectedDays(list)

        dialogBinding.okText.setOnClickListener {
            model.id.let { it1 ->
                getCheckVibrateAndLabel?.getVibrateAndLabel(
                    it1,
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
            }
            dismiss()
            cancel()
        }
        dialogBinding.dayPicker.setSelectedDays(selectedDays)

        dialogBinding.dayPicker.setDaySelectionChangedListener { selectedDays ->
            this.selectedDays = selectedDays.toMutableList()
        }
        dialogBinding.cancelText.setOnClickListener {
            dismiss()
            cancel()
        }

        setView(dialogBinding.root)
    }
}
package uz.murodjon_sattorov.myclock.core.helpers

import ca.antonious.materialdaypicker.MaterialDayPicker

class WeeklyAlarms {
    private var selectedDays: MutableSet<MaterialDayPicker.Weekday> = mutableSetOf()

    fun setSelectedDays(days: MutableSet<MaterialDayPicker.Weekday>) {
        selectedDays.clear()
        selectedDays.addAll(days)
    }

    fun getSelectedDays(): Set<MaterialDayPicker.Weekday> {
        return selectedDays
    }

    fun isMondaySelected(): Boolean {
        return selectedDays.contains(MaterialDayPicker.Weekday.MONDAY)
    }

    fun isTuesdaySelected(): Boolean {
        return selectedDays.contains(MaterialDayPicker.Weekday.TUESDAY)
    }

    fun isWednesdaySelected(): Boolean {
        return selectedDays.contains(MaterialDayPicker.Weekday.WEDNESDAY)
    }

    fun isThursdaySelected(): Boolean {
        return selectedDays.contains(MaterialDayPicker.Weekday.THURSDAY)
    }

    fun isFridaySelected(): Boolean {
        return selectedDays.contains(MaterialDayPicker.Weekday.FRIDAY)
    }

    fun isSaturdaySelected(): Boolean {
        return selectedDays.contains(MaterialDayPicker.Weekday.SATURDAY)
    }

    fun isSundaySelected(): Boolean {
        return selectedDays.contains(MaterialDayPicker.Weekday.SUNDAY)
    }
}

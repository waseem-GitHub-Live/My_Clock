package uz.murodjon_sattorov.myclock.views.dialogs

interface GetCheckVibrateAndLabel {
    fun getVibrateAndLabel(
        requestCode: Int,
        hour: String,
        minutes: String,
        vibrate: Boolean,
        repeat: Boolean,
        label: String,
        monday: Boolean,
        tuesday: Boolean,
        wednesday: Boolean,
        thursday: Boolean,
        friday: Boolean,
        saturday: Boolean,
        sunday: Boolean
    )
}

package uz.murodjon_sattorov.myclock.views.fragments

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rm.rmswitch.RMSwitch
import uz.murodjon_sattorov.myclock.R
import uz.murodjon_sattorov.myclock.core.adapters.AlarmAdapter
import uz.murodjon_sattorov.myclock.core.helpers.AlarmBroadcast
import uz.murodjon_sattorov.myclock.core.models.AlarmModel
import uz.murodjon_sattorov.myclock.databinding.FragmentAlarmsBinding
import uz.murodjon_sattorov.myclock.viewmodel.AlarmViewModel
import uz.murodjon_sattorov.myclock.views.dialogs.AlarmSetDialog
import uz.murodjon_sattorov.myclock.views.dialogs.AlarmUpdateDialog
import uz.murodjon_sattorov.myclock.views.dialogs.GetCheckVibrateAndLabel
import java.util.*


class AlarmsFragment : Fragment() {

    private var _binding: FragmentAlarmsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: AlarmAdapter
    private lateinit var alarmViewModel: AlarmViewModel
    private var alarmBroadcast: AlarmBroadcast? = null
    val selectedDays = listOf(Calendar.MONDAY,
        Calendar.TUESDAY,
        Calendar.WEDNESDAY,
        Calendar.THURSDAY,
        Calendar.FRIDAY,
        Calendar.SATURDAY,
        Calendar.SUNDAY)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlarmsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupListeners()
        setupViewModel()
        loadAlarms()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupViews() {
        adapter = AlarmAdapter()
        binding.alarmList.adapter = adapter
        binding.alarmList.layoutManager = LinearLayoutManager(requireContext())
        alarmBroadcast = AlarmBroadcast()
        alarmBroadcast?.createNotificationChannel(requireContext())
    }

    private fun setupListeners() {
        binding.floatingActionButton.setOnClickListener {
            openDialog()
        }

        adapter.setOnClick(object : AlarmAdapter.SetOnClickListener {
            override fun onUpdateClick(model: AlarmModel) {
                val dialog = AlarmUpdateDialog(
                    requireContext(), model
                )
                dialog.setTitle("Select time")
                dialog.setCancelable(true)
                dialog.setOnCheckVibrateAndLabel(object : GetCheckVibrateAndLabel {
                    override fun getVibrateAndLabel(
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
                    ) {
                        alarmBroadcast?.cancelAlarm(requireContext(), requestCode)
                        alarmViewModel =
                            ViewModelProvider(this@AlarmsFragment)[AlarmViewModel::class.java]
                        val alarmModel = AlarmModel(
                            requestCode,
                            Integer.parseInt(hour),
                            Integer.parseInt(minutes),
                            label,
                            vibrate,
                            repeat,
                            monday,
                            tuesday,
                            wednesday,
                            thursday,
                            friday,
                            saturday,
                            sunday
                        )
                        alarmBroadcast?.setAlarm(
                            requireContext(),
                            Integer.parseInt(hour),
                            Integer.parseInt(minutes),
                            requestCode
                        )
                        val notification = getServiceNotification(
                            requireContext(),
                            "Alarm set for $hour:$minutes",
                            selectedDays
                        )
                        if (notification != null) {
                            val notificationManager = NotificationManagerCompat.from(requireContext())
                            notificationManager.notify(NOTIFICATION_ID, notification)
                        }

                        val notificationManager =
                            NotificationManagerCompat.from(requireContext())
                        if (notification != null) {
                            notificationManager.notify(NOTIFICATION_ID, notification)
                        }
                        alarmViewModel.updateAlarm(alarmModel)
                        loadAlarms()
                    }
                })

                dialog.show()
            }

            override fun onRepeatClick(model: AlarmModel, view: RMSwitch) {
                model.repeat = !view.isChecked

                alarmViewModel.updateAlarm(model)

                if (model.repeat) {
                    model.hour.let {
                        model.minute.let { it1 ->
                            model.id.let { it2 ->
                                alarmBroadcast?.setAlarm(
                                    requireContext(),
                                    it, it1, it2
                                )
                            }
                        }
                    }
                } else {
                    model.id.let { alarmBroadcast?.cancelAlarm(requireContext(), it) }
                }
                loadAlarms()
            }

            override fun onDeleteSwipe(model: AlarmModel) {
                deleteAlarm(model)
            }
        })
    }

    private fun setupViewModel() {
        alarmViewModel = ViewModelProvider(this)[AlarmViewModel::class.java]
    }

    private fun loadAlarms() {
        alarmViewModel.readAllAlarm.observe(viewLifecycleOwner) { alarmList ->
            alarmList?.let {
                adapter.addData(it)
            }
        }
    }

    private fun openDialog() {
        val dialog = AlarmSetDialog(requireContext())
        dialog.setTitle("Select time")
        dialog.setCancelable(false)
        dialog.setOnCheckVibrateAndLabel(object : GetCheckVibrateAndLabel {
            override fun getVibrateAndLabel(
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
            ) {
                val alarmModel = AlarmModel(
                    requestCode,
                    Integer.parseInt(hour),
                    Integer.parseInt(minutes),
                    label,
                    vibrate,
                    repeat,
                    monday,
                    tuesday,
                    wednesday,
                    thursday,
                    friday,
                    saturday,
                    sunday
                )
                alarmBroadcast?.setAlarm(
                    requireContext(),
                    Integer.parseInt(hour),
                    Integer.parseInt(minutes),
                    requestCode
                )
                alarmBroadcast?.showNotification(
                    requireContext(),
                    "Alarm",
                    "Your alarm notification content",
                    selectedDays
                )

                alarmViewModel.addAlarm(alarmModel)
                loadAlarms()
            }
        })

        dialog.show()
    }

    private fun deleteAlarm(model: AlarmModel) {
        model.id.let { alarmBroadcast?.cancelAlarm(requireContext(), it) }
        alarmViewModel.deleteAlarm(model)
        loadAlarms()
    }

    private fun getServiceNotification(
        context: Context,
        content: String,
        selectedDays: List<Int>
    ): Notification? {
        val channelId = "alarm_channel"
        val channelName = "Alarm Channel"
        val channelDescription = "Channel for alarm notifications"
        val currentDayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        val alarmBroadcast = AlarmBroadcast()
        alarmBroadcast.createNotificationChannel(context)

        if (currentDayOfWeek !in selectedDays) {
            return null
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = channelDescription

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Alarm Notification")
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_round_access_alarm_24)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        return notificationBuilder.build()
    }
    companion object {
        private const val NOTIFICATION_ID = 1
    }
}
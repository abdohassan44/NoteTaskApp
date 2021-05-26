package com.example.notetaskapp.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.notetaskapp.R
import com.example.notetaskapp.databinding.FragmentNewNoteBinding
import com.example.notetaskapp.dp.Note
import com.example.notetaskapp.utils.currentDate
import com.example.notetaskapp.utils.formatReminderDate
import com.example.notetaskapp.utils.toast
import com.example.notetaskapp.viewmodel.NoteViewModel
import com.example.notetaskapp.work.createSchedule
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

@AndroidEntryPoint
class NewNoteFragment : Fragment(R.layout.fragment_new_note), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private var _binding: FragmentNewNoteBinding? = null
    private val binding get() = _binding!!
    private val noteViewModel: NoteViewModel by activityViewModels()
    private lateinit var mView: View
    private lateinit var pickedDateTime: Calendar
    private lateinit var currentDateTime: Calendar
    var reminder: Long? = null
    private lateinit var uiScope: CoroutineScope

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNewNoteBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uiScope = CoroutineScope(Dispatchers.Default)
        binding.buttonSave.setOnClickListener {
            saveNote(mView)
        }
        binding.cardtime.setOnClickListener {
            pickDate()
        }

        mView = view
    }

    private fun saveNote(view: View) {
        val noteTitle = binding.etNoteTitle.text.toString().trim()
        val noteBody = binding.etNoteBody.text.toString().trim()

        when {
            noteTitle.isEmpty() ->
                activity?.toast("Please enter note title")
            noteBody.isEmpty() ->
                activity?.toast("Please enter note body")
            reminder == null ->
                activity?.toast("Please enter note date")

            else -> {
                val insertNote =
                    Note(null, noteTitle, noteBody, currentDate().timeInMillis, reminder)

                noteViewModel.addNote(insertNote)

                uiScope.launch {
                    withContext(Dispatchers.Main) {
                        createSchedule(requireContext(), noteViewModel.getNote())

                    }
                }


                Snackbar.make(
                    view, "Note saved successfully",
                    Snackbar.LENGTH_SHORT
                ).show()
                view.findNavController().navigate(R.id.action_newNoteFragment_to_homeFragment)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        pickedDateTime = currentDate()
        pickedDateTime.set(p1, p2, p3)
        currentDateTime = currentDate()
        val hourOfDay = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val minuteOfDay = currentDateTime.get(Calendar.MINUTE)
        val timePickerDialog =
            TimePickerDialog(requireContext(), this, hourOfDay, minuteOfDay, false)
        timePickerDialog.show()
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        pickedDateTime.set(Calendar.HOUR_OF_DAY, p1)
        pickedDateTime.set(Calendar.MINUTE, p2)
        if (pickedDateTime.timeInMillis <= currentDate().timeInMillis) {
            pickedDateTime.run {
                set(Calendar.DAY_OF_MONTH, currentDateTime.get(Calendar.DAY_OF_MONTH) + 1)
                set(Calendar.YEAR, currentDateTime.get(Calendar.YEAR))
                set(Calendar.MONTH, currentDateTime.get(Calendar.MONTH))
            }
        }
        reminder = pickedDateTime.timeInMillis
        binding.textNoteReminder.text = formatReminderDate(reminder!!)

    }

    private fun pickDate() {
        currentDateTime = currentDate()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog =
            DatePickerDialog(requireContext(), this, startYear, startMonth, startDay)
        datePickerDialog.show()
    }

}
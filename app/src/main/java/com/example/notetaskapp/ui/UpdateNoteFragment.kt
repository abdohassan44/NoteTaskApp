package com.example.notetaskapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notetaskapp.R
import com.example.notetaskapp.databinding.FragmentUpdateNoteBinding
import com.example.notetaskapp.dp.Note
import com.example.notetaskapp.utils.currentDate
import com.example.notetaskapp.utils.toast
import com.example.notetaskapp.viewmodel.NoteViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateNoteFragment : Fragment(R.layout.fragment_update_note) {

    private var _binding: FragmentUpdateNoteBinding? = null
    private val binding get() = _binding!!

    private val args: UpdateNoteFragmentArgs by navArgs()
    private lateinit var currentNote: Note
    private val noteViewModel: NoteViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUpdateNoteBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentNote = args.note!!

        binding.etNoteBody.setText(currentNote.text)
        binding.etNoteTitle.setText(currentNote.title)

        binding.buttonUpdate.setOnClickListener {
            val noteTitle = binding.etNoteTitle.text.toString().trim()
            val noteBody = binding.etNoteBody.text.toString().trim()
            when {
                noteTitle.isEmpty() ->
                    activity?.toast("Please enter note title")
                noteBody.isEmpty() ->
                    activity?.toast("Please enter note body")

                else -> {
                    val note = Note(
                        null,
                        noteTitle,
                        noteBody,
                        currentDate().timeInMillis,
                        currentNote.reminder
                    )

                    noteViewModel.updateNote(note)
                    Snackbar.make(
                        view, "Note updated successfully",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    view.findNavController().popBackStack()
                }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
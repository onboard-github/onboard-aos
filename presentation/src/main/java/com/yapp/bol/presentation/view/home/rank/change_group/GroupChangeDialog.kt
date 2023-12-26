package com.yapp.bol.presentation.view.home.rank.change_group

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yapp.bol.presentation.R
import com.yapp.bol.presentation.databinding.DialogChangeGroupBinding
import com.yapp.bol.presentation.model.JoinedGroupViewItem

class GroupChangeDialog(
    private val onGroupClick: GroupChangeItemClickListener? = null,
    private val onSearchGroupClick: SearchGroupClickListener? = null,
) : BottomSheetDialogFragment() {
    private lateinit var _binding: DialogChangeGroupBinding
    private val binding: DialogChangeGroupBinding
        get() = _binding
    private val groupChangeAdapter by lazy {
        GroupChangeAdapter()
    }

    fun interface SearchGroupClickListener {
        fun onClick()
    }

    fun submitGroupList(list: List<JoinedGroupViewItem>) {
        groupChangeAdapter.submitList(list)
    }

    override fun getTheme() = R.style.TransparentBottomSheetDialogFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogChangeGroupBinding.inflate(inflater, container, false)
        setWindow()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configureUI()
        handleEvent()
    }

    private fun configureUI() {
        binding.rvJoinedGroup.adapter = groupChangeAdapter
    }

    private fun handleEvent() {
        groupChangeAdapter.setOnGroupClickListener {
            onGroupClick?.onClick(it)
            dismiss()
        }
        binding.btnGroupSearch.setOnClickListener {
            dismiss()
            onSearchGroupClick?.onClick()
        }
    }

    private fun setWindow() {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
    }
}

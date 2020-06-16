package com.kapil.presentation.base

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.kapil.presentation.R


abstract class BaseFragment : Fragment(), BaseView {

    abstract val viewModel: BaseViewModel

    override fun showMessage(messageResId: Int) {
        Toast.makeText(context, messageResId, Toast.LENGTH_LONG).show()
    }

    override fun closeKeyboard() {
        (context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
            ?.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    override fun showDialog(
        @StringRes titleResId: Int,
        @StringRes messageResId: Int,
        positiveAction: () -> Unit
    ) {
        AlertDialog.Builder(requireContext()).create().apply {
            setTitle(titleResId)
            setMessage(getString(messageResId))
            setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.action_negative)) { dialog, _ ->
                dialog.dismiss()
            }
            setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.action_positive)) { dialog, _ ->
                positiveAction()
                dialog.dismiss()
            }
            show()
        }
    }
}
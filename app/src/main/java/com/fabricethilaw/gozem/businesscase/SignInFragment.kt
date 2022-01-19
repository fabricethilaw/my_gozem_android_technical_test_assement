package com.fabricethilaw.gozem.businesscase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.fabricethilaw.gozem.R
import com.fabricethilaw.gozem.businesscase.extensions.safeNavigate
import com.fabricethilaw.gozem.businesscase.extensions.showMessage
import com.fabricethilaw.gozem.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val sharedViewModel by activityViewModels<BusinessCaseViewModel>()

    private lateinit var progressDialog: DialogProgressBar

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSignInBinding.inflate(inflater)
        binding.fragment = this
        progressDialog = DialogProgressBar(requireContext())
        with(binding.inputEmail.editText) {
            this?.doOnTextChanged { _, _, _, _ ->
                binding.inputEmail.isErrorEnabled = false
            }
        }
        with(binding.inputPassword.editText) {
            this?.doOnTextChanged { _, _, _, _ ->
                binding.inputPassword.isErrorEnabled = false
            }
        }

        return binding.root

    }

    private fun emailAndPasswordAreValid(email: String, password: String): Boolean {

        val emailIsValid = Validator.validateEmail(email)
        if (!emailIsValid) {
            binding.inputEmail.error = getString(R.string.error_invalid_email)
        }

        val passwordIsValid = Validator.validatePassword(password)
        if (!passwordIsValid) {
            binding.inputPassword.error = getString(R.string.error_invalid_password)
        }
        return emailIsValid && passwordIsValid
    }

    fun signIn() {
        val email: String = binding.inputEmail.editText?.text?.toString() ?: ""
        val password: String = binding.inputPassword.editText?.text.toString()

        if (emailAndPasswordAreValid(email, password)) {
            progressDialog.show()
            sharedViewModel.signIn(email, password, onError = {
                progressDialog.hide()
                binding.root.showMessage(R.string.sign_in_error, it)
            }) {
                progressDialog.hide()
                findNavController().safeNavigate(SignInFragmentDirections.actionSignInToHome())
            }
        }
    }


    fun goToSignUp() {
        findNavController().safeNavigate(
            SignInFragmentDirections.actionSignInToSignUp()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
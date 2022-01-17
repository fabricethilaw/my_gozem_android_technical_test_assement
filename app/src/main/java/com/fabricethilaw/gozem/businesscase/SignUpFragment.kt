package com.fabricethilaw.gozem.businesscase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.fabricethilaw.gozem.R
import com.fabricethilaw.gozem.databinding.FragmentSignupBinding
import com.fabricethilaw.gozem.showMessage

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SignUpFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val sharedViewModel by activityViewModels<BusinessCaseViewModel>()
    private lateinit var progressDialog: DialogProgressBar

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        binding.fragment = this
        progressDialog = DialogProgressBar(requireContext())
        with(binding.inputFullName) {
            editText?.addTextChangedListener {
                isErrorEnabled = false
            }
        }
        with(binding.inputEmail) {
            editText?.addTextChangedListener {
                isErrorEnabled = false
            }
        }
        with(binding.inputPassword) {
            editText?.addTextChangedListener {
                isErrorEnabled = false
            }
        }
        with(binding.inputConfirmPassword) {
            editText?.addTextChangedListener {
                isErrorEnabled = false
            }
        }

        return binding.root

    }

    fun register() {
        val name = binding.inputFullName.editText?.text?.toString() ?: ""
        val email = binding.inputEmail.editText?.text?.toString() ?: ""
        val password = binding.inputPassword.editText?.text?.toString() ?: ""
        val confirmedPassword = binding.inputConfirmPassword.editText?.text?.toString() ?: ""

        if (inputIsValid(name, email, password, confirmedPassword)) {
            progressDialog.show()
            sharedViewModel.register(name, email, password, onError = {
                progressDialog.hide()
                binding.root.showMessage(R.string.sign_up_error, it)
            }) {
                progressDialog.hide()
                findNavController().navigate(R.id.action_SignUp_to_Home)
            }
        }

    }


    private fun inputIsValid(
        name: String,
        email: String,
        password: String,
        passwordConfirm: String
    ): Boolean {
        val nameIsValid = Validator.validateName(name)
        val emailIsValid = Validator.validateEmail(email)
        val passwordIsValid = Validator.validatePassword(password)

        if (!emailIsValid) {
            binding.inputEmail.error = getString(R.string.error_invalid_email)
        }

        if (!passwordIsValid) {
            binding.inputPassword.error = getString(R.string.error_invalid_password)
        }

        if (password != passwordConfirm) {
            binding.inputConfirmPassword.error =
                getString(R.string.error_confirm_password_not_matching)
        }

        return nameIsValid && emailIsValid
                && passwordIsValid
                && password == passwordConfirm
    }

    fun goToSignIn() {
        findNavController().navigate(R.id.action_SignUp_to_SignIn)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.citrus.cookiteasy.presentation.registration

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.citrus.cookiteasy.R
import com.citrus.cookiteasy.databinding.FragmentRegistrationBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegistrationFragment : Fragment() {
    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private val viewModel: RegistrationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNav.visibility = View.GONE

        navController = findNavController()
        setupPasswordToggles()
        setupTextFields()
        setupRegistrationButton()
        setupClickOutsideToHideKeyboard()
        setupPasswordValidation()
        observeRegistrationState()
    }

    private fun setupPasswordToggles() {
        setupPasswordToggle(
            binding.registrationPasswordInputLayout,
            binding.registrationPasswordEditText
        )
        setupPasswordToggle(
            binding.registrationPasswordAgainInputLayout,
            binding.registrationPasswordAgainEditText
        )
    }

    private fun setupTextFields() {
        listOf(
            binding.registrationLoginEditText,
            binding.registrationPasswordEditText,
            binding.registrationPasswordAgainEditText
        ).forEach { editText ->
            editText.setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                    hideKeyboard()
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun setupRegistrationButton() {
        binding.registrationBtn.setOnClickListener {
            val username = binding.registrationLoginEditText.text.toString().trim()
            val password = binding.registrationPasswordEditText.text.toString().trim()
            val passwordAgain = binding.registrationPasswordAgainEditText.text.toString().trim()

            if (passwordAgain.isNotEmpty() && password != passwordAgain) {
                showTopSnackbar("Пароли не совпадают")
               }

            if (validateInput(username, password, passwordAgain)) {
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.registerUser(username, password)
                }
            }
        }
    }

    private fun validateInput(username: String, password: String, passwordAgain: String): Boolean {
        return when {
            username.isEmpty() -> {
                showTopSnackbar("Введите имя пользователя")
                false
            }

            password.isEmpty() -> {
                showTopSnackbar("Введите пароль")
                false
            }

            passwordAgain.isEmpty() -> {
                showTopSnackbar("Повторите пароль")
                false
            }

            password != passwordAgain -> {
                showTopSnackbar("Пароли не совпадают")
                false
            }

            password.length < 6 -> {
                showTopSnackbar("Пароль должен содержать минимум 6 символов")
                false
            }

            else -> true
        }
    }

    private fun setupPasswordValidation() {
        binding.registrationPasswordAgainEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val password = binding.registrationPasswordEditText.text.toString()
                val passwordAgain = s.toString()

//                if (passwordAgain.isNotEmpty() && password != passwordAgain) {
//                    showTopSnackbar("Пароли не совпадают")
//                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupClickOutsideToHideKeyboard() {
        binding.root.setOnClickListener {
            hideKeyboard()
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    private fun setupPasswordToggle(
        passwordLayout: TextInputLayout,
        passwordEditText: TextInputEditText
    ) {
        passwordLayout.setEndIconOnClickListener {
            val isCurrentlyVisible = passwordEditText.transformationMethod == null

            if (isCurrentlyVisible) {
                passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
                passwordLayout.endIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_slash_eye)
            } else {
                passwordEditText.transformationMethod = null
                passwordLayout.endIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_eye)
            }

            passwordEditText.setSelection(passwordEditText.text?.length ?: 0)
        }
    }

    private fun observeRegistrationState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.registrationState.collect { state ->
                when (state) {
                    is RegistrationViewModel.RegistrationState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is RegistrationViewModel.RegistrationState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        // Получаем введенные email и пароль
                        val username = binding.registrationLoginEditText.text.toString()
                        val password = binding.registrationPasswordEditText.text.toString()

                        navController.navigate(
                            RegistrationFragmentDirections.actionRegistrationToProfileInfo(
                                username = state.username,
                                password = password // Передаем пароль
                            )
                        )
                    }

                    is RegistrationViewModel.RegistrationState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        showTopSnackbar(state.message)
                    }

                    RegistrationViewModel.RegistrationState.Idle -> {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun showTopSnackbar(message: String) {
        val snackbar = Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
        val params = snackbar.view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        params.topMargin = 32.dpToPx()
        snackbar.view.layoutParams = params

        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.error_red))
        snackbar.setTextColor(Color.WHITE)
        snackbar.show()
    }

    private fun Int.dpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
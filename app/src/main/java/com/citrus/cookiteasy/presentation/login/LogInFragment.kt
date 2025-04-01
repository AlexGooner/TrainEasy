package com.citrus.cookiteasy.presentation.login

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.citrus.cookiteasy.R
import com.citrus.cookiteasy.databinding.FragmentLogInBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LogInFragment : Fragment() {

    private var _binding: FragmentLogInBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private val viewModel: LogInViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLogInBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNav.visibility = View.GONE

        navController = findNavController()

        val passwordLayout = binding.passwordInputLayout
        val passwordEditText = passwordLayout.editText

        // Настройка переключения иконок
        passwordLayout.setEndIconOnClickListener {
            val isCurrentlyVisible = passwordEditText?.transformationMethod == null

            if (isCurrentlyVisible) {
                // Переключаем на скрытый пароль
                passwordEditText?.transformationMethod = PasswordTransformationMethod.getInstance()
                passwordLayout.endIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_slash_eye)
            } else {
                // Показываем пароль
                passwordEditText.transformationMethod = null
                passwordLayout.endIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_eye)
            }

            // Перемещаем курсор в конец текста
            passwordEditText?.setSelection(passwordEditText.text?.length ?: 0)
        }

        binding.loginButton.setOnClickListener {
            val username = binding.loginEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (validateInput(username, password)) {
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.login(username, password)
                }
            }
        }
        observeLoginState()

        binding.toRegTv.setOnClickListener {
            val action = LogInFragmentDirections.actionLoginToRegistration()
            navController.navigate(action)
        }

    }

    private fun observeLoginState(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loginState.collect { state ->
                when(state) {
                    is LogInViewModel.LoginState.Loading -> {binding.progressBar.visibility = View.VISIBLE}
                    is LogInViewModel.LoginState.Success -> {navController.navigate(
                        LogInFragmentDirections.actionLoginToWelcome(state.user.username))}
                    is LogInViewModel.LoginState.Error -> {binding.progressBar.visibility = View.GONE
                    showError(state.message)}
                    LogInViewModel.LoginState.Idle -> {binding.progressBar.visibility = View.GONE}
                }
            }
        }
    }

    private fun validateInput(username: String, password: String) : Boolean {
        return when {
            username.isEmpty() -> {binding.loginEditText.error = "Введите логин"
            false}
            password.isEmpty() -> {binding.passwordEditText.error = "Введите пароль"

            false}
            else -> true
        }
    }
    private fun showError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.error_red))
            .show()
    }
}
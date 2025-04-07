package com.citrus.cookiteasy.presentation.profileinfo

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.citrus.cookiteasy.R
import com.citrus.cookiteasy.data.database.User
import com.citrus.cookiteasy.databinding.FragmentProfileInfoBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale
import kotlin.math.pow
import kotlin.math.roundToInt

@AndroidEntryPoint
class ProfileInfoFragment : Fragment() {

    private var _binding: FragmentProfileInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private val viewModel: ProfileInfoViewModel by viewModels()
    private var username: String? = null
    private var password: String? = null
    private var selectedGender: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileInfoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNav.visibility = View.GONE

        navController = findNavController()

        username = arguments?.getString("username")
        if (username.isNullOrEmpty()) {
            navController.popBackStack()
            return
        }


        password = arguments?.getString("password")

        val genders = listOf("Мужской", "Женский")


        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            genders
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.genderSpinner.adapter = adapter

        binding.genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?,
                                        view: View?,
                                        position: Int,
                                        id: Long) {
                selectedGender = genders[position]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        binding.profileinfoBirthField.setOnClickListener {
            showDatePickerDialog()
        }

        setupFields()

        binding.profileinfoReadyBtn.setOnClickListener {
            if (validateInput()) {
                saveProfileData()
                val action = ProfileInfoFragmentDirections.actionProfileInfoToWelcome(username!!)
                navController.navigate(action)
            }
        }
        setupClickOutsideToHideKeyboard()

    }

    private fun getSelectedGender(): String {
        return selectedGender
    }

    private fun setupFields() {
        val nameFilter = InputFilter { source, start, end, dest, dstart, dend ->
            val regex = Regex("[a-zA-Zа-яА-Я\\s-]")
            source.filter { char -> regex.matches(char.toString()) }
        }

        binding.profileinfoNameEditText.filters = arrayOf(nameFilter)
        binding.profileinfoLastNameEditText.filters = arrayOf(nameFilter)

        binding.profileinfoHeightEditText.inputType = InputType.TYPE_CLASS_NUMBER
        binding.profileinfoWeightEditText.inputType = InputType.TYPE_CLASS_NUMBER

        listOf(
            binding.profileinfoNameEditText,
            binding.profileinfoLastNameEditText,
            binding.profileinfoBirthField,
            binding.profileinfoHeightEditText,
            binding.profileinfoWeightEditText
        ).forEach { editText ->
            editText.setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                    editText.clearFocus()
                    hideKeyboard()
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format(
                    Locale.getDefault(),
                    "%02d.%02d.%d",
                    selectedDay,
                    selectedMonth + 1,
                    selectedYear
                )
                binding.profileinfoBirthField.text = formattedDate
            },
            year,
            month,
            day
        )
        datePicker.show()
    }


    private fun validateInput(): Boolean {
        return when {
            binding.profileinfoNameEditText.text.isNullOrEmpty() -> {
                showTopSnackbar("Введите имя")
                false
            }
            binding.profileinfoLastNameEditText.text.isNullOrEmpty() -> {
                showTopSnackbar("Введите фамилию")
                false
            }
            binding.profileinfoBirthField.text.isNullOrEmpty() -> {
                showTopSnackbar("Введите дату рождения")
                false
            }
            !isValidDate(binding.profileinfoBirthField.text.toString()) -> {
                showTopSnackbar("Некорректная дата. Используйте формат ДД.ММ.ГГГГ")
                false
            }
            binding.profileinfoHeightEditText.text.isNullOrEmpty() -> {
                showTopSnackbar("Выберите рост")
                false
            }
            binding.profileinfoWeightEditText.text.isNullOrEmpty() -> {
                showTopSnackbar("Выберите вес")
                false
            }

            else -> true
        }
    }

    private fun isValidDate(date: String): Boolean {
        return try {
            val parts = date.split(".")
            if (parts.size != 3) return false

            val day = parts[0].toInt()
            val month = parts[1].toInt()
            val year = parts[2].toInt()

            if (day !in 1..31 || month !in 1..12 || year < 1900 || year > Calendar.getInstance().get(Calendar.YEAR)) {
                return false
            }

            true
        } catch (e: Exception) {
            false
        }
    }

    private fun saveProfileData() {
        val firstName = binding.profileinfoNameEditText.text.toString()
        val lastName = binding.profileinfoLastNameEditText.text.toString()
        val birthDate = binding.profileinfoBirthField.text.toString()
        val height = binding.profileinfoHeightEditText.text.toString()
        val weight = binding.profileinfoWeightEditText.text.toString()
        val bmi = calculateBMI(height.toInt(), weight.toInt())
        val gender = getSelectedGender()
        Log.d("BMIIIIIIIIIIIIIIIIIII", " first name is $firstName, last name is $lastName, birth - $birthDate, height - $height" +
                ", weight is $weight,  $bmi is $bmi, gender is $gender")

        viewLifecycleOwner.lifecycleScope.launch {
                username?.let { un ->
                    password?.let { pwd ->
                        val user = User(
                            username = un,
                            password = pwd, // Используем переданный пароль
                            firstName = firstName,
                            lastName = lastName,
                            birthDate = birthDate,
                            height = height.toInt(),
                            weight = weight.toInt(),
                            bmi = bmi,
                            gender = gender
                        )

                        viewModel.updateUser(user)
                    } ?: showTopSnackbar("Ошибка: пароль не получен")
                }
            }
        }


    private fun calculateBMI(height: Int, weight: Int): Float {
        val heightInMeters = height / 100f
        return (weight / (heightInMeters * heightInMeters)).roundTo(1)
    }

    private fun Float.roundTo(decimalPlaces: Int): Float {
        val factor = 10f.pow(decimalPlaces)
        return (this * factor).roundToInt() / factor
    }

    private fun setupClickOutsideToHideKeyboard() {
        binding.root.setOnClickListener {
            hideKeyboard()
        }
    }

    @SuppressLint("ServiceCast")
    private fun hideKeyboard() {
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
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
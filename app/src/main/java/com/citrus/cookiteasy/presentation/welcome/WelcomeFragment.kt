package com.citrus.cookiteasy.presentation.welcome

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.CharacterStyle
import android.text.style.UpdateAppearance
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.citrus.cookiteasy.R
import com.citrus.cookiteasy.databinding.FragmentWelcomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.core.graphics.toColorInt


class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWelcomeBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNav.visibility = View.GONE

        navController  = findNavController()

        val username = arguments?.getString("username")

        val spannableString = SpannableString(username)

        val colors = intArrayOf(
            "#800080".toColorInt(),
            "#FFA500".toColorInt(),
            "#FBFB53".toColorInt(),
            "#FF0000".toColorInt()
        )
        val shader = LinearGradient(
            0f, 0f, 0f, binding.animatedTextView.textSize,
            colors,
            null,
            Shader.TileMode.CLAMP
        )
        spannableString.setSpan(
            ShaderSpan(shader),
            0,
            username!!.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.animatedTextView.text = spannableString
        animation(binding.animatedTextView, username)


    }

    private fun animation(textView: TextView, username:String) {
        val fadeIn = ObjectAnimator.ofFloat(textView, "alpha", 0f, 1f)
        fadeIn.duration = 5000
        fadeIn.start()


        fadeIn.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                Handler(Looper.getMainLooper()).postDelayed({

                    val action = WelcomeFragmentDirections.actionWelcomeToHome(username)
                    navController.navigate(action)
                }, 2000)
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class ShaderSpan(private val shader: Shader) : CharacterStyle(), UpdateAppearance {
        override fun updateDrawState(ds: TextPaint) {
            ds.shader = shader
        }
    }
}
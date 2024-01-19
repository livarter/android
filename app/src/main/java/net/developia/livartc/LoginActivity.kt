package net.developia.livartc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import net.developia.livartc.databinding.ActivityLoginBinding

/**
 * LIVARTC
 * Created by 최현서
 * Date: 2024-01-19
 * Time: 오후 2:32
 */
class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val intent = Intent(this, MainActivity::class.java)
        //카카오 로그인 버튼 누르면, MainActivity 이동
        binding.kakaoBtn.setOnClickListener{
            startActivity(intent)
        }
    }
}
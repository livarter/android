package net.developia.livartc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import net.developia.livartc.databinding.ActivityReplyWriteBinding

class ReplyWriteActivity : AppCompatActivity() {
    lateinit var binding: ActivityReplyWriteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReplyWriteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}
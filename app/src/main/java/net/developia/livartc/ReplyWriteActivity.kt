package net.developia.livartc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import net.developia.livartc.databinding.ActivityReplyWriteBinding

class ReplyWriteActivity : AppCompatActivity() {
    lateinit var binding: ActivityReplyWriteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReplyWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
             reviewWrite.addTextChangedListener(object : TextWatcher {
                var maxText = ""
                override fun beforeTextChanged(pos: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    maxText = pos.toString()
                }
                override fun onTextChanged(pos: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    if(reviewWrite.lineCount > 4){
                        Toast.makeText(this@ReplyWriteActivity,
                            "최대 4줄까지 입력 가능합니다.",
                            Toast.LENGTH_SHORT).show()

                        reviewWrite.setText(maxText)
                        reviewWrite.setSelection(reviewWrite.length())
                        reviewTextCnt.text = ("${reviewWrite.length()} / 40")
                    } else if(reviewWrite.length() > 40){
                        Toast.makeText(this@ReplyWriteActivity, "최대 40자까지 입력 가능합니다.",
                            Toast.LENGTH_SHORT).show()

                        reviewWrite.setText(maxText)
                        reviewWrite.setSelection(reviewWrite.length())
                        reviewTextCnt.text = ("${reviewWrite.length()} / 40")
                    } else {
                        reviewTextCnt.text = ("${reviewWrite.length()} / 40")
                    }
                }
                override fun afterTextChanged(p0: Editable?) {

                }
            })
        }

        binding.saveBtn.setOnClickListener {
            Log.d("hschoi", binding.reviewWrite.text.toString())
        }
    }
}
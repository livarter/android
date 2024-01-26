package net.developia.livartc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import net.developia.livartc.databinding.ActivityReplyWriteBinding
import net.developia.livartc.login.TokenManager
import net.developia.livartc.retrofit.MyApplication
import net.developia.livartc.retrofit.RetrofitInstance
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class ReplyWriteActivity : AppCompatActivity() {
    lateinit var binding: ActivityReplyWriteBinding
    private var productId = 0L
    lateinit var reviewContent: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReplyWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        productId = intent.getLongExtra("productId",0L)

        with(binding){
             reviewWrite.addTextChangedListener(object : TextWatcher {
                var maxText = ""
                override fun beforeTextChanged(pos: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    maxText = pos.toString()
                }
                override fun onTextChanged(pos: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    if(reviewWrite.lineCount > 10){
                        Toast.makeText(this@ReplyWriteActivity,
                            "최대 10줄까지 입력 가능합니다.",
                            Toast.LENGTH_SHORT).show()

                        reviewWrite.setText(maxText)
                        reviewWrite.setSelection(reviewWrite.length())
                        reviewTextCnt.text = ("${reviewWrite.length()} / 300")
                    } else if(reviewWrite.length() > 300){
                        Toast.makeText(this@ReplyWriteActivity, "최대 300자까지 입력 가능합니다.",
                            Toast.LENGTH_SHORT).show()

                        reviewWrite.setText(maxText)
                        reviewWrite.setSelection(reviewWrite.length())
                        reviewTextCnt.text = ("${reviewWrite.length()} / 300")
                    } else {
                        reviewTextCnt.text = ("${reviewWrite.length()} / 300")
                    }
                }
                override fun afterTextChanged(p0: Editable?) {

                }
            })
        }

        binding.saveBtn.setOnClickListener {
            Log.d("ReplyWrite", "리뷰내용:${binding.reviewWrite.text}")
            reviewContent = "${binding.reviewWrite.text}"
            showWriteDialog()
        }
    }
    fun showWriteDialog() {
        val builder = AlertDialog.Builder(this)
        // 제목 설정
        builder.setTitle("리뷰를 작성하시겠습니까?")

        // 아니오 버튼 설정
        builder.setNegativeButton("아니오") { dialog, which ->
            // 아무 동작 없이 다이얼로그 닫기
            dialog.dismiss()
        }
        // 예 버튼 설정
        builder.setPositiveButton("예") { dialog, which ->
            writeReview()
        }
        // 다이얼로그 생성 및 표시
        builder.create().show()
    }
    private fun writeReview() {
        val jwtToken = TokenManager.getToken(MyApplication.instance)!!
        RetrofitInstance.api.saveReview(jwtToken, productId, reviewContent).enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                Log.d("insertReply", " ${response.body()}")
                finish()
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("insertReply", " $t")
            }
        })
    }

}
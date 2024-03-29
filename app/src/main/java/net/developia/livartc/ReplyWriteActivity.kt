package net.developia.livartc

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import net.developia.livartc.databinding.ActivityReplyWriteBinding
import net.developia.livartc.login.TokenManager
import net.developia.livartc.model.Product
import net.developia.livartc.retrofit.MyApplication
import net.developia.livartc.retrofit.RetrofitInstance
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID
/**
 * LIVARTC
 * Created by 최현서
 * Date: 2024-01-24
 * Time: 13:10
 * 작업 내용: 리뷰 작성 구현
 */
class ReplyWriteActivity : AppCompatActivity() {
    lateinit var binding: ActivityReplyWriteBinding

    private var productId = 0L
    private var productName : String? = ""
    private var productPrice = 0L
    private var productDesc : String? = ""
    private var productImage : String? = ""
    private var brandName : String? = ""

    lateinit var reviewContent: String
    private var uploadedFileName: String? = null
    private var imgUri: Uri? = null
    //리뷰 이미지 넣었는지 여부 확인용
    private var imgStatus = 0
    private lateinit var filePath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReplyWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productId = intent.getLongExtra("productId",0L)
        productName = intent.getStringExtra("productName")
        productPrice = intent.getLongExtra("productPrice", 0L)
        productDesc = intent.getStringExtra("productDesc")
        productImage = intent.getStringExtra("productImage")
        brandName = intent.getStringExtra("brandName")

        Glide
            .with(binding.productImg.context)
            .load(productImage)
            .into(binding.productImg)
        binding.productName.text = productName
        binding.productBrand.text = brandName



        binding.backBtn.setOnClickListener {
            finish()
        }

        with(binding){
             reviewWrite.addTextChangedListener(object : TextWatcher {
                var maxText = ""
                override fun beforeTextChanged(pos: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    maxText = pos.toString()
                }
                override fun onTextChanged(pos: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    if(reviewWrite.lineCount > 10){
                        customToast("최대 10줄까지 입력 가능합니다.")

                        reviewWrite.setText(maxText)
                        reviewWrite.setSelection(reviewWrite.length())
                        reviewTextCnt.text = ("${reviewWrite.length()} / 300")
                    } else if(reviewWrite.length() > 300){
                        customToast("최대 300자까지 입력 가능합니다.")

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
            if(reviewContent.length >= 5) showWriteDialog()
            else showLessDialog()
        }

        //사진 추가 버튼 눌렀을 때 갤러리로 이동 후 requestLauncher 실행
        val requestLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult())
        {
            if(it.resultCode === android.app.Activity.RESULT_OK){
                imgUri = it.data?.data
                imgStatus = 1
                Glide
                    .with(applicationContext)
                    .load(it.data?.data)
                    .into(binding.uploadImg)
                val cursor = contentResolver.query(it.data?.data as Uri,
                    arrayOf<String>(MediaStore.Images.Media.DATA), null, null, null);
                cursor?.moveToFirst().let {
                    filePath=cursor?.getString(0) as String
                }
            }
        }
        binding.uploadImg.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*"
            )
            requestLauncher.launch(intent)
        }
    }

    private fun showLessDialog() {
        val builder = AlertDialog.Builder(this)
        // 제목 설정
        builder.setTitle("리뷰를 5자 이상 작성해주세요.")

        // 아니오 버튼 설정
        builder.setNegativeButton("확인") { dialog, which ->
            // 아무 동작 없이 다이얼로그 닫기
            dialog.dismiss()
        }
        // 다이얼로그 생성 및 표시
        builder.create().show()
    }

    private fun showWriteDialog() {
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


        if (imgStatus == 1) {
            uploadedFileName = uploadImage(this@ReplyWriteActivity, generateImageFileName(), filePath)
        }
        val jwtToken = TokenManager.getToken(MyApplication.instance)!!
        RetrofitInstance.api.saveReview(jwtToken, productId, reviewContent, uploadedFileName).enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                Log.d("insertReply", " ${response.body()}")
                binding.progressbar.visibility = View.VISIBLE

                //사진이 파이어 베이스에 업로드 될 때까지 3초 Progress바 보여줌
                //(리뷰 작성한후 페이지에 보이게 하기위해)
                val product = Product(productId.toInt(), productName?:"", productPrice, productDesc, productImage?:"", brandName?:"", "", "")
                val intent = Intent(this@ReplyWriteActivity, ProductActivity::class.java)
                intent.putExtra("type", "detail")
                intent.putExtra("product", product)
                if(imgStatus==1){
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.progressbar.visibility = View.INVISIBLE
                    startActivity(intent)
                    finish()
                }, 3000)}
                else {
                    startActivity(intent)
                    finish()
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("insertReply", " $t")
            }
        })
    }

    private fun generateImageFileName(): String {
        // UUID 생성
        val uuid = UUID.randomUUID().toString()

        // 현재 날짜 및 시간을 포함하는 형식 지정
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss")
        val currentDate = dateFormat.format(Date())

        // 최종적으로 파일 이름 생성
        return "image_${currentDate}_${uuid}.png"
    }

    private fun uploadImage(activity: AppCompatActivity, filename: String, filePath:String): String {
        //add............................
        val storage = MyApplication.storage
        val storageRef = storage.reference
        //새로 생성된 파일명.
        val imgRef = storageRef.child("review/${filename}")
        Log.d("Firebase: imageName", "review/${filename}")
        Log.d("Firebase: filePath", filePath)

        val file = Uri.fromFile(File(filePath))
        Log.d("Firebase: file","file $file")
        imgUri?.let { it ->
            Log.d("Firebase: Storage Save", "이미지 저장 시작")
            imgRef.putFile(it)
                .addOnSuccessListener {
                    Log.d("Firebase: Storage Save", "이미지 저장 완료")
                }
                .addOnFailureListener{
                    Log.d("Firebase: Storage Save", "이미지 저장 실패 ${it.printStackTrace()}")
                    activity.finish()
                }
        }
        return filename
    }

    @SuppressLint("MissingInflatedId")
    fun customToast(message: String) {
        val inflater = LayoutInflater.from(this@ReplyWriteActivity)
        val layout = inflater.inflate(R.layout.toast_board, this.findViewById(R.id.toast_layout_root) as ViewGroup?)
        val textView = layout.findViewById<TextView>(R.id.text_board)
        textView.text = message

        val toastView = Toast.makeText(this@ReplyWriteActivity, message, Toast.LENGTH_SHORT)
        toastView.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 150)
        toastView.view = layout
        toastView.show()
    }

}
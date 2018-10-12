package br.ufpa.smartufpa.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.ufpa.smartufpa.R
import br.ufpa.smartufpa.models.retrofit.UsersList
import br.ufpa.smartufpa.utils.retrofit.RetrofitClient
import br.ufpa.smartufpa.utils.retrofit.UserAPI
import kotlinx.android.synthetic.main.activity_enter_screen.*

class EnterScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_screen)

        btn_login.setOnClickListener {
//            startActivity(Intent(this,OSMLoginActivity::class.java))
            startActivity(Intent(this,MainActivity::class.java))
        }

        btn_register.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }
    }

//    private fun attemptLogin(email: String, password: String) {
//
//        val call = retrofitUserByEmail(email)
//        call.enqueue(object : Callback<UsersList> {
//            override fun onResponse(call: Call<UsersList>, response: Response<UsersList>) {
//                val status = response.isSuccessful
//                val usersList = response.body()!!.usersList
//                if (status && usersList.size == 1) {
//                    Log.i(TAG, "onResponse: " + response.body()!!)
//                    if (passwordMatches(usersList, password)) {
//                        startActivity(Intent(this@EnterScreenActivity, MainActivity::class.java))
//                        finish()
//                    }
//
//                }
//            }
//
//            override fun onFailure(call: Call<UsersList>, t: Throwable) {
//                t.printStackTrace()
//            }
//        })
//    }
//
//    private fun retrofitUserByEmail(email: String): Call<UsersList> {
//        val retrofitClient = RetrofitClient.getInstance(this, null)
//        val userAPI = retrofitClient.create(UserAPI::class.java)
//        return userAPI.userByEmail(email)
//    }
//
//    private fun passwordMatches(usersList: List<User>, password: String): Boolean {
//        return usersList[0].password.matches(password.toRegex())
//    }
}
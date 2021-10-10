package com.example.floral
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri

class SifaaPreviewDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sifaa_activity_details_preview)

        var fName = intent.getStringExtra("name")!!
        var email = intent.getStringExtra("email")!!
        var orgCompany = intent.getStringExtra("companyOrg")!!
        var employeeId = intent.getStringExtra("empID")!!
        var mobileNo = intent.getStringExtra("mobile")!!
        val urlImage = intent.getStringExtra("imageUri")!!

        if(fName.isEmpty()) fName = "Not filled"
        if(email.isEmpty()) email = "Not filled"
        if(orgCompany.isEmpty()) orgCompany = "Not filled"
        if(employeeId.isEmpty()) employeeId = "Not filled"
        if(mobileNo.isEmpty()) mobileNo = "Not filled"

        findViewById<TextView>(R.id.pdName).text = fName
        findViewById<TextView>(R.id.pdMail).text = email
        findViewById<TextView>(R.id.pdOrganization).text = orgCompany
        findViewById<TextView>(R.id.pdEmpId).text = employeeId
        findViewById<TextView>(R.id.pdMobileNo).text = mobileNo

        if (urlImage.isNotEmpty()) {
            val iCard = findViewById<ImageView>(R.id.preview_details_image_view_employee_id_card)
            iCard.setImageURI(urlImage.toUri())
            iCard.visibility = ViewGroup.VISIBLE
        } else {
            findViewById<TextView>(R.id.preview_details_text_view_emp_id).text = "Your ID Card (Not Available)"
        }

    }

    fun goBack(view: View) {
        onBackPressed()
    }
}
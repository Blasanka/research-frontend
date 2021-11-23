package com.example.floral
import SifaaAdapters.SifaaSavedCardsAdapter
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import SifaaDataModels.SifaaSavedCardItem
import SifaaServices.DatabaseHandler

class SifaaMethodPayment : AppCompatActivity(), SifaaSavedCardsAdapter.OnItemClickListener {

    private lateinit var totalTextViewPayment: TextView
    private lateinit var confirmButtonPayment: Button
    private lateinit var paymentButtonWallet: Button
    private lateinit var paymentButtonCreditDebit: Button
    private lateinit var paymentButtonBhimUpi: Button

    private lateinit var cashRadioPayment: RadioButton
    private lateinit var radioWallets: RadioButton
    private lateinit var savedRadioCard: RadioButton
    private lateinit var creditRadioDebit: RadioButton
    private lateinit var bhimRadioUpi: RadioButton
    private lateinit var netRadioBanking: RadioButton

    private lateinit var wSection: LinearLayout
    private lateinit var cdSection: LinearLayout
    private lateinit var biSection: LinearLayout

    private lateinit var allWalletsLinearLayout: LinearLayout

    var totalItemPrice = 0.0F
    var totalTaxPrice = 0.0F
    var subTotalPrice = 0.0F

    var takeAwayTime = ""

    private var selectedWallet = ""
    private var selectedSavedCard = ""
    private var enteredCreditDebitCard = ""
    private var enteredUPI = ""

    private lateinit var savedCardRecyclerView: RecyclerView
    private lateinit var savedCardsSifaaAdapter: SifaaSavedCardsAdapter
    private val savedCardItems = ArrayList<SifaaSavedCardItem>()

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setIcon(R.drawable.ic_alert)
            .setTitle("Alert!")
            .setMessage("Do you want to cancel the payment?")
            .setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ ->
                super.onBackPressed()
            })
            .setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, _ ->
                dialogInterface.dismiss()
            })
            .create().show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sifaa_activity_payment)

        totalItemPrice = intent.getFloatExtra("totalItemPrice", 0.0F)
        totalTaxPrice = intent.getFloatExtra("totalTaxPrice", 0.0F)
        subTotalPrice = intent.getFloatExtra("subTotalPrice", 0.0F)

        takeAwayTime = intent?.getStringExtra("takeAwayTime").toString()

        totalTextViewPayment = findViewById(R.id.total_payment_tv)
        totalTextViewPayment.text = "\$%.2f".format(subTotalPrice)

        cashRadioPayment = findViewById(R.id.cash_payment_radio_btn)
        radioWallets = findViewById(R.id.wallets_radio_btn)
        savedRadioCard = findViewById(R.id.saved_cards_radio_btn)
        creditRadioDebit = findViewById(R.id.credit_debit_card_radio_btn)
        bhimRadioUpi = findViewById(R.id.bhim_upi_radio_btn)
        netRadioBanking = findViewById(R.id.net_banking_radio_btn)

        wSection = findViewById(R.id.wallets_section_ll)
        cdSection = findViewById(R.id.credit_debit_section_ll)
        biSection = findViewById(R.id.bhim_upi_section_ll)

        setupPaymentButtons()
        setupWallets()

        savedCardRecyclerView = findViewById(R.id.payment_saved_cards_recycler_view)
        savedCardsSifaaAdapter = SifaaSavedCardsAdapter(
            this,
            savedCardItems,
            subTotalPrice,
            this
        )
        savedCardRecyclerView.adapter = savedCardsSifaaAdapter
        savedCardRecyclerView.layoutManager = LinearLayoutManager(this@SifaaMethodPayment)

        findViewById<ImageView>(R.id.payment_go_back_iv).setOnClickListener { onBackPressed() }
    }

    private fun setupWallets() {
        allWalletsLinearLayout = findViewById(R.id.wallets_ll)
        allWalletsLinearLayout.getChildAt(0).setOnClickListener {setClickedWallet(0)}
        allWalletsLinearLayout.getChildAt(1).setOnClickListener {setClickedWallet(1)}
        allWalletsLinearLayout.getChildAt(2).setOnClickListener {setClickedWallet(2)}
        allWalletsLinearLayout.getChildAt(3).setOnClickListener {setClickedWallet(3)}
        allWalletsLinearLayout.getChildAt(4).setOnClickListener {setClickedWallet(4)}
        setClickedWallet(0) //by default selected wallet
    }

    private fun setClickedWallet(selectedWalletPos: Int) {
        for(i in 0..4) {
            allWalletsLinearLayout.getChildAt(i).setBackgroundDrawable(resources.getDrawable(R.drawable.sifaa_border_unselect_option))
        }
        allWalletsLinearLayout.getChildAt(selectedWalletPos).setBackgroundDrawable(resources.getDrawable(R.drawable.sifaa_border_option))

        when(selectedWalletPos) {
            0 -> selectedWallet = "PayTM"
            1 -> selectedWallet = "Google Pay"
            2 -> selectedWallet = "PhonePe"
            3 -> selectedWallet = "Amazon Pay"
            4 -> selectedWallet = "Jio Money"
        }
    }

    private fun setupPaymentButtons() {
        confirmButtonPayment = findViewById(R.id.confirm_payment_btn)
        paymentButtonWallet = findViewById(R.id.payment_wallet_btn)
        paymentButtonCreditDebit = findViewById(R.id.payment_credit_debit_card_btn)
        paymentButtonBhimUpi = findViewById(R.id.payment_bhim_upi_btn)

        paymentButtonWallet.text = "Pay Securely \$%.2f".format(subTotalPrice)
        paymentButtonCreditDebit.text = "Pay \$%.2f".format(subTotalPrice)
        paymentButtonBhimUpi.text = "Pay \$%.2f".format(subTotalPrice)

        confirmButtonPayment.setOnClickListener { orderDone() }
        paymentButtonWallet.setOnClickListener { orderDone() }
        paymentButtonCreditDebit.setOnClickListener { doCreditDebitCardPayment() }
        paymentButtonBhimUpi.setOnClickListener { doBHIMUPIPayment() }
    }

    private fun doBHIMUPIPayment() {
        val upiET: EditText = findViewById(R.id.payment_bhim_upi_et)
        if(upiET.text.isEmpty()) {
            upiET.error = "Enter Your UPI ID"
            return
        }
        val pattern = Regex("([a-zA-Z0-9])@([a-zA-Z0-9])")
        if(!pattern.containsMatchIn(upiET.text.toString())) {
            upiET.error = "Invalid UPI ID"
            return
        }
        enteredUPI = upiET.text.toString()
        orderDone()
    }

    private fun doCreditDebitCardPayment() {
        val cardNumberET = findViewById<EditText>(R.id.payment_credit_debit_card_number_et)
        val cardExpiryDateET = findViewById<EditText>(R.id.payment_credit_debit_expiry_date_et)
        val cardCVVET = findViewById<EditText>(R.id.payment_credit_debit_cvv_et)
        val cardHolderNameET = findViewById<EditText>(R.id.payment_credit_debit_card_holder_name_et)

        var allTrue = true
        if(cardNumberET.length() != 16) {
            cardNumberET.error = "Invalid Card Number"
            allTrue = false
        }
        if(cardExpiryDateET.length() != 5) {
            cardExpiryDateET.error = "Invalid Date Format"
            allTrue = false
        }
        if(cardCVVET.length() != 3) {
            cardCVVET.error = "Invalid CVV Number"
            allTrue = false
        }
        if(cardHolderNameET.text.isEmpty()) {
            cardHolderNameET.error = "Name is required"
            allTrue = false
        }

        if(!allTrue) return

        //Don't Save CVV
        if(findViewById<SwitchCompat>(R.id.payment_credit_debit_saved_card_switch).isChecked) {
            val cardItem = SifaaSavedCardItem(
                cardNumberET.text.toString(),
                cardHolderNameET.text.toString(),
                cardExpiryDateET.text.toString()
            )
            val result = DatabaseHandler(this).insertSavedCardDetails(cardItem)
            // if result is -1, then Card is already saved, if 1 then it is saved in database
        }
        enteredCreditDebitCard = "XXXX${cardNumberET.text.substring(12,16)}, ${cardHolderNameET.text}"
        orderDone()
    }

    fun chooseModeOfPayment(view: View) {
        var payMethod = ""
        payMethod = if(view is RadioButton) {
            ((view.parent as LinearLayout).getChildAt(1) as TextView).text.toString()
        } else {
            (((view as CardView).getChildAt(0) as LinearLayout).getChildAt(1) as TextView).text.toString()
        }

        cashRadioPayment.isChecked = false
        radioWallets.isChecked = false
        savedRadioCard.isChecked = false
        creditRadioDebit.isChecked = false
        bhimRadioUpi.isChecked = false
        netRadioBanking.isChecked = false

        wSection.visibility = ViewGroup.GONE
        cdSection.visibility = ViewGroup.GONE
        biSection.visibility = ViewGroup.GONE
        savedCardRecyclerView.visibility = ViewGroup.GONE

        confirmButtonPayment.visibility = ViewGroup.INVISIBLE

        when(payMethod) {
            getString(R.string.cash_payment) -> {
                cashRadioPayment.isChecked = true
                confirmButtonPayment.visibility = ViewGroup.VISIBLE
            }
            getString(R.string.wallets) -> {
                radioWallets.isChecked = true
                wSection.visibility = ViewGroup.VISIBLE
            }
            getString(R.string.saved_cards) -> {
                savedRadioCard.isChecked = true
                savedCardRecyclerView.visibility = ViewGroup.VISIBLE
                loadSavedCardsFromDatabase()
            }
            getString(R.string.credit_or_debit_card) -> {
                creditRadioDebit.isChecked = true
                cdSection.visibility = ViewGroup.VISIBLE
            }
            getString(R.string.bhim_upi) -> {
                bhimRadioUpi.isChecked = true
                biSection.visibility = ViewGroup.VISIBLE
            }
            getString(R.string.net_banking) -> {
                Toast.makeText(this, "NOT AVAILABLE", Toast.LENGTH_SHORT).show()
                netRadioBanking.isChecked = true
            }
        }
    }

    private fun orderDone() {
        var paymentMethod = ""
        when {
            cashRadioPayment.isChecked -> paymentMethod = "Pending: Cash Payment"
            radioWallets.isChecked -> paymentMethod = "Paid: $selectedWallet Wallet"
            savedRadioCard.isChecked -> paymentMethod = "Paid: $selectedSavedCard"
            creditRadioDebit.isChecked -> paymentMethod = "Paid: $enteredCreditDebitCard"
            bhimRadioUpi.isChecked -> paymentMethod = "Paid: $enteredUPI"
            //netRadioBanking.isChecked -> paymentMethod = "Paid: Net Banking"
        }

        val intent = Intent(this, SifaaOrderDoneActivity::class.java)
        intent.putExtra("totalItemPrice", totalItemPrice)
        intent.putExtra("totalTaxPrice", totalTaxPrice)
        intent.putExtra("subTotalPrice", subTotalPrice)
        intent.putExtra("takeAwayTime", takeAwayTime)
        intent.putExtra("paymentMethod", paymentMethod)

        startActivity(intent)
        finish()
    }

    private fun loadSavedCardsFromDatabase() {
        savedCardItems.clear()
        savedCardsSifaaAdapter.notifyDataSetChanged()

        val data = DatabaseHandler(this).readSavedCardsData()

        if(data.size == 0) {
            Toast.makeText(this, "No Saved Cards", Toast.LENGTH_SHORT).show()
            return
        }

        for(i in 0 until data.size) {
            val currentItem =  SifaaSavedCardItem()
            currentItem.cardNumber = data[i].cardNumber
            currentItem.cardHolderName = data[i].cardHolderName
            currentItem.cardExpiryDate = data[i].cardExpiryDate

            savedCardItems.add(currentItem)
            savedCardsSifaaAdapter.notifyItemInserted(i)
        }

    }

    override fun onItemClick(position: Int) {
        for(i in 0 until savedCardItems.size) {
            savedCardItems[i].isSelected = false
        }
        savedCardItems[position].isSelected = true
        savedCardsSifaaAdapter.notifyDataSetChanged()
    }

    override fun onItemPayButtonClick(position: Int) {
        selectedSavedCard = "XXXX${savedCardItems[position].cardNumber.substring(12,16)}, ${savedCardItems[position].cardHolderName}"
        orderDone()
    }
}
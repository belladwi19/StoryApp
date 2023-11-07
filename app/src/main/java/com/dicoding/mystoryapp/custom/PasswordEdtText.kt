package com.dicoding.mystoryapp.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.dicoding.mystoryapp.R

class PasswordEdtText : AppCompatEditText{
    private var length = 0

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    private fun init(){
        addTextChangedListener(object : TextWatcher{
            @SuppressLint("StringFormatInvalid")
            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                length = p1 + p3
                error = if (length < 6) resources.getString(R.string.pass_length_acc, if (hint != null) hint else "Field") else null
            }

            override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                //no action
            }
            override fun afterTextChanged(p0: Editable?) {
                //no action
            }
        })
    }

    constructor(context: Context): super(context){
        init()
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs){
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr){
        init()
    }
}
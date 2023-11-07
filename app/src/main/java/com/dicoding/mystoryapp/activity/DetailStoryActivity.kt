package com.dicoding.mystoryapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.mystoryapp.R
import com.dicoding.mystoryapp.databinding.ActivityDetailStoryBinding
import com.dicoding.mystoryapp.fragment.SettingStoryFragment
import com.dicoding.mystoryapp.fragment.StoryFragment
import com.dicoding.mystoryapp.response.ListStoryItem

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    @SuppressLint("StringFormatMatches", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (supportActionBar != null){
            (supportActionBar as ActionBar).title = resources.getString(R.string.app_bar_detail)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val dataStory = intent.getParcelableExtra<ListStoryItem>(MainActivity.DETAIL)
        val tvvName = findViewById<TextView>(R.id.tvv_name)
        val tvvDescription = findViewById<TextView>(R.id.tvv_description)

        Glide.with(this)
            .load(dataStory?.photoUrl)
            .apply(RequestOptions().override(300,300))
            .into(binding.imgStory)
        with(this){
            binding.apply {
                tvvName.text = resources.getString(R.string.detail_text_name) + dataStory?.name
                tvvDescription.text = resources.getString(R.string.detail_text_dsc) + dataStory?.description
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onOptionsItemSelected(menu: MenuItem): Boolean {
        when (menu.itemId) {
            R.id.navigation_home -> {
                val intentActivity = Intent(this, MainActivity::class.java)
                startActivity(intentActivity)
            }

            R.id.navigation_new_story -> {
                supportFragmentManager.commit {
                    replace(R.id.activity_main, StoryFragment(), StoryFragment::class.java.simpleName)
                }
            }

            R.id.navigation_setting -> {
                supportFragmentManager.commit {
                    replace(R.id.activity_main, SettingStoryFragment(), SettingStoryFragment::class.java.simpleName)
                }
            }
        }
        return super.onOptionsItemSelected(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    companion object {
        const val DETAIL = "DETAIL"
    }
}
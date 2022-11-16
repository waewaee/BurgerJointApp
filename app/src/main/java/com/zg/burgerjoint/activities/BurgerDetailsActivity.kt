package com.zg.burgerjoint.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.Explode
import android.view.Window
import android.view.animation.AnimationUtils
import com.bumptech.glide.Glide
import com.zg.burgerjoint.R
import com.zg.burgerjoint.data.vos.BurgerVO
import com.zg.burgerjoint.mvp.presenters.BurgerDetailsPresenter
import com.zg.burgerjoint.mvp.presenters.impls.BurgerDetailsPresenterImpl
import com.zg.burgerjoint.mvp.views.BurgerDetailsView
import kotlinx.android.synthetic.main.activity_burger_details.*
import kotlinx.android.synthetic.main.view_item_burger.view.*

class BurgerDetailsActivity : BaseActivity(),BurgerDetailsView {

    companion object{

        private const val EXTRA_BURGER_ID = "Burger Id Extra"

        fun newIntent(context: Context, burgerId : Int): Intent{
            val intent = Intent(context, BurgerDetailsActivity::class.java)
            intent.putExtra(EXTRA_BURGER_ID,burgerId)
            return intent
        }
    }

    private lateinit var mPresenter : BurgerDetailsPresenter

    var isFavorite : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpTransitions()
        setContentView(R.layout.activity_burger_details)
        setUpPresenter()
        setUpListeners()
        val burgerId = intent.getIntExtra(EXTRA_BURGER_ID, 0)

        mPresenter.onBurgerDetailsUiReady(this, burgerId)
    }

    private fun setUpTransitions(){
        with(window){
            requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
            val explode = Explode()
            explode.duration = 200
            enterTransition = explode
            exitTransition = explode
        }
    }

    private fun setUpListeners(){
        ivBurger.setOnClickListener {
            val animator = AnimationUtils.loadAnimation(this, R.anim.rotate)
            ivBurger.startAnimation(animator)
        }

        btnFavorite.setOnClickListener {
            if(!isFavorite){
                btnFavorite.speed = 1.0f
                btnFavorite.playAnimation()
                isFavorite = true
            } else {
                btnFavorite.speed = -4.0f
                btnFavorite.playAnimation()
                isFavorite = false
            }
        }
    }

    private fun setUpPresenter(){
        mPresenter = getPresenter<BurgerDetailsPresenterImpl,BurgerDetailsView>()
    }

    override fun displayBurgerDetails(burger : BurgerVO) {
        tvBurgerName.text = burger.burgerName
        tvDescription.text = burger.burgerDescription
        Glide.with(ivBurger)
            .load(burger.burgerImageUrl)
            .into(ivBurger)
    }

}
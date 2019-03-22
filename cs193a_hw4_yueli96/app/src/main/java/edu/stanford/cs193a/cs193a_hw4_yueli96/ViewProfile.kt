package edu.stanford.cs193a.cs193a_hw4_yueli96

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.koushikdutta.ion.Ion
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_view_profile.*
import kotlinx.android.synthetic.main.fragment_view_profile.view.*
import kotlinx.android.synthetic.main.fragment_view_users.*


class ViewProfile : Fragment() {
    // user id of the current profile
    var curID = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_profile, container, false)
    }

    // display profile of selected user and set rating bar listener for each user
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (activity?.intent != null) {
            curID = activity!!.intent.getIntExtra("user_id", 0)
            displayProfile(curID)
            setRatingBarListener()
        }
    }

    // helper, set rating bar listener, store rating to preferences, keyword is the user's id
    private fun setRatingBarListener(){
        rating_bar.setOnRatingBarChangeListener{_, rating, _->
            // update preferences
            val prefs = activity!!.getSharedPreferences("ratingInfo", MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putFloat(curID.toString(), rating)
            editor.apply()

            // add animation effects
            YoYo.with(Techniques.Tada)
                .duration(200)
                .repeat(2)
                .playOn(rating_bar);
        }
    }

    // display profile of selected user
    fun displayProfile(userID: Int) {
        curID = userID // update current selected id
        user_name.text = FriendrMainActivity.usersList[curID] // update current selected user name

        // load user rating from preferences
        val prefs = activity!!.getSharedPreferences("ratingInfo", MODE_PRIVATE)
        rating_bar.rating = prefs.getFloat(userID.toString(), 0f)

        // load user image
        Picasso.get()
            .load(FriendrMainActivity.BASE_URL + FriendrMainActivity.usersList[userID] + ".jpg")
            .into(user_image)

        // load user description
        Ion.with(this)
           .load(FriendrMainActivity.BASE_URL + FriendrMainActivity.usersList[userID] + ".txt")
            .asString()
            .setCallback { _, description ->
                user_description.text = description
            }
    }

}

package edu.stanford.cs193a.cs193a_hw4_yueli96


import android.content.Intent
import android.content.res.Configuration
import android.graphics.Point
import android.media.Image
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import com.koushikdutta.ion.Ion
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_view_users.*
import kotlinx.android.synthetic.main.user.view.*


class ViewUsers : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_users, container, false)
    }

    // display all the users and set up image button listener
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        displayUserImages(FriendrMainActivity.usersList)
        setUpImageButtonListener()
    }

    // load images for each user and add to grid layout
    private fun displayUserImages(usersList: List<String>){
        for(user in usersList){
            val userImage = layoutInflater.inflate(R.layout.user, null)
            Picasso.get()
                .load(FriendrMainActivity.BASE_URL + user + ".jpg")
                .into(userImage.user_image)
            // set id for each image, id associate with their names and ratings
            userImage.id = usersList.indexOf(user)
            user_image_grid.addView(userImage)

            // get size of the screen, set image size
            val display = activity!!.windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)

            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                userImage.layoutParams.width = size.x / 2
                userImage.layoutParams.height = size.x / 2
            } else {
                userImage.layoutParams.width = size.x / 4
                userImage.layoutParams.height = size.x / 4
            }
        }
    }

    // show user's profile when being clicked
    private fun setUpImageButtonListener(){
        for (i in 0 until user_image_grid.childCount - 1) {
            val user = user_image_grid.getChildAt(i) as ImageButton
            user.setOnClickListener {
                showProfile(user)
            }
        }
    }

    // on click function for each image button
    private fun showProfile(user: ImageButton){
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // if portrait mode, start a new activity
            val myIntent = Intent(activity, ProfileActivity::class.java)
            myIntent.putExtra("user_id", user.id)
            startActivity(myIntent)
        } else {
            // if landscape mode, update the profile fragment
            val userProfile = fragmentManager!!.findFragmentById(R.id.view_profile_fragment) as ViewProfile
            userProfile.displayProfile(user.id)
        }
    }

}

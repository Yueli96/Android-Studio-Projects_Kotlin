package edu.stanford.cs108.cs193a

/*
Kotlin basic:
var -> variable that could be changed
val -> things that could be change
able to refer to view by its id: left_button.text.toString().toInt()
or val leftbutton = findViewById<Button>(R.id.left_button)
*/


/*
            Layout
Some concepts:
Layout managers(more flexible general)
ViewGroup
XML: a language for describing hierarchical text data

    Linear Layout: can be nested inside each other
Gravity:alignment direction that widges are pulled -> android:gravity="center|right"
Weight: relative element sizes by integers  android:layout_weight="1"
Widget box model:
    content: size of widget itself
    padding: artificial increase to widget size outside of content -> android:padding="40sp" inside a widget
    border:outside padding, a line around edge of widgets
    margin: invisible seperation from neighboring widgets -> android:layout_margin="40sp"

Sizing a widget:
    width/height: wrap_content/ match_parent

layout_gravity & gravity

    Grid Layout:
    Table Layout:
    Nested Layout:

    Constraint Layout
 */
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class Lecture2_Jan10 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

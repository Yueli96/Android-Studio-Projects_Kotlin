package edu.stanford.cs193a.cs193a_hw2_yueli96

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_to_do_list.*
import android.widget.AdapterView.OnItemClickListener
import kotlinx.android.synthetic.main.list_item.*
import android.R.layout.list_content
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.support.v4.content.ContextCompat.getSystemService
import android.view.LayoutInflater
import android.view.ViewGroup
import android.R.layout.list_content
import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.res.Resources
import android.graphics.Paint
import android.support.v4.content.ContextCompat.getSystemService
import android.widget.*
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import kotlinx.android.synthetic.main.list_item.view.*
import org.w3c.dom.Text
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.Reader
import java.util.*

/*
 * Yue Li <yueli96@stanford.edu>
 * CS 193A, Winter 2019
 * Homework Assignment 2
 * TodoListApp 1.0 - This app allows you to create a to-do list.
 * Note:
 * Adding: click ADD to add a new item
 * Complete: long-press an item to complete it and move it to done-list
 * Move to top: click the CLOCK icon and move it to the top
 * Remove: click the BIN icon to remove it from current list
 * Scrolling: scroll to view more items
 * Persistent: list are persistently stored
 */

class ToDoListActivity : AppCompatActivity() {
    private val todo_items: MutableList<String> = LinkedList()
    private val done_items: MutableList<String> = LinkedList()
    private lateinit var todoListAdapter: ArrayAdapter<String>
    private lateinit var doneListAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do_list)
        todoListAdapter = ArrayAdapter(this, R.layout.list_item, R.id.item_content, todo_items)
        doneListAdapter = ArrayAdapter(this, R.layout.finished_list, R.id.item_content, done_items)
        todo_list.setAdapter(todoListAdapter)
        done_list.setAdapter(doneListAdapter)

        todo_list.setOnItemLongClickListener{ parent, view, position, id ->
            onLongClickList(position, view)
        }
    }

    // function called in the itemLongClickListener
    // this function gets the item that was long-pressed and move it to done list
    fun onLongClickList(position: Int, view: View): Boolean{
        val textview: TextView = view.item_content as TextView
        done_items.add(textview.text.toString())
        todo_items.removeAt(position)
        todoListAdapter.notifyDataSetChanged()
        doneListAdapter.notifyDataSetChanged()
        return true
    }

    // called when the bin icon of to-do list is clicked
    fun deleteFromTodoList(view: View){
        val str = getString(view)
        todo_items.remove(str)
        todoListAdapter.notifyDataSetChanged()
    }

    // called when the bin icon of done list is clicked
    fun deleteFromDoneList(view: View){
        val str = getString(view)
        done_items.remove(str)
        doneListAdapter.notifyDataSetChanged()
    }

    // called when the clock icon is clicked, move the item to the head of the list
    fun moveToTop(view: View){
        val str: String = getString(view)
        todo_items.remove(str)
        todo_items.add(0, str)
        todoListAdapter.notifyDataSetChanged()
    }

    // helper
    // when an icon is clicked, get its parent row first, then get its item_content
    fun getString(view: View): String{
        val row: RelativeLayout = view.getParent() as RelativeLayout
        //val textview: TextView = row.getChildAt(1) as TextView
        val text: String = row.item_content.text.toString()
        return text
    }

    // add an item to to-do list
    fun onClickAdd(view: View){
        val item:String = add_item.text.toString()
        if(item.isEmpty()){
            return
        }
        todo_items.add(item)
        todoListAdapter.notifyDataSetChanged()
        add_item.setText("")
    }

    // store the two list into interior storage when stops the activity
    override fun onStop() {
        super.onStop()
        Log.d("liyue", "stop")
        writeToFile(todo_items, "todo.txt")
        writeToFile(done_items, "done.txt")
        todo_items.clear()
        done_items.clear()
    }

    // read strings to the two list when start the activity
    override fun onStart() {
        super.onStart()
        val todoListDir = filesDir.toString() + "/" + "todo.txt"
        val doneListDir = filesDir.toString() + "/" + "done.txt"
        val todoList = readFile(todoListDir)
        val doneList = readFile(doneListDir)
        for(item in todoList){
            Log.d("addFile", item)
            todo_items.add(item)
        }
        for(item in doneList){
            done_items.add(item)
        }
    }

    // helper, read each line of a file into a list
    private fun readFile(filename: String): List<String>{
        val file = File(filename)
        try {
            if (!file.exists()) {
                file.createNewFile()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return file.readLines()
    }

    // helper, create and write a list to a file
    private fun writeToFile(list: MutableList<String>, filename: String) {
        val fn = filesDir.toString() + "/" + filename
        val file = File(fn)
        try {
            file.createNewFile()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        try {
            val outputStream = openFileOutput(filename, Context.MODE_PRIVATE)
            for(item in list) {
                Log.d("writeFile", item)
                outputStream.write((item + "\n").toByteArray())
            }
            outputStream.close()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
    }
}

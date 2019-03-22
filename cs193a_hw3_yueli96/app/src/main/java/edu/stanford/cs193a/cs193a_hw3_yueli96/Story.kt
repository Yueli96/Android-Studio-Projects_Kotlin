package edu.stanford.cs193a.cs193a_hw3_yueli96
/*
 * CS 193A, Marty Stepp
 *
 * *** NOTE: YOU MAY NEED TO MODIFY THIS FILE BY PUTTING IT INTO A 'package'. ***
 *
 * This class represents a Mad Libs story that comes from a text file.
 * You can construct it and pass an input stream or Scanner to read the story text.
 *
 * You can get the story's text by calling its toString method.
 * A Story is Serializable, so it can be packed into an Intent as "extra" data.
 */

import android.content.Context
import android.support.annotation.RawRes
import java.io.*
import java.util.*

class Story : Serializable {
    // text of the story
    private var text = ""

    // list of placeholders to fill in
    private var _placeholders : MutableList<Placeholder> = mutableListOf()

    /**
     * sets whether this story will be outputted in HTML mode with B tags around placeholders
     * (default false)
    */
    var isHtmlMode: Boolean = false                 // set true to surround placeholders with <b></b> tags


    // properties
    /** returns a read-only list of placeholders in the story */
    val placeholders: List<String>
        get() = this._placeholders.map { it -> it.text }

    /** returns total number of placeholders in the story */
    val placeholderCount: Int
        get() = _placeholders.size

    /** Returns "an" if the the placeholder at the given index begins with a vowel (AEIOU), else "a". */
    fun aVsAn(index: Int): String {
        return aVsAn(_placeholders[index].text)
    }

    /** Returns "an" if the given string begins with a vowel (AEIOU), else "a". */
    fun aVsAn(s: String): String {
        return if (startsWithVowel(s)) "an" else "a"
    }

    /** sets the placeholder at the given index to be replaced by the given text */
    fun fillPlaceholder(index: Int, replacementText: String) {
        _placeholders[index].replacement = replacementText
    }

    /** reads initial story text from the given Activity context and raw resource ID. */
    fun read(context: Context, @RawRes resourceID: Int) {
        read(context.resources.openRawResource(resourceID))
    }

    /** reads initial story text from the given Activity context and raw resource name. */
    fun read(context: Context, resource: String) {
        val fileID = context.resources.getIdentifier(resource, "raw", context.packageName)      // R.raw.tarzan
        read(context.resources.openRawResource(fileID))
    }

    /** reads initial story text from the given input stream */
    fun read(stream: InputStream) {
        read(Scanner(stream))
    }

    /** reads initial story text from the given Reader */
    fun read(input: Reader) {
        read(Scanner(input))
    }

    /** reads initial story text from the given Scanner */
    fun read(input: Scanner) {
        // read data from input source
        val sb = StringBuilder()
        while (input.hasNextLine()) {
            sb.append(input.nextLine())
            sb.append('\n')
        }
        this.text = sb.toString()

        // find _placeholders in input
        var lt = text.indexOf('<')
        var gt = text.indexOf('>')
        while (lt in 0..(gt - 1)) {
            val phText = text.substring(lt + 1, gt)
            val ph = Placeholder(phText, lt)
            _placeholders.add(ph)

            lt = text.indexOf('<', gt + 1)
            if (lt < 0) break
            gt = text.indexOf('>', lt + 1)
        }
    }

    /** Returns true if the placeholder at the given index begins with a vowel. */
    fun startsWithVowel(index: Int): Boolean {
        return startsWithVowel(_placeholders[index].text)
    }

    /** Returns true if the given string begins with a vowel (AEIOU). */
    fun startsWithVowel(s: String): Boolean {
        return !s.isEmpty() && "aeiou".indexOf(s.trim().toLowerCase()[0]) >= 0
    }

    /** Returns story text, including any _placeholders filled in. */
    override fun toString(): String {
        var storyText = text

        // fill in the _placeholders
        // (go backwards so that the indexes don't shift under our feet)
        for (i in _placeholders.indices.reversed()) {
            val ph = _placeholders[i]
            var repl = ph.replacement
            if (isHtmlMode) {
                repl = "<b>$repl</b>"
            }

            val before = storyText.substring(0, ph.index)
            val after = storyText.substring(ph.index + ph.text.length + 2)   // +2 because of <>
            storyText = before + repl + after
        }
        return storyText
    }

    /**
     * A small class to represent a placeholder such as "<proper noun>" and an index in the
     * original text at which it occurs.
    </proper> */
    private class Placeholder
            (var text: String, val index: Int) : Serializable {
        var replacement: String = ""

        /** Returns a text representation of the placeholder for debugging. */
        override fun toString(): String {
            return "{text=$text, replacement=$replacement, index=$index}"
        }
    }
}
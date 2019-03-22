package edu.stanford.cs193a.cs193a_hw7_yueli96

import java.io.Serializable
import java.util.*
/**
 * This is a simple wrapper class around database 'node' rows.
 */
class ChannelNode: Serializable {
    var name: String = ""
    var description: String = ""
    var members = HashMap<String, UserNode>()
    var messages = HashMap<String, MessageNode>()

    constructor()

    constructor(name: String, description: String){
        this.name = name
        this.description = description
    }
    override fun toString(): String {
        return ("DBNode {name=$name description=$description")
    }
}

class UserNode: Serializable{
    //username
    var username: String = ""
    var email: String = ""
    constructor()
    constructor(email: String){
        this.email = email
        this.username = email.split('@')[0]
        val re = Regex("[^A-Za-z0-9 ]")
        this.username = re.replace(username, "")

    }

    override fun toString(): String {
        return ("userNode {username=$username email=$email")
    }

}

class MessageNode: Serializable{
    var from: String = ""
    var text: String = ""
    var timestamp: String = ""

    constructor()
    constructor(from: String, text: String, timestamp: String){
        this.from = from
        this.text = text
        this.timestamp = timestamp

    }
    override fun toString(): String {
        return ("$from: $text")
    }
}

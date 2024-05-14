package social

import java.util.concurrent.locks.Lock

interface User {
    val userName: String
    val yearOfBirth: Int
    val bio: String
    val currentFriends: List<User>
    val lock: Lock

    fun hasFriend(user: User): Boolean

    fun removeFriend(user: User): Boolean
    fun considerFriendRequest(user: User): Boolean
    fun removeLongestStandingFriend(): User?
}
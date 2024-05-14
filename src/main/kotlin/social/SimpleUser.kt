package social

import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

class SimpleUser(
    override val userName: String,
    override val yearOfBirth: Int,
    override val bio: String,
    val befriendingStrategy: (User, User) -> Boolean = ::standardStrategy
) : User {

    private val friends: MutableList<User> = mutableListOf()

    override val lock: Lock = ReentrantLock()
    override val currentFriends: List<User>
        get() = friends.toList()

    init {
        if (yearOfBirth !in 1900..2100)
            throw IllegalArgumentException("Invalid birth year")
    }

    override fun considerFriendRequest(user: User): Boolean =
        if (befriendingStrategy(this, user))
            friends.add(user)
        else
            false


    override fun hasFriend(user: User): Boolean = friends.any { it.userName == user.userName }

    override fun removeFriend(user: User): Boolean = friends.removeIf { it.userName == user.userName }

    override fun removeLongestStandingFriend(): User? = friends.removeFirstOrNull()

}
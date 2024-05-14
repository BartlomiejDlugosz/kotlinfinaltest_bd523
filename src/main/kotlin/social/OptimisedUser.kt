package social

import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

class OptimisedUser (
    override val userName: String, override val yearOfBirth: Int, override val bio: String,
    val befriendingStrategy: (User, User) -> Boolean = ::standardStrategy
    ) : User {

        private val friends: OrderedMap<String, User> = HashMapLinked()

        override val lock: Lock = ReentrantLock()
        override val currentFriends: List<User>
        get() = friends.values

        init {
            if (yearOfBirth !in 1900..2100)
                throw IllegalArgumentException("Invalid birth year")
        }

        override fun considerFriendRequest(user: User): Boolean =
            if (befriendingStrategy(this, user)) {
                friends[user.userName] = user
                true
            }
            else
                false


        override fun hasFriend(user: User): Boolean = friends.containsKey(user.userName)

        override fun removeFriend(user: User): Boolean = friends.remove(user.userName) != null

        override fun removeLongestStandingFriend(): User? = friends.removeLongestStandingEntry()?.second

    }
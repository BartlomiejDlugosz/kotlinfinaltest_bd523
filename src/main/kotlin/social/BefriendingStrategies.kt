package social

fun standardStrategy(targetUser: User, candidateUser: User): Boolean = targetUser.currentFriends.all{ it.userName != candidateUser.userName }

fun unfriendlyStrategy(targetUser: User, candidateUser: User): Boolean = false

fun limitOfFiveStrategy(targetUser: User, candidateUser: User): Boolean =
    if (!standardStrategy(targetUser, candidateUser))
        false
    else {
        while (targetUser.currentFriends.size >= 5) targetUser.removeLongestStandingFriend()
        true
    }


fun interestedInDogsStrategy(targetUser: User, candidateUser: User): Boolean =
    if (!standardStrategy(targetUser, candidateUser))
        false
    else {
        candidateUser.bio.split(" ").any { it.lowercase() == "dog" }
    }
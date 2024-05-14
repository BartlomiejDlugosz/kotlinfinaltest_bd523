package social;

import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

final public class Matchmaker {
    // If you decide to implement Matchmaker in Kotlin (for reduced marks), you will need to delete
    // this file, and add a Matchmarker.kt file with your Kotlin files.
    BiFunction<User, User, Boolean> compatible;

    Matchmaker(BiFunction<User, User, Boolean> func) {
        this.compatible = func;
    }

    public void tryMatching(User user1, User user2) throws InterruptedException {
        while (true) {
            if (user1.getLock().tryLock()) {
                try {
                    if (user2.getLock().tryLock()) {
                        try {
                            if (compatible.apply(user1, user2)) {
                                user1.considerFriendRequest(user2);
                                user2.considerFriendRequest(user1);
                            }
                            return;
                        } finally {
                            user2.getLock().unlock();
                        }
                    }
                } finally {
                    user1.getLock().unlock();
                }
            }
        }
    }
}

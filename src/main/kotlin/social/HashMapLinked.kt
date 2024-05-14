package social

private const val INITIAL_BUCKETS = 8
private const val LOAD_FACTOR = 0.75

class HashMapLinked<K, V> : OrderedMap<K, V> {

    private class Node<K, V>(
        val key: K,
        var value: V,
        var prev: Node<K, V>?,
        var next: Node<K, V>? = null,
    )

    private var head: Node<K, V>? = null
    private var tail: Node<K, V>? = null

    private var buckets: MutableList<MutableList<Node<K, V>>>

    init {
        buckets = mutableListOf()
        for (i in 0..<INITIAL_BUCKETS) {
            buckets.add(mutableListOf())
        }
    }

    override var size = 0
        private set

    override val values: List<V>
        get() {
            val values = mutableListOf<V>()
            var current = head
            while (current != null) {
                values.add(current.value)
                current = current.next
            }
            return values
        }

    override fun containsKey(key: K): Boolean = getBucket(key).any { it.key == key }

    override fun remove(key: K): V? =
        if (containsKey(key)) {
            val bucket = getBucket(key)
            val currentItem = bucket.first { it.key == key }
            bucket.remove(currentItem)
            if (head == currentItem) head = currentItem.next
            if (tail == currentItem) tail = currentItem.prev
            currentItem.prev?.next = currentItem.next
            currentItem.next?.prev = currentItem.prev
            size--

            currentItem.value
        } else
            null

    override fun set(key: K, value: V): V? {
        val previousValue = remove(key)
        val newNode = Node(key, value, tail)
        getBucket(key).add(newNode)
        if (head == null) head = newNode
        tail?.next = newNode
        tail = newNode
        size++
        resize()

        return previousValue
    }

    override fun removeLongestStandingEntry(): Pair<K, V>? {
        if (head != null) {
            val key = head!!.key
            val removed = remove(key)
            return Pair(key, removed!!)
        }
        return null
    }

    private fun getBucket(key: K) = buckets[key.hashCode().mod(buckets.size)]

    private fun resize() {
        if (size <= LOAD_FACTOR * buckets.size) {
            return
        }
        val allContent = mutableListOf<Node<K, V>>()
        for (bucket in buckets) {
            allContent.addAll(bucket)
        }

        val newNumBuckets = buckets.size * 2

        buckets = mutableListOf()
        for (i in 0..<newNumBuckets) {
            buckets.add(mutableListOf())
        }

        for (node in allContent) {
            getBucket(node.key).add(node)
        }
    }
}

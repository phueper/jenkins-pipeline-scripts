def call(String label, Closure body) {
    lock(label: label, quantity: 1) {
        return body()
    }
}

package pw.forst.olb.hub.error

interface TasksErrorHandling<T> {
    fun handle(task: T, ex: Exception)
    fun handle(task: T, ex: Throwable)
}


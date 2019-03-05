package pw.forst.olb.common.dto.docker

sealed class DockerTaskResult {
    abstract val result: DockerTaskResultFlag
    abstract val dockerTask: DockerTask
}

data class DockerFailureTaskResult(
    override val dockerTask: DockerTask,
    val throwable: Throwable
) : DockerTaskResult() {
    override val result: DockerTaskResultFlag
        get() = DockerTaskResultFlag.FAILURE
}

data class DockerResourcesChangeTaskResult(
    val task: DockerResourcesChangeTask,
    override val result: DockerTaskResultFlag = DockerTaskResultFlag.SUCCESS
) : DockerTaskResult() {
    override val dockerTask: DockerTask
        get() = task

}

data class DockerJobStopTaskResult(
    val task: DockerJobStopTask,
    override val result: DockerTaskResultFlag = DockerTaskResultFlag.SUCCESS

) : DockerTaskResult() {
    override val dockerTask: DockerTask
        get() = task

}

data class DockerCommandTaskResult(
    val task: DockerCommandTask,
    override val result: DockerTaskResultFlag = DockerTaskResultFlag.SUCCESS

) : DockerTaskResult() {
    override val dockerTask: DockerTask
        get() = task

}

data class DockerContainerCreateResult(
    val containerId: String,
    val task: DockerContainerCreateTask,
    override val result: DockerTaskResultFlag = DockerTaskResultFlag.SUCCESS
) : DockerTaskResult() {
    override val dockerTask: DockerTask
        get() = task

}

enum class DockerTaskResultFlag {
    SUCCESS, FAILURE
}

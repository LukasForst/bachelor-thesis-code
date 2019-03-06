package pw.forst.olb.hub.error


open class DockerException(message: String? = null) : Exception(message)

class ContainerNotCreatedException(message: String? = null) : DockerException(message)

class DockerHostNotReachedException(message: String? = null) : DockerException(message)

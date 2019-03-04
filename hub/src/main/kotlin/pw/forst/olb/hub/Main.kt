package pw.forst.olb.hub

import com.spotify.docker.client.DefaultDockerClient
import com.spotify.docker.client.DockerClient.LogsParam
import com.spotify.docker.client.messages.ContainerConfig
import com.spotify.docker.client.messages.HostConfig
import com.spotify.docker.client.messages.PortBinding
import mu.KLogging


object Main : KLogging() {

    @JvmStatic
    fun main(args: Array<String>) {
        // Create a client based on DOCKER_HOST and DOCKER_CERT_PATH env vars
        val docker = DefaultDockerClient.fromEnv().build()

        // Pull an image
        docker.pull("alpine:3.7")

//     HostConfig.Bind container ports to host ports
        val ports = arrayOf("80", "22")
        val portBindings = HashMap<String, List<PortBinding>>()
        for (port in ports) {
            val hostPorts = ArrayList<PortBinding>()
            hostPorts.add(PortBinding.of("0.0.0.0", port))
            portBindings[port] = hostPorts
        }

        // Bind container port 443 to an automatically allocated available host port.
        val randomPort = ArrayList<PortBinding>()
        randomPort.add(PortBinding.randomPort("0.0.0.0"))
        portBindings["443"] = randomPort

        val hostConfig = HostConfig.builder().portBindings(portBindings).build()

        // Create container with exposed ports
        val containerConfig = ContainerConfig.builder()
            .hostConfig(hostConfig)
            .image("alpine:3.7").exposedPorts(*ports)
            .cmd("sh", "-c", "echo Hello World from docker!! ; while :; do sleep 1; echo Hop ; done")
            .build()

        val creation = docker.createContainer(containerConfig)
        val id = creation.id()


        // Start container
        docker.startContainer(id)

        // Exec command inside running container with attached STDOUT and STDERR
//    val command = arrayOf("sh", "-c", "ls")
//    val execCreation = docker.execCreate(
//        id, command, DockerClient.ExecCreateParam.attachStdout(),
//        DockerClient.ExecCreateParam.attachStderr()
//    )
//
//    docker.execStart(execCreation.id()).attach(System.out, System.err)
//    val execOutput = output.readFully()
//    val loger = LoggingOutputStream(A.logger.underlyingLogger, Level.INFO)
//    docker.attachContainer(id, DockerClient.AttachParameter.LOGS).attach(loger, System.err)
//    docker.logs(id).use { it.attach(loger, loger) }

        docker.logs(id, LogsParam.stdout(), LogsParam.stderr()).use { stream -> logger.info { stream.readFully() } }

//    docker.updateContainer(id, HostConfig)

        readLine()

        docker.logs(id, LogsParam.stdout(), LogsParam.stderr()).use { stream -> logger.info { stream.readFully() } }
        // Kill container
        docker.killContainer(id)

        // Remove container
        docker.removeContainer(id)


        // Close the docker client
        docker.close()
    }
}

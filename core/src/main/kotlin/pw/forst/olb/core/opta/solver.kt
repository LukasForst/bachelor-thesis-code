package pw.forst.olb.core.opta

import org.optaplanner.core.api.solver.SolverFactory
import pw.forst.olb.core.opta.domain.Assignment
import pw.forst.olb.core.opta.domain.Job
import pw.forst.olb.core.opta.domain.Plan
import pw.forst.olb.core.opta.domain.ResourcesStack
import pw.forst.olb.core.opta.domain.Time
import pw.forst.olb.core.opta.domain.getPure

fun main(args: Array<String>) {
    // Build the Solver
    val solverFactory = SolverFactory.createFromXmlResource<Plan>(
        "solverConfiguration.xml"
    )
    val solver = solverFactory.buildSolver()

    val result = solver.solve(getPlan()).getPure()

    println(result)
}

fun getPlan(): Plan =
    Plan(
        id = "default",
        jobDomain = getJobs(),
        resourcesStackDomain = getReousources(),
        times = getTime(),
        assignments = getAssignments(getJobs().size * getReousources().size * getTime().size)
    )

fun getAssignments(count: Int) = (0 until count).map { Assignment(id = it.toString()) }

fun getJobs() = listOf(
    Job(
        id = 1L,
        maxTime = 100L,
        maxPrice = 100.0,
        minCpu = 1.0
    ),
    Job(
        id = 2L,
        maxTime = 100L,
        maxPrice = 100.0,
        minCpu = 2.0
    ),
    Job(
        id = 3L,
        maxTime = 100L,
        maxPrice = 100.0,
        minCpu = 3.0
    )
)

fun getTime() = (0L until 500L step Time.periodDuration).map { Time(position = it) }

fun getReousources(): List<ResourcesStack> = listOf(
    ResourcesStack(
        id = 1L,
        cpus = 5.0
    ),
    ResourcesStack(
        id = 2L,
        cpus = 3.0
    )
)

//package pw.forst.olb.simulation.execution
//
//import pw.forst.olb.common.dto.AllocationPlanWithHistory
//import pw.forst.olb.common.dto.SchedulingProperties
//import pw.forst.olb.common.dto.Time
//import pw.forst.olb.common.dto.impl.SchedulingPropertiesImpl
//import pw.forst.olb.core.api.InputToDomainConverter
//import pw.forst.olb.core.api.OlbCoreApi
//import pw.forst.olb.core.api.OlbCoreApiImpl
//import pw.forst.olb.core.solver.OptaplannerSolverFactory
//import pw.forst.olb.simulation.input.DomainBuilder
//import pw.forst.olb.simulation.input.data.DataParser
//import pw.forst.olb.simulation.input.data.JobWithHistoryFactory
//
//class PlanEnhancementExecutor {
//    fun run(folderPath: String): AllocationPlanWithHistory {
//        val planFromFolder = buildDomainBuilder().build(folderPath)
//        val api = buildApi()
//
//        val properties = generateProperties()
//
//
//        api.enhancePlan(planFromFolder,)
//    }
//
//
//    private fun generateProperties(currentPlan: AllocationPlanWithHistory, jobsCount: Int, planningHorizon: Long, runningTime: Time, cores: Int? = null): SchedulingProperties =
//        SchedulingPropertiesImpl()
//
//    private fun buildDomainBuilder(): DomainBuilder = DomainBuilder(DataParser(), JobWithHistoryFactory())
//
//    private fun buildApi(): OlbCoreApi = OlbCoreApiImpl(InputToDomainConverter(), OptaplannerSolverFactory(), true)
//}

package pw.forst.olb.core.opta.domain

import org.optaplanner.core.api.domain.entity.PlanningEntity
import org.optaplanner.core.api.domain.variable.PlanningVariable

@PlanningEntity
data class Assignment(

    var id: String = "",

    @field:PlanningVariable(valueRangeProviderRefs = ["jobRange"]) var job: Job? = null,

    @field:PlanningVariable(valueRangeProviderRefs = ["resourcesRange"]) var resourcesStack: ResourcesStack? = null,

    @field:PlanningVariable(valueRangeProviderRefs = ["timeRange"]) var time: Time? = null
) {
    val isValid: Boolean
        get() = job != null && resourcesStack != null && time != null
}

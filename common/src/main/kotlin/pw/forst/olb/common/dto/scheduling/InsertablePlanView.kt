package pw.forst.olb.common.dto.scheduling

import pw.forst.olb.common.dto.Time

interface InsertablePlanView : PlanView {

    fun insert(time: Time, view: TimePlanView): InsertablePlanView
}

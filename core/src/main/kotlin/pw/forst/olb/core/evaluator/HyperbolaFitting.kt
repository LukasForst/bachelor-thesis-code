package pw.forst.olb.core.evaluator

import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.job.JobValue
import pw.forst.olb.common.dto.job.impl.JobValueImpl
import pw.forst.olb.common.dto.resources.ResourcesAllocation
import pw.forst.olb.common.dto.scheduling.JobPlanView
import pw.forst.olb.core.evaluator.leastsquares.HyperbolaFitter

class HyperbolaFitting(
    private val initialParameters: DoubleArray? = null
) : JobAssignmentValueProvider {

    override fun jobAssignmentValue(jobView: JobPlanView, allocation: ResourcesAllocation, time: Time): JobValue? {
        val fitter = if (initialParameters != null) HyperbolaFitter(initialParameters) else HyperbolaFitter()
        val xs = mutableListOf<Double>()
        val ys = mutableListOf<Double>()

        jobView.values.forEach { (time, value) -> xs.add(time.seconds.toDouble()); ys.add(value.value) }

        return fitter.predict(xs, ys, time.seconds.toDouble())?.let { JobValueImpl(it) }
    }

}

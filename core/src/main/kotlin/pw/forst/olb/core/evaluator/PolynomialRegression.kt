package pw.forst.olb.core.evaluator

import org.apache.commons.math3.fitting.PolynomialCurveFitter
import org.apache.commons.math3.fitting.WeightedObservedPoints
import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.job.JobValue
import pw.forst.olb.common.dto.resources.ResourcesAllocation
import pw.forst.olb.common.dto.scheduling.JobPlanView


class PolynomialRegression : JobAssignmentValueProvider {
    override fun jobAssignmentValue(jobView: JobPlanView, allocation: ResourcesAllocation, time: Time): JobValue? {
        val polynomialCurveFitter = PolynomialCurveFitter.create(2)
        val points = WeightedObservedPoints()
        jobView.values.forEach { (time, value) -> points.add(time.seconds.toDouble(), value.value) }

        val values = polynomialCurveFitter.fit(points.toList())
        TODO()
    }

}

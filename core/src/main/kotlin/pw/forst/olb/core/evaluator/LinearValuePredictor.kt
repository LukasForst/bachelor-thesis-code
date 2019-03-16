package pw.forst.olb.core.evaluator

import mu.KLogging
import org.apache.commons.math3.stat.regression.SimpleRegression
import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.job.JobValue
import pw.forst.olb.common.dto.job.impl.JobValueImpl
import pw.forst.olb.common.dto.resources.ResourcesAllocation
import pw.forst.olb.common.dto.scheduling.JobPlanView

class LinearValuePredictor : JobAssignmentValueProvider {

    private companion object : KLogging()

    override fun jobAssignmentValue(jobView: JobPlanView, allocation: ResourcesAllocation, time: Time): JobValue? {
        if (jobView.totalSchedulingTime == Time.zero) return null.also { logger.info { "No data to predict!" } }

        val regression = SimpleRegression()
        jobView.values.forEach { (time, value) -> regression.addData(time.seconds.toDouble(), value.value) }
        val predicted = regression.predict(time.seconds.toDouble())
        if (predicted == Double.NaN) return null.also { logger.info { "It was not possible to create prediction!" } }

        return JobValueImpl(predicted)
    }
}

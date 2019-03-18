package pw.forst.olb.common.dto.job

import pw.forst.olb.common.dto.Time

interface JobMetaData {

    val job: Job

    val iterationTime: Map<Time, Collection<Iteration>>

    val values: Map<Iteration, JobValue>
}

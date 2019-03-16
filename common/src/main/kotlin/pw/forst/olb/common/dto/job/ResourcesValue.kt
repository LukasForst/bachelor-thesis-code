package pw.forst.olb.common.dto.job

import pw.forst.olb.common.dto.Time

interface ResourcesValue {

    val data: Map<Time, JobValue>
}

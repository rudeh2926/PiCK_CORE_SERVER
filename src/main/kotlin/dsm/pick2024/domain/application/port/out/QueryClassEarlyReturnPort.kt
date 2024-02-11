package dsm.pick2024.domain.application.port.out

import dsm.pick2024.domain.application.domain.Application
import dsm.pick2024.domain.application.domain.EarlyReturn

interface QueryClassEarlyReturnPort {
    fun findByGradeAndClassNum(grade: Int, classNum: Int): List<EarlyReturn>
}

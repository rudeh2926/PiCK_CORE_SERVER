package dsm.pick2024.domain.earlyreturn.presentation.dto.response

import dsm.pick2024.domain.main.Main
import java.time.LocalTime
import java.util.*

data class QuerySimpleMyEarlyResponse(
    val userId: UUID,
    val username: String,
    val startTime: LocalTime,
    val type: Main
)

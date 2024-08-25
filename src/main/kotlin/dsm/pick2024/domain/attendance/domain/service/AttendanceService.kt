package dsm.pick2024.domain.attendance.domain.service

import dsm.pick2024.domain.application.enums.ApplicationType
import dsm.pick2024.domain.attendance.domain.Attendance
import dsm.pick2024.domain.attendance.enums.AttendanceStatus
import org.springframework.stereotype.Component
import java.time.LocalTime

@Component
class AttendanceService {

    companion object {
        val period6 = LocalTime.of(14, 30) to LocalTime.of(15, 30) // 6교시
        val period7 = LocalTime.of(15, 30) to LocalTime.of(16, 30) // 7교시
        val period8 = LocalTime.of(16, 40) to LocalTime.of(18, 30) // 8교시
        val period9 = LocalTime.of(18, 40) to LocalTime.of(19, 30) // 9교시
        val period10 = LocalTime.of(19, 40) to LocalTime.of(20, 30) // 10교시

        val periods = listOf(period6, period7, period8, period9, period10)
        val periodNames = listOf("6교시", "7교시", "8교시", "9교시", "10교시")
        val periodMap = periodNames.zip(periods).toMap()
    }

    // 교시 혹은 시간을 기반으로 교시 목록을 반환하는 함수
    fun translateApplication(start: String, end: String?, applicationType: ApplicationType): List<String> {
        return when (applicationType) {
            ApplicationType.PERIOD -> listOf(start, end!!)
            ApplicationType.TIME -> {
                val startTime = LocalTime.parse(start)
                val endTime = end?.let { LocalTime.parse(it) }
                getMatchingPeriods(startTime, endTime)
            }
        }
    }

    // 주어진 교시 혹은 시간에 해당하는 출석 상태를 업데이트하는 함수
    fun updateAttendance(
        start: String,
        end: String,
        applicationType: ApplicationType,
        attendance: Attendance
    ): Attendance {
        val matchingPeriods = when (applicationType) {
            ApplicationType.PERIOD -> {
                val startIndex = periodNames.indexOf(start)
                val endIndex = periodNames.indexOf(end)
                (startIndex.takeIf { it != -1 } to endIndex.takeIf { it != -1 })
                    .takeIf { it.first != null && it.second != null && it.first!! <= it.second!! }
                    ?.let { periods.subList(it.first!!, it.second!! + 1) } ?: emptyList()
            }
            ApplicationType.TIME -> {
                val startTime = LocalTime.parse(start)
                val endTime = LocalTime.parse(end)
                periods.filter { startTime <= it.second && endTime >= it.first }
            }
        }

        var updatedAttendance = attendance

        matchingPeriods.forEach { period ->
            updatedAttendance = when (period) {
                period6 -> updatedAttendance.copy(period6 = AttendanceStatus.GO_OUT)
                period7 -> updatedAttendance.copy(period7 = AttendanceStatus.GO_OUT)
                period8 -> updatedAttendance.copy(period8 = AttendanceStatus.GO_OUT)
                period9 -> updatedAttendance.copy(period9 = AttendanceStatus.GO_OUT)
                period10 -> updatedAttendance.copy(period10 = AttendanceStatus.GO_OUT)
                else -> updatedAttendance
            }
        }

        return updatedAttendance
    }

    private fun getMatchingPeriods(startTime: LocalTime, endTime: LocalTime?): List<String> {
        return periods
            .mapIndexed { index, period ->
                if (startTime <= period.second && (endTime == null || endTime >= period.first)) {
                    periodNames[index]
                } else {
                    null
                }
            }
            .filterNotNull()
    }
}

package dsm.pick2024.domain.classroom.service

import dsm.pick2024.domain.attendance.enums.AttendanceStatus
import dsm.pick2024.domain.application.enums.Status.NO
import dsm.pick2024.domain.application.enums.Status.OK
import dsm.pick2024.domain.event.dto.ChangeStatusRequest
import dsm.pick2024.domain.attendance.domain.Attendance
import dsm.pick2024.domain.attendance.port.out.QueryAttendancePort
import dsm.pick2024.domain.attendance.port.out.SaveAttendancePort
import dsm.pick2024.domain.classroom.domain.Classroom
import dsm.pick2024.domain.classroom.exception.ClassroomNotFoundException
import dsm.pick2024.domain.classroom.port.`in`.ChangeClassroomStatusUseCase
import dsm.pick2024.domain.classroom.port.out.DeleteClassRoomPort
import dsm.pick2024.domain.classroom.port.out.QueryClassroomPort
import dsm.pick2024.domain.classroom.port.out.SaveClassRoomPort
import dsm.pick2024.domain.classroom.presentation.dto.request.ClassroomStatusRequest
import dsm.pick2024.domain.user.exception.UserNotFoundException
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ChangeClassroomStatusService(
    private val queryClassroomPort: QueryClassroomPort,
    private val deleteClassRoomPort: DeleteClassRoomPort,
    private val saveClassRoomPort: SaveClassRoomPort,
    private val queryAttendancePort: QueryAttendancePort,
    private val saveAttendancePort: SaveAttendancePort,
    private val eventPublisher: ApplicationEventPublisher
) : ChangeClassroomStatusUseCase {
    @Transactional
    override fun changeClassroomStatus(request: ClassroomStatusRequest) {
        if (request.status == NO) {
            for (id in request.idList) {
                val classroom = queryClassroomPort.findByUserId(id) ?: throw ClassroomNotFoundException
                deleteClassRoomPort.deleteByUserId(classroom.userId)
            }
            eventPublisher.publishEvent(ChangeStatusRequest(this, request.idList))
        } else {
            val update = mutableListOf<Classroom>()
            val updateAttendanceList = mutableListOf<Attendance>()

            request.idList.forEach { id ->
                val classroom = queryClassroomPort.findByUserId(id) ?: throw ClassroomNotFoundException

                val updatedClassroom = classroom.copy(status = OK)
                update.add(updatedClassroom)

                val updatedAttendance = queryAttendancePort.findByUserId(classroom.userId)?.run {
                    copy(
                        period6 = getStatus(classroom, period6, 6),
                        period7 = getStatus(classroom, period7, 7),
                        period8 = getStatus(classroom, period8, 8),
                        period9 = getStatus(classroom, period9, 9),
                        period10 = getStatus(classroom, period10, 10)
                    )
                } ?: throw UserNotFoundException

                updateAttendanceList.add(updatedAttendance)
            }

            saveClassRoomPort.saveAll(update)
            saveAttendancePort.saveAll(updateAttendanceList)
            eventPublisher.publishEvent(ChangeStatusRequest(this, updateAttendanceList.map { it.userId }))
        }
    }

    private fun getStatus(
        classroom: Classroom,
        status: AttendanceStatus,
        period: Int
    ): AttendanceStatus {
        return if (period in classroom.startPeriod..classroom.endPeriod) {
            AttendanceStatus.MOVEMENT
        } else {
            status
        }
    }
}

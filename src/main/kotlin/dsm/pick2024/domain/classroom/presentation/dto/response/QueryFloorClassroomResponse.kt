package dsm.pick2024.domain.classroom.presentation.dto.response

data class QueryFloorClassroomResponse(
    val name: String,
    val classroomName: String,
    val move: String,
    val grade: Int,
    val classNum: Int,
    val num: Int
)

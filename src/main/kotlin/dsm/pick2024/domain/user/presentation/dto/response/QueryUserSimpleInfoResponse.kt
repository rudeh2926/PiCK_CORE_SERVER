package dsm.pick2024.domain.user.presentation.dto.response

data class QueryUserSimpleInfoResponse (
    val name: String,
    val grade: Int,
    val classNum: Int,
    val num: Int,
)

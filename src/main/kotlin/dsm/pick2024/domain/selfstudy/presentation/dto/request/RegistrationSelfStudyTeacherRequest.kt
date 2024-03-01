package dsm.pick2024.domain.selfstudy.presentation.dto.request

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import javax.validation.constraints.NotBlank

data class RegistrationSelfStudyTeacherRequest(
    @NotBlank
    val floor: Int,
    @NotBlank
    val teacher: String,
    @NotBlank
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    val date: LocalDate
)

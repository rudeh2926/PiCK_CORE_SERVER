package dsm.pick2024.domain.applicationstory.presentation

import dsm.pick2024.domain.applicationstory.port.`in`.QueryUserApplicationUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@Tag(name = "application story API")
@RestController
@RequestMapping("/story")
class ApplicationStoryController(
    private val queryUserApplicationUseCase: QueryUserApplicationUseCase
) {

    @Operation(summary = "외출기록 조회 기능")
    @GetMapping("/query/{userId}")
    fun queryUserApplicationStory(@PathVariable(name = "userId") userId: UUID) =
        queryUserApplicationUseCase.queryUserApplicationStory(userId)
}

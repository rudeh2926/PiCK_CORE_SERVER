package dsm.pick2024.domain.selfstudy.persistence

import dsm.pick2024.domain.selfstudy.domain.SelfStudy
import dsm.pick2024.domain.selfstudy.mapper.SelfStudyMapper
import dsm.pick2024.domain.selfstudy.persistence.repository.SelfStudyRepository
import dsm.pick2024.domain.selfstudy.port.out.SelfStudyPort
import dsm.pick2024.domain.selfstudy.port.out.SelfStudySavePort
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class SelfStudyPersistenceAdapter (
    private val selfStudyRepository: SelfStudyRepository,
    private val selfStudyMapper: SelfStudyMapper
): SelfStudyPort {

    override fun save(selfStudy: SelfStudy) {
        selfStudyRepository.save(selfStudyMapper.toEntity(selfStudy))
    }

    override fun findByDate(date: LocalDate) =
        selfStudyRepository.findByDate(date).let { selfStudyMapper.toDomain(it) }
}

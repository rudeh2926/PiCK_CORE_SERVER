package dsm.pick2024.domain.timetable.peristence

import com.querydsl.jpa.impl.JPAQueryFactory
import dsm.pick2024.domain.timetable.domain.Timetable
import dsm.pick2024.domain.timetable.entity.QTimetableJpaEntity
import dsm.pick2024.domain.timetable.mapper.TimetableMapper
import dsm.pick2024.domain.timetable.peristence.repository.TimetableRepository
import dsm.pick2024.domain.timetable.port.out.TimetablePort
import java.util.UUID
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class TimetablePersistenceAdapter(
    private val timetableMapper: TimetableMapper,
    private val timetableRepository: TimetableRepository,
    private val jpaQueryFactory: JPAQueryFactory

) : TimetablePort {
    override fun save(timetable: Timetable) {
        timetableRepository.save(timetableMapper.toEntity(timetable))
    }

    override fun deleteAll() = timetableRepository.deleteAll()

    override fun saveAll(timetables: MutableList<Timetable>) {
        val entities = timetables.map { timetableMapper.toEntity(it) }
        timetableRepository.saveAll(entities)
    }

    override fun findById(id: UUID) =
        timetableRepository.findByIdOrNull(id).let { timetableMapper.toDomain(it!!) }

    override fun findTimetableByDayWeekPort(dayWeek: Int, grade: Int, classNum: Int): List<Timetable> {
        return jpaQueryFactory
            .selectFrom(QTimetableJpaEntity.timetableJpaEntity)
            .where(QTimetableJpaEntity.timetableJpaEntity.grade.eq(grade))
            .where(QTimetableJpaEntity.timetableJpaEntity.classNum.eq(classNum))
            .where(QTimetableJpaEntity.timetableJpaEntity.dayWeek.eq(dayWeek))
            .fetch()
            .map { timetableMapper.toDomain(it) }
    }

    override fun findByDayOfWeekAndPeriodAndGradeAndClassNum(
        dayWeek: Int,
        period: Int,
        grade: Int,
        classNum: Int
    ) = timetableRepository.findByDayWeekAndPeriodAndGradeAndClassNum(dayWeek, period, grade, classNum).let {
        timetableMapper.toDomain(
            it
        )
    }
}

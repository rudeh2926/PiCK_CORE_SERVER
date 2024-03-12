package dsm.pick2024.domain.meal.service

import dsm.pick2024.domain.meal.port.`in`.QueryDayMealUseCase
import dsm.pick2024.domain.meal.port.out.FindMealsByMealDatePort
import dsm.pick2024.domain.meal.presentation.dto.response.MealDetailsResponse
import dsm.pick2024.domain.meal.presentation.dto.response.MealResponse
import java.time.LocalDate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class QueryDayMealService(
    private val findMealsByMealDatePort: FindMealsByMealDatePort
) : QueryDayMealUseCase {

    @Transactional(readOnly = true)
    override fun queryDayMeal(date: LocalDate): MealDetailsResponse? {
        val meals = findMealsByMealDatePort.findMealsByMealDate(date)

        return meals?.let { mealList ->
            val mealResponse = MealResponse(
                breakfast = mealList.map { it.toSplit(it.breakfast).toString() },
                lunch = mealList.map { it.toSplit(it.lunch).toString() },
                dinner = mealList.map { it.toSplit(it.dinner).toString() }
            )
            MealDetailsResponse(date, mealResponse)
        }
    }
}

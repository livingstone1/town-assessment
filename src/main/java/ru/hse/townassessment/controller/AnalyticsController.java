package ru.hse.townassessment.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.townassessment.model.AlgorithmEnum;
import ru.hse.townassessment.model.AnalyticsResponse;
import ru.hse.townassessment.service.AnalyticsService;

import java.util.List;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AnalyticsController {

    /** Сервис аналитических расчетов. */
    AnalyticsService analyticsService;

    /**
     * Получить полные аналитические данные по актуальной экспертной оценке городов по алгоритмам.
     */
    @GetMapping
    public List<AnalyticsResponse> analyze(@RequestParam(required = false) AlgorithmEnum algorithm) {
        return analyticsService.analyze(algorithm);
    }
}

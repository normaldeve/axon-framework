package com.junwoo.axonstudy.queries;

import com.junwoo.axonstudy.entity.Elephant;
import com.junwoo.axonstudy.repository.ElephantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 5.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ElephantQueryHandler {
    private final ElephantRepository elephantRepository;

    @QueryHandler
    private Elephant getElephant(GetElephantQuery query) {
        Optional<Elephant> optionalElephant = elephantRepository.findById(query.getId());

        return optionalElephant.orElse(null);
    }

    @QueryHandler(queryName = "list")
    private List<Elephant> getElephants() {
        Optional<List<Elephant>> optionalElephants = Optional.of(elephantRepository.findAll());

        return optionalElephants.orElse(null);
    }

}

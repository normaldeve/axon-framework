package com.junwoo.axonstudy.events;

import com.junwoo.axonstudy.command.BackToReadyCommand;
import com.junwoo.axonstudy.dto.StatusEnum;
import com.junwoo.axonstudy.entity.Elephant;
import com.junwoo.axonstudy.repository.ElephantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.AllowReplay;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 5.
 */
@Slf4j
@Component
@ProcessingGroup("elephant")
@AllowReplay
@RequiredArgsConstructor
public class ElephantEventHandler {

    private final ElephantRepository elephantRepository;
    private final transient EventGateway eventGateway;
    private final transient CommandGateway commandGateway;

    @EventHandler
    private void on(CreatedElephantEvent event) {
        log.info("[@EventHandler] CreatedElephantEvent for Id: {}", event.getId());

        Elephant elephant = new Elephant();
        elephant.setId(event.getId());
        elephant.setName(event.getName());
        elephant.setWeight(event.getWeight());
        elephant.setStatus(event.getStatus());

        try {
            elephantRepository.save(elephant);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    @EventHandler
    private void on(EnteredElephantEvent event) {
        log.info("[@EventHandler] EnteredElephantEvent for Id: {}", event.getId());

        Elephant elephant = getEntity(event.getId());
        if (elephant != null) {
            elephant.setStatus(event.getStatus());
            elephantRepository.save(elephant);

            if (elephant.getWeight() > 100) {
                log.info("==== 100kg 넘어서 담을 수 없습니다! 실패 이벤트를 발송합니다");
                eventGateway.publish(new FailedEnterElephantEvent(event.getId()));
                return;
            }
        }
    }

    @EventHandler
    private void on(ExitedElephantEvent event) {
        log.info("[@EventHandler] ExitedElephantEvent for Id: {}", event.getId());

        Elephant elephant = getEntity(event.getId());
        if (elephant != null) {
            elephant.setStatus(event.getStatus());
            elephantRepository.save(elephant);
        }
    }

    @EventHandler
    private void on(FailedEnterElephantEvent event) {
        log.info("[@EventHandler] FailedEnterElephantEvent for Id: {}", event.getId());

        commandGateway.send(BackToReadyCommand.builder()
                .id(event.getId())
                .status(StatusEnum.READY.value())
                .build());
    }

    @EventHandler
    private void on(BackToReadyCompletedEvent event) {
        log.info("[@EventHandler] BackToReadyCompletedEvent for Id: {}", event.getId());

        Elephant elephant = getEntity(event.getId());
        if (elephant != null) {
            elephant.setStatus(event.getStatus());
            elephantRepository.save(elephant);
        }
    }

    private Elephant getEntity(String id) {
        Optional<Elephant> optionalElephant = elephantRepository.findById(id);

        return optionalElephant.orElse(null);
    }

    @ResetHandler
    private void replayAll() {
        log.info("[@ResetHandler] Executing replayAll");
        elephantRepository.deleteAll();
    }

}

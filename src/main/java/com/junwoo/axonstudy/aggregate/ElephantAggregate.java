package com.junwoo.axonstudy.aggregate;

import com.junwoo.axonstudy.command.BackToReadyCommand;
import com.junwoo.axonstudy.command.CreateElephantCommand;
import com.junwoo.axonstudy.command.EnterElephantCommand;
import com.junwoo.axonstudy.command.ExitElephantCommand;
import com.junwoo.axonstudy.events.BackToReadyCompletedEvent;
import com.junwoo.axonstudy.events.CreatedElephantEvent;
import com.junwoo.axonstudy.events.EnteredElephantEvent;
import com.junwoo.axonstudy.events.ExitedElephantEvent;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.modelling.command.AggregateMember;
import org.axonframework.spring.stereotype.Aggregate;
import org.checkerframework.checker.units.qual.C;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 5.
 */
@Slf4j
@Aggregate
@NoArgsConstructor
public class ElephantAggregate {

    @AggregateIdentifier
    private String id;

    @AggregateMember
    private String name;

    @AggregateMember
    private int weight;

    @AggregateMember
    private String status;

    @CommandHandler
    private ElephantAggregate(CreateElephantCommand cmd) {
        log.info("[@CommandHandler] CreateElephantCommand for Id: {}", cmd.getId());

        CreatedElephantEvent event = new CreatedElephantEvent(
                cmd.getId(), cmd.getName(), cmd.getWeight(), cmd.getStatus()
        );

        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    private void handle(EnterElephantCommand cmd) {
        log.info("[@CommandHandler] EnterElephantCommand for Id: {}", cmd.getId());

        AggregateLifecycle.apply(new EnteredElephantEvent(cmd.getId(), cmd.getStatus()));
    }

    @CommandHandler
    private void handle(ExitElephantCommand cmd) {
        log.info("[@CommandHandler] ExitElephantCommand for Id: {}", cmd.getId());

        AggregateLifecycle.apply(new ExitedElephantEvent(cmd.getId(), cmd.getStatus()));
    }

    @CommandHandler
    private void handle(BackToReadyCommand cmd) {
        log.info("[@CommandHandler] BackToReadyCommand for Id: {}", cmd.getId());

        AggregateLifecycle.apply(new BackToReadyCompletedEvent(cmd.getId(), cmd.getStatus()));
    }

    @EventSourcingHandler
    private void on(CreatedElephantEvent event) {
        log.info("[@EventSourcingHandler] CreatedElephantEvent for Id: {}", event.getId());

        this.id = event.getId();
        this.name = event.getName();
        this.weight = event.getWeight();
        this.status = event.getStatus();
    }

    @EventSourcingHandler
    private void on(EnteredElephantEvent event) {
        log.info("[@EventSourcingHandler] EnteredElephantEvent for Id: {}", event.getId());

        log.info("======== [넣기] Event Replay => 코끼리 상태: {}", this.status);

        this.status = event.getStatus();

        log.info("======== [넣기] 최종 코끼리 상태: {}", this.status);
    }

    @EventSourcingHandler
    private void on(ExitedElephantEvent event) {
        log.info("[@EventSourcingHandler] ExitedElephantEvent for Id: {}", event.getId());

        log.info("======== [꺼내기] Event Replay => 코끼리 상태: {}", this.status);

        this.status = event.getStatus();

        log.info("======== [꺼내기] 최종 코끼리 상태: {}", this.status);
    }

    @EventSourcingHandler
    private void on(BackToReadyCompletedEvent event) {
        log.info("[@EventSourcingHandler] BackToReadyCompletedEvent for Id: {}", event.getId());

        this.status = event.getStatus();
    }

}

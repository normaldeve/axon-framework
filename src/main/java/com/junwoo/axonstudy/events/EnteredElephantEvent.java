package com.junwoo.axonstudy.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 5.
 */
@Data
@AllArgsConstructor
public class EnteredElephantEvent {

    String id;

    String status;
}

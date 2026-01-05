package com.junwoo.axonstudy.command;

import lombok.Builder;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 5.
 */
@Value
@Builder
public class ExitElephantCommand {

    @TargetAggregateIdentifier
    String id;

    String status;
}

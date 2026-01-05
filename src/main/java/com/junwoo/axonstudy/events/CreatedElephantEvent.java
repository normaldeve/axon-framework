package com.junwoo.axonstudy.events;

import com.junwoo.axonstudy.aggregate.ElephantAggregate;
import com.junwoo.axonstudy.command.CreateElephantCommand;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.axonframework.commandhandling.CommandHandler;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 5.
 */
@Data
@AllArgsConstructor
public class CreatedElephantEvent {

    private String id;
    private String name;
    private int weight;
    private String status;

}

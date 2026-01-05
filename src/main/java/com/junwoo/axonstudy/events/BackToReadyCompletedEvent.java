package com.junwoo.axonstudy.events;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 5.
 */
@Data
@AllArgsConstructor
public class BackToReadyCompletedEvent {
    private String id;
    private String status;
}

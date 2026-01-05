package com.junwoo.axonstudy.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 5.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultVO<T> {
    private boolean resultCode;
    private String returnMessage;
    private T result;
}

package com.abomb4.rabbitdlx;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author abomb4 2020-01-03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeadLetterEntity {

    private String exchangeName;
    private String queueName;
    private String routingKey;
    private String body;
}

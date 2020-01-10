package com.abomb4.rabbitdlx.common.constants;

/**
 * @author yangrl14628 2020-01-02
 */
public interface MqHeaders {

    /** Dead Exchange name */
    String X_FIRST_DEATH_EXCHANGE = "x-first-death-exchange";

    /** Dead Queue name */
    String X_FIRST_DEATH_QUEUE = "x-first-death-queue";

    /** Dead erason */
    String X_FIRST_DEATH_REASON = "x-first-death-reason";
}

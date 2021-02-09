package com.soufianekre.smallhub.helper.rx;

import io.reactivex.Scheduler;

public interface SchedulerProvider {

    Scheduler io();
    Scheduler computation();
    Scheduler ui();
}

package com.nninjoon.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class TimedConfig {
	@Bean
	public TimedAspect timedAspect(MeterRegistry meterRegistry) {
		return new TimedAspect(meterRegistry);
	}
}

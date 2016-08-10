package org.devoware.homonculus.setup;

import java.lang.management.ManagementFactory;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import javax.validation.Validator;

import org.devoware.homonculus.lifecycle.Managed;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.JvmAttributeGaugeSet;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.jvm.BufferPoolMetricSet;
import com.codahale.metrics.jvm.ClassLoadingGaugeSet;
import com.codahale.metrics.jvm.FileDescriptorRatioGauge;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

public class Environment {
  private final PriorityQueue<Managed> managed =
      new PriorityQueue<Managed>(5, new Comparator<Managed>() {

        @Override
        public int compare(Managed lc1, Managed lc2) {
          return lc1.getPriority() - lc2.getPriority();
        }

      });
  private final ObjectMapper objectMapper;
  private final Validator validator;
  private final MetricRegistry metrics;
  private final HealthCheckRegistry healthCheckRegistry;

  public Environment(ObjectMapper objectMapper, Validator validator, MetricRegistry metrics, HealthCheckRegistry healthCheckRegistry) {
    this.objectMapper = objectMapper;
    this.validator = validator;
    this.metrics = metrics;
    this.healthCheckRegistry = healthCheckRegistry;
    initialize();
  }

  private void initialize() {
      metrics.register("jvm.attribute", new JvmAttributeGaugeSet());
      metrics.register("jvm.buffers", new BufferPoolMetricSet(ManagementFactory
                                                                             .getPlatformMBeanServer()));
      metrics.register("jvm.classloader", new ClassLoadingGaugeSet());
      metrics.register("jvm.filedescriptor", new FileDescriptorRatioGauge());
      metrics.register("jvm.gc", new GarbageCollectorMetricSet());
      metrics.register("jvm.memory", new MemoryUsageGaugeSet());
      metrics.register("jvm.threads", new ThreadStatesGaugeSet());

      JmxReporter.forRegistry(metrics).build().start();
  }

  public void manage(Managed managed) {
    this.managed.add(managed);
  }

  public List<Managed> getManagedResources() {
    return Lists.newArrayList(managed);
  }

  public ObjectMapper getObjectMapper() {
    return objectMapper;
  }

  public Validator getValidator() {
    return validator;
  }

  public MetricRegistry metrics() {
    return metrics;
  }

  public HealthCheckRegistry healthChecks() {
    return healthCheckRegistry;
  }
}

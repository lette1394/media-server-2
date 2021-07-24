package com.github.lette1394.mediaserver2.archrules;

import com.tngtech.archunit.core.domain.Dependency;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

public class DependOnSameOrLowerPackages extends ArchCondition<JavaClass> {
  private final String[] commonPackages;

  public DependOnSameOrLowerPackages(String... commonPackages) {
    super("depend on same or lower packages");
    this.commonPackages = commonPackages;
  }

  @Override
  public void check(final JavaClass clazz, final ConditionEvents events) {
    for (Dependency dependency : clazz.getDirectDependenciesFromSelf()) {
      boolean conditionSatisfied = isConditionSatisfied(dependency.getOriginClass(), dependency.getTargetClass());
      events.add(new SimpleConditionEvent(dependency, conditionSatisfied, dependency.getDescription()));
    }
  }

  private boolean isConditionSatisfied(JavaClass origin, JavaClass target) {
    String originPackageName = origin.getPackageName();
    String targetSubPackagePrefix = target.getPackageName() + ".";

    for (String commonPackage : commonPackages) {
      if (commonPackage.isEmpty()) {
        continue;
      }
      if (targetSubPackagePrefix.startsWith(commonPackage)) {
        return true;
      }
    }
    return targetSubPackagePrefix.startsWith(originPackageName);
  }
}

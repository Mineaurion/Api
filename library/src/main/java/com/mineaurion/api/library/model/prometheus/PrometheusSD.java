package com.mineaurion.api.library.model.prometheus;

import java.util.ArrayList;
import java.util.List;

public class PrometheusSD {
    private List<String> targets = new ArrayList<>();
    private Labels labels;

    public PrometheusSD(List<String> targets, Labels labels) {
        this.targets = targets;
        this.labels = labels;
    }

    public PrometheusSD(String target, Labels labels){
        this.targets.add(target);
        this.labels = labels;
    }

    public List<String> getTargets() {
        return targets;
    }

    public void setTargets(List<String> targets) {
        this.targets = targets;
    }

    public Labels getLabels() {
        return labels;
    }

    public void setLabels(Labels labels) {
        this.labels = labels;
    }

}

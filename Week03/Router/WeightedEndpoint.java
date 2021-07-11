package io.github.kimmking.gateway.router;

public class WeightedEndpoint {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getBegin() {
        return begin;
    }

    private int weight;
    private int begin;

    public void setBegin(int begin) {
        this.begin = begin;
    }


    public WeightedEndpoint (String name, int weigth) {
        this.name = name;
        this.weight = weigth;
    }

}

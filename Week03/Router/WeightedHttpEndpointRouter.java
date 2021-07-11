package io.github.kimmking.gateway.router;

import java.util.List;
import java.util.Random;

public class WeightedHttpEndpointRouter {

    private Random random = new Random();
    private int factor;
    private List<WeightedEndpoint> myUrls;


    public WeightedHttpEndpointRouter(List<WeightedEndpoint> urls) {
        factor = 0;
        this.myUrls = urls;
        for(WeightedEndpoint weightedEndpoint:myUrls) {
            weightedEndpoint.setBegin(factor);
            factor += weightedEndpoint.getWeight();
        }

    }

    public String route(List<WeightedEndpoint> urls) {

        WeightedEndpoint selected= null;
        int rand = random.nextInt(factor);
        for (int i = myUrls.size() - 1; i >= 0; i--) {
            WeightedEndpoint resource = myUrls.get(i);
            if (rand >= resource.getBegin()) {
                selected = resource;
                break;
            }
        }
        return selected.getName();
    }
}

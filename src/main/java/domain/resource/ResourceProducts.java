package domain.resource;

import java.util.List;

public class ResourceProducts {
    private final List<ResourceStack> baseProducts;
    private final List<ResourceStack> cityBonusProducts;

    public ResourceProducts(List<ResourceStack> baseProducts, List<ResourceStack> cityBonusProducts){
        this.baseProducts = List.copyOf(baseProducts);
        this.cityBonusProducts = List.copyOf(cityBonusProducts);
    }

    public List<ResourceStack> getTier1Products(){
        return baseProducts;
    }

    public List<ResourceStack> getTier2Products(){
        return cityBonusProducts;
    }
}

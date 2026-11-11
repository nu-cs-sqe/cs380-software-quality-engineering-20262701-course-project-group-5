package domain.resource;

import java.util.List;

public class ResourceProducts {
    private final List<ResourceStack> tier1Products;
    private final List<ResourceStack> tier2Products;

    public ResourceProducts(List<ResourceStack> tier1Products, List<ResourceStack> tier2Products){
        this.tier1Products = List.copyOf(tier1Products);
        this.tier2Products = List.copyOf(tier2Products);
    }

    public List<ResourceStack> getTier1Products(){
        return tier1Products;
    }

    public List<ResourceStack> getTier2Products(){
        return tier2Products;
    }
}

package domain.resource;

import java.util.List;

public class ResourceProducts {
    List<ResourceStack> tier1Products;
    List<ResourceStack> tier2Products;

    public ResourceProducts(List<ResourceStack> tier1Products, List<ResourceStack> tier2Products){
        this.tier1Products = tier1Products;
        this.tier2Products = tier2Products;
    }

    public List<ResourceStack> getTier1Products(){
        return tier1Products;
    }

    public List<ResourceStack> getTier2Products(){
        return tier2Products;
    }
}

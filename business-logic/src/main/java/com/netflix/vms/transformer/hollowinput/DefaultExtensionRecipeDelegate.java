package com.netflix.vms.transformer.hollowinput;

import com.netflix.hollow.objects.delegate.HollowObjectDelegate;

public interface DefaultExtensionRecipeDelegate extends HollowObjectDelegate {

    public int getRecipeNameOrdinal(int ordinal);

    public int getExtensionOrdinal(int ordinal);

    public DefaultExtensionRecipeTypeAPI getTypeAPI();

}
package com.netflix.vms.transformer.hollowinput;

import com.netflix.hollow.objects.delegate.HollowObjectDelegate;

public interface RolloutPhaseDelegate extends HollowObjectDelegate {

    public long getSeasonMovieId(int ordinal);

    public Long getSeasonMovieIdBoxed(int ordinal);

    public int getElementsOrdinal(int ordinal);

    public int getNameOrdinal(int ordinal);

    public boolean getShowCoreMetadata(int ordinal);

    public Boolean getShowCoreMetadataBoxed(int ordinal);

    public int getWindowsOrdinal(int ordinal);

    public int getPhaseTypeOrdinal(int ordinal);

    public boolean getOnHold(int ordinal);

    public Boolean getOnHoldBoxed(int ordinal);

    public RolloutPhaseTypeAPI getTypeAPI();

}
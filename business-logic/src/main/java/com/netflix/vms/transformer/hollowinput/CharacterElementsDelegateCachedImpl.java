package com.netflix.vms.transformer.hollowinput;

import com.netflix.hollow.objects.delegate.HollowObjectAbstractDelegate;
import com.netflix.hollow.read.dataaccess.HollowObjectTypeDataAccess;
import com.netflix.hollow.HollowObjectSchema;
import com.netflix.hollow.read.customapi.HollowTypeAPI;
import com.netflix.hollow.objects.delegate.HollowCachedDelegate;

public class CharacterElementsDelegateCachedImpl extends HollowObjectAbstractDelegate implements HollowCachedDelegate, CharacterElementsDelegate {

    private final int characterNameOrdinal;
    private final int bladeBottomLineOrdinal;
    private final int characterBioOrdinal;
    private final int bladeTopLineOrdinal;
   private CharacterElementsTypeAPI typeAPI;

    public CharacterElementsDelegateCachedImpl(CharacterElementsTypeAPI typeAPI, int ordinal) {
        this.characterNameOrdinal = typeAPI.getCharacterNameOrdinal(ordinal);
        this.bladeBottomLineOrdinal = typeAPI.getBladeBottomLineOrdinal(ordinal);
        this.characterBioOrdinal = typeAPI.getCharacterBioOrdinal(ordinal);
        this.bladeTopLineOrdinal = typeAPI.getBladeTopLineOrdinal(ordinal);
        this.typeAPI = typeAPI;
    }

    public int getCharacterNameOrdinal(int ordinal) {
        return characterNameOrdinal;
    }

    public int getBladeBottomLineOrdinal(int ordinal) {
        return bladeBottomLineOrdinal;
    }

    public int getCharacterBioOrdinal(int ordinal) {
        return characterBioOrdinal;
    }

    public int getBladeTopLineOrdinal(int ordinal) {
        return bladeTopLineOrdinal;
    }

    @Override
    public HollowObjectSchema getSchema() {
        return typeAPI.getTypeDataAccess().getSchema();
    }

    @Override
    public HollowObjectTypeDataAccess getTypeDataAccess() {
        return typeAPI.getTypeDataAccess();
    }

    public CharacterElementsTypeAPI getTypeAPI() {
        return typeAPI;
    }

    public void updateTypeAPI(HollowTypeAPI typeAPI) {
        this.typeAPI = (CharacterElementsTypeAPI) typeAPI;
    }

}
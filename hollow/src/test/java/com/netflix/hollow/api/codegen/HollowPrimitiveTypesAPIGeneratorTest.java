/*
 *  Copyright 2017 Netflix, Inc.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package com.netflix.hollow.api.codegen;

import com.netflix.hollow.core.write.objectmapper.HollowPrimaryKey;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;

public class HollowPrimitiveTypesAPIGeneratorTest extends AbstractHollowAPIGeneratorTest {

    @Override
    @Before
    public void setup() throws IOException {
        isCleanupAfterEnabled=false;
        sourceFolder="/Users/dsu/work/hollow/src";
    }

    @Override
    protected HollowAPIGenerator initGenerator(HollowAPIGenerator.Builder builder) {
        builder.withErgonomicShortcuts();
        builder.withPackageGrouping();
        builder.withHollowPrimitiveTypes(true);
        return super.initGenerator(builder);
    }

    @Test
    public void test() throws Exception {
        String apiClassName = "PrimitiveTypeTestAPI";
        String packageName = "codegen.primitive.types";
        runGenerator(apiClassName, packageName, Movie.class);
    }

    @HollowPrimaryKey(fields = { "id" })
    static class Movie {
        int i;
        long l;
        boolean b;
        float f;
        double d;

        // Native Types
        Integer iObj;
        Long lObj;
        Boolean bObj;
        Float fObj;
        Double dObj;
        String sObj;

        // Collections
        Map<String, Boolean> map;

        // Set
        Set<Long> setLong;
        Set<Integer> setInteger;
        Set<Float> setFloat;
        Set<Double> setDouble;
        Set<String> setString;
        Set<Boolean> setBoolean;

        // List
        List<Long> listLong;
        List<Integer> listInteger;
        List<Float> listFloat;
        List<Double> listDouble;
        List<String> listString;
        List<Boolean> listBoolean;
    }
}
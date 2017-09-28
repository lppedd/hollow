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
package com.netflix.hollow.api.codegen.indexes;

import com.netflix.hollow.api.codegen.HollowConsumerJavaFileGenerator;

public abstract class HollowIndexGenerator extends HollowConsumerJavaFileGenerator {
    public static final String SUB_PACKAGE_NAME = "index";

    protected final String apiClassname;
    protected final String classPostfix;
    protected final boolean useAggressiveSubstitutions;

    public HollowIndexGenerator(String packageName, String apiClassname, String classPostfix, boolean useAggressiveSubstitutions, boolean usePackageGrouping) {
        super(packageName, SUB_PACKAGE_NAME, usePackageGrouping);
        this.apiClassname = apiClassname;
        this.classPostfix = classPostfix;
        this.useAggressiveSubstitutions = useAggressiveSubstitutions;
    }
}
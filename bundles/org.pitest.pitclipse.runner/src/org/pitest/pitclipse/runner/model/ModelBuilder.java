/*******************************************************************************
 * Copyright 2012-2019 Phil Glover and contributors
 *  
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *  
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/

package org.pitest.pitclipse.runner.model;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.pitest.pitclipse.runner.PitResults;
import org.pitest.pitclipse.runner.results.DetectionStatus;
import org.pitest.pitclipse.runner.results.Mutations;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

public class ModelBuilder {

    private final ProjectStructureService eclipseStructureService;

    public ModelBuilder(ProjectStructureService jdtHelper) {
        this.eclipseStructureService = jdtHelper;
    }

    public MutationsModel buildFrom(PitResults results) {
        Mutations mutations = results.getMutations();
        List<String> projects = results.getProjects();

        List<Status> statuses = buildMutationModelFor(projects, mutations);
        return MutationsModel.make(statuses);
    }

    private List<Status> buildMutationModelFor(List<String> projects, Mutations mutations) {
        ImmutableList.Builder<Status> builder = ImmutableList.builder();
        for (DetectionStatus status : DetectionStatus.values()) {
            List<org.pitest.pitclipse.runner.results.Mutations.Mutation> mutationsForStatus = selectMutationsByStatus(
                    mutations, status);
            if (!mutationsForStatus.isEmpty()) {
                ImmutableList.Builder<ProjectMutations> projectMutations = ImmutableList.builder();
                for (String project : projects) {
                    ProjectMutations projectMutation = buildProjectMutation(project, mutationsForStatus);
                    if (!projectMutation.getPackageMutations().isEmpty()) {
                        projectMutations.add(projectMutation);
                    }
                }
                builder.add(Status.builder().withDetectionStatus(status).withProjectMutations(projectMutations.build())
                        .build());
            }
        }
        return builder.build();
    }

    private List<org.pitest.pitclipse.runner.results.Mutations.Mutation> selectMutationsByStatus(
            Mutations mutations, final DetectionStatus status) {
        return mutations.getMutation().stream()
                .filter(mutation -> status == mutation.getStatus())
                .collect(Collectors.toList());
    }

    private ProjectMutations buildProjectMutation(String project,
            List<org.pitest.pitclipse.runner.results.Mutations.Mutation> mutations) {
        List<PackageMutations> packages = buildPackageMutationsFor(project, mutations);
        return ProjectMutations.builder().withProjectName(project)
                .withPackageMutations(packages).build();
    }

    private List<PackageMutations> buildPackageMutationsFor(String project,
            List<org.pitest.pitclipse.runner.results.Mutations.Mutation> mutations) {
        List<ClassMutations> classMutations = buildClassMutationsFor(project, mutations);
        return packageMutationsFrom(project, classMutations);
    }

    private List<PackageMutations> packageMutationsFrom(final String project, List<ClassMutations> classMutations) {
        Multimap<String, ClassMutations> mutationsByPackage = Multimaps.index(classMutations,
                mutations -> eclipseStructureService.packageFrom(project, mutations.getClassName()));
        ImmutableList.Builder<PackageMutations> builder = ImmutableList.builder();
        for (String pkg : mutationsByPackage.keySet()) {
            builder.add(PackageMutations.builder().withPackageName(pkg).withClassMutations(mutationsByPackage.get(pkg))
                    .build());
        }
        return builder.build();
    }

    private List<ClassMutations> buildClassMutationsFor(final String project,
            List<org.pitest.pitclipse.runner.results.Mutations.Mutation> mutations) {
        Map<String, List<Mutation>> transformedMutations = mutations.stream()
            .filter(mutatedClass ->
                eclipseStructureService.isClassInProject(mutatedClass.getMutatedClass(), project))
            .collect(groupingBy(
                    Mutations.Mutation::getMutatedClass,
                    mapping(dtoMutation ->
                        Mutation.builder().withKillingTest(dtoMutation.getKillingTest())
                            .withLineNumber(dtoMutation.getLineNumber().intValue())
                            .withMutatedMethod(dtoMutation.getMutatedMethod())
                            .withMutator(dtoMutation.getMutator()).withStatus(dtoMutation.getStatus())
                            .withDescription(dtoMutation.getDescription()).build(),
                        Collectors.toList())));
        return classMutationsFrom(transformedMutations);
    }

    private List<ClassMutations> classMutationsFrom(Map<String, List<Mutation>> mutationsByClass) {
        List<ClassMutations> classMutations = new ArrayList<>();
        for (Entry<String, List<Mutation>> entry : mutationsByClass.entrySet()) {
            List<Mutation> sortedMutations = entry.getValue().stream()
                    .sorted(MutationSorter.INSTANCE)
                    .collect(Collectors.toList());
            classMutations.add(
                ClassMutations.builder()
                    .withClassName(entry.getKey())
                    .withMutations(sortedMutations).build());
        }
        return classMutations;
    }
}

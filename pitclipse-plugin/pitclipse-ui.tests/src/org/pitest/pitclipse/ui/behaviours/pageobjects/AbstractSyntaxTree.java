package org.pitest.pitclipse.ui.behaviours.pageobjects;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.pitest.pitclipse.ui.behaviours.StepException;

import com.google.common.collect.ImmutableList;

public class AbstractSyntaxTree {

	public void removeAllMethods(ClassContext context) {
		IJavaProject javaProject = getJavaProject(context);
		try {
			IProgressMonitor progressMonitor = new NullProgressMonitor();
			IType type = javaProject.findType(context
					.getFullyQualifiedTestClassName());
			for (IMethod method : type.getMethods()) {
				method.delete(true, progressMonitor);
			}
		} catch (JavaModelException e) {
			throw new StepException(e);
		}
	}

	private IJavaProject getJavaProject(ClassContext context) {
		return getJavaProject(context.getProjectName());
	}

	private IJavaProject getJavaProject(String projectName) {
		IProject project = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(projectName);
		return JavaCore.create(project);
	}

	public void addMethod(ConcreteClassContext context) {
		IJavaProject javaProject = getJavaProject(context);
		try {
			IType type = javaProject.findType(context
					.getFullyQualifiedTestClassName());
			NullProgressMonitor progressMonitor = new NullProgressMonitor();
			type.createMethod(context.getMethod(), null, false, progressMonitor);
		} catch (JavaModelException e) {
			throw new StepException(e);
		}
	}

	public void deleteProject(String projectName) {
		NullProgressMonitor progressMonitor = new NullProgressMonitor();
		IProject project = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(projectName);
		try {
			project.delete(true, progressMonitor);
		} catch (CoreException e) {
			throw new StepException(e);
		}
	}

	public void addJUnitToClassPath(String projectName) {
		IJavaProject project = getJavaProject(projectName);
		try {
			Path junitPath = new Path("org.eclipse.jdt.junit.JUNIT_CONTAINER/4");
			IClasspathEntry junitEntry = JavaCore.newContainerEntry(junitPath);
			IClasspathEntry junitClasspath = JavaCore
					.newContainerEntry(junitEntry.getPath());

			List<IClasspathEntry> entries = ImmutableList
					.<IClasspathEntry> builder().add(project.getRawClasspath())
					.add(junitClasspath).build();

			// add a new entry using the path to the container
			project.setRawClasspath(
					entries.toArray(new IClasspathEntry[entries.size()]), null);
		} catch (JavaModelException e) {
			throw new StepException(e);
		}
	}

}

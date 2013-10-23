package org.pitest.pitclipse.ui.view.mutations;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.pitest.pitclipse.pitrunner.model.ClassMutations;
import org.pitest.pitclipse.pitrunner.model.Mutation;
import org.pitest.pitclipse.pitrunner.model.MutationsModel;
import org.pitest.pitclipse.pitrunner.model.MutationsModelVisitor;
import org.pitest.pitclipse.pitrunner.model.PackageMutations;
import org.pitest.pitclipse.pitrunner.model.ProjectMutations;
import org.pitest.pitclipse.pitrunner.model.Status;
import org.pitest.pitclipse.pitrunner.model.Visitable;

public class ViewContentProvider implements ITreeContentProvider {

	@Override
	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public Object[] getElements(Object element) {
		if (element instanceof Visitable) {
			Visitable visitable = (Visitable) element;
			return visitable.accept(StructureVisitor.INSTANCE);
		}
		return nothing();
	}

	@Override
	public Object[] getChildren(Object element) {
		if (element instanceof Visitable) {
			Visitable visitable = (Visitable) element;
			return visitable.accept(StructureVisitor.INSTANCE);
		}
		return nothing();
	}

	@Override
	public Object getParent(Object element) {
		return nothing();
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof Visitable) {
			Visitable visitable = (Visitable) element;
			Object[] children = visitable.accept(StructureVisitor.INSTANCE);
			return children.length > 0;
		}
		return false;
	}

	private enum StructureVisitor implements MutationsModelVisitor<Object[]> {
		INSTANCE;

		@Override
		public Object[] visitModel(MutationsModel mutationsModel) {
			List<ProjectMutations> projectMutations = mutationsModel.getProjectMutations();
			return projectMutations.toArray();
		}

		@Override
		public Object[] visitProject(ProjectMutations projectMutations) {
			List<PackageMutations> packageMutations = projectMutations.getPackageMutations();
			return packageMutations.toArray();
		}

		@Override
		public Object[] visitPackage(PackageMutations packageMutations) {
			List<ClassMutations> classMutations = packageMutations.getClassMutations();
			return classMutations.toArray();
		}

		@Override
		public Object[] visitClass(ClassMutations classMutations) {
			List<Mutation> mutations = classMutations.getMutations();
			return mutations.toArray();
		}

		@Override
		public Object[] visitMutation(Mutation mutation) {
			return nothing();
		}

		@Override
		public Object[] visitStatus(Status status) {
			// TODO Auto-generated method stub
			return null;
		}
	}

	private static final Object[] nothing() {
		return new Object[0];
	}
}
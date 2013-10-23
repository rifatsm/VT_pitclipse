package org.pitest.pitclipse.ui.view.mutations;

import static org.pitest.pitclipse.pitrunner.results.DetectionStatus.KILLED;
import static org.pitest.pitclipse.pitrunner.results.DetectionStatus.MEMORY_ERROR;
import static org.pitest.pitclipse.pitrunner.results.DetectionStatus.NON_VIABLE;
import static org.pitest.pitclipse.pitrunner.results.DetectionStatus.NOT_STARTED;
import static org.pitest.pitclipse.pitrunner.results.DetectionStatus.RUN_ERROR;
import static org.pitest.pitclipse.pitrunner.results.DetectionStatus.STARTED;
import static org.pitest.pitclipse.pitrunner.results.DetectionStatus.TIMED_OUT;

import java.net.URL;
import java.util.Set;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE.SharedImages;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.pitest.pitclipse.pitrunner.model.ClassMutations;
import org.pitest.pitclipse.pitrunner.model.Mutation;
import org.pitest.pitclipse.pitrunner.model.MutationsModel;
import org.pitest.pitclipse.pitrunner.model.MutationsModelVisitor;
import org.pitest.pitclipse.pitrunner.model.PackageMutations;
import org.pitest.pitclipse.pitrunner.model.ProjectMutations;
import org.pitest.pitclipse.pitrunner.model.Status;
import org.pitest.pitclipse.pitrunner.model.Visitable;
import org.pitest.pitclipse.pitrunner.results.DetectionStatus;

import com.google.common.collect.Sets;

public class ViewLabelProvider extends LabelProvider {

	private static final Image MUTATION_DETECTED = getBundleImage("detected.gif");
	private static final Image MUTATION_NOT_DETECTED = getBundleImage("not_detected.gif");
	private static final Set<DetectionStatus> DETECTED_STATUSES = Sets.immutableEnumSet(KILLED, TIMED_OUT, NON_VIABLE,
			MEMORY_ERROR, NOT_STARTED, STARTED, RUN_ERROR);

	@Override
	public String getText(Object element) {
		if (element instanceof Visitable) {
			Visitable visitable = (Visitable) element;
			return visitable.accept(LabelVisitor.INSTANCE);
		}
		return "";
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof Visitable) {
			Visitable visitable = (Visitable) element;
			return visitable.accept(ImageVisitor.INSTANCE);
		}
		return null;
	}

	private static Image getBundleImage(String file) {
		Bundle bundle = FrameworkUtil.getBundle(ViewLabelProvider.class);
		URL url = FileLocator.find(bundle, new Path("icons/" + file), null);
		ImageDescriptor image = ImageDescriptor.createFromURL(url);
		return image.createImage();

	}

	private enum LabelVisitor implements MutationsModelVisitor<String> {
		INSTANCE;

		@Override
		public String visitModel(MutationsModel mutationsModel) {
			return "Mutations";
		}

		@Override
		public String visitProject(ProjectMutations projectMutations) {
			return projectMutations.getProjectName();
		}

		@Override
		public String visitPackage(PackageMutations packageMutations) {
			return packageMutations.getPackageName();
		}

		@Override
		public String visitClass(ClassMutations classMutations) {
			return classMutations.getClassName();
		}

		@Override
		public String visitMutation(Mutation mutation) {
			return Integer.toString(mutation.getLineNumber());
		}

		@Override
		public String visitStatus(Status status) {
			// TODO Auto-generated method stub
			return null;
		}
	}

	private enum ImageVisitor implements MutationsModelVisitor<Image> {

		INSTANCE;

		@Override
		public Image visitModel(MutationsModel mutationsModel) {
			return getPlatformIcon(ISharedImages.IMG_OBJ_ELEMENT);
		}

		@Override
		public Image visitProject(ProjectMutations projectMutations) {
			return getPlatformIcon(SharedImages.IMG_OBJ_PROJECT);
		}

		@Override
		public Image visitPackage(PackageMutations packageMutations) {
			return getJavaIcon(org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_PACKAGE);
		}

		@Override
		public Image visitClass(ClassMutations classMutations) {
			return getJavaIcon(org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_CLASS);
		}

		private Image getPlatformIcon(String icon) {
			return PlatformUI.getWorkbench().getSharedImages().getImage(icon);
		}

		private Image getJavaIcon(String icon) {
			return JavaUI.getSharedImages().getImage(icon);
		}

		@Override
		public Image visitMutation(Mutation mutation) {
			if (mutationWasDetected(mutation.getStatus())) {
				return MUTATION_DETECTED;
			}
			return MUTATION_NOT_DETECTED;
		}

		private boolean mutationWasDetected(DetectionStatus status) {
			return DETECTED_STATUSES.contains(status);
		}

		@Override
		public Image visitStatus(Status status) {
			// TODO Auto-generated method stub
			return null;
		}

	}

}

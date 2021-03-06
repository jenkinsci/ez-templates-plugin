package com.joelj.jenkins.eztemplates.listener;

import com.google.common.base.Throwables;
import hudson.model.Item;
import hudson.model.Job;
import hudson.model.JobProperty;
import hudson.model.listeners.ItemListener;

import static com.joelj.jenkins.eztemplates.utils.ProjectUtils.getProperty;


/**
 * Listens to changes only on {@link Job}s with a given {@link hudson.model.JobProperty}.
 */
public abstract class PropertyListener<J extends JobProperty> extends ItemListener {

    private final boolean updateEnabled;

    private final Class<J> propertyType;

    @SuppressWarnings("unchecked")
    public PropertyListener(Class<J> propertyType) {
        this.propertyType = propertyType;        // TODO Prefer TypeToken not available in guava-11
        updateEnabled = !VersionEvaluator.jobSaveUsesBulkchange();
    }

    @Override
    public final void onCreated(Item item) {
        J property = getProperty(item, propertyType);
        if (property != null) {
            try {
                onCreatedProperty((Job) item, property);
            } catch (Exception e) {
                throw Throwables.propagate(e);
            }
        }
    }

    /**
     * @see ItemListener#onCreated(Item)
     */
    public void onCreatedProperty(Job item, J property) throws Exception {
    }

    @Override
    public final void onCopied(Item src, Item item) {
        J property = getProperty(item, propertyType);
        if (property != null) {
            try {
                onCopiedProperty((Job) src, (Job) item, property);
            } catch (Exception e) {
                throw Throwables.propagate(e);
            }
        }
    }

    /**
     * @see ItemListener#onCopied(Item, Item)
     */
    public void onCopiedProperty(Job src, Job item, J property) throws Exception {
    }

    @Override
    public final void onDeleted(Item item) {
        J property = getProperty(item, propertyType);
        if (property != null) {
            try {
                onDeletedProperty((Job) item, property);
            } catch (Exception e) {
                throw Throwables.propagate(e);
            }
        }
    }

    /**
     * @see ItemListener#onDeleted(Item)
     */
    public void onDeletedProperty(Job item, J property) throws Exception {
    }

    @Override
    public final void onRenamed(Item item, String oldName, String newName) {
        J property = getProperty(item, propertyType);
        if (property != null) {
            try {
                onRenamedProperty((Job) item, oldName, newName, property);
            } catch (Exception e) {
                throw Throwables.propagate(e);
            }
        }
    }

    /**
     * @see ItemListener#onRenamed(Item, String, String)
     */
    public void onRenamedProperty(Job item, String oldName, String newName, J property) throws Exception {

    }

    @Override
    public final void onLocationChanged(Item item, String oldFullName, String newFullName) {
        J property = getProperty(item, propertyType);
        if (property != null) {
            try {
                onLocationChangedProperty((Job) item, oldFullName, newFullName, property);
            } catch (Exception e) {
                throw Throwables.propagate(e);
            }
        }
    }

    /**
     * @see ItemListener#onLocationChanged(Item, String, String)
     */
    public void onLocationChangedProperty(Job item, String oldFullName, String newFullName, J property) throws Exception {
    }

    @Override
    public final void onUpdated(Item item) {
        if (!updateEnabled || EzTemplateChange.contains(item, propertyType)) {
            return; // Ignore item listener updates if we trust the more general-purpose SaveableListener
        }
        J property = getProperty(item, propertyType);
        if (property != null) {
            try {
                onUpdatedProperty((Job) item, property);
            } catch (Exception e) {
                throw new RuntimeException("EZ Templates failed", e);
            }
        }
    }

    /**
     * @see ItemListener#onUpdated(Item)
     */
    public void onUpdatedProperty(Job item, J property) throws Exception {
    }

}

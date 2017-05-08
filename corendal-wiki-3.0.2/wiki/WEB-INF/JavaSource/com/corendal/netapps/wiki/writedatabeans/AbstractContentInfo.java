/*   
 * Corendal NetApps Wiki - Web application for content management   
 * Copyright (C) Thierry Danard   
 *   
 * This program is free software; you can redistribute it and|or   
 * modify it under the terms of the GNU General Public License   
 * as published by the Free Software Foundation; either version 2   
 * of the License, or (at your option) any later version.   
 *   
 * This program is distributed in the hope that it will be useful,   
 * but WITHOUT ANY WARRANTY; without even the implied warranty of   
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the   
 * GNU General Public License for more details.   
 *   
 * You should have received a copy of the GNU General Public License   
 * along with this program; if not, write to the Free Software   
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.   
 */
package com.corendal.netapps.wiki.writedatabeans;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.corendal.netapps.framework.core.constants.EntryLogTypeConstants;
import com.corendal.netapps.framework.core.databeans.AbstractWriteDataBean;
import com.corendal.netapps.framework.modelaccess.globals.DataSessionSetGlobal;
import com.corendal.netapps.framework.modelaccess.interfaces.HibernateDataSession;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.interfaces.SingleKey;
import com.corendal.netapps.framework.modelaccess.utils.LoggerUtil;
import com.corendal.netapps.framework.modelaccess.utils.NextPrimaryKeyUtil;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.constants.DataSessionTypeConstants;
import com.corendal.netapps.wiki.dictionaries.EntitiesDictionary;
import com.corendal.netapps.wiki.interfaces.ContentInfo;
import com.corendal.netapps.wiki.interfaces.ContentInfoMapping;
import com.corendal.netapps.wiki.writedatamappings.DefaultContentInfoMapping;

/**
 * AbstractContentInfo is the abstract class retrieving information about each
 * content info of the application from the database.
 * 
 * @version $Id: AbstractContentInfo.java,v 1.12 2007/04/06 19:45:43 tdanard
 *          Exp $
 */
public abstract class AbstractContentInfo extends AbstractWriteDataBean
        implements Cloneable, ContentInfo {
    /** Fully qualified name of this class. */
    private static final String ABSTRACT_CLASS_NAME = "com.corendal.netapps.wiki.writedatabeans.AbstractContentInfo";

    /** Nam in the content_info table */
    private String name;

    /** Dsc in the content_info table */
    private String description;

    /** Enabled_flag in the content_info table */
    private String enabledFlag;

    /**
     * Name of the named query for the findByEnabledFlag method.
     */
    private static final String FIND_BY_ENABLED_FLAG = ABSTRACT_CLASS_NAME
            + ".findByEnabledFlag";

    /**
     * Default class constructor.
     */
    protected AbstractContentInfo() {
        super(DataSessionTypeConstants.WIKI_SHARED);
    }

    /**
     * Returns a new instance of a content info mapping.
     * 
     * @return a com.corendal.netapps.qadocumentation.interfaces.ContentInfo object
     */
    protected ContentInfoMapping getContentInfoMappingNewInstance() {
        return new DefaultContentInfoMapping();
    }

    /**
     * Returns a clone. Overrides AbstractWriteDataBean.clone.
     */
    @Override
    public Object clone() {
        return (AbstractContentInfo) super.clone();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.ReadDataBean#load()
     */
    @Override
    public void load() {
        /*
         * set the record as "not found"
         */
        this.setIsFound(false);

        /*
         * get the JDBC data session
         */
        HibernateDataSession ds = DataSessionSetGlobal.get()
                .getSharedHibernateDataSession(
                        DataSessionTypeConstants.WIKI_SHARED);

        /*
         * get the primary key of the content_info
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_INFOS);
        PrimaryKey recordPk = this.getPrimaryKey();

        try {
            /*
             * get the hibernate session
             */
            Session session = ds.getReadSession();

            /*
             * load the content info mapping
             */
            ContentInfoMapping mapping = (ContentInfoMapping) session.get(
                    DefaultContentInfoMapping.class, recordPk.toString());
            /*
             * retrieve the content info information from the database
             */
            if (mapping != null) {
                /*
                 * set the record as "found"
                 */
                this.setIsFound(true);

                /*
                 * remember the content info information
                 */
                this.name = mapping.getName();
                this.description = mapping.getDescription();
                this.enabledFlag = mapping.getEnabledFlag();
            }
        } catch (Exception e) {
            /*
             * log the exception
             */
            this.appendLoadTrace(e.getMessage());
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, recordPk);
        }

        /*
         * load this write data bean
         */
        if (this.getIsFound()) {
            super.load(entityPk, recordPk);
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentInfo#getName()
     */
    public String getName() {
        return this.name;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentInfo#getDescription()
     */
    public String getDescription() {
        return this.description;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Enabled#getEnabledFlag()
     */
    public String getEnabledFlag() {
        return this.enabledFlag;
    }

    /**
     * Returns the ordered list of all enabled content info primary keys.
     * 
     * @param enabledFlag
     *            enabled flag to find
     * 
     * 
     * @return a java.util.List object
     */
    public static final List<PrimaryKey> findByEnabledFlag(String enabledFlag) {
        /*
         * initialize the result
         */
        List<PrimaryKey> primaryKeys = new ArrayList<PrimaryKey>();

        /*
         * get the JDBC data session
         */
        HibernateDataSession ds = DataSessionSetGlobal.get()
                .getSharedHibernateDataSession(
                        DataSessionTypeConstants.WIKI_SHARED);

        /*
         * find the primary key of each content info
         */
        try {
            /*
             * get the query
             */
            Query query = ds.getReadNamedQuery(FIND_BY_ENABLED_FLAG);

            /*
             * associate the enabled flag
             */
            query.setParameter("enabledFlag", enabledFlag);

            /*
             * loop through the results
             */
            primaryKeys = ds.getPrimaryKeys(query);
        } catch (Exception e) {
            /*
             * log the exception
             */
            PrimaryKey entityPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_INFOS);
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, null);
        }

        /*
         * return all content info primary keys
         */
        return primaryKeys;
    }

    /**
     * Returns the next id for a new content info.
     * 
     * 
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    private static PrimaryKey getNextPrimaryKey() {
        return NextPrimaryKeyUtil.getNextSequentialAlphanumericSingleKey(
                "content_info", EntitiesDictionary.CONTENT_INFOS,
                DataSessionSetGlobal.get().getSharedHibernateDataSession(
                        DataSessionTypeConstants.WIKI_SHARED));
    }

    /**
     * Creates a content info in the database with a name and description.
     * 
     * @param name
     *            name to set
     * @param description
     *            description to set
     * 
     * 
     */
    public void add(String name, String description) {
        /*
         * set the record as "done" by default
         */
        this.setIsDone(true);

        /*
         * get the JDBC data session
         */
        HibernateDataSession ds = DataSessionSetGlobal.get()
                .getSharedHibernateDataSession(
                        DataSessionTypeConstants.WIKI_SHARED);

        /*
         * find the next id
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_INFOS);
        SingleKey recordPk = (SingleKey) getNextPrimaryKey();

        try {
            /*
             * get the hibernate session
             */
            Session session = ds.getWriteSession();

            /*
             * save the content info record
             */
            ContentInfoMapping mapping = getContentInfoMappingNewInstance();
            mapping.setId(recordPk.toString());
            mapping.setName(name);
            mapping.setDescription(description);
            mapping.setEnabledFlag("Y");
            session.save(mapping);
        } catch (Exception e) {
            /*
             * log the exception
             */
            this.setIsDone(false);
            this.appendStoreTrace(e.getMessage());
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, null);
        }

        /*
         * add an entry log and load the content info information
         */
        if (this.getIsDone()) {
            PrimaryKey typePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntryLogTypeConstants.INSERT);
            this.addEntryLog(entityPk, recordPk, typePk,
                    DataSessionTypeConstants.WIKI_SHARED);
            this.setPrimaryKeyClearCacheAndLoad(recordPk);
        }
    }

    /**
     * Removes this content info.
     */
    public void remove() {
        /*
         * set the record as "done" by default
         */
        this.setIsDone(true);

        /*
         * get the JDBC data session
         */
        HibernateDataSession ds = DataSessionSetGlobal.get()
                .getSharedHibernateDataSession(
                        DataSessionTypeConstants.WIKI_SHARED);

        /*
         * get the primary key of this content map
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_INFOS);
        PrimaryKey recordPk = this.getPrimaryKey();

        try {
            /*
             * get the hibernate session
             */
            Session session = ds.getWriteSession();

            /*
             * load the content info mapping
             */
            ContentInfoMapping mapping = (ContentInfoMapping) session.get(
                    DefaultContentInfoMapping.class, recordPk.toString());

            /*
             * retrieve the content info from the database
             */
            if (mapping != null) {
                session.delete(mapping);
            }
        } catch (Exception e) {
            /*
             * log the exception
             */
            this.setIsDone(false);
            this.appendStoreTrace(e.getMessage());
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, recordPk);
        }

        /*
         * refresh this content info
         */
        if (this.getIsDone()) {
            PrimaryKey typePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntryLogTypeConstants.DELETE);
            this.addEntryLog(entityPk, recordPk, typePk,
                    DataSessionTypeConstants.WIKI_SHARED);
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentInfo#storeInfo(java.lang.String,
     *      java.lang.String)
     */
    public void storeInfo(String name, String description) {
        /*
         * set the record as "done" by default
         */
        this.setIsDone(true);

        /*
         * get the JDBC data session
         */
        HibernateDataSession ds = DataSessionSetGlobal.get()
                .getSharedHibernateDataSession(
                        DataSessionTypeConstants.WIKI_SHARED);

        /*
         * get the primary key of this account
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_INFOS);
        PrimaryKey recordPk = this.getPrimaryKey();

        /*
         * update this content info
         */
        try {
            /*
             * get the hibernate session
             */
            Session session = ds.getWriteSession();

            /*
             * load the content info mapping
             */
            ContentInfoMapping mapping = (ContentInfoMapping) session.get(
                    DefaultContentInfoMapping.class, recordPk.toString());

            /*
             * update the content info using that mapping
             */
            if (mapping != null) {
                mapping.setName(name);
                mapping.setDescription(description);
                session.update(mapping);
            }
        } catch (Exception e) {
            /*
             * log the exception
             */
            this.setIsDone(false);
            this.appendStoreTrace(e.getMessage());
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, recordPk);
        }

        /*
         * refresh this content info
         */
        if (this.getIsDone()) {
            PrimaryKey typePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntryLogTypeConstants.UPDATE);
            this.addEntryLog(entityPk, recordPk, typePk,
                    DataSessionTypeConstants.WIKI_SHARED);
            this.clearCacheAndLoad();
        }
    }

    /**
     * Cleans up all data related to this entity.
     */
    public static void cleanup() {
        /*
         * call the generic data cleanup
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_INFOS);
        String tableName = "content_info";
        AbstractWriteDataBean.cleanup(entityPk, tableName,
                DataSessionTypeConstants.WIKI_SHARED);
    }
}

// end AbstractContentInfo

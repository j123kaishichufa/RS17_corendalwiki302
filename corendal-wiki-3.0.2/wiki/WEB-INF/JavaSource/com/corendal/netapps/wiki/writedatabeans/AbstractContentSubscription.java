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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.corendal.netapps.framework.core.constants.EntryLogTypeConstants;
import com.corendal.netapps.framework.core.databeans.AbstractWriteDataBean;
import com.corendal.netapps.framework.core.writedatafactories.AccountRoleXrefFactory;
import com.corendal.netapps.framework.helpdesk.writedatafactories.GroupRoleXrefFactory;
import com.corendal.netapps.framework.modelaccess.globals.DataSessionSetGlobal;
import com.corendal.netapps.framework.modelaccess.interfaces.HibernateDataSession;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.interfaces.SingleKey;
import com.corendal.netapps.framework.modelaccess.utils.CloseUtil;
import com.corendal.netapps.framework.modelaccess.utils.LoggerUtil;
import com.corendal.netapps.framework.modelaccess.utils.NextPrimaryKeyUtil;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.constants.DataSessionTypeConstants;
import com.corendal.netapps.wiki.dictionaries.EntitiesDictionary;
import com.corendal.netapps.wiki.dictionaries.RolesDictionary;
import com.corendal.netapps.wiki.interfaces.ContentSubscription;
import com.corendal.netapps.wiki.interfaces.ContentSubscriptionMapping;
import com.corendal.netapps.wiki.writedatamappings.DefaultContentSubscriptionMapping;

/**
 * AbstractContentSubscription is the abstract class retrieving information
 * about each content subscription of the application from the database.
 * 
 * @version $Id: AbstractContentSubscription.java,v 1.1 2005/09/06 21:25:33
 *          tdanard Exp $
 */
public abstract class AbstractContentSubscription extends AbstractWriteDataBean
        implements Cloneable, ContentSubscription {
    /** Fully qualified name of this class. */
    private static final String ABSTRACT_CLASS_NAME = "com.corendal.netapps.wiki.writedatabeans.AbstractContentSubscription";

    /** Content_id in the content_subscript table */
    private String contentId;

    /** Content_subscription_typ_id in the content_subscript table */
    private String typeId;

    /** Content_subscription_mod_id in the content_subscript table */
    private String modeId;

    /** Enabled_flag in the content_subscript table */
    private String enabledFlag;

    /**
     * Name of the named query for the findEnabledByContentPrimaryKey method.
     */
    private static final String FIND_ENABLED_BY_CONTENT_PRIMARY_KEY = ABSTRACT_CLASS_NAME
            + ".findEnabledByContentPrimaryKey";

    /**
     * Name of the named query for the findEnabledByContentAndTypePrimaryKeys
     * method.
     */
    private static final String FIND_ENABLED_BY_CONTENT_AND_TYPE_PRIMARY_KEYS = ABSTRACT_CLASS_NAME
            + ".findEnabledByContentAndTypePrimaryKeys";

    /**
     * Name of the named query for the findEnabledByTypePrimaryKey method.
     */
    private static final String FIND_ENABLED_BY_TYPE_PRIMARY_KEY = ABSTRACT_CLASS_NAME
            + ".findEnabledByTypePrimaryKey";

    /**
     * Name of the named query for the findEnabledByModePrimaryKey method.
     */
    private static final String FIND_ENABLED_BY_MODE_PRIMARY_KEY = ABSTRACT_CLASS_NAME
            + ".findEnabledByModePrimaryKey";

    /**
     * Default class constructor.
     */
    protected AbstractContentSubscription() {
        super(DataSessionTypeConstants.WIKI_SHARED);
    }

    /**
     * Returns a new instance of a content subscription mapping.
     * 
     * @return a
     *         com.corendal.netapps.qadocumentation.interfaces.ContentSubscription
     *         object
     */
    protected ContentSubscriptionMapping getContentSubscriptionMappingNewInstance() {
        return new DefaultContentSubscriptionMapping();
    }

    /**
     * Returns a clone. Overrides AbstractWriteDataBean.clone.
     */
    @Override
    public Object clone() {
        return (AbstractContentSubscription) super.clone();
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
         * get the primary key of the subscription
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_SUBSCRIPTIONS);
        PrimaryKey recordPk = this.getPrimaryKey();

        try {
            /*
             * get the hibernate session
             */
            Session session = ds.getReadSession();

            /*
             * load the content subscription mapping
             */
            ContentSubscriptionMapping mapping = (ContentSubscriptionMapping) session
                    .get(DefaultContentSubscriptionMapping.class, recordPk
                            .toString());
            /*
             * retrieve the content subscription information from the database
             */
            if (mapping != null) {
                /*
                 * set the record as "found"
                 */
                this.setIsFound(true);

                /*
                 * remember the content subscription information
                 */
                this.contentId = mapping.getContentId();
                this.typeId = mapping.getContentSubscriptionTypeId();
                this.modeId = mapping.getContentSubscriptionModeId();
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
     * @see com.corendal.netapps.wiki.interfaces.ContentSubscription#getContentPrimaryKey()
     */
    public PrimaryKey getContentPrimaryKey() {
        return PrimaryKeyUtil.getAlphanumericSingleKeyOrNull(this.contentId);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentSubscription#getModePrimaryKey()
     */
    public PrimaryKey getModePrimaryKey() {
        return PrimaryKeyUtil.getAlphanumericSingleKeyOrNull(this.modeId);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentSubscription#getTypePrimaryKey()
     */
    public PrimaryKey getTypePrimaryKey() {
        return PrimaryKeyUtil.getAlphanumericSingleKeyOrNull(this.typeId);
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Enabled#getEnabledFlag()
     */
    public String getEnabledFlag() {
        return this.enabledFlag;
    }

    /**
     * Returns the next id for a new content subscription.
     * 
     * 
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    private static PrimaryKey getNextPrimaryKey() {
        return NextPrimaryKeyUtil.getNextSequentialAlphanumericSingleKey(
                "content_subscript", EntitiesDictionary.CONTENT_SUBSCRIPTIONS,
                DataSessionSetGlobal.get().getSharedHibernateDataSession(
                        DataSessionTypeConstants.WIKI_SHARED));
    }

    /**
     * Creates a content subscription in the database.
     * 
     * @param contentPk
     *            primary key of the content being subscribed to
     * @param typePk
     *            primary key of the type of the content subscription
     * @param modePk
     *            primary key of the mode of the content subscription
     * 
     * 
     */
    public void add(PrimaryKey contentPk, PrimaryKey typePk, PrimaryKey modePk) {
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
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_SUBSCRIPTIONS);
        SingleKey recordPk = (SingleKey) getNextPrimaryKey();

        try {
            /*
             * get the hibernate session
             */
            Session session = ds.getWriteSession();

            /*
             * save the content subscription record
             */
            ContentSubscriptionMapping mapping = getContentSubscriptionMappingNewInstance();
            mapping.setId(recordPk.toString());
            if (contentPk != null) {
                mapping.setContentId(contentPk.toString());
            } else {
                mapping.setContentId(null);
            }
            if (typePk != null) {
                mapping.setContentSubscriptionTypeId(typePk.toString());
            } else {
                mapping.setContentSubscriptionTypeId(null);
            }
            if (modePk != null) {
                mapping.setContentSubscriptionModeId(modePk.toString());
            } else {
                mapping.setContentSubscriptionModeId(null);
            }
            mapping.setOrder(Integer.parseInt(recordPk.toString()));
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
         * add an entry log and load the content subscription information
         */
        if (this.getIsDone()) {
            PrimaryKey typePk2 = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntryLogTypeConstants.INSERT);
            this.addEntryLog(entityPk, recordPk, typePk2,
                    DataSessionTypeConstants.WIKI_SHARED);
            this.setPrimaryKeyClearCacheAndLoad(recordPk);
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentSubscription#storeSubscriberAccount(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey)
     */
    public void storeSubscriberAccount(PrimaryKey subscriberPk) {
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
         * get the primary key of this content subscription entity
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_SUBSCRIPTIONS);
        PrimaryKey subscriberRolePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(RolesDictionary.SUBSCRIBER);
        PrimaryKey recordPk = this.getPrimaryKey();

        /*
         * remove account role xref from the database
         */
        AccountRoleXrefFactory.remove(this, ds, entityPk, subscriberRolePk,
                recordPk);

        /*
         * add the subscriber record
         */
        if (this.getIsDone()) {
            AccountRoleXrefFactory.create(this, ds, entityPk, subscriberRolePk,
                    recordPk, subscriberPk);
        }

        /*
         * refresh this content subscription
         */
        if (this.getIsDone()) {
            PrimaryKey typePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntryLogTypeConstants.UPDATE);
            this.addEntryLog(entityPk, this.getPrimaryKey(), typePk,
                    DataSessionTypeConstants.WIKI_SHARED);
            this.clearCacheAndLoad();
        }
    }

    /**
     * Stores the subscriber group of this content subscription.
     */
    public void storeSubscriberGroup(PrimaryKey subscriberPk) {
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
         * get the primary key of this content subscription entity
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_SUBSCRIPTIONS);
        PrimaryKey rolePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(RolesDictionary.SUBSCRIBER);
        PrimaryKey recordPk = this.getPrimaryKey();

        GroupRoleXrefFactory.remove(this, ds, entityPk, rolePk, recordPk);

        /*
         * add the subscriber record
         */
        if (this.getIsDone()) {
            GroupRoleXrefFactory.create(this, ds, entityPk, rolePk, recordPk,
                    subscriberPk);
        }

        /*
         * refresh this content subscription
         */
        if (this.getIsDone()) {
            PrimaryKey typePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntryLogTypeConstants.UPDATE);
            this.addEntryLog(entityPk, this.getPrimaryKey(), typePk,
                    DataSessionTypeConstants.WIKI_SHARED);
            this.clearCacheAndLoad();
        }
    }

    /**
     * Removes this content subscription.
     */
    public void remove() {
        /*
         * set the record as "done" by default
         */
        this.setIsDone(true);

        /*
         * initialize the prepared statement and the result set
         */
        PreparedStatement prepStmt = null;

        /*
         * get the JDBC data session
         */
        HibernateDataSession ds = DataSessionSetGlobal.get()
                .getSharedHibernateDataSession(
                        DataSessionTypeConstants.WIKI_SHARED);

        /*
         * get the primary key of this content subscription
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_SUBSCRIPTIONS);
        PrimaryKey recordPk = this.getPrimaryKey();
        PrimaryKey subscriberRolePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(RolesDictionary.SUBSCRIBER);

        /*
         * remove account role xref from the database
         */
        AccountRoleXrefFactory.remove(this, ds, entityPk, subscriberRolePk,
                recordPk);

        /*
         * remove the subscription record
         */
        if (this.getIsDone()) {
            try {
                /*
                 * get the hibernate session
                 */
                Session session = ds.getWriteSession();

                /*
                 * load the content subscription mapping
                 */
                ContentSubscriptionMapping mapping = (ContentSubscriptionMapping) session
                        .get(DefaultContentSubscriptionMapping.class, recordPk
                                .toString());

                /*
                 * retrieve the content subscription from the database
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
            } finally {
                /*
                 * close the prepared statement
                 */
                CloseUtil.closePreparedStatement(prepStmt);
            }
        }

        /*
         * refresh this content subscription
         */
        if (this.getIsDone()) {
            PrimaryKey typePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntryLogTypeConstants.DELETE);
            this.addEntryLog(entityPk, recordPk, typePk,
                    DataSessionTypeConstants.WIKI_SHARED);
        }
    }

    /**
     * Returns the ordered list of all enabled content subscription primary keys
     * for a content primary key.
     * 
     * @param contentPk
     *            primary key of the content
     * 
     * 
     * @return a java.util.List object
     */
    public static final List<PrimaryKey> findEnabledByContentPrimaryKey(
            PrimaryKey contentPk) {
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
         * find the primary key of each content subscription
         */
        try {
            /*
             * get the query
             */
            Query query = ds
                    .getReadNamedQuery(FIND_ENABLED_BY_CONTENT_PRIMARY_KEY);

            /*
             * associate the enabled flag
             */
            query.setParameter("contentId", contentPk.toString());
            query.setParameter("enabledFlag", "Y");

            /*
             * loop through the results
             */
            primaryKeys = ds.getPrimaryKeys(query);
        } catch (Exception e) {
            /*
             * log the exception
             */
            PrimaryKey entityPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_SUBSCRIPTIONS);
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, null);
        }

        /*
         * return all content subscription primary keys
         */
        return primaryKeys;
    }

    /**
     * Returns the ordered list of all enabled content subscription primary keys
     * for a content primary key and a subscription type primary key.
     * 
     * @param contentPk
     *            primary key of the content
     * @param typePk
     *            primary key of the subscription type
     * 
     * 
     * @return a java.util.List object
     */
    public static final List<PrimaryKey> findEnabledByContentAndTypePrimaryKeys(
            PrimaryKey contentPk, PrimaryKey typePk) {
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
         * find the primary key of each content subscription
         */
        try {
            /*
             * get the query
             */
            Query query = ds
                    .getReadNamedQuery(FIND_ENABLED_BY_CONTENT_AND_TYPE_PRIMARY_KEYS);

            /*
             * associate the enabled flag
             */
            query.setParameter("contentId", contentPk.toString());
            query.setParameter("contentSubscriptionTypeId", typePk.toString());
            query.setParameter("enabledFlag", "Y");

            /*
             * loop through the results
             */
            primaryKeys = ds.getPrimaryKeys(query);
        } catch (Exception e) {
            /*
             * log the exception
             */
            PrimaryKey entityPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_SUBSCRIPTIONS);
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, null);
        }

        /*
         * return all content subscription primary keys
         */
        return primaryKeys;
    }

    /**
     * Returns the ordered list of all enabled content subscription primary keys
     * for a type primary key.
     * 
     * @param typePk
     *            primary key of the type to query
     * 
     * 
     * @return a java.util.List object
     */
    public static final List<PrimaryKey> findEnabledByTypePrimaryKey(
            PrimaryKey typePk) {
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
         * find the primary key of each content subscription
         */
        try {
            /*
             * get the query
             */
            Query query = ds
                    .getReadNamedQuery(FIND_ENABLED_BY_TYPE_PRIMARY_KEY);

            /*
             * associate the enabled flag
             */
            query.setParameter("contentSubscriptionTypeId", typePk.toString());
            query.setParameter("enabledFlag", "Y");

            /*
             * loop through the results
             */
            primaryKeys = ds.getPrimaryKeys(query);
        } catch (Exception e) {
            /*
             * log the exception
             */
            PrimaryKey entityPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_SUBSCRIPTIONS);
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, null);
        }

        /*
         * return all content subscription primary keys
         */
        return primaryKeys;
    }

    /**
     * Returns the ordered list of all enabled content subscription primary keys
     * for a mode primary key.
     * 
     * @param modePk
     *            primary key of the mode to query
     * 
     * 
     * @return a java.util.List object
     */
    public static final List<PrimaryKey> findEnabledByModePrimaryKey(
            PrimaryKey modePk) {
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
         * find the primary key of each content subscription
         */
        try {
            /*
             * get the query
             */
            Query query = ds
                    .getReadNamedQuery(FIND_ENABLED_BY_MODE_PRIMARY_KEY);

            /*
             * associate the enabled flag
             */
            query.setParameter("contentSubscriptionModeId", modePk.toString());
            query.setParameter("enabledFlag", "Y");

            /*
             * loop through the results
             */
            primaryKeys = ds.getPrimaryKeys(query);
        } catch (Exception e) {
            /*
             * log the exception
             */
            PrimaryKey entityPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_SUBSCRIPTIONS);
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, null);
        }

        /*
         * return all content subscription primary keys
         */
        return primaryKeys;
    }

    /**
     * Returns the ordered list of all enabled content subscription primary keys
     * for an individual primary key.
     * 
     * @param individualPk
     *            primary key of the content
     * 
     * 
     * @return a java.util.List object
     */
    public static final List<PrimaryKey> findEnabledByIndividualPrimaryKey(
            PrimaryKey individualPk) {
        /*
         * initialize the result
         */
        List<PrimaryKey> primaryKeys = new ArrayList<PrimaryKey>();

        /*
         * initialize the prepared statement and the result set
         */
        PreparedStatement prepStmt = null;
        ResultSet rs = null;

        /*
         * get the JDBC data session
         */
        HibernateDataSession ds = DataSessionSetGlobal.get()
                .getSharedHibernateDataSession(
                        DataSessionTypeConstants.WIKI_SHARED);

        /*
         * retrieve the content information from the database
         */
        String selectStatement = "select content_subscript.id " + "from "
                + ds.getTableNameWithDefaultSchema("content_subscript") + ", "
                + ds.getTableNameWithDefaultSchema("account_role_xref") + " "
                + "where account_role_xref.account_id= ? "
                + "and account_role_xref.role_id= ? "
                + "and account_role_xref.entity_id= ? "
                + "and content_subscript.enabled_flag = ? "
                + "and account_role_xref.record_id= content_subscript.id "
                + "order by content_subscript.ord asc";
        // FIXME: hibernate select query

        try {
            /*
             * create the prepared statement
             */
            prepStmt = ds.getReadConnection().prepareStatement(selectStatement);

            /*
             * associate the enabled flag
             */
            prepStmt.setString(1, individualPk.toString());
            prepStmt.setString(2, RolesDictionary.SUBSCRIBER);
            prepStmt.setString(3, EntitiesDictionary.CONTENT_SUBSCRIPTIONS);
            prepStmt.setString(4, "Y");

            /*
             * execute the query
             */
            rs = prepStmt.executeQuery();

            /*
             * loop the results
             */
            while (rs.next()) {
                primaryKeys.add(PrimaryKeyUtil.getAlphanumericSingleKey(rs
                        .getString(1)));
            }
        } catch (Exception e) {
            /*
             * log the exception
             */
            PrimaryKey entityPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_SUBSCRIPTIONS);
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, null);
        } finally {
            /*
             * close the recordset and the prepared statement
             */
            CloseUtil.closeResultSet(rs);
            CloseUtil.closePreparedStatement(prepStmt);
        }

        /*
         * return all content subscription primary keys
         */
        return primaryKeys;
    }

    /**
     * Returns the ordered list of all enabled content subscription primary keys
     * for an group primary key.
     * 
     * @param groupPk
     *            primary key of the content
     * 
     * 
     * @return a java.util.List object
     */
    public static final List<PrimaryKey> findEnabledByGroupPrimaryKey(
            PrimaryKey groupPk) {
        /*
         * initialize the result
         */
        List<PrimaryKey> primaryKeys = new ArrayList<PrimaryKey>();

        /*
         * initialize the prepared statement and the result set
         */
        PreparedStatement prepStmt = null;
        ResultSet rs = null;

        /*
         * get the JDBC data session
         */
        HibernateDataSession ds = DataSessionSetGlobal.get()
                .getSharedHibernateDataSession(
                        DataSessionTypeConstants.WIKI_SHARED);

        /*
         * retrieve the content information from the database
         */
        String selectStatement = "select content_subscript.id " + "from "
                + ds.getTableNameWithDefaultSchema("content_subscript") + ", "
                + ds.getTableNameWithDefaultSchema("group_obj_role_xref") + " "
                + "where group_obj_role_xref.group_obj_id= ? "
                + "and group_obj_role_xref.role_id= ? "
                + "and group_obj_role_xref.entity_id= ? "
                + "and content_subscript.enabled_flag = ? "
                + "and group_obj_role_xref.record_id= content_subscript.id "
                + "order by content_subscript.ord asc";
        // FIXME: hibernate select query

        try {
            /*
             * create the prepared statement
             */
            prepStmt = ds.getReadConnection().prepareStatement(selectStatement);

            /*
             * associate the enabled flag
             */
            prepStmt.setString(1, groupPk.toString());
            prepStmt.setString(2, RolesDictionary.SUBSCRIBER);
            prepStmt.setString(3, EntitiesDictionary.CONTENT_SUBSCRIPTIONS);
            prepStmt.setString(4, "Y");

            /*
             * execute the query
             */
            rs = prepStmt.executeQuery();

            /*
             * loop the results
             */
            while (rs.next()) {
                primaryKeys.add(PrimaryKeyUtil.getAlphanumericSingleKey(rs
                        .getString(1)));
            }
        } catch (Exception e) {
            /*
             * log the exception
             */
            PrimaryKey entityPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_SUBSCRIPTIONS);
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, null);
        } finally {
            /*
             * close the recordset and the prepared statement
             */
            CloseUtil.closeResultSet(rs);
            CloseUtil.closePreparedStatement(prepStmt);
        }

        /*
         * return all content subscription primary keys
         */
        return primaryKeys;
    }

    /**
     * Returns the ordered list of all enabled content subscription primary keys
     * for an individual and mode primary keys.
     * 
     * @param individualPk
     *            primary key of the individual
     * @param modePk
     *            primary key of the mode of the content subscription
     * 
     * 
     * @return a java.util.List object
     */
    public static final List<PrimaryKey> findEnabledByIndividualAndModePrimaryKeys(
            PrimaryKey individualPk, PrimaryKey modePk) {
        /*
         * initialize the result
         */
        List<PrimaryKey> primaryKeys = new ArrayList<PrimaryKey>();

        /*
         * initialize the prepared statement and the result set
         */
        PreparedStatement prepStmt = null;
        ResultSet rs = null;

        /*
         * get the JDBC data session
         */
        HibernateDataSession ds = DataSessionSetGlobal.get()
                .getSharedHibernateDataSession(
                        DataSessionTypeConstants.WIKI_SHARED);

        /*
         * retrieve the content information from the database
         */
        String selectStatement = "select content_subscript.id " + "from "
                + ds.getTableNameWithDefaultSchema("content_subscript") + ", "
                + ds.getTableNameWithDefaultSchema("account_role_xref") + " "
                + "where account_role_xref.account_id= ? "
                + "and account_role_xref.role_id= ? "
                + "and account_role_xref.entity_id= ? "
                + "and content_subscript.content_subscript_mod_id = ? "
                + "and content_subscript.enabled_flag = ? "
                + "and account_role_xref.record_id= content_subscript.id "
                + "order by content_subscript.ord asc";
        // FIXME: hibernate select query

        try {
            /*
             * create the prepared statement
             */
            prepStmt = ds.getReadConnection().prepareStatement(selectStatement);

            /*
             * associate the enabled flag
             */
            prepStmt.setString(1, individualPk.toString());
            prepStmt.setString(2, RolesDictionary.SUBSCRIBER);
            prepStmt.setString(3, EntitiesDictionary.CONTENT_SUBSCRIPTIONS);
            prepStmt.setString(4, modePk.toString());
            prepStmt.setString(5, "Y");

            /*
             * execute the query
             */
            rs = prepStmt.executeQuery();

            /*
             * loop the results
             */
            while (rs.next()) {
                primaryKeys.add(PrimaryKeyUtil.getAlphanumericSingleKey(rs
                        .getString(1)));
            }
        } catch (Exception e) {
            /*
             * log the exception
             */
            PrimaryKey entityPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_SUBSCRIPTIONS);
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, null);
        } finally {
            /*
             * close the recordset and the prepared statement
             */
            CloseUtil.closeResultSet(rs);
            CloseUtil.closePreparedStatement(prepStmt);
        }

        /*
         * return all content subscription primary keys
         */
        return primaryKeys;
    }

    /**
     * Returns the ordered list of all enabled content subscription primary keys
     * for a group and mode primary keys.
     * 
     * @param groupPk
     *            primary key of the group
     * @param modePk
     *            primary key of the mode of content subscription
     * 
     * 
     * @return a java.util.List object
     */
    public static final List<PrimaryKey> findEnabledByGroupAndModePrimaryKeys(
            PrimaryKey groupPk, PrimaryKey modePk) {
        /*
         * initialize the result
         */
        List<PrimaryKey> primaryKeys = new ArrayList<PrimaryKey>();

        /*
         * initialize the prepared statement and the result set
         */
        PreparedStatement prepStmt = null;
        ResultSet rs = null;

        /*
         * get the JDBC data session
         */
        HibernateDataSession ds = DataSessionSetGlobal.get()
                .getSharedHibernateDataSession(
                        DataSessionTypeConstants.WIKI_SHARED);

        /*
         * retrieve the content information from the database
         */
        String selectStatement = "select content_subscript.id " + "from "
                + ds.getTableNameWithDefaultSchema("content_subscript") + ", "
                + ds.getTableNameWithDefaultSchema("group_obj_role_xref") + " "
                + "where group_obj_role_xref.group_obj_id= ? "
                + "and group_obj_role_xref.role_id= ? "
                + "and group_obj_role_xref.entity_id= ? "
                + "and content_subscript.content_subscript_mod_id = ? "
                + "and content_subscript.enabled_flag = ? "
                + "and group_obj_role_xref.record_id= content_subscript.id "
                + "order by content_subscript.ord asc";
        // FIXME: hibernate select query

        try {
            /*
             * create the prepared statement
             */
            prepStmt = ds.getReadConnection().prepareStatement(selectStatement);

            /*
             * associate the enabled flag
             */
            prepStmt.setString(1, groupPk.toString());
            prepStmt.setString(2, RolesDictionary.SUBSCRIBER);
            prepStmt.setString(3, EntitiesDictionary.CONTENT_SUBSCRIPTIONS);
            prepStmt.setString(4, modePk.toString());
            prepStmt.setString(5, "Y");

            /*
             * execute the query
             */
            rs = prepStmt.executeQuery();

            /*
             * loop the results
             */
            while (rs.next()) {
                primaryKeys.add(PrimaryKeyUtil.getAlphanumericSingleKey(rs
                        .getString(1)));
            }
        } catch (Exception e) {
            /*
             * log the exception
             */
            PrimaryKey entityPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_SUBSCRIPTIONS);
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, null);
        } finally {
            /*
             * close the recordset and the prepared statement
             */
            CloseUtil.closeResultSet(rs);
            CloseUtil.closePreparedStatement(prepStmt);
        }

        /*
         * return all content subscription primary keys
         */
        return primaryKeys;
    }

    /**
     * Cleans up all data related to this entity.
     */
    public static void cleanup() {
        /*
         * call the generic data cleanup
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_SUBSCRIPTIONS);
        String tableName = "content_subscript";
        AbstractWriteDataBean.cleanup(entityPk, tableName,
                DataSessionTypeConstants.WIKI_SHARED);
    }
}

// end AbstractContentSubscription

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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.corendal.netapps.framework.core.databeans.AbstractWriteDataBean;
import com.corendal.netapps.framework.core.readdatafactories.IconFactory;
import com.corendal.netapps.framework.core.utils.FullTextUtil;
import com.corendal.netapps.framework.modelaccess.globals.DataSessionSetGlobal;
import com.corendal.netapps.framework.modelaccess.interfaces.HibernateDataSession;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.interfaces.ScoredPrimaryKey;
import com.corendal.netapps.framework.modelaccess.interfaces.SingleKey;
import com.corendal.netapps.framework.modelaccess.utils.CloseUtil;
import com.corendal.netapps.framework.modelaccess.utils.LoggerUtil;
import com.corendal.netapps.framework.modelaccess.utils.NextPrimaryKeyUtil;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.framework.modelaccess.utils.SQLUtil;
import com.corendal.netapps.framework.modelaccess.utils.TimestampUtil;
import com.corendal.netapps.wiki.constants.DataSessionTypeConstants;
import com.corendal.netapps.wiki.constants.EntryLogTypeConstants;
import com.corendal.netapps.wiki.dictionaries.ContentTypesDictionary;
import com.corendal.netapps.wiki.dictionaries.EntitiesDictionary;
import com.corendal.netapps.wiki.dictionaries.IconsDictionary;
import com.corendal.netapps.wiki.dictionaries.RolesDictionary;
import com.corendal.netapps.wiki.interfaces.Content;
import com.corendal.netapps.wiki.interfaces.ContentMapMapping;
import com.corendal.netapps.wiki.interfaces.ContentVersionMapping;
import com.corendal.netapps.wiki.writedatamappings.DefaultContentMapMapping;

/**
 * AbstractContent is the abstract class retrieving information about each
 * content of the application from the database.
 * 
 * @version $Id: AbstractContent.java,v 1.35 2008/02/21 16:14:55 tdanard Exp $
 */
public abstract class AbstractContent extends AbstractWriteDataBean implements
        Cloneable, Content {
    /** Fully qualified name of this class. */
    private static final String ABSTRACT_CLASS_NAME = "com.corendal.netapps.wiki.writedatabeans.AbstractContent";

    /** Version_num in the content_version table */
    private int versionNum;

    /** Version_info_id in the content_version table */
    private String versionInfoId;

    /** Request_id in the content_version table */
    private String requestId;

    /** Content_typ_id in the content table */
    private String contentTypeId;

    /** Content_class_typ_id in the content table */
    private String contentClassTypeId;

    /** Enabled_flag in the content table */
    private String enabledFlag;

    /** Content_rule_typ_id in the content table */
    private String contentRuleTypeId;

    /**
     * Name of the named query for the findByEnabledFlag method.
     */
    private static final String FIND_BY_ENABLED_FLAG = ABSTRACT_CLASS_NAME
            + ".findByEnabledFlag";

    /**
     * Name of the named query for the removeReference method.
     */
    private static final String REMOVE_REFERENCE = ABSTRACT_CLASS_NAME
            + ".removeReference";

    /**
     * Name of the named query for the storeContentOrder method.
     */
    private static final String STORE_CONTENT_ORDER = ABSTRACT_CLASS_NAME
            + ".storeContentOrder";

    /**
     * Name of the named query for the getFirstRequestPrimaryKey method.
     */
    private static final String GET_FIRST_REQUEST_PRIMARY_KEY = ABSTRACT_CLASS_NAME
            + ".getFirstRequestPrimaryKey";

    /**
     * Default class constructor.
     */
    protected AbstractContent() {
        super(DataSessionTypeConstants.WIKI_SHARED);
        this.setModifyType(EntryLogTypeConstants.SUPERSEDE, true);
    }

    /**
     * Returns a new instance of a content map mapping.
     * 
     * @return a com.corendal.netapps.wiki.interfaces.ContentMap object
     */
    protected ContentMapMapping getContentMapMappingNewInstance() {
        return new DefaultContentMapMapping();
    }

    /**
     * Returns a clone. Overrides AbstractWriteDataBean.clone.
     */
    @Override
    public Object clone() {
        return (AbstractContent) super.clone();
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
         * get the primary key of the article
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENTS);
        PrimaryKey recordPk = this.getPrimaryKey();

        /*
         * retrieve the content information from the database
         */
        String selectStatement = "select content_typ_id, content_version.version_num, "
                + "content_version.version_info_id, content_version.request_id, "
                + "content.content_class_typ_id, "
                + "content.content_rule_typ_id, content.enabled_flag "
                + "from "
                + ds.getTableNameWithDefaultSchema("content_version")
                + ", "
                + ds.getTableNameWithDefaultSchema("content")
                + " "
                + "where content_version.content_id = ? "
                + "and content_version.current_flag = ? "
                + "and content.id = content_version.content_id ";
        // FIXME: hibernate select query

        try {
            /*
             * create the prepared statement
             */
            prepStmt = ds.getReadConnection().prepareStatement(selectStatement);

            /*
             * associate the id
             */
            prepStmt.setString(1, recordPk.toString());
            prepStmt.setString(2, "Y");

            /*
             * execute the query
             */
            rs = prepStmt.executeQuery();

            /*
             * loop the results
             */
            while (rs.next()) {
                this.setIsFound(true);
                this.contentTypeId = rs.getString(1);
                this.versionNum = rs.getInt(2);
                this.versionInfoId = rs.getString(3);
                this.requestId = rs.getString(4);
                this.contentClassTypeId = rs.getString(5);
                this.contentRuleTypeId = rs.getString(6);
                this.enabledFlag = rs.getString(7);
            }
        } catch (Exception e) {
            /*
             * log the exception
             */
            this.appendLoadTrace(e.getMessage());
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, recordPk);
        } finally {
            /*
             * close the recordset and the prepared statement
             */
            CloseUtil.closeResultSet(rs);
            CloseUtil.closePreparedStatement(prepStmt);
        }

        /*
         * load this write data bean
         */
        if (this.getIsFound()) {
            if (this.contentTypeId.equals(ContentTypesDictionary.ARTICLE)) {
                PrimaryKey derivedEntityPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(EntitiesDictionary.CONTENTS);
                super.load(derivedEntityPk, recordPk);
            } else if (this.contentTypeId.equals(ContentTypesDictionary.IMAGE)) {
                PrimaryKey derivedEntityPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(EntitiesDictionary.IMAGES);
                super.load(derivedEntityPk, recordPk);
            } else if (this.contentTypeId
                    .equals(ContentTypesDictionary.ARTICLE)) {
                PrimaryKey derivedEntityPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);
                super.load(derivedEntityPk, recordPk);
            } else {
                super.load(entityPk, recordPk);
            }
        }
    }

    /*
     * @see com.corendal.netapps.framework.workflow.interfaces.ProcessDefinition#getVersionNum()
     */
    public int getVersionNum() {
        return this.versionNum;
    }

    /*
     * @see com.corendal.netapps.framework.helpdesk.interfaces.NativeGroup#getInfoPrimaryKey()
     */
    public PrimaryKey getInfoPrimaryKey() {
        return PrimaryKeyUtil
                .getAlphanumericSingleKeyOrNull(this.versionInfoId);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Article#getLastRequestPrimaryKey()
     */
    public PrimaryKey getLastRequestPrimaryKey() {
        return PrimaryKeyUtil.getAlphanumericSingleKeyOrNull(this.requestId);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Article#getFirstRequestPrimaryKey()
     */
    public PrimaryKey getFirstRequestPrimaryKey() {
        /*
         * initialize the request id
         */
        String firstRequestId = null;

        /*
         * get the JDBC data session
         */
        HibernateDataSession ds = DataSessionSetGlobal.get()
                .getSharedHibernateDataSession(
                        DataSessionTypeConstants.WIKI_SHARED);

        /*
         * get the primary key of the article
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);
        PrimaryKey recordPk = this.getPrimaryKey();

        try {
            /*
             * get the query
             */
            Query query = ds.getReadNamedQuery(GET_FIRST_REQUEST_PRIMARY_KEY);

            /*
             * associate the query parameters
             */
            query.setParameter("contentId", recordPk.toString());
            query.setParameter("versionNumber", 1);

            /*
             * only get one version
             */
            query.setMaxResults(1);

            /*
             * get content version mapping
             */
            ContentVersionMapping contentVersionMapping = (ContentVersionMapping) query
                    .uniqueResult();

            /*
             * store values from mapping
             */
            if (contentVersionMapping != null) {
                firstRequestId = contentVersionMapping.getRequestId();
            }
        } catch (Exception e) {
            /*
             * log the exception
             */
            this.appendLoadTrace(e.getMessage());
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, recordPk);
        }

        /*
         * return
         */
        return PrimaryKeyUtil.getAlphanumericSingleKeyOrNull(firstRequestId);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Content#getTypePrimaryKey()
     */
    public PrimaryKey getTypePrimaryKey() {
        return PrimaryKeyUtil
                .getAlphanumericSingleKeyOrNull(this.contentTypeId);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Classified#getClassificationTypePrimaryKey()
     */
    public PrimaryKey getClassificationTypePrimaryKey() {
        return PrimaryKeyUtil
                .getAlphanumericSingleKeyOrNull(this.contentClassTypeId);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Classified#getRuleTypePrimaryKey()
     */
    public PrimaryKey getRuleTypePrimaryKey() {
        return PrimaryKeyUtil
                .getAlphanumericSingleKeyOrNull(this.contentRuleTypeId);
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Enabled#getEnabledFlag()
     */
    public String getEnabledFlag() {
        return this.enabledFlag;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Content#storeReference(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey)
     */
    public void storeReference(PrimaryKey parentDirectoryPk,
            PrimaryKey referenceRequestPk) {
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
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_MAPS);
        SingleKey recordPk = (SingleKey) getNextContentMapPrimaryKey();

        try {
            /*
             * get the hibernate session
             */
            Session session = ds.getWriteSession();

            /*
             * save the content map record
             */
            ContentMapMapping mapping = getContentMapMappingNewInstance();
            mapping.setId(recordPk.toString());
            if (parentDirectoryPk != null) {
                mapping.setParentContentId(parentDirectoryPk.toString());
            } else {
                mapping.setParentContentId(null);
            }
            mapping.setChildContentId(this.getPrimaryKey().toString());
            mapping.setMainFlag("N");
            if (referenceRequestPk != null) {
                mapping.setRequestId(referenceRequestPk.toString());
            } else {
                mapping.setRequestId(null);
            }
            mapping.setOrder(Integer.parseInt(recordPk.toString()));

            // makes
            // sure
            // this
            // reference
            // will
            // be
            // last
            mapping.setEnabledFlag("Y");
            session.save(mapping);
        } catch (Exception e) {
            /*
             * log the exception
             */
            this.setIsDone(false);
            this.appendStoreTrace(e.getMessage());
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, recordPk);
        }

        /*
         * refresh the content
         */
        if (this.getIsDone()) {
            PrimaryKey typePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(com.corendal.netapps.framework.core.constants.EntryLogTypeConstants.INSERT);
            this.addEntryLog(entityPk, recordPk, typePk,
                    DataSessionTypeConstants.WIKI_SHARED);
            this.clearCacheAndLoad();
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Content#removeReference(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey)
     */
    public void removeReference(PrimaryKey parentDirectoryPk,
            PrimaryKey referenceRequestPk) {
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
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_MAPS);
        PrimaryKey recordPk = this.getPrimaryKey();

        try {
            /*
             * get the query
             */
            Query query = ds.getWriteNamedQuery(REMOVE_REFERENCE);

            /*
             * associate the query parameters
             */
            query.setParameter("mainFlag", "N");
            query.setParameter("childContentId", recordPk.toString());
            query.setParameter("parentContentId", parentDirectoryPk.toString());

            /*
             * execute the query
             */
            query.executeUpdate();
        } catch (Exception e) {
            /*
             * log the exception
             */
            this.setIsDone(false);
            this.appendStoreTrace(e.getMessage());
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, recordPk);
        }

        /*
         * refresh the content
         */
        if (this.getIsDone()) {
            PrimaryKey typePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(com.corendal.netapps.framework.core.constants.EntryLogTypeConstants.DELETE);
            this.addEntryLog(entityPk, recordPk, typePk,
                    DataSessionTypeConstants.WIKI_SHARED);
        }
    }

    /**
     * Returns the extension id of this image based upon the file extension.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    private PrimaryKey getExtensionPrimaryKey() {
        /*
         * initialize the image id
         */
        String extensionId = null;

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
         * get the primary key of the stored file
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENTS);
        PrimaryKey recordPk = this.getPrimaryKey();

        /*
         * retrieve the stored file information from the database
         */
        String selectStatement = "select file_obj.extension_id " + "from "
                + ds.getTableNameWithDefaultSchema("content_version")
                + ", content,file_obj_role_xref, file_obj "
                + "where content_version.content_id = ? "
                + "and content.content_typ_id = ? "
                + "and content_version.current_flag = ? "
                + "and file_obj_role_xref.entity_id = ? "
                + "and file_obj_role_xref.role_id = ? "
                + "and content.id = content_version.content_id "
                + "and file_obj_role_xref.record_id = content_version.id "
                + "and file_obj_role_xref.file_obj_id = file_obj.id ";
        // FIXME: hibernate select query

        try {
            /*
             * create the prepared statement
             */
            prepStmt = ds.getReadConnection().prepareStatement(selectStatement);

            /*
             * associate the id
             */
            prepStmt.setString(1, recordPk.toString());
            prepStmt.setString(2, ContentTypesDictionary.IMAGE);
            prepStmt.setString(3, "Y");
            prepStmt.setString(4, EntitiesDictionary.IMAGE_VERSIONS);
            prepStmt.setString(5, RolesDictionary.ATTACHMENT);

            /*
             * execute the query
             */
            rs = prepStmt.executeQuery();

            /*
             * loop the results
             */
            while (rs.next()) {
                extensionId = rs.getString(1);
            }
        } catch (Exception e) {
            /*
             * log the exception
             */
            this.appendLoadTrace(e.getMessage());
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, recordPk);
        } finally {
            /*
             * close the recordset and the prepared statement
             */
            CloseUtil.closeResultSet(rs);
            CloseUtil.closePreparedStatement(prepStmt);
        }

        /*
         * return
         */
        return PrimaryKeyUtil.getAlphanumericSingleKeyOrNull(extensionId);
    }

    /**
     * Returns the next id for a new content map.
     * 
     * 
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    private static PrimaryKey getNextContentMapPrimaryKey() {
        return NextPrimaryKeyUtil.getNextSequentialAlphanumericSingleKey(
                "content_map", EntitiesDictionary.CONTENT_MAPS,
                DataSessionSetGlobal.get().getSharedHibernateDataSession(
                        DataSessionTypeConstants.WIKI_SHARED));
    }

    /**
     * Returns the direct icon id of this content based upon the file extension.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    private PrimaryKey getDirectExtensionIconPrimaryKey() {
        /*
         * get the extension id
         */
        PrimaryKey extensionPk = this.getExtensionPrimaryKey();

        /*
         * get the list of icon primary keys
         */
        if (extensionPk != null) {
            List<PrimaryKey> icons = IconFactory
                    .findEnabledPrimaryKeysByExtensionPrimaryKeyAndDirectFlagAndSecureFlag(
                            extensionPk, "Y", "N");

            /*
             * return
             */
            if (icons.size() > 0) {
                return icons.get(0);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Returns the indirect icon id of this content based upon the file
     * extension.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    private PrimaryKey getIndirectExtensionIconPrimaryKey() {
        /*
         * get the extension id
         */
        PrimaryKey extensionPk = this.getExtensionPrimaryKey();

        /*
         * get the list of icon primary keys
         */
        if (extensionPk != null) {
            List<PrimaryKey> icons = IconFactory
                    .findEnabledPrimaryKeysByExtensionPrimaryKeyAndDirectFlagAndSecureFlag(
                            extensionPk, "N", "N");

            /*
             * return
             */
            if (icons.size() > 0) {
                return icons.get(0);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Returns the secure direct icon id of this content based upon the file
     * extension.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    private PrimaryKey getSecureDirectExtensionIconPrimaryKey() {
        /*
         * get the extension id
         */
        PrimaryKey extensionPk = this.getExtensionPrimaryKey();

        /*
         * get the list of icon primary keys
         */
        if (extensionPk != null) {
            List<PrimaryKey> icons = IconFactory
                    .findEnabledPrimaryKeysByExtensionPrimaryKeyAndDirectFlagAndSecureFlag(
                            extensionPk, "Y", "Y");

            /*
             * return
             */
            if (icons.size() > 0) {
                return icons.get(0);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Returns the secure indirect icon id of this content based upon the file
     * extension.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    private PrimaryKey getSecureIndirectExtensionIconPrimaryKey() {
        /*
         * get the extension id
         */
        PrimaryKey extensionPk = this.getExtensionPrimaryKey();

        /*
         * get the list of icon primary keys
         */
        if (extensionPk != null) {
            List<PrimaryKey> icons = IconFactory
                    .findEnabledPrimaryKeysByExtensionPrimaryKeyAndDirectFlagAndSecureFlag(
                            extensionPk, "N", "Y");

            /*
             * return
             */
            if (icons.size() > 0) {
                return icons.get(0);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ResourceRequest#getDirectPreviewIconPrimaryKey()
     */
    public PrimaryKey getDirectPreviewIconPrimaryKey() {
        /*
         * get the preview icon based upon the content type
         */
        PrimaryKey articleContentTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.ARTICLE);

        if (this.getTypePrimaryKey().equals(articleContentTypePk)) {
            return PrimaryKeyUtil
                    .getAlphanumericSingleKey(IconsDictionary.DIRECT_ARTICLE);
        } else {
            /*
             * use the extension for images
             */
            PrimaryKey imagePk = this.getDirectExtensionIconPrimaryKey();

            if (imagePk != null) {
                return imagePk;
            } else {
                return PrimaryKeyUtil
                        .getAlphanumericSingleKey(IconsDictionary.DIRECT_UNKNOWN);
            }
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Previewed#getSecureDirectPreviewIconPrimaryKey()
     */
    public PrimaryKey getSecureDirectPreviewIconPrimaryKey() {
        /*
         * get the preview icon based upon the content type
         */
        PrimaryKey articleContentTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.ARTICLE);

        if (this.getTypePrimaryKey().equals(articleContentTypePk)) {
            return PrimaryKeyUtil
                    .getAlphanumericSingleKey(IconsDictionary.SECURE_DIRECT_ARTICLE);
        } else {
            /*
             * use the extension for images
             */
            PrimaryKey imagePk = this.getSecureDirectExtensionIconPrimaryKey();

            if (imagePk != null) {
                return imagePk;
            } else {
                return PrimaryKeyUtil
                        .getAlphanumericSingleKey(IconsDictionary.SECURE_DIRECT_UNKNOWN);
            }
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Previewed#getIndirectPreviewIconPrimaryKey()
     */
    public PrimaryKey getIndirectPreviewIconPrimaryKey() {
        /*
         * get the preview icon based upon the content type
         */
        PrimaryKey articleContentTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.ARTICLE);

        if (this.getTypePrimaryKey().equals(articleContentTypePk)) {
            return PrimaryKeyUtil
                    .getAlphanumericSingleKey(IconsDictionary.INDIRECT_ARTICLE);
        } else {
            /*
             * use the extension for images
             */
            PrimaryKey imagePk = this.getIndirectExtensionIconPrimaryKey();

            if (imagePk != null) {
                return imagePk;
            } else {
                return PrimaryKeyUtil
                        .getAlphanumericSingleKey(IconsDictionary.INDIRECT_UNKNOWN);
            }
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Previewed#getSecureIndirectPreviewIconPrimaryKey()
     */
    public PrimaryKey getSecureIndirectPreviewIconPrimaryKey() {
        /*
         * get the preview icon based upon the content type
         */
        PrimaryKey articleContentTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.ARTICLE);

        if (this.getTypePrimaryKey().equals(articleContentTypePk)) {
            return PrimaryKeyUtil
                    .getAlphanumericSingleKey(IconsDictionary.SECURE_INDIRECT_ARTICLE);
        } else {
            /*
             * use the extension for images
             */
            PrimaryKey imagePk = this
                    .getSecureIndirectExtensionIconPrimaryKey();

            if (imagePk != null) {
                return imagePk;
            } else {
                return PrimaryKeyUtil
                        .getAlphanumericSingleKey(IconsDictionary.SECURE_INDIRECT_UNKNOWN);
            }
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Content#storeContentOrder(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      int)
     */
    public void storeContentOrder(PrimaryKey parentArticlePk, int order) {
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
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENTS);

        try {
            /*
             * get the query
             */
            Query query = ds.getWriteNamedQuery(STORE_CONTENT_ORDER);

            /*
             * associate the query parameters
             */
            query.setParameter("ord", order);
            query.setParameter("childContentId", this.getPrimaryKey()
                    .toString());
            query.setParameter("parentContentId", parentArticlePk.toString());

            /*
             * execute the query
             */
            query.executeUpdate();
        } catch (Exception e) {
            /*
             * log the exception
             */
            this.setIsDone(false);
            this.appendStoreTrace(e.getMessage());
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk,
                    parentArticlePk);
        }

        /*
         * refresh the content
         */
        if (this.getIsDone()) {
            PrimaryKey typePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(com.corendal.netapps.framework.core.constants.EntryLogTypeConstants.UPDATE);
            this.addEntryLog(entityPk, parentArticlePk, typePk,
                    DataSessionTypeConstants.WIKI_SHARED);
            this.clearCacheAndLoad();
        }
    }

    /**
     * Returns the ordered list of all content primary keys for a keyword.
     * 
     * @param keyword
     *            keyword to find
     * 
     * 
     * @return a java.util.List object
     */
    public static final List<PrimaryKey> findEnabledByKeyword(String keyword) {
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
        String selectStatement = "select content.id, "
                + SQLUtil.getMatchSelectCall(ds, "content_info.nam") + " "
                + "from " + ds.getTableNameWithDefaultSchema("content") + ", "
                + ds.getTableNameWithDefaultSchema("content_version") + ", "
                + ds.getTableNameWithDefaultSchema("content_info") + " "
                + "where " + SQLUtil.getMatchWhereCall(ds, "content_info.nam")
                + " " + "and content_version.version_info_id= content_info.id "
                + "and content_version.content_id= content.id "
                + "and content_version.current_flag= ? "
                + "and content.enabled_flag= ? " + "union "
                + "select content.id, "
                + SQLUtil.getMatchSelectCall(ds, "content_info.dsc") + " "
                + "from " + ds.getTableNameWithDefaultSchema("content") + ", "
                + ds.getTableNameWithDefaultSchema("content_version") + ", "
                + ds.getTableNameWithDefaultSchema("content_info") + " "
                + "where " + SQLUtil.getMatchWhereCall(ds, "content_info.dsc")
                + " " + "and content_version.version_info_id= content_info.id "
                + "and content_version.content_id= content.id "
                + "and content_version.current_flag= ? "
                + "and content.enabled_flag= ? " + "order by 2 desc ";
        // FIXME: hibernate select query

        try {
            /*
             * create the prepared statement
             */
            prepStmt = ds.getReadConnection().prepareStatement(selectStatement);

            /*
             * associate the keyword
             */
            prepStmt.setString(1, keyword);
            prepStmt.setString(2, keyword);
            prepStmt.setString(3, "Y");
            prepStmt.setString(4, "Y");
            prepStmt.setString(5, keyword);
            prepStmt.setString(6, keyword);
            prepStmt.setString(7, "Y");
            prepStmt.setString(8, "Y");

            /*
             * execute the query
             */
            rs = prepStmt.executeQuery();

            /*
             * loop the results
             */
            while (rs.next()) {
                ScoredPrimaryKey scoredPk = PrimaryKeyUtil
                        .getAlphanumericScoredSingleKey(rs.getString(1));
                scoredPk.setScore(rs.getFloat(2));
                primaryKeys.add(scoredPk);
            }
        } catch (Exception e) {
            /*
             * log the exception
             */
            PrimaryKey entityPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntitiesDictionary.CONTENTS);
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, null);
        } finally {
            /*
             * close the recordset and the prepared statement
             */
            CloseUtil.closeResultSet(rs);
            CloseUtil.closePreparedStatement(prepStmt);
        }

        /*
         * return all content primary keys
         */
        return FullTextUtil.getPrimaryKeysWithoutDuplicates(primaryKeys);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Content#getMainParentPrimaryKey()
     */
    public PrimaryKey getMainParentPrimaryKey() {
        /*
         * initialize the parent id
         */
        PrimaryKey parentPrimaryKey = null;

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
         * get the primary key of the content
         */
        PrimaryKey recordPk = this.getPrimaryKey();

        /*
         * retrieve the article information from the database
         */
        String selectStatement = "select content_map.parent_content_id "
                + "from " + ds.getTableNameWithDefaultSchema("content_map")
                + ", " + ds.getTableNameWithDefaultSchema("content") + " "
                + "where content_map.child_content_id=? "
                + "and content_map.main_flag=? "
                + "and content.enabled_flag=? "
                + "and content_map.parent_content_id=content.id ";
        // FIXME: hibernate select query

        try {
            /*
             * create the prepared statement
             */
            prepStmt = ds.getReadConnection().prepareStatement(selectStatement);

            /*
             * associate the enabled flag
             */
            prepStmt.setString(1, recordPk.toString());
            prepStmt.setString(2, "Y");
            prepStmt.setString(3, "Y");

            /*
             * execute the query
             */
            rs = prepStmt.executeQuery();

            /*
             * loop the results
             */
            while (rs.next()) {
                parentPrimaryKey = PrimaryKeyUtil.getAlphanumericSingleKey(rs
                        .getString(1));
            }
        } catch (Exception e) {
            /*
             * log the exception
             */
            PrimaryKey entityPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntitiesDictionary.CONTENTS);
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, recordPk);
        } finally {
            /*
             * close the recordset and the prepared statement
             */
            CloseUtil.closeResultSet(rs);
            CloseUtil.closePreparedStatement(prepStmt);
        }

        /*
         * return the parent primary key
         */
        return parentPrimaryKey;
    }

    /**
     * Returns the ordered list of all content primary keys for an article
     * primary key.
     * 
     * @param articlePk
     *            primary key of the article to parse
     * 
     * 
     * @return a java.util.List object
     */
    public static final List<PrimaryKey> findEnabledByParentArticlePrimaryKey(
            PrimaryKey articlePk) {
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
        String selectStatement = "select content.id " + "from "
                + ds.getTableNameWithDefaultSchema("content") + ", "
                + ds.getTableNameWithDefaultSchema("content_map") + " "
                + "where content_map.parent_content_id=? "
                + "and content.enabled_flag=? "
                + "and content_map.child_content_id=content.id "
                + "order by content_map.ord asc";
        // FIXME: hibernate select query

        try {
            /*
             * create the prepared statement
             */
            prepStmt = ds.getReadConnection().prepareStatement(selectStatement);

            /*
             * associate the keyword
             */
            prepStmt.setString(1, articlePk.toString());
            prepStmt.setString(2, "Y");

            /*
             * execute the query
             */
            rs = prepStmt.executeQuery();

            /*
             * loop the results
             */
            while (rs.next()) {
                PrimaryKey pk = PrimaryKeyUtil.getAlphanumericSingleKey(rs
                        .getString(1));
                primaryKeys.add(pk);
            }
        } catch (Exception e) {
            /*
             * log the exception
             */
            PrimaryKey entityPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntitiesDictionary.CONTENTS);
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, null);
        } finally {
            /*
             * close the recordset and the prepared statement
             */
            CloseUtil.closeResultSet(rs);
            CloseUtil.closePreparedStatement(prepStmt);
        }

        /*
         * return all content primary keys
         */
        return primaryKeys;
    }

    /**
     * Returns the ordered list of all direct content primary keys for an
     * article primary key.
     * 
     * @param articlePk
     *            primary key of the article to parse
     * 
     * 
     * @return a java.util.List object
     */
    public static final List<PrimaryKey> findDirectEnabledByParentArticlePrimaryKey(
            PrimaryKey articlePk) {
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
        String selectStatement = "select content.id " + "from "
                + ds.getTableNameWithDefaultSchema("content") + ", "
                + ds.getTableNameWithDefaultSchema("content_map") + " "
                + "where content_map.parent_content_id=? "
                + "and content_map.main_flag=? "
                + "and content.enabled_flag=? "
                + "and content_map.child_content_id=content.id "
                + "order by content_map.ord asc";
        // FIXME: hibernate select query

        try {
            /*
             * create the prepared statement
             */
            prepStmt = ds.getReadConnection().prepareStatement(selectStatement);

            /*
             * associate the keyword
             */
            prepStmt.setString(1, articlePk.toString());
            prepStmt.setString(2, "Y");
            prepStmt.setString(3, "Y");

            /*
             * execute the query
             */
            rs = prepStmt.executeQuery();

            /*
             * loop the results
             */
            while (rs.next()) {
                PrimaryKey pk = PrimaryKeyUtil.getAlphanumericSingleKey(rs
                        .getString(1));
                primaryKeys.add(pk);
            }
        } catch (Exception e) {
            /*
             * log the exception
             */
            PrimaryKey entityPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntitiesDictionary.CONTENTS);
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, null);
        } finally {
            /*
             * close the recordset and the prepared statement
             */
            CloseUtil.closeResultSet(rs);
            CloseUtil.closePreparedStatement(prepStmt);
        }

        /*
         * return all content primary keys
         */
        return primaryKeys;
    }

    /**
     * Returns the ordered list of all enabled contents created during a date
     * interval.
     * 
     * @param createdDateFrom
     *            lower bound of the date interval
     * @param createdDateTo
     *            upper bound of the date interval
     * 
     * 
     * @return a java.util.List object
     */
    public static final List<PrimaryKey> findEnabledByBoundCreatedDate(
            java.util.Date createdDateFrom, java.util.Date createdDateTo) {
        /*
         * convert the created date into a timestamp
         */
        Timestamp createdDateTimestampFrom = TimestampUtil
                .getDateToTimestamp(createdDateFrom);
        Timestamp createdDateTimestampTo = TimestampUtil
                .getDateToTimestamp(createdDateTo);

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
        String selectStatement = "select distinct content.id, content.ord "
                + "from " + ds.getTableNameWithDefaultSchema("content") + ", "
                + ds.getTableNameWithDefaultSchema("entry_log") + " "
                + "where (entry_log.entity_id = ? or entry_log.entity_id = ?) "
                + "and entry_log.typ_id = ? " + "and entry_log.timestamp >= ? "
                + "and entry_log.timestamp < ? "
                + "and content.enabled_flag = ? "
                + "and content.id = entry_log.record_id "
                + "order by content.ord asc";
        // FIXME: hibernate select query

        try {
            /*
             * create the prepared statement
             */
            prepStmt = ds.getReadConnection().prepareStatement(selectStatement);

            /*
             * associate the enabled flag
             */
            prepStmt.setString(1, EntitiesDictionary.IMAGES);
            prepStmt.setString(2, EntitiesDictionary.ARTICLES);
            prepStmt
                    .setString(
                            3,
                            com.corendal.netapps.framework.core.constants.EntryLogTypeConstants.INSERT);
            prepStmt.setTimestamp(4, createdDateTimestampFrom);
            prepStmt.setTimestamp(5, createdDateTimestampTo);
            prepStmt.setString(6, "Y");

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
                    .getAlphanumericSingleKey(EntitiesDictionary.CONTENTS);
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, null);
        } finally {
            /*
             * close the recordset and the prepared statement
             */
            CloseUtil.closeResultSet(rs);
            CloseUtil.closePreparedStatement(prepStmt);
        }

        /*
         * return all image primary keys
         */
        return primaryKeys;
    }

    /**
     * Returns the ordered list of all enabled contents created after a date.
     * 
     * @param createdDate
     *            lower bound of the date
     * 
     * 
     * @return a java.util.List object
     */
    public static final List<PrimaryKey> findEnabledByLowerBoundCreatedDate(
            java.util.Date createdDate) {
        /*
         * convert the created date into a timestamp
         */
        Timestamp createdDateTimestamp = TimestampUtil
                .getDateToTimestamp(createdDate);

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
        String selectStatement = "select distinct content.id, content.ord "
                + "from " + ds.getTableNameWithDefaultSchema("content") + ", "
                + ds.getTableNameWithDefaultSchema("entry_log") + " "
                + "where (entry_log.entity_id = ? or entry_log.entity_id = ?) "
                + "and entry_log.typ_id = ? " + "and entry_log.timestamp >= ? "
                + "and content.enabled_flag = ? "
                + "and content.id = entry_log.record_id "
                + "order by content.ord asc";
        // FIXME: hibernate select query

        try {
            /*
             * create the prepared statement
             */
            prepStmt = ds.getReadConnection().prepareStatement(selectStatement);

            /*
             * associate the enabled flag
             */
            prepStmt.setString(1, EntitiesDictionary.IMAGES);
            prepStmt.setString(2, EntitiesDictionary.ARTICLES);
            prepStmt
                    .setString(
                            3,
                            com.corendal.netapps.framework.core.constants.EntryLogTypeConstants.INSERT);
            prepStmt.setTimestamp(4, createdDateTimestamp);
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
                    .getAlphanumericSingleKey(EntitiesDictionary.CONTENTS);
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, null);
        } finally {
            /*
             * close the recordset and the prepared statement
             */
            CloseUtil.closeResultSet(rs);
            CloseUtil.closePreparedStatement(prepStmt);
        }

        /*
         * return all image primary keys
         */
        return primaryKeys;
    }

    /**
     * Returns the ordered list of all enabled contents created after a date.
     * 
     * @param createdDate
     *            upper bound of the date
     * 
     * 
     * @return a java.util.List object
     */
    public static final List<PrimaryKey> findEnabledByUpperBoundCreatedDate(
            java.util.Date createdDate) {
        /*
         * convert the created date into a timestamp
         */
        Timestamp createdDateTimestamp = TimestampUtil
                .getDateToTimestamp(createdDate);

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
        String selectStatement = "select distinct content.id, content.ord "
                + "from " + ds.getTableNameWithDefaultSchema("content") + ", "
                + ds.getTableNameWithDefaultSchema("entry_log") + " "
                + "where (entry_log.entity_id = ? or entry_log.entity_id = ?) "
                + "and entry_log.typ_id = ? " + "and entry_log.timestamp < ? "
                + "and content.enabled_flag = ? "
                + "and content.id = entry_log.record_id "
                + "order by content.ord asc";
        // FIXME: hibernate select query

        try {
            /*
             * create the prepared statement
             */
            prepStmt = ds.getReadConnection().prepareStatement(selectStatement);

            /*
             * associate the enabled flag
             */
            prepStmt.setString(1, EntitiesDictionary.IMAGES);
            prepStmt.setString(2, EntitiesDictionary.ARTICLES);
            prepStmt
                    .setString(
                            3,
                            com.corendal.netapps.framework.core.constants.EntryLogTypeConstants.INSERT);
            prepStmt.setTimestamp(4, createdDateTimestamp);
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
                    .getAlphanumericSingleKey(EntitiesDictionary.CONTENTS);
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, null);
        } finally {
            /*
             * close the recordset and the prepared statement
             */
            CloseUtil.closeResultSet(rs);
            CloseUtil.closePreparedStatement(prepStmt);
        }

        /*
         * return all image primary keys
         */
        return primaryKeys;
    }

    /**
     * Returns the ordered list of all enabled contents modified during a date
     * interval.
     * 
     * @param modifiedDateFrom
     *            lower bound of the date interval
     * @param modifiedDateTo
     *            upper bound of the date interval
     * 
     * 
     * @return a java.util.List object
     */
    public static final List<PrimaryKey> findEnabledByBoundModifiedDate(
            java.util.Date modifiedDateFrom, java.util.Date modifiedDateTo) {
        /*
         * convert the created date into a timestamp
         */
        Timestamp modifiedDateTimestampFrom = TimestampUtil
                .getDateToTimestamp(modifiedDateFrom);
        Timestamp modifiedDateTimestampTo = TimestampUtil
                .getDateToTimestamp(modifiedDateTo);

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
        String selectStatement = "select distinct content.id, content.ord "
                + "from " + ds.getTableNameWithDefaultSchema("content") + ", "
                + ds.getTableNameWithDefaultSchema("entry_log") + " "
                + "where (entry_log.entity_id = ? or entry_log.entity_id = ?) "
                + "and entry_log.typ_id in (?, ?) "
                + "and entry_log.timestamp >= ? "
                + "and entry_log.timestamp < ? "
                + "and content.enabled_flag = ? "
                + "and content.id = entry_log.record_id "
                + "order by content.ord asc";
        // FIXME: hibernate select query

        try {
            /*
             * create the prepared statement
             */
            prepStmt = ds.getReadConnection().prepareStatement(selectStatement);

            /*
             * associate the enabled flag
             */
            prepStmt.setString(1, EntitiesDictionary.IMAGES);
            prepStmt.setString(2, EntitiesDictionary.ARTICLES);
            prepStmt
                    .setString(
                            3,
                            com.corendal.netapps.framework.core.constants.EntryLogTypeConstants.INSERT);
            prepStmt
                    .setString(
                            4,
                            com.corendal.netapps.framework.core.constants.EntryLogTypeConstants.MODIFY);
            prepStmt.setTimestamp(5, modifiedDateTimestampFrom);
            prepStmt.setTimestamp(6, modifiedDateTimestampTo);
            prepStmt.setString(7, "Y");

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
                    .getAlphanumericSingleKey(EntitiesDictionary.CONTENTS);
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, null);
        } finally {
            /*
             * close the recordset and the prepared statement
             */
            CloseUtil.closeResultSet(rs);
            CloseUtil.closePreparedStatement(prepStmt);
        }

        /*
         * return all image primary keys
         */
        return primaryKeys;
    }

    /**
     * Returns the ordered list of all enabled contents modified after a date.
     * 
     * @param modifiedDate
     *            lower bound of the date
     * 
     * 
     * @return a java.util.List object
     */
    public static final List<PrimaryKey> findEnabledByLowerBoundModifiedDate(
            java.util.Date modifiedDate) {
        /*
         * convert the created date into a timestamp
         */
        Timestamp modifiedDateTimestamp = TimestampUtil
                .getDateToTimestamp(modifiedDate);

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
        String selectStatement = "select distinct content.id, content.ord "
                + "from " + ds.getTableNameWithDefaultSchema("content") + ", "
                + ds.getTableNameWithDefaultSchema("entry_log") + " "
                + "where (entry_log.entity_id = ? or entry_log.entity_id = ?) "
                + "and entry_log.typ_id in (?, ?) "
                + "and entry_log.timestamp >= ? "
                + "and content.enabled_flag = ? "
                + "and content.id = entry_log.record_id "
                + "order by content.ord asc";
        // FIXME: hibernate select query

        try {
            /*
             * create the prepared statement
             */
            prepStmt = ds.getReadConnection().prepareStatement(selectStatement);

            /*
             * associate the enabled flag
             */
            prepStmt.setString(1, EntitiesDictionary.IMAGES);
            prepStmt.setString(2, EntitiesDictionary.ARTICLES);
            prepStmt
                    .setString(
                            3,
                            com.corendal.netapps.framework.core.constants.EntryLogTypeConstants.INSERT);
            prepStmt
                    .setString(
                            4,
                            com.corendal.netapps.framework.core.constants.EntryLogTypeConstants.MODIFY);
            prepStmt.setTimestamp(5, modifiedDateTimestamp);
            prepStmt.setString(6, "Y");

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
                    .getAlphanumericSingleKey(EntitiesDictionary.CONTENTS);
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, null);
        } finally {
            /*
             * close the recordset and the prepared statement
             */
            CloseUtil.closeResultSet(rs);
            CloseUtil.closePreparedStatement(prepStmt);
        }

        /*
         * return all image primary keys
         */
        return primaryKeys;
    }

    /**
     * Returns the ordered list of all enabled contents modified after a date.
     * 
     * @param modifiedDate
     *            upper bound of the date
     * 
     * 
     * @return a java.util.List object
     */
    public static final List<PrimaryKey> findEnabledByUpperBoundModifiedDate(
            java.util.Date modifiedDate) {
        /*
         * convert the created date into a timestamp
         */
        Timestamp modifiedDateTimestamp = TimestampUtil
                .getDateToTimestamp(modifiedDate);

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
        String selectStatement = "select distinct content.id, content.ord "
                + "from " + ds.getTableNameWithDefaultSchema("content") + ", "
                + ds.getTableNameWithDefaultSchema("entry_log") + " "
                + "where (entry_log.entity_id = ? or entry_log.entity_id = ?) "
                + "and entry_log.typ_id in (?, ?) "
                + "and entry_log.timestamp < ? "
                + "and content.enabled_flag = ? "
                + "and content.id = entry_log.record_id "
                + "order by content.ord asc";
        // FIXME: hibernate select query

        try {
            /*
             * create the prepared statement
             */
            prepStmt = ds.getReadConnection().prepareStatement(selectStatement);

            /*
             * associate the enabled flag
             */
            prepStmt.setString(1, EntitiesDictionary.IMAGES);
            prepStmt.setString(2, EntitiesDictionary.ARTICLES);
            prepStmt
                    .setString(
                            3,
                            com.corendal.netapps.framework.core.constants.EntryLogTypeConstants.INSERT);
            prepStmt
                    .setString(
                            4,
                            com.corendal.netapps.framework.core.constants.EntryLogTypeConstants.MODIFY);
            prepStmt.setTimestamp(5, modifiedDateTimestamp);
            prepStmt.setString(6, "Y");

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
                    .getAlphanumericSingleKey(EntitiesDictionary.CONTENTS);
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, null);
        } finally {
            /*
             * close the recordset and the prepared statement
             */
            CloseUtil.closeResultSet(rs);
            CloseUtil.closePreparedStatement(prepStmt);
        }

        /*
         * return all image primary keys
         */
        return primaryKeys;
    }

    /**
     * Returns a list of all contents for a content request primary key.
     * 
     * @param contentRequestPk
     *            primary key of the content request
     * 
     * 
     * @return a java.util.List object
     */
    public static final List<PrimaryKey> findEnabledByContentRequestPrimaryKey(
            PrimaryKey contentRequestPk) {
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
        String selectStatement = "select distinct content.id, content.ord "
                + "from " + ds.getTableNameWithDefaultSchema("content") + ", "
                + ds.getTableNameWithDefaultSchema("content_version") + " "
                + "where content_version.request_id = ? "
                + "and content_version.enabled_flag = ? "
                + "and content.enabled_flag = ? "
                + "and content_version.content_id = content.id "
                + "order by content.ord asc ";
        // FIXME: hibernate select query

        try {
            /*
             * create the prepared statement
             */
            prepStmt = ds.getReadConnection().prepareStatement(selectStatement);

            /*
             * associate the id
             */
            prepStmt.setString(1, contentRequestPk.toString());
            prepStmt.setString(2, "Y");
            prepStmt.setString(3, "Y");

            /*
             * execute the query
             */
            rs = prepStmt.executeQuery();

            /*
             * loop the results
             */
            while (rs.next()) {
                PrimaryKey pk = PrimaryKeyUtil.getAlphanumericSingleKey(rs
                        .getString(1));
                primaryKeys.add(pk);
            }
        } catch (Exception e) {
            /*
             * log the exception
             */
            PrimaryKey entityPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntitiesDictionary.CONTENTS);
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, null);
        } finally {
            /*
             * close the recordset and the prepared statement
             */
            CloseUtil.closeResultSet(rs);
            CloseUtil.closePreparedStatement(prepStmt);
        }

        /*
         * return all content primary keys
         */
        return primaryKeys;
    }

    /**
     * Returns the ordered list of all enabled content primary keys.
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
         * find the primary key of each content
         */
        try {
            /*
             * get the query
             */
            Query query = ds.getReadNamedQuery(FIND_BY_ENABLED_FLAG);

            /*
             * associate the enabled flag
             */
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
                    .getAlphanumericSingleKey(EntitiesDictionary.CONTENTS);
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, null);
        }

        /*
         * return all content primary keys
         */
        return primaryKeys;
    }
}

// end AbstractContent

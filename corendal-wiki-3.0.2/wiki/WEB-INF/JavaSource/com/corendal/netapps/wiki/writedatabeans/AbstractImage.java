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
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.corendal.netapps.framework.configuration.utils.CaseUtil;
import com.corendal.netapps.framework.core.databeans.AbstractWriteDataBean;
import com.corendal.netapps.framework.core.interfaces.EntryLogMapping;
import com.corendal.netapps.framework.core.readdatafactories.IconFactory;
import com.corendal.netapps.framework.core.utils.DateUtil;
import com.corendal.netapps.framework.core.utils.FullTextUtil;
import com.corendal.netapps.framework.core.writedatafactories.AccountRoleXrefFactory;
import com.corendal.netapps.framework.core.writedatafactories.StoredFileRoleXrefFactory;
import com.corendal.netapps.framework.helpdesk.writedatafactories.GroupRoleXrefFactory;
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
import com.corendal.netapps.wiki.interfaces.ContentMapMapping;
import com.corendal.netapps.wiki.interfaces.ContentMapping;
import com.corendal.netapps.wiki.interfaces.ContentVersionMapping;
import com.corendal.netapps.wiki.interfaces.Image;
import com.corendal.netapps.wiki.writedatamappings.DefaultContentMapMapping;
import com.corendal.netapps.wiki.writedatamappings.DefaultContentMapping;
import com.corendal.netapps.wiki.writedatamappings.DefaultContentVersionMapping;

/**
 * AbstractImage is the abstract class retrieving information about each image
 * of the application from the database.
 * 
 * @version $Id: AbstractImage.java,v 1.52 2008/02/21 16:14:54 tdanard Exp $
 */
public abstract class AbstractImage extends AbstractWriteDataBean implements
        Cloneable, Image {
    /** Fully qualified name of this class. */
    private static final String ABSTRACT_CLASS_NAME = "com.corendal.netapps.wiki.writedatabeans.AbstractImage";

    /** Version_num in the content_version table */
    private int versionNum;

    /** Version_info_id in the content_version table */
    private String versionInfoId;

    /** File_id in the file_obj_role_xref table */
    private String fileId;

    /** Request_id in the content_version table */
    private String requestId;

    /** Friendly address in the content_version table */
    private String friendlyAddress;

    /** Enabled_flag in the content table */
    private String enabledFlag;

    /** Author account_id from the the account_role_xref table */
    private String authorId;

    /** Content_class_typ_id in the content table */
    private String contentClassTypeId;

    /** Parent_content_id from the the content_map table */
    private String mainParentId;

    /** Id from the the content_version table */
    private String versionId;

    /** Content_rule_typ_id in the content table */
    private String contentRuleTypeId;

    /** Cmnt in the content_version table */
    private String comment;

    /**
     * Name of the named query for the findByEnabledFlag method.
     */
    private static final String FIND_BY_ENABLED_FLAG = ABSTRACT_CLASS_NAME
            + ".findByEnabledFlag";

    /**
     * Name of the named query for the storeParentContent point 1 method.
     */
    private static final String STORE_PARENT_CONTENT_1 = ABSTRACT_CLASS_NAME
            + ".storeParentContent.1";

    /**
     * Name of the named query for the storeParentContent point 2 method.
     */
    private static final String STORE_PARENT_CONTENT_2 = ABSTRACT_CLASS_NAME
            + ".storeParentContent.2";

    /**
     * Name of the named query for the remove point 1 method.
     */
    private static final String REMOVE_1 = ABSTRACT_CLASS_NAME + ".remove.1";

    /**
     * Name of the named query for the supersede method.
     */
    private static final String SUPERSEDE = ABSTRACT_CLASS_NAME + ".supersede";

    /**
     * Name of the named query for the storeInfo method.
     */
    private static final String STORE_INFO = ABSTRACT_CLASS_NAME + ".storeInfo";

    /**
     * Name of the named query for the storeFriendlyAddress method.
     */
    private static final String STORE_FRIENDLY_ADDRESS = ABSTRACT_CLASS_NAME
            + ".storeFriendlyAddress";

    /**
     * Name of the named query for the remove point 2 method.
     */
    private static final String REMOVE_2 = ABSTRACT_CLASS_NAME + ".remove.2";

    /**
     * Name of the named query for the remove point 3 method.
     */
    private static final String REMOVE_3 = ABSTRACT_CLASS_NAME + ".remove.3";

    /**
     * Name of the named query for the storeComment method.
     */
    private static final String STORE_COMMENT = ABSTRACT_CLASS_NAME
            + ".storeComment";

    /**
     * Name of the named query for the find enabled by creation order method.
     */
    private static final String FIND_ENABLED_BY_CREATION_ORDER = "com.corendal.netapps.framework.core.writedatabeans.AbstractEntryLog"
            + ".findByLowerBoundTimestampAndEntityIdAndTypeId";

    /**
     * Name of the named query for the getFirstRequestPrimaryKey method.
     */
    private static final String GET_FIRST_REQUEST_PRIMARY_KEY = ABSTRACT_CLASS_NAME
            + ".getFirstRequestPrimaryKey";

    /**
     * Name of the named query for the load method.
     */
    private static final String LOAD = ABSTRACT_CLASS_NAME + ".load";

    /**
     * Name of the named query for the findEnabledByRank point 1 method.
     */
    private static final String FIND_ENABLED_BY_RANK_1 = ABSTRACT_CLASS_NAME
            + ".findEnabledByRank.1";

    /**
     * Default class constructor.
     */
    protected AbstractImage() {
        super(DataSessionTypeConstants.WIKI_SHARED);
        this.setModifyType(EntryLogTypeConstants.SUPERSEDE, true);
    }

    /**
     * Returns a new instance of a content version mapping.
     * 
     * @return a com.corendal.netapps.wiki.interfaces.ContentVerison object
     */
    protected ContentVersionMapping getContentVersionMappingNewInstance() {
        return new DefaultContentVersionMapping();
    }

    /**
     * Returns a new instance of a content mapping.
     * 
     * @return a com.corendal.netapps.wiki.interfaces.Content object
     */
    protected ContentMapping getContentMappingNewInstance() {
        return new DefaultContentMapping();
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
        return (AbstractImage) super.clone();
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
         * get the primary key of the image
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.IMAGES);
        PrimaryKey recordPk = this.getPrimaryKey();

        /*
         * retrieve the image information from the database
         */
        String selectStatement = "select content_version.version_num, "
                + "content_version.version_info_id, content_version.request_id, "
                + "content_version.friendly_address, "
                + "content.content_class_typ_id, content_version.id, "
                + "content.content_rule_typ_id, content_version.cmnt, content.enabled_flag "
                + "from " + ds.getTableNameWithDefaultSchema("content_version")
                + ", " + ds.getTableNameWithDefaultSchema("content") + " "
                + "where content_version.content_id = ? "
                + "and content.content_typ_id = ? "
                + "and content_version.current_flag = ? "
                + "and content.id = content_version.content_id ";
        // FIXME: hibernate select query

        try {
            /*
             * create the prepared statement
             */
            prepStmt = ds.getReadConnection().prepareStatement(selectStatement);

            /*
             * associate the id and the image type
             */
            prepStmt.setString(1, recordPk.toString());
            prepStmt.setString(2, ContentTypesDictionary.IMAGE);
            prepStmt.setString(3, "Y");

            /*
             * execute the query
             */
            rs = prepStmt.executeQuery();

            /*
             * loop the results
             */
            while (rs.next()) {
                this.setIsFound(true);
                this.versionNum = rs.getInt(1);
                this.versionInfoId = rs.getString(2);
                this.requestId = rs.getString(3);
                this.friendlyAddress = rs.getString(4);
                this.contentClassTypeId = rs.getString(5);
                this.versionId = rs.getString(6);
                this.contentRuleTypeId = rs.getString(7);
                this.comment = ds.getStringFromCharacterStream(rs, 8);
                this.enabledFlag = rs.getString(9);
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
         * retrieve the parent information from the database
         */
        if (this.getIsFound()) {
            /*
             * retrieve the parent information from the database
             */
            try {
                /*
                 * get the query
                 */
                Query query = ds.getReadNamedQuery(LOAD);

                /*
                 * associate the query parameters
                 */
                query.setParameter("childContentId", recordPk.toString());
                query.setParameter("mainFlag", "Y");

                /*
                 * only get one content map
                 */
                query.setMaxResults(1);

                /*
                 * get content map mapping
                 */
                ContentMapMapping contentMapMapping = (ContentMapMapping) query
                        .uniqueResult();

                /*
                 * store values from mapping
                 */
                if (contentMapMapping != null) {
                    this.mainParentId = contentMapMapping.getParentContentId();
                }
            } catch (Exception e) {
                /*
                 * log the exception
                 */
                this.appendLoadTrace(e.getMessage());
                LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, recordPk);
            }
        }

        /*
         * retrieve the author information from the database
         */
        if (this.getIsFound()) {
            selectStatement = "select account_role_xref.account_id " + "from "
                    + ds.getTableNameWithDefaultSchema("account_role_xref")
                    + ", "
                    + ds.getTableNameWithDefaultSchema("content_version") + " "
                    + "where content_version.version_num= ? "
                    + "and content_version.content_id = ? "
                    + "and account_role_xref.role_id = ? "
                    + "and account_role_xref.entity_id = ? "
                    + "and content_version.id=account_role_xref.record_id";
            // FIXME: hibernate select query

            try {
                /*
                 * create the prepared statement
                 */
                prepStmt = ds.getReadConnection().prepareStatement(
                        selectStatement);

                /*
                 * associate the id, the version number and the editor role
                 */
                prepStmt.setInt(1, this.versionNum);
                prepStmt.setString(2, recordPk.toString());
                prepStmt.setString(3, RolesDictionary.AUTHOR);
                prepStmt.setString(4, EntitiesDictionary.IMAGE_VERSIONS);

                /*
                 * execute the query
                 */
                rs = prepStmt.executeQuery();

                /*
                 * loop the results
                 */
                this.authorId = null;

                while (rs.next()) {
                    this.authorId = rs.getString(1);
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
        }

        /*
         * retrieve the file information from the database
         */
        if (this.getIsFound()) {
            selectStatement = "select file_obj_role_xref.file_obj_id "
                    + "from "
                    + ds.getTableNameWithDefaultSchema("file_obj_role_xref")
                    + ", content_version "
                    + "where content_version.version_num= ? "
                    + "and content_version.content_id = ? "
                    + "and file_obj_role_xref.role_id = ? "
                    + "and file_obj_role_xref.entity_id = ? "
                    + "and content_version.id=file_obj_role_xref.record_id";
            // FIXME: hibernate select query

            try {
                /*
                 * create the prepared statement
                 */
                prepStmt = ds.getReadConnection().prepareStatement(
                        selectStatement);

                /*
                 * associate the id, the version number and the editor role
                 */
                prepStmt.setInt(1, this.versionNum);
                prepStmt.setString(2, recordPk.toString());
                prepStmt.setString(3, RolesDictionary.ATTACHMENT);
                prepStmt.setString(4, EntitiesDictionary.IMAGE_VERSIONS);

                /*
                 * execute the query
                 */
                rs = prepStmt.executeQuery();

                /*
                 * loop the results
                 */
                this.fileId = null;

                while (rs.next()) {
                    this.fileId = rs.getString(1);
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
        }

        /*
         * load this write data bean
         */
        if (this.getIsFound()) {
            super.load(entityPk, recordPk);
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Image#setFriendlyAddressAndLoad(java.lang.String)
     */
    public void setFriendlyAddressAndLoad(String friendlyAddress) {
        this.friendlyAddress = CaseUtil
                .getLowerCaseDeleteAccents(friendlyAddress);
        this.loadByFriendlyAddress();
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Image#loadByFriendlyAddress()
     */
    public void loadByFriendlyAddress() {
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
         * find the content id using the friendly address
         */
        String selectStatement = "select content.id " + "from "
                + ds.getTableNameWithDefaultSchema("content") + ", "
                + ds.getTableNameWithDefaultSchema("content_version") + " "
                + "where content_version.friendly_address= ? "
                + "and content_version.current_flag= ? "
                + "and content.enabled_flag = ? "
                + "and content.content_typ_id= ? "
                + "and content_version.content_id=content.id ";
        // FIXME: hibernate select query

        try {
            /*
             * create the prepared statement
             */
            prepStmt = ds.getReadConnection().prepareStatement(selectStatement);

            /*
             * associate the id
             */
            prepStmt.setString(1, this.friendlyAddress);
            prepStmt.setString(2, "Y");
            prepStmt.setString(3, "Y");
            prepStmt.setString(4, ContentTypesDictionary.IMAGE);

            /*
             * execute the query
             */
            rs = prepStmt.executeQuery();

            /*
             * loop the results
             */
            while (rs.next()) {
                this.setIsFound(true);
                this.setPrimaryKey(PrimaryKeyUtil.getAlphanumericSingleKey(rs
                        .getString(1)));
            }
        } catch (Exception e) {
            /*
             * log the exception
             */
            this.appendLoadTrace(e.getMessage());

            PrimaryKey entityPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntitiesDictionary.IMAGES);
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, null);
        } finally {
            /*
             * close the recordset and the prepared statement
             */
            CloseUtil.closeResultSet(rs);
            CloseUtil.closePreparedStatement(prepStmt);
        }

        /*
         * load the other attributes
         */
        if (this.getIsFound()) {
            this.load();
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Image#getFriendlyAddress()
     */
    public String getFriendlyAddress() {
        return this.friendlyAddress;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Image#getComment()
     */
    public String getComment() {
        return this.comment;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Classified#getClassificationTypePrimaryKey()
     */
    public PrimaryKey getClassificationTypePrimaryKey() {
        return PrimaryKeyUtil
                .getAlphanumericSingleKeyOrNull(this.contentClassTypeId);
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
                .getAlphanumericSingleKey(EntitiesDictionary.IMAGES);
        PrimaryKey recordPk = this.getPrimaryKey();

        /*
         * retrieve the stored file information from the database
         */
        String selectStatement = "select file_obj.extension_id " + "from "
                + ds.getTableNameWithDefaultSchema("content_version")
                + ", content, file_obj_role_xref, file_obj "
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
     * Returns the direct icon id of this image based upon the file extension.
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
     * Returns the indirect icon id of this image based upon the file extension.
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
     * Returns the secure direct icon id of this image based upon the file
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
     * Returns the secure indirect icon id of this image based upon the file
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
        PrimaryKey iconPk = this.getDirectExtensionIconPrimaryKey();

        if (iconPk != null) {
            return iconPk;
        } else {
            return PrimaryKeyUtil
                    .getAlphanumericSingleKey(IconsDictionary.DIRECT_UNKNOWN);
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Previewed#getIndirectPreviewIconPrimaryKey()
     */
    public PrimaryKey getIndirectPreviewIconPrimaryKey() {
        PrimaryKey iconPk = this.getIndirectExtensionIconPrimaryKey();

        if (iconPk != null) {
            return iconPk;
        } else {
            return PrimaryKeyUtil
                    .getAlphanumericSingleKey(IconsDictionary.INDIRECT_UNKNOWN);
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Previewed#getSecureDirectPreviewIconPrimaryKey()
     */
    public PrimaryKey getSecureDirectPreviewIconPrimaryKey() {
        PrimaryKey iconPk = this.getSecureDirectExtensionIconPrimaryKey();

        if (iconPk != null) {
            return iconPk;
        } else {
            return PrimaryKeyUtil
                    .getAlphanumericSingleKey(IconsDictionary.SECURE_DIRECT_UNKNOWN);
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Previewed#getSecureIndirectPreviewIconPrimaryKey()
     */
    public PrimaryKey getSecureIndirectPreviewIconPrimaryKey() {
        PrimaryKey iconPk = this.getSecureIndirectExtensionIconPrimaryKey();

        if (iconPk != null) {
            return iconPk;
        } else {
            return PrimaryKeyUtil
                    .getAlphanumericSingleKey(IconsDictionary.SECURE_INDIRECT_UNKNOWN);
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Image#getAuthorAccountPrimaryKey()
     */
    public PrimaryKey getAuthorAccountPrimaryKey() {
        return PrimaryKeyUtil.getAlphanumericSingleKeyOrNull(this.authorId);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Image#getMainParentPrimaryKey()
     */
    public PrimaryKey getMainParentPrimaryKey() {
        return PrimaryKeyUtil.getAlphanumericSingleKeyOrNull(this.mainParentId);
    }

    /*
     * @see com.corendal.netapps.framework.workflow.interfaces.ProcessDefinition#getVersionPrimaryKey()
     */
    public PrimaryKey getVersionPrimaryKey() {
        return PrimaryKeyUtil.getAlphanumericSingleKeyOrNull(this.versionId);
    }

    /*
     * @see com.corendal.netapps.framework.workflow.interfaces.ProcessDefinition#getVersionNum()
     */
    public int getVersionNum() {
        return this.versionNum;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Image#getFilePrimaryKey()
     */
    public PrimaryKey getFilePrimaryKey() {
        return PrimaryKeyUtil.getAlphanumericSingleKeyOrNull(this.fileId);
    }

    /*
     * @see com.corendal.netapps.framework.helpdesk.interfaces.NativeGroup#getInfoPrimaryKey()
     */
    public PrimaryKey getInfoPrimaryKey() {
        return PrimaryKeyUtil
                .getAlphanumericSingleKeyOrNull(this.versionInfoId);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Image#getLastRequestPrimaryKey()
     */
    public PrimaryKey getLastRequestPrimaryKey() {
        return PrimaryKeyUtil.getAlphanumericSingleKeyOrNull(this.requestId);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Image#getFirstRequestPrimaryKey()
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
         * get the primary key of the image
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.IMAGES);
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

    /**
     * Returns the ordered list of all image primary keys for a keyword.
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
         * retrieve the image information from the database
         */
        String selectStatement = "select content.id, "
                + SQLUtil.getMatchSelectCall(ds, "content_info.nam") + " "
                + "from " + ds.getTableNameWithDefaultSchema("content") + ", "
                + ds.getTableNameWithDefaultSchema("content_version") + ", "
                + ds.getTableNameWithDefaultSchema("content_info") + " "
                + "where content.content_typ_id= ? " + "and "
                + SQLUtil.getMatchWhereCall(ds, "content_info.nam") + " "
                + "and content_version.version_info_id= content_info.id "
                + "and content_version.content_id= content.id "
                + "and content_version.current_flag= ? "
                + "and content.enabled_flag= ? " + "union "
                + "select content.id, "
                + SQLUtil.getMatchSelectCall(ds, "content_info.dsc") + " "
                + "from " + ds.getTableNameWithDefaultSchema("content") + ", "
                + ds.getTableNameWithDefaultSchema("content_version") + ", "
                + ds.getTableNameWithDefaultSchema("content_info") + " "
                + "where content.content_typ_id= ? " + "and "
                + SQLUtil.getMatchWhereCall(ds, "content_info.dsc") + " "
                + "and content_version.version_info_id= content_info.id "
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
            prepStmt.setString(2, ContentTypesDictionary.IMAGE);
            prepStmt.setString(3, keyword);
            prepStmt.setString(4, "Y");
            prepStmt.setString(5, "Y");
            prepStmt.setString(6, keyword);
            prepStmt.setString(7, ContentTypesDictionary.IMAGE);
            prepStmt.setString(8, keyword);
            prepStmt.setString(9, "Y");
            prepStmt.setString(10, "Y");

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
                    .getAlphanumericSingleKey(EntitiesDictionary.IMAGES);
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
        return FullTextUtil.getPrimaryKeysWithoutDuplicates(primaryKeys);
    }

    /**
     * Returns the next id for a new content.
     * 
     * 
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    private static PrimaryKey getNextContentPrimaryKey() {
        return NextPrimaryKeyUtil.getNextSequentialAlphanumericSingleKey(
                "content", EntitiesDictionary.IMAGES, DataSessionSetGlobal
                        .get().getSharedHibernateDataSession(
                                DataSessionTypeConstants.WIKI_SHARED));
    }

    /**
     * Returns the next id for a new content version.
     * 
     * 
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    private static PrimaryKey getNextContentVersionPrimaryKey() {
        return NextPrimaryKeyUtil.getNextSequentialAlphanumericSingleKey(
                "content_version", EntitiesDictionary.IMAGES,
                DataSessionSetGlobal.get().getSharedHibernateDataSession(
                        DataSessionTypeConstants.WIKI_SHARED));
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
                "content_map", EntitiesDictionary.IMAGES, DataSessionSetGlobal
                        .get().getSharedHibernateDataSession(
                                DataSessionTypeConstants.WIKI_SHARED));
    }

    /**
     * Creates a image in the database.
     * 
     * @param contentInfoPk
     *            primary key of a content info
     * @param authorPk
     *            primary key of an author account
     * @param filePk
     *            primary key of a stored file
     * @param parentContentPk
     *            primary key of a parent article
     * @param contentRequestPk
     *            primary key of a content request
     * @param classificationTypePk
     *            primary key of a classification type
     * @param ruleTypePk
     *            primary key of a rule type
     * @param comment
     *            a comment
     * 
     * 
     */
    public void add(PrimaryKey contentInfoPk, PrimaryKey authorPk,
            PrimaryKey filePk, PrimaryKey parentContentPk,
            PrimaryKey contentRequestPk, PrimaryKey classificationTypePk,
            PrimaryKey ruleTypePk, String comment) {
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
         * get the images entity
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.IMAGES);

        /*
         * find the next id
         */
        SingleKey recordPk1 = (SingleKey) getNextContentPrimaryKey();
        SingleKey recordPk2 = (SingleKey) getNextContentVersionPrimaryKey();

        try {
            /*
             * get the hibernate session
             */
            Session session = ds.getWriteSession();

            /*
             * save the content copy record
             */
            ContentMapping mapping = getContentMappingNewInstance();
            mapping.setId(recordPk1.toString());
            mapping.setContentTypeId(ContentTypesDictionary.IMAGE);
            if (classificationTypePk != null) {
                mapping.setContentClassTypeId(classificationTypePk.toString());
            } else {
                mapping.setContentClassTypeId(null);
            }
            mapping.setWhatsNewFlag("N");
            if (ruleTypePk != null) {
                mapping.setContentRuleTypeId(ruleTypePk.toString());
            } else {
                mapping.setContentRuleTypeId(null);
            }
            mapping.setOrder(Integer.parseInt(recordPk1.toString()));
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
         * insert into the content_version table
         */
        if (this.getIsDone()) {
            try {
                /*
                 * get the hibernate session
                 */
                Session session = ds.getWriteSession();

                /*
                 * save the content version record
                 */
                ContentVersionMapping mapping = getContentVersionMappingNewInstance();
                mapping.setId(recordPk2.toString());
                mapping.setCurrentFlag("Y");
                mapping.setVersionNumber(1);
                if (contentInfoPk != null) {
                    mapping.setVersionInfoId(contentInfoPk.toString());
                } else {
                    mapping.setVersionInfoId(null);
                }
                mapping.setContentId(recordPk1.toString());
                if (contentRequestPk != null) {
                    mapping.setRequestId(contentRequestPk.toString());
                } else {
                    mapping.setRequestId(null);
                }
                mapping.setComment(comment);
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
        }

        /*
         * insert into the content_map table
         */
        if (this.getIsDone()) {
            /*
             * find the next id
             */
            SingleKey recordPk3 = (SingleKey) getNextContentMapPrimaryKey();

            try {
                /*
                 * get the hibernate session
                 */
                Session session = ds.getWriteSession();

                /*
                 * save the content map record
                 */
                ContentMapMapping mapping = getContentMapMappingNewInstance();
                mapping.setId(recordPk3.toString());
                if (parentContentPk != null) {
                    mapping.setParentContentId(parentContentPk.toString());
                } else {
                    mapping.setParentContentId(null);
                }
                mapping.setChildContentId(recordPk1.toString());
                mapping.setMainFlag("Y");
                mapping.setOrder(Integer.parseInt(recordPk3.toString()));

                // makes
                // sure
                // this
                // article
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
                LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, null);
            }
        }

        /*
         * get the images entity
         */
        PrimaryKey entityPk2 = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.IMAGE_VERSIONS);

        /*
         * insert the author role
         */
        if (this.getIsDone()) {
            PrimaryKey authorRolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.AUTHOR);
            AccountRoleXrefFactory.create(this, ds, entityPk2, authorRolePk,
                    recordPk2, authorPk);
        }

        /*
         * insert into the file_obj_role_xref table
         */
        if (this.getIsDone()) {
            PrimaryKey rolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.ATTACHMENT);
            StoredFileRoleXrefFactory.create(this, ds, entityPk2, rolePk,
                    recordPk2, filePk);
        }

        /*
         * add an entry log and load the image information
         */
        if (this.getIsDone()) {
            PrimaryKey typePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(com.corendal.netapps.framework.core.constants.EntryLogTypeConstants.INSERT);
            this.addEntryLog(entityPk, recordPk1, typePk,
                    DataSessionTypeConstants.WIKI_SHARED);
            this.setPrimaryKeyClearCacheAndLoad(recordPk1);
        }
    }

    /**
     * Returns an ordered list of all document primary keys that have been
     * created recently. Only the documents created the last 7 days are listed.
     * 
     * @param cnt
     *            number of rows to return
     * 
     * 
     * @return a java.util.List object
     */
    public static final List<PrimaryKey> findEnabledByCreationOrder(int cnt) {
        /*
         * get the time limit
         */
        java.util.Date today = DateUtil.getTruncated(new java.util.Date());
        java.util.Date createdDateFrom = DateUtil.getRemovedDays(today, 7);
        Timestamp createdDateTimestampFrom = TimestampUtil
                .getDateToTimestamp(createdDateFrom);

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

        try {
            /*
             * get the hibernate session
             */
            Session session = ds.getReadSession();

            /*
             * get the query
             */
            Query query1 = ds.getReadNamedQuery(FIND_ENABLED_BY_CREATION_ORDER);

            /*
             * associate the id
             */
            query1.setParameter("timestamp", createdDateTimestampFrom);
            query1.setParameter("entityId", EntitiesDictionary.IMAGES);
            query1
                    .setParameter(
                            "typeId",
                            com.corendal.netapps.framework.core.constants.EntryLogTypeConstants.INSERT);

            /*
             * excute the query
             */
            Iterator it = query1.list().iterator();

            /*
             * initialize the list of record ids looked at
             */
            List<String> pastRecordIds = new ArrayList<String>();

            /*
             * loop the results
             */
            int itemCount = 0;

            while ((it.hasNext()) && (itemCount < cnt)) {

                /*
                 * get the record id
                 */
                EntryLogMapping currentMapping = (EntryLogMapping) it.next();
                String currentRecordId = currentMapping.getRecordId();

                /*
                 * check that the record id was not already looked at
                 */
                if (!(pastRecordIds.contains(currentRecordId))) {

                    /*
                     * load a matching document
                     */
                    ContentMapping mapping = (ContentMapping) session.get(
                            DefaultContentMapping.class, currentRecordId);

                    /*
                     * check that the id matches a document
                     */
                    if (mapping != null) {
                        if ((mapping.getContentTypeId() != null)
                                && (mapping.getContentTypeId()
                                        .equals(ContentTypesDictionary.IMAGE))) {
                            PrimaryKey currentPk = PrimaryKeyUtil
                                    .getAlphanumericSingleKey(mapping.getId());
                            primaryKeys.add(currentPk);
                        }
                    }

                    /*
                     * add the record id to the list
                     */
                    pastRecordIds.add(currentRecordId);
                }
            }
        } catch (Exception e) {
            /*
             * log the exception
             */
            PrimaryKey entityPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntitiesDictionary.IMAGES);
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, null);
        }

        /*
         * return all document primary keys
         */
        return primaryKeys;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Image#supersede(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey)
     */
    public void supersede(PrimaryKey contentRequestPk) {
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
         * get the images entity
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.IMAGES);

        /*
         * find the next id
         */
        SingleKey recordPk2 = (SingleKey) getNextContentVersionPrimaryKey();

        try {
            /*
             * get the query
             */
            Query query = ds.getWriteNamedQuery(SUPERSEDE);

            /*
             * associate the query parameters
             */
            query.setParameter("currentFlag", "N");
            query.setParameter("contentId", this.getPrimaryKey().toString());

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
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, this
                    .getPrimaryKey());
        }

        /*
         * insert into the content_version table
         */
        if (this.getIsDone()) {
            try {
                /*
                 * get the hibernate session
                 */
                Session session = ds.getWriteSession();

                /*
                 * save the content version record
                 */
                ContentVersionMapping mapping = getContentVersionMappingNewInstance();
                mapping.setId(recordPk2.toString());
                mapping.setCurrentFlag("Y");
                mapping.setVersionNumber(this.getVersionNum() + 1);
                mapping.setVersionInfoId(this.getInfoPrimaryKey().toString());
                mapping.setContentId(this.getPrimaryKey().toString());
                if (contentRequestPk != null) {
                    mapping.setRequestId(contentRequestPk.toString());
                } else {
                    mapping.setRequestId(null);
                }
                mapping.setFriendlyAddress(this.getFriendlyAddress());
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
        }

        /*
         * get the images entity
         */
        PrimaryKey entityPk2 = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.IMAGE_VERSIONS);

        /*
         * insert the author role
         */
        if ((this.getIsDone()) && (this.authorId != null)) {
            PrimaryKey authorRolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.AUTHOR);
            PrimaryKey authorPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(this.authorId);
            AccountRoleXrefFactory.create(this, ds, entityPk2, authorRolePk,
                    recordPk2, authorPk);
        }

        /*
         * insert into the file_obj_role_xref table
         */
        if (this.getIsDone()) {
            PrimaryKey rolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.ATTACHMENT);
            PrimaryKey versionPk = this.getFilePrimaryKey();
            StoredFileRoleXrefFactory.create(this, ds, entityPk2, rolePk,
                    recordPk2, versionPk);
        }

        /*
         * refresh the image
         */
        if (this.getIsDone()) {
            PrimaryKey typePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntryLogTypeConstants.SUPERSEDE);
            this.addEntryLog(entityPk, this.getPrimaryKey(), typePk,
                    DataSessionTypeConstants.WIKI_SHARED);
            this.clearCacheAndLoad();
        }
    }

    /*
     * stores the author of this image.
     */
    public void storeAuthor(PrimaryKey authorPk) {
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
         * get the primary key of this image entity
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.IMAGES);

        /*
         * get the primary key of this notification action entity
         */
        PrimaryKey entityPk2 = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.IMAGE_VERSIONS);
        PrimaryKey authorRolePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(RolesDictionary.AUTHOR);
        PrimaryKey recordPk = this.getPrimaryKey();

        /*
         * remove account role xref from the database
         */
        AccountRoleXrefFactory.remove(this, ds, entityPk2, authorRolePk,
                recordPk);

        /*
         * add the author record
         */
        if (this.getIsDone()) {
            AccountRoleXrefFactory.create(this, ds, entityPk2, authorRolePk,
                    recordPk, authorPk);
        }

        /*
         * refresh the image
         */
        if (this.getIsDone()) {
            PrimaryKey typePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(com.corendal.netapps.framework.core.constants.EntryLogTypeConstants.UPDATE);
            this.addEntryLog(entityPk, this.getPrimaryKey(), typePk,
                    DataSessionTypeConstants.WIKI_SHARED);
            this.clearCacheAndLoad();
        }
    }

    /*
     * stores the parent content of this image.
     */
    public void storeParentContent(PrimaryKey parentContentPk) {
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
         * get the primary key of this image entity
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.IMAGES);

        try {
            /*
             * get the query
             */
            Query query = ds.getWriteNamedQuery(STORE_PARENT_CONTENT_1);

            /*
             * associate the query parameters
             */
            query.setParameter("mainFlag", "Y");
            query.setParameter("childContentId", this.getPrimaryKey()
                    .toString());

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
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, this
                    .getPrimaryKey());
        }

        if (this.getIsDone()) {
            try {
                /*
                 * get the query
                 */
                Query query = ds.getWriteNamedQuery(STORE_PARENT_CONTENT_2);

                /*
                 * associate the query parameters
                 */
                query.setParameter("mainFlag", "Y");
                query.setParameter("childContentId", this.getPrimaryKey()
                        .toString());
                query.setParameter("parentContentId", parentContentPk
                        .toString());

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
                LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, this
                        .getPrimaryKey());
            }
        }

        /*
         * add the map record
         */
        if (this.getIsDone()) {
            /*
             * find the next id
             */
            SingleKey recordPk3 = (SingleKey) getNextContentMapPrimaryKey();

            try {
                /*
                 * get the hibernate session
                 */
                Session session = ds.getWriteSession();

                /*
                 * save the content map record
                 */
                ContentMapMapping mapping = getContentMapMappingNewInstance();
                mapping.setId(recordPk3.toString());
                if (parentContentPk != null) {
                    mapping.setParentContentId(parentContentPk.toString());
                } else {
                    mapping.setParentContentId(null);
                }
                mapping.setChildContentId(this.getPrimaryKey().toString());
                mapping.setMainFlag("Y");
                mapping.setOrder(Integer.parseInt(recordPk3.toString()));

                // makes
                // sure
                // this
                // article
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
                LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, null);
            }
        }

        /*
         * refresh the image
         */
        if (this.getIsDone()) {
            PrimaryKey typePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(com.corendal.netapps.framework.core.constants.EntryLogTypeConstants.UPDATE);
            this.addEntryLog(entityPk, this.getPrimaryKey(), typePk,
                    DataSessionTypeConstants.WIKI_SHARED);
            this.clearCacheAndLoad();
        }
    }

    /*
     * stores the file primary key of this image.
     */
    public void storeFilePrimaryKey(PrimaryKey filePk) {
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
         * get the primary key of this image entity
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.IMAGES);
        PrimaryKey entityVersionPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.IMAGES);

        PrimaryKey attachmentRolePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(RolesDictionary.ATTACHMENT);
        PrimaryKey versionPk = this.getVersionPrimaryKey();

        StoredFileRoleXrefFactory.remove(this, ds, entityVersionPk,
                attachmentRolePk, versionPk);

        /*
         * add the file record
         */
        if (this.getIsDone()) {
            StoredFileRoleXrefFactory.create(this, ds, entityVersionPk,
                    attachmentRolePk, versionPk, filePk);
        }

        /*
         * refresh the image
         */
        if (this.getIsDone()) {
            PrimaryKey typePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(com.corendal.netapps.framework.core.constants.EntryLogTypeConstants.UPDATE);
            this.addEntryLog(entityPk, this.getPrimaryKey(), typePk,
                    DataSessionTypeConstants.WIKI_SHARED);
            this.clearCacheAndLoad();
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Image#storeInfo(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey)
     */
    public void storeInfo(PrimaryKey infoPk) {
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
         * get the primary key of this image
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.IMAGES);

        try {
            /*
             * get the query
             */
            Query query = ds.getWriteNamedQuery(STORE_INFO);

            /*
             * associate the query parameters
             */
            query.setParameter("currentFlag", "Y");
            query.setParameter("versionInfoId", infoPk.toString());
            query.setParameter("contentId", this.getPrimaryKey().toString());

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
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, this
                    .getPrimaryKey());
        }

        /*
         * refresh the image
         */
        if (this.getIsDone()) {
            PrimaryKey typePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(com.corendal.netapps.framework.core.constants.EntryLogTypeConstants.UPDATE);
            this.addEntryLog(entityPk, this.getPrimaryKey(), typePk,
                    DataSessionTypeConstants.WIKI_SHARED);
            this.clearCacheAndLoad();
        }
    }

    /*
     * stores the classification type of this image.
     */
    public void storeClassificationType(PrimaryKey classificationTypePk) {
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
         * get the primary key of this image
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.IMAGES);

        /*
         * update this content
         */
        try {
            /*
             * get the hibernate session
             */
            Session session = ds.getWriteSession();

            /*
             * load the content mapping
             */
            ContentMapping mapping = (ContentMapping) session.get(
                    DefaultContentMapping.class, this.getPrimaryKey()
                            .toString());

            /*
             * update the content using that mapping
             */
            if (mapping != null) {
                mapping.setContentClassTypeId(classificationTypePk.toString());
                session.update(mapping);
            }
        } catch (Exception e) {
            /*
             * log the exception
             */
            this.setIsDone(false);
            this.appendStoreTrace(e.getMessage());
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, this
                    .getPrimaryKey());
        }

        /*
         * refresh the image
         */
        if (this.getIsDone()) {
            PrimaryKey typePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(com.corendal.netapps.framework.core.constants.EntryLogTypeConstants.UPDATE);
            this.addEntryLog(entityPk, this.getPrimaryKey(), typePk,
                    DataSessionTypeConstants.WIKI_SHARED);
            this.clearCacheAndLoad();
        }
    }

    /*
     * stores the rule type of this image.
     */
    public void storeRuleType(PrimaryKey ruleTypePk) {
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
         * get the primary key of this image
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.IMAGES);

        /*
         * update this content
         */
        try {
            /*
             * get the hibernate session
             */
            Session session = ds.getWriteSession();

            /*
             * load the content mapping
             */
            ContentMapping mapping = (ContentMapping) session.get(
                    DefaultContentMapping.class, this.getPrimaryKey()
                            .toString());

            /*
             * update the content using that mapping
             */
            if (mapping != null) {
                mapping.setContentRuleTypeId(ruleTypePk.toString());
                session.update(mapping);
            }
        } catch (Exception e) {
            /*
             * log the exception
             */
            this.setIsDone(false);
            this.appendStoreTrace(e.getMessage());
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, this
                    .getPrimaryKey());
        }

        /*
         * refresh the image
         */
        if (this.getIsDone()) {
            PrimaryKey typePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(com.corendal.netapps.framework.core.constants.EntryLogTypeConstants.UPDATE);
            this.addEntryLog(entityPk, this.getPrimaryKey(), typePk,
                    DataSessionTypeConstants.WIKI_SHARED);
            this.clearCacheAndLoad();
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Image#storeFriendlyAddress(java.lang.String)
     */
    public void storeFriendlyAddress(String friendlyAddress) {
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
         * get the primary key of this image
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.IMAGES);

        try {
            /*
             * get the query
             */
            Query query = ds.getWriteNamedQuery(STORE_FRIENDLY_ADDRESS);

            /*
             * associate the query parameters
             */
            query.setParameter("currentFlag", "Y");
            query.setParameter("friendlyAddress", CaseUtil
                    .getLowerCaseDeleteAccents(friendlyAddress));
            query.setParameter("contentId", this.getPrimaryKey().toString());

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
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, this
                    .getPrimaryKey());
        }

        /*
         * refresh the image
         */
        if (this.getIsDone()) {
            PrimaryKey typePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(com.corendal.netapps.framework.core.constants.EntryLogTypeConstants.UPDATE);
            this.addEntryLog(entityPk, this.getPrimaryKey(), typePk,
                    DataSessionTypeConstants.WIKI_SHARED);
            this.clearCacheAndLoad();
        }
    }

    /**
     * Adds an allowed access group to this image.
     */
    public void addAllowedAccessGroup(PrimaryKey groupPk) {
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
         * get the primary key of this image
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.IMAGES);
        PrimaryKey rolePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(RolesDictionary.ACCESS_GROUP_ALLOWED);
        PrimaryKey recordPk = this.getPrimaryKey();

        GroupRoleXrefFactory.create(this, ds, entityPk, rolePk, recordPk,
                groupPk);

        /*
         * refresh the image
         */
        if (this.getIsDone()) {
            PrimaryKey typePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(com.corendal.netapps.framework.core.constants.EntryLogTypeConstants.UPDATE);
            this.addEntryLog(entityPk, this.getPrimaryKey(), typePk,
                    DataSessionTypeConstants.WIKI_SHARED);
            this.clearCacheAndLoad();
        }
    }

    /*
     * add a denied access group to this image.
     */
    public void addDeniedAccessGroup(PrimaryKey groupPk) {
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
         * get the primary key of this image
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.IMAGES);
        PrimaryKey rolePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(RolesDictionary.ACCESS_GROUP_DENIED);
        PrimaryKey recordPk = this.getPrimaryKey();

        GroupRoleXrefFactory.create(this, ds, entityPk, rolePk, recordPk,
                groupPk);

        /*
         * refresh the image
         */
        if (this.getIsDone()) {
            PrimaryKey typePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(com.corendal.netapps.framework.core.constants.EntryLogTypeConstants.UPDATE);
            this.addEntryLog(entityPk, this.getPrimaryKey(), typePk,
                    DataSessionTypeConstants.WIKI_SHARED);
            this.clearCacheAndLoad();
        }
    }

    /*
     * removes an access group from this image.
     */
    public void removeAccessGroup(PrimaryKey groupPk) {
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
         * get the primary key of this image entity
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.IMAGES);

        PrimaryKey roleAllowedPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(RolesDictionary.ACCESS_GROUP_ALLOWED);
        PrimaryKey roleDeniedPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(RolesDictionary.ACCESS_GROUP_DENIED);
        PrimaryKey recordPk = this.getPrimaryKey();

        GroupRoleXrefFactory.remove(this, ds, entityPk, roleAllowedPk,
                recordPk, groupPk);
        GroupRoleXrefFactory.remove(this, ds, entityPk, roleDeniedPk, recordPk,
                groupPk);

        /*
         * refresh the image
         */
        if (this.getIsDone()) {
            PrimaryKey typePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(com.corendal.netapps.framework.core.constants.EntryLogTypeConstants.UPDATE);
            this.addEntryLog(entityPk, this.getPrimaryKey(), typePk,
                    DataSessionTypeConstants.WIKI_SHARED);
            this.clearCacheAndLoad();
        }
    }

    /*
     * removes this image
     */
    public void remove(PrimaryKey referenceRequestPk) {
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
         * get the primary key of this image map
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.IMAGES);
        PrimaryKey recordPk = this.getPrimaryKey();

        try {
            /*
             * get the query
             */
            Query query = ds.getWriteNamedQuery(REMOVE_1);

            /*
             * associate the query parameters
             */
            query.setParameter("childContentId", recordPk.toString());

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
         * remove all records in the account_role_xref
         */
        if (this.getIsDone()) {
            try {
                /*
                 * get the query
                 */
                Query query1 = ds.getReadNamedQuery(REMOVE_3);

                /*
                 * associate the id
                 */
                query1.setParameter("contentId", recordPk.toString());

                /*
                 * excute the query
                 */
                Iterator it = query1.iterate();

                /*
                 * loop the results
                 */
                while (it.hasNext()) {
                    /*
                     * get the primary key of this image map
                     */
                    PrimaryKey entityPk2 = PrimaryKeyUtil
                            .getAlphanumericSingleKey(EntitiesDictionary.IMAGE_VERSIONS);
                    ContentVersionMapping mapping = (ContentVersionMapping) it
                            .next();

                    if (mapping != null) {
                        PrimaryKey idRecordPk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(mapping.getId());
                        /*
                         * remove all account role xref records
                         */
                        AccountRoleXrefFactory.removeAll(this, ds, entityPk2,
                                idRecordPk);

                        StoredFileRoleXrefFactory.removeAll(this, ds,
                                entityPk2, idRecordPk);
                    }
                }
            } catch (Exception e) {
                /*
                 * log the exception
                 */
                this.appendLoadTrace(e.getMessage());
                LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, recordPk);
            }
        }

        /*
         * remove all records in the content_version table
         */
        if (this.getIsDone()) {
            try {
                /*
                 * get the query
                 */
                Query query = ds.getWriteNamedQuery(REMOVE_2);

                /*
                 * associate the query parameters
                 */
                query.setParameter("contentId", recordPk.toString());

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
        }

        /*
         * remove all records in the group_obj_role_xref
         */
        if (this.getIsDone()) {
            GroupRoleXrefFactory.removeAll(this, ds, entityPk, recordPk);
        }

        /*
         * remove all records in the content table
         */
        if (this.getIsDone()) {
            try {
                /*
                 * get the hibernate session
                 */
                Session session = ds.getWriteSession();

                /*
                 * load the content mapping
                 */
                ContentMapping mapping = (ContentMapping) session.get(
                        DefaultContentMapping.class, recordPk.toString());

                /*
                 * retrieve the content from the database
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
        }

        /*
         * refresh this image
         */
        if (this.getIsDone()) {
            PrimaryKey typePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(com.corendal.netapps.framework.core.constants.EntryLogTypeConstants.DELETE);
            this.addEntryLog(entityPk, recordPk, typePk,
                    DataSessionTypeConstants.WIKI_SHARED);
            this.clearCacheAndLoad();
        }
    }

    /**
     * Returns the name-ordered list of all image primary keys for an author
     * primary key.
     * 
     * @param authorPk
     *            primary key of an author account
     * 
     * 
     * @return a java.util.List object
     */
    public static final List<PrimaryKey> findEnabledByAuthorPrimaryKey(
            PrimaryKey authorPk) {
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
                + ds.getTableNameWithDefaultSchema("account_role_xref") + ", "
                + ds.getTableNameWithDefaultSchema("content") + ", "
                + ds.getTableNameWithDefaultSchema("content_version") + " "
                + "where account_role_xref.record_id = content_version.id "
                + "and content.id = content_version.content_id "
                + "and account_role_xref.role_id = ? "
                + "and account_role_xref.account_id = ? "
                + "and content.enabled_flag = ? "
                + "and content_version.current_flag = ? "
                + "and account_role_xref.entity_id = ? "
                + "order by content.ord asc";
        // FIXME: hibernate select query

        try {
            /*
             * create the prepared statement
             */
            prepStmt = ds.getReadConnection().prepareStatement(selectStatement);

            /*
             * associate the author
             */
            prepStmt.setString(1, RolesDictionary.AUTHOR);
            prepStmt.setString(2, authorPk.toString());
            prepStmt.setString(3, "Y");
            prepStmt.setString(4, "Y");
            prepStmt.setString(5, EntitiesDictionary.IMAGE_VERSIONS);

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
                    .getAlphanumericSingleKey(EntitiesDictionary.IMAGES);
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, null);
        } finally {
            /*
             * close the recordset and the prepared statement
             */
            CloseUtil.closeResultSet(rs);
            CloseUtil.closePreparedStatement(prepStmt);
        }

        /*
         * return all article primary keys
         */
        return primaryKeys;
    }

    /**
     * Returns a ranked list of all image primary keys that have been accessed.
     * 
     * @param cnt
     *            number of rows to return
     * 
     * 
     * @return a java.util.List object
     */
    public static final List<PrimaryKey> findEnabledByRank(int cnt) {
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
         * retrieve the content access period information from the database
         */
        Date maxAccessDate = null;

        try {
            /*
             * get the query
             */
            Query query = ds.getReadNamedQuery(FIND_ENABLED_BY_RANK_1);

            /*
             * only get one benefit
             */
            query.setMaxResults(1);

            /*
             * get benefit mapping
             */
            maxAccessDate = (Date) query.uniqueResult();
        } catch (Exception e) {
            /*
             * log the exception
             */
            PrimaryKey entityPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntitiesDictionary.IMAGES);
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, null);
        }

        /*
         * retrieve the content information from the database
         */
        if (maxAccessDate != null) {
            String selectStatement = "select content_id, access_rank, access_num "
                    + "from "
                    + ds.getTableNameWithDefaultSchema("content_access_period")
                    + ", "
                    + ds.getTableNameWithDefaultSchema("content")
                    + " "
                    + "where content_access_period.content_id = content.id "
                    + "and content.content_typ_id = ? "
                    + "and content_access_period.access_date = ? "
                    + "order by access_date desc, access_rank ";
            // FIXME: hibernate select query

            try {
                /*
                 * create the prepared statement
                 */
                prepStmt = ds.getReadConnection().prepareStatement(
                        selectStatement);

                /*
                 * associate the id
                 */
                prepStmt.setString(1, ContentTypesDictionary.IMAGE);
                prepStmt.setDate(2, DateUtil.getDateToSQLDate(maxAccessDate));

                /*
                 * execute the query
                 */
                rs = prepStmt.executeQuery();

                /*
                 * loop the results
                 */
                int itemCount = 0;

                while ((rs.next()) && (itemCount < cnt)) {
                    PrimaryKey pk = PrimaryKeyUtil.getAlphanumericSingleKey(rs
                            .getString(1));
                    primaryKeys.add(pk);
                    itemCount++;
                }
            } catch (Exception e) {
                /*
                 * log the exception
                 */
                PrimaryKey entityPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(EntitiesDictionary.IMAGES);
                LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, null);
            } finally {
                /*
                 * close the recordset and the prepared statement
                 */
                CloseUtil.closeResultSet(rs);
                CloseUtil.closePreparedStatement(prepStmt);
            }
        }

        /*
         * return all image primary keys
         */
        return primaryKeys;
    }

    /**
     * Returns the ordered list of all enabled images created during a date
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
         * retrieve the image information from the database
         */
        String selectStatement = "select distinct content.id, content.ord "
                + "from " + ds.getTableNameWithDefaultSchema("content") + ", "
                + ds.getTableNameWithDefaultSchema("entry_log") + " "
                + "where entry_log.entity_id = ? "
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
            prepStmt
                    .setString(
                            2,
                            com.corendal.netapps.framework.core.constants.EntryLogTypeConstants.INSERT);
            prepStmt.setTimestamp(3, createdDateTimestampFrom);
            prepStmt.setTimestamp(4, createdDateTimestampTo);
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
                    .getAlphanumericSingleKey(EntitiesDictionary.IMAGES);
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
     * Returns the ordered list of all enabled images created after a date.
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
         * retrieve the image information from the database
         */
        String selectStatement = "select distinct content.id, content.ord "
                + "from " + ds.getTableNameWithDefaultSchema("content") + ", "
                + ds.getTableNameWithDefaultSchema("entry_log") + " "
                + "where entry_log.entity_id = ? "
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
            prepStmt
                    .setString(
                            2,
                            com.corendal.netapps.framework.core.constants.EntryLogTypeConstants.INSERT);
            prepStmt.setTimestamp(3, createdDateTimestamp);
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
                    .getAlphanumericSingleKey(EntitiesDictionary.IMAGES);
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
     * Returns the ordered list of all enabled images created after a date.
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
         * retrieve the image information from the database
         */
        String selectStatement = "select distinct content.id, content.ord "
                + "from " + ds.getTableNameWithDefaultSchema("content") + ", "
                + ds.getTableNameWithDefaultSchema("entry_log") + " "
                + "where entry_log.entity_id = ? "
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
            prepStmt
                    .setString(
                            2,
                            com.corendal.netapps.framework.core.constants.EntryLogTypeConstants.INSERT);
            prepStmt.setTimestamp(3, createdDateTimestamp);
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
                    .getAlphanumericSingleKey(EntitiesDictionary.IMAGES);
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
     * Returns the ordered list of all enabled images modified during a date
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
         * retrieve the image information from the database
         */
        String selectStatement = "select distinct content.id, content.ord "
                + "from " + ds.getTableNameWithDefaultSchema("content") + ", "
                + ds.getTableNameWithDefaultSchema("entry_log") + " "
                + "where entry_log.entity_id = ? "
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
            prepStmt
                    .setString(
                            2,
                            com.corendal.netapps.framework.core.constants.EntryLogTypeConstants.INSERT);
            prepStmt
                    .setString(
                            3,
                            com.corendal.netapps.framework.core.constants.EntryLogTypeConstants.MODIFY);
            prepStmt.setTimestamp(4, modifiedDateTimestampFrom);
            prepStmt.setTimestamp(5, modifiedDateTimestampTo);
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
                    .getAlphanumericSingleKey(EntitiesDictionary.IMAGES);
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
     * Returns the ordered list of all enabled images modified after a date.
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
         * retrieve the image information from the database
         */
        String selectStatement = "select distinct content.id, content.ord "
                + "from " + ds.getTableNameWithDefaultSchema("content") + ", "
                + ds.getTableNameWithDefaultSchema("entry_log") + " "
                + "where entry_log.entity_id = ? "
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
            prepStmt
                    .setString(
                            2,
                            com.corendal.netapps.framework.core.constants.EntryLogTypeConstants.INSERT);
            prepStmt
                    .setString(
                            4,
                            com.corendal.netapps.framework.core.constants.EntryLogTypeConstants.MODIFY);
            prepStmt.setTimestamp(4, modifiedDateTimestamp);
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
                    .getAlphanumericSingleKey(EntitiesDictionary.IMAGES);
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
     * Returns the ordered list of all enabled images modified after a date.
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
         * retrieve the image information from the database
         */
        String selectStatement = "select distinct content.id, content.ord "
                + "from " + ds.getTableNameWithDefaultSchema("content") + ", "
                + ds.getTableNameWithDefaultSchema("entry_log") + " "
                + "where entry_log.entity_id = ? "
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
            prepStmt
                    .setString(
                            2,
                            com.corendal.netapps.framework.core.constants.EntryLogTypeConstants.INSERT);
            prepStmt
                    .setString(
                            3,
                            com.corendal.netapps.framework.core.constants.EntryLogTypeConstants.MODIFY);
            prepStmt.setTimestamp(4, modifiedDateTimestamp);
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
                    .getAlphanumericSingleKey(EntitiesDictionary.IMAGES);
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

    /*
     * stores the comment of this image.
     */
    public void storeComment(String comment) {
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
         * get the primary key of this image
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.IMAGES);

        try {
            /*
             * get the query
             */
            Query query = ds.getWriteNamedQuery(STORE_COMMENT);

            /*
             * associate the query parameters
             */
            query.setParameter("comment", comment);
            query.setParameter("contentId", this.getPrimaryKey().toString());
            query.setParameter("currentFlag", "Y");

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
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, this
                    .getPrimaryKey());
        }

        /*
         * refresh the image
         */
        if (this.getIsDone()) {
            PrimaryKey typePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(com.corendal.netapps.framework.core.constants.EntryLogTypeConstants.UPDATE);
            this.addEntryLog(entityPk, this.getPrimaryKey(), typePk,
                    DataSessionTypeConstants.WIKI_SHARED);
            this.clearCacheAndLoad();
        }
    }

    /**
     * Returns the ordered list of all enabled image primary keys.
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
            query.setParameter("contentTypeId", ContentTypesDictionary.ARTICLE);
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
                    .getAlphanumericSingleKey(EntitiesDictionary.IMAGES);
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, null);
        }

        /*
         * return all image primary keys
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
                .getAlphanumericSingleKey(EntitiesDictionary.IMAGES);
        String tableName = "content";
        AbstractWriteDataBean.cleanup(entityPk, tableName,
                DataSessionTypeConstants.WIKI_SHARED);
    }
}

// end AbstractImage

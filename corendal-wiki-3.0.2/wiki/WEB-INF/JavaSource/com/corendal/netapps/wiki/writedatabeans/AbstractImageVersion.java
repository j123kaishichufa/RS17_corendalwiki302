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
import com.corendal.netapps.framework.core.readdatafactories.IconFactory;
import com.corendal.netapps.framework.core.writedatafactories.AccountRoleXrefFactory;
import com.corendal.netapps.framework.core.writedatafactories.StoredFileRoleXrefFactory;
import com.corendal.netapps.framework.modelaccess.globals.DataSessionSetGlobal;
import com.corendal.netapps.framework.modelaccess.interfaces.HibernateDataSession;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.CloseUtil;
import com.corendal.netapps.framework.modelaccess.utils.LoggerUtil;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.constants.DataSessionTypeConstants;
import com.corendal.netapps.wiki.dictionaries.ContentTypesDictionary;
import com.corendal.netapps.wiki.dictionaries.EntitiesDictionary;
import com.corendal.netapps.wiki.dictionaries.IconsDictionary;
import com.corendal.netapps.wiki.dictionaries.RolesDictionary;
import com.corendal.netapps.wiki.interfaces.ContentVersionMapping;
import com.corendal.netapps.wiki.interfaces.ImageVersion;
import com.corendal.netapps.wiki.writedatamappings.DefaultContentVersionMapping;

/**
 * AbstractImageVersion is the abstract class retrieving information about each
 * image version of the application from the database.
 * 
 * @version $Id: AbstractImageVersion.java,v 1.10 2006/06/28 17:01:10 tdanard
 *          Exp $
 */
public abstract class AbstractImageVersion extends AbstractWriteDataBean
        implements Cloneable, ImageVersion {
    /** Fully qualified name of this class. */
    private static final String ABSTRACT_CLASS_NAME = "com.corendal.netapps.wiki.writedatabeans.AbstractImageVersion";

    /** Content_id in the content_version table */
    private String contentId;

    /** Version_num in the content_version table */
    private int versionNum;

    /** Version_info_id in the content_version table */
    private String versionInfoId;

    /** File_id in the file_obj_role_xref table */
    private String fileId;

    /** Request_id in the content_version table */
    private String requestId;

    /** Current_flag in the content table */
    private String currentFlag;

    /** Enabled_flag in the content table */
    private String enabledFlag;

    /** Author account_id from the the account_role_xref table */
    private String authorId;

    /** Content_rule_typ_id in the content table */
    private String contentRuleTypeId;

    /** Cmnt in the content_version table */
    private String comment;

    /**
     * Name of the named query for the findEnabledByImagePrimaryKey method.
     */
    private static final String FIND_ENABLED_BY_IMAGE_PRIMARY_KEY = ABSTRACT_CLASS_NAME
            + ".findEnabledByImagePrimaryKey";

    /**
     * Default class constructor.
     */
    protected AbstractImageVersion() {
        super(DataSessionTypeConstants.WIKI_SHARED);
    }

    /**
     * Returns a clone. Overrides AbstractWriteDataBean.clone.
     */
    @Override
    public Object clone() {
        return (AbstractImageVersion) super.clone();
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
         * get the primary key of the image version
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.IMAGE_VERSIONS);
        PrimaryKey recordPk = this.getPrimaryKey();

        /*
         * retrieve the image version information from the database
         */
        String selectStatement = "select content_version.version_num, "
                + "content_version.version_info_id, content_version.request_id, "
                + "content.enabled_flag, content_version.current_flag, content_version.content_id, "
                + "content.content_rule_typ_id, content_version.cmnt "
                + "from " + ds.getTableNameWithDefaultSchema("content_version")
                + ", " + ds.getTableNameWithDefaultSchema("content") + " "
                + "where content_version.id = ? "
                + "and content.content_typ_id = ? "
                + "and content.id = content_version.content_id ";
        // FIXME: hibernate select query

        try {
            /*
             * create the prepared statement
             */
            prepStmt = ds.getReadConnection().prepareStatement(selectStatement);

            /*
             * associate the id and the image version type
             */
            prepStmt.setString(1, recordPk.toString());
            prepStmt.setString(2, ContentTypesDictionary.IMAGE);

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
                this.enabledFlag = rs.getString(4);
                this.currentFlag = rs.getString(5);
                this.contentId = rs.getString(6);
                this.contentRuleTypeId = rs.getString(7);
                this.comment = ds.getStringFromCharacterStream(rs, 8);
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
         * retrieve the author information from the database
         */
        if (this.getIsFound()) {
            PrimaryKey rolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.AUTHOR);
            this.authorId = AccountRoleXrefFactory.getSingleAccountId(this, ds,
                    entityPk, rolePk, recordPk);
        }

        /*
         * retrieve the file information from the database
         */
        if (this.getIsFound()) {
            PrimaryKey rolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.ATTACHMENT);
            this.fileId = StoredFileRoleXrefFactory.getSingleStoredFileId(this,
                    ds, entityPk, rolePk, recordPk);
        }

        /*
         * load this write data bean
         */
        if (this.getIsFound()) {
            super.load(entityPk, recordPk);
        }
    }

    /**
     * Returns the extension id of this image version based upon the file
     * extension.
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
                + "where content_version.id = ? "
                + "and content.content_typ_id = ? "
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
            prepStmt.setString(3, EntitiesDictionary.IMAGE_VERSIONS);
            prepStmt.setString(4, RolesDictionary.ATTACHMENT);

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
     * Returns the direct icon id of this image version based upon the file
     * extension.
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
     * Returns the indirect icon id of this image version based upon the file
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
     * Returns the secure direct icon id of this image version based upon the
     * file extension.
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
     * Returns the secure indirect icon id of this image version based upon the
     * file extension.
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
     * @see com.corendal.netapps.wiki.interfaces.ImageVersion#getAuthorAccountPrimaryKey()
     */
    public PrimaryKey getAuthorAccountPrimaryKey() {
        return PrimaryKeyUtil.getAlphanumericSingleKeyOrNull(this.authorId);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ImageVersion#getVersionNum()
     */
    public int getVersionNum() {
        return this.versionNum;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ImageVersion#getContentPrimaryKey()
     */
    public PrimaryKey getContentPrimaryKey() {
        return PrimaryKeyUtil.getAlphanumericSingleKeyOrNull(this.contentId);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ImageVersion#getFilePrimaryKey()
     */
    public PrimaryKey getFilePrimaryKey() {
        return PrimaryKeyUtil.getAlphanumericSingleKeyOrNull(this.fileId);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ImageVersion#getInfoPrimaryKey()
     */
    public PrimaryKey getInfoPrimaryKey() {
        return PrimaryKeyUtil
                .getAlphanumericSingleKeyOrNull(this.versionInfoId);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ImageVersion#getRequestPrimaryKey()
     */
    public PrimaryKey getRequestPrimaryKey() {
        return PrimaryKeyUtil.getAlphanumericSingleKeyOrNull(this.requestId);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ImageVersion#getComment()
     */
    public String getComment() {
        return this.comment;
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
     * @see com.corendal.netapps.wiki.interfaces.ImageVersion#getCurrentFlag()
     */
    public String getCurrentFlag() {
        return this.currentFlag;
    }

    /**
     * Returns the version-ordered list of all image version primary keys for a
     * image primary key.
     * 
     * @param imagePk
     *            primary key of the image to parse
     * 
     * 
     * @return a java.util.List object
     */
    public static final List<PrimaryKey> findEnabledByImagePrimaryKey(
            PrimaryKey imagePk) {
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
            Query query = ds
                    .getReadNamedQuery(FIND_ENABLED_BY_IMAGE_PRIMARY_KEY);

            /*
             * associate the enabled flag
             */
            query.setParameter("contentId", imagePk.toString());
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
                    .getAlphanumericSingleKey(EntitiesDictionary.IMAGE_VERSIONS);
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, null);
        }

        /*
         * return all image version primary keys
         */
        return primaryKeys;
    }

    /**
     * Removes this image version.
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
                .getAlphanumericSingleKey(EntitiesDictionary.IMAGE_VERSIONS);
        PrimaryKey recordPk = this.getPrimaryKey();

        try {
            /*
             * get the hibernate session
             */
            Session session = ds.getWriteSession();

            /*
             * load the content version mapping
             */
            ContentVersionMapping mapping = (ContentVersionMapping) session
                    .get(DefaultContentVersionMapping.class, recordPk
                            .toString());

            /*
             * retrieve the content version from the database
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
         * refresh this image version
         */
        if (this.getIsDone()) {
            PrimaryKey typePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntryLogTypeConstants.DELETE);
            this.addEntryLog(entityPk, recordPk, typePk,
                    DataSessionTypeConstants.WIKI_SHARED);
        }
    }

    /**
     * Stores the current flag of this image version.
     */
    public void storeCurrentFlag(String currentFlag) {
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
         * get the primary key of this image version
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.IMAGE_VERSIONS);

        /*
         * update this content version
         */
        try {
            /*
             * get the hibernate session
             */
            Session session = ds.getWriteSession();

            /*
             * load the content version mapping
             */
            ContentVersionMapping mapping = (ContentVersionMapping) session
                    .get(DefaultContentVersionMapping.class, this
                            .getPrimaryKey().toString());

            /*
             * update the content version using that mapping
             */
            if (mapping != null) {
                mapping.setCurrentFlag(currentFlag);
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
         * refresh the image version
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
     * Cleans up all data related to this entity.
     */
    public static void cleanup() {
        /*
         * call the generic data cleanup
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.IMAGE_VERSIONS);
        String tableName = "content_version";
        AbstractWriteDataBean.cleanup(entityPk, tableName,
                DataSessionTypeConstants.WIKI_SHARED);
    }
}

// end AbstractImageVersion

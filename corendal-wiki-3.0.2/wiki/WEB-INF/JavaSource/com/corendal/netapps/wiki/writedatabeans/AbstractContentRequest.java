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

import com.corendal.netapps.framework.configuration.utils.StringUtil;
import com.corendal.netapps.framework.core.databeans.AbstractWriteDataBean;
import com.corendal.netapps.framework.core.readdatafactories.IconFactory;
import com.corendal.netapps.framework.core.utils.DateUtil;
import com.corendal.netapps.framework.core.utils.FullTextUtil;
import com.corendal.netapps.framework.core.utils.HTMLFormatUtil;
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
import com.corendal.netapps.wiki.dictionaries.ContentRequestTypesDictionary;
import com.corendal.netapps.wiki.dictionaries.ContentTypesDictionary;
import com.corendal.netapps.wiki.dictionaries.EntitiesDictionary;
import com.corendal.netapps.wiki.dictionaries.IconsDictionary;
import com.corendal.netapps.wiki.dictionaries.RequestStatusTypesDictionary;
import com.corendal.netapps.wiki.dictionaries.RolesDictionary;
import com.corendal.netapps.wiki.interfaces.ContentMapping;
import com.corendal.netapps.wiki.interfaces.ContentRequest;
import com.corendal.netapps.wiki.interfaces.ContentRequestMapping;
import com.corendal.netapps.wiki.writedatamappings.DefaultContentMapping;
import com.corendal.netapps.wiki.writedatamappings.DefaultContentRequestMapping;

/**
 * AbstractContentRequest is the abstract class retrieving information about
 * each content request of the application from the database.
 * 
 * @version $Id: AbstractContentRequest.java,v 1.1 2005/09/06 21:25:33 tdanard
 *          Exp $
 */
public abstract class AbstractContentRequest extends AbstractWriteDataBean
        implements Cloneable, ContentRequest {
    /** Fully qualified name of this class. */
    private static final String ABSTRACT_CLASS_NAME = "com.corendal.netapps.wiki.writedatabeans.AbstractContentRequest";

    /** Request_typ_id in the content_request table */
    private String requestTypeId;

    /** Content_info_id in the content_request table */
    private String contentInfoId;

    /** Editor account_id from the the account_role_xref table */
    private String editorId;

    /** Associate editors group_obj_id from the the group_obj_role_xref table */
    private String associateEditorsId;

    /** Author account_id from the the account_role_xref table */
    private String authorId;

    /** Approver account_id from the the account_role_xref table */
    private String approverId;

    /** Requestor account_id from the the account_role_xref table */
    private String requestorId;

    /** Content_typ_id in the content_request table */
    private String contentTypeId;

    /** Friendly_address in the content_request table */
    private String friendlyAddress;

    /** Parent_content_id in the content_request table */
    private String parentArticleId;

    /** Child_content_id in the content_request table */
    private String childContentId;

    /** File_id in the file_obj_role_xref table */
    private String fileId;

    /** Body in the content_request table */
    private String bodyHTML;

    /** Status_typ_id in the content_request table */
    private String statusTypeId;

    /** Content_class_typ_id in the content_request table */
    private String contentClassTypeId;

    /** Content_rule_typ_id in the content table */
    private String contentRuleTypeId;

    /** Cmnt in the content_version table */
    private String comment;

    /** Enabled_flag in the content table */
    private String enabledFlag;

    /**
     * Name of the named query for the findEnabledPendingByContentPrimaryKey
     * method.
     */
    private static final String FIND_ENABLED_PENDING_BY_CONTENT_PRIMARY_KEY = ABSTRACT_CLASS_NAME
            + ".findEnabledPendingByContentPrimaryKey";

    /**
     * Name of the named query for the
     * findEnabledPendingByParentArticlePrimaryKey method.
     */
    private static final String FIND_ENABLED_PENDING_BY_PARENT_ARTICLE_PRIMARY_KEY = ABSTRACT_CLASS_NAME
            + ".findEnabledPendingByParentArticlePrimaryKey";

    /**
     * Default class constructor.
     */
    protected AbstractContentRequest() {
        super(DataSessionTypeConstants.WIKI_SHARED);
    }

    /**
     * Returns a new instance of a content request mapping.
     * 
     * @return a com.corendal.netapps.qadocumentation.interfaces.MaterialRequest
     *         object
     */
    protected ContentRequestMapping getContentRequestMappingNewInstance() {
        return new DefaultContentRequestMapping();
    }

    /**
     * Returns a clone. Overrides AbstractWriteDataBean.clone.
     */
    @Override
    public Object clone() {
        return (AbstractContentRequest) super.clone();
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
         * get the primary key of the content request
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_REQUESTS);
        PrimaryKey recordPk = this.getPrimaryKey();

        try {
            /*
             * get the hibernate session
             */
            Session session = ds.getReadSession();

            /*
             * load the content request mapping
             */
            ContentRequestMapping mapping = (ContentRequestMapping) session
                    .get(DefaultContentRequestMapping.class, recordPk
                            .toString());
            /*
             * retrieve the content request information from the database
             */
            if (mapping != null) {
                /*
                 * set the record as "found"
                 */
                this.setIsFound(true);

                /*
                 * remember the content request information
                 */
                this.requestTypeId = mapping.getRequestTypeId();
                this.contentInfoId = mapping.getContentInfoId();
                this.bodyHTML = mapping.getBody();
                this.contentTypeId = mapping.getContentTypeId();
                this.friendlyAddress = mapping.getFriendlyAddress();
                this.parentArticleId = mapping.getParentContentId();
                this.childContentId = mapping.getChildContentId();
                this.statusTypeId = mapping.getStatusTypeId();
                this.contentClassTypeId = mapping.getContentClassTypeId();
                this.contentRuleTypeId = mapping.getContentRuleTypeId();
                this.comment = mapping.getComment();
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
         * retrieve the classification type in case of reference request
         */
        if ((this.getIsFound())
                && (StringUtil.getIsEmpty(this.contentClassTypeId))) {
            try {
                /*
                 * get the hibernate session
                 */
                Session session = ds.getWriteSession();

                /*
                 * load the content mapping
                 */
                ContentMapping contentMapping = (ContentMapping) session.get(
                        DefaultContentMapping.class, recordPk.toString());
                /*
                 * retrieve the content information from the database
                 */
                if (contentMapping != null) {
                    /*
                     * set the record as "found"
                     */
                    this.setIsFound(true);

                    /*
                     * remember the content information
                     */
                    this.contentClassTypeId = contentMapping
                            .getContentClassTypeId();
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
         * retrieve the editor from the database
         */
        if (this.getIsFound()) {
            PrimaryKey rolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.EDITOR);
            this.editorId = AccountRoleXrefFactory.getSingleAccountId(this, ds,
                    entityPk, rolePk, recordPk);
        }

        /*
         * retrieve the associate editors from the database
         */
        if (this.getIsFound()) {
            PrimaryKey rolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.EDITOR);
            this.associateEditorsId = GroupRoleXrefFactory.getSingleGroupId(
                    this, ds, entityPk, rolePk, recordPk);
        }

        /*
         * retrieve the requestor from the database
         */
        if (this.getIsFound()) {
            PrimaryKey rolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.REQUESTOR);
            this.requestorId = AccountRoleXrefFactory.getSingleAccountId(this,
                    ds, entityPk, rolePk, recordPk);
        }

        /*
         * retrieve the approver from the database
         */
        if (this.getIsFound()) {
            PrimaryKey rolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.APPROVER);
            this.approverId = AccountRoleXrefFactory.getSingleAccountId(this,
                    ds, entityPk, rolePk, recordPk);
        }

        /*
         * retrieve the author from the database
         */
        if (this.getIsFound()) {
            PrimaryKey rolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.AUTHOR);
            this.authorId = AccountRoleXrefFactory.getSingleAccountId(this, ds,
                    entityPk, rolePk, recordPk);
        }

        /*
         * retrieve the file from the database
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

    /*
     * @see com.corendal.netapps.wiki.interfaces.Classified#getClassificationTypePrimaryKey()
     */
    public PrimaryKey getClassificationTypePrimaryKey() {
        return PrimaryKeyUtil
                .getAlphanumericSingleKeyOrNull(this.contentClassTypeId);
    }

    /**
     * Returns the extension of this content based upon the file extension.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    private PrimaryKey getExtensionPrimaryKey() {
        /*
         * initialize the extension id
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
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_REQUESTS);
        PrimaryKey recordPk = this.getPrimaryKey();

        /*
         * retrieve the stored file information from the database
         */
        String selectStatement = "select file_obj.extension_id " + "from "
                + ds.getTableNameWithDefaultSchema("content_request")
                + ", file_obj_role_xref, file_obj "
                + "where content_request.id = ? "
                + "and content_request.content_typ_id = ? "
                + "and file_obj_role_xref.entity_id = ? "
                + "and file_obj_role_xref.role_id = ? "
                + "and file_obj_role_xref.record_id = content_request.id "
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
            prepStmt.setString(3, EntitiesDictionary.CONTENT_REQUESTS);
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
     * Returns the direct icon id of this content request based upon the file
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
     * Returns the indirect icon id of this content request based upon the file
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

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequest#getDirectPreviewIconPrimaryKey()
     */
    public PrimaryKey getDirectPreviewIconPrimaryKey() {
        /*
         * get the preview icon based upon the content type
         */
        PrimaryKey articleContentTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.ARTICLE);

        if (this.getContentTypePrimaryKey().equals(articleContentTypePk)) {
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
     * @see com.corendal.netapps.wiki.interfaces.ContentRequest#getIndirectPreviewIconPrimaryKey()
     */
    public PrimaryKey getIndirectPreviewIconPrimaryKey() {
        /*
         * get the preview icon based upon the content type
         */
        PrimaryKey articleContentTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.ARTICLE);

        if (this.getContentTypePrimaryKey().equals(articleContentTypePk)) {
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
     * @see com.corendal.netapps.wiki.interfaces.ContentRequest#getEditorAccountPrimaryKey()
     */
    public PrimaryKey getEditorAccountPrimaryKey() {
        return PrimaryKeyUtil.getAlphanumericSingleKeyOrNull(this.editorId);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequest#getAssociateEditorsGroupPrimaryKey()
     */
    public PrimaryKey getAssociateEditorsGroupPrimaryKey() {
        return PrimaryKeyUtil
                .getAlphanumericSingleKeyOrNull(this.associateEditorsId);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequest#getAuthorAccountPrimaryKey()
     */
    public PrimaryKey getAuthorAccountPrimaryKey() {
        return PrimaryKeyUtil.getAlphanumericSingleKeyOrNull(this.authorId);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequest#getRequestorAccountPrimaryKey()
     */
    public PrimaryKey getRequestorAccountPrimaryKey() {
        return PrimaryKeyUtil.getAlphanumericSingleKeyOrNull(this.requestorId);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequest#getApproverAccountPrimaryKey()
     */
    public PrimaryKey getApproverAccountPrimaryKey() {
        return PrimaryKeyUtil.getAlphanumericSingleKeyOrNull(this.approverId);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequest#getParentContentPrimaryKey()
     */
    public PrimaryKey getParentContentPrimaryKey() {
        return PrimaryKeyUtil
                .getAlphanumericSingleKeyOrNull(this.parentArticleId);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequest#getChildContentPrimaryKey()
     */
    public PrimaryKey getChildContentPrimaryKey() {
        return PrimaryKeyUtil
                .getAlphanumericSingleKeyOrNull(this.childContentId);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequest#getFilePrimaryKey()
     */
    public PrimaryKey getFilePrimaryKey() {
        return PrimaryKeyUtil.getAlphanumericSingleKeyOrNull(this.fileId);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequest#getBodyHTML()
     */
    public String getBodyHTML() {
        return this.bodyHTML;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequest#getBodyText()
     */
    public String getBodyText() {
        return HTMLFormatUtil.getHTMLToText(this.bodyHTML);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequest#getContentInfoPrimaryKey()
     */
    public PrimaryKey getContentInfoPrimaryKey() {
        return PrimaryKeyUtil
                .getAlphanumericSingleKeyOrNull(this.contentInfoId);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequest#getRequestTypePrimaryKey()
     */
    public PrimaryKey getRequestTypePrimaryKey() {
        return PrimaryKeyUtil
                .getAlphanumericSingleKeyOrNull(this.requestTypeId);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequest#getContentTypePrimaryKey()
     */
    public PrimaryKey getContentTypePrimaryKey() {
        return PrimaryKeyUtil
                .getAlphanumericSingleKeyOrNull(this.contentTypeId);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequest#getFriendlyAddress()
     */
    public String getFriendlyAddress() {
        return this.friendlyAddress;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequest#getStatusTypePrimaryKey()
     */
    public PrimaryKey getStatusTypePrimaryKey() {
        return PrimaryKeyUtil.getAlphanumericSingleKeyOrNull(this.statusTypeId);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Classified#getRuleTypePrimaryKey()
     */
    public PrimaryKey getRuleTypePrimaryKey() {
        return PrimaryKeyUtil
                .getAlphanumericSingleKeyOrNull(this.contentRuleTypeId);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequest#getComment()
     */
    public String getComment() {
        return this.comment;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Enabled#getEnabledFlag()
     */
    public String getEnabledFlag() {
        return this.enabledFlag;
    }

    /**
     * Returns the ordered list of all content request primary keys for a
     * keyword.
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
         * retrieve the content request information from the database
         */
        String selectStatement = "select content_request.id, "
                + SQLUtil.getMatchSelectCall(ds, "content_info.nam") + " "
                + "from "
                + ds.getTableNameWithDefaultSchema("content_access_log") + ", "
                + ds.getTableNameWithDefaultSchema("content_info") + " "
                + "where content_request.status_typ_id= ? " + "and "
                + SQLUtil.getMatchWhereCall(ds, "content_info.nam") + " "
                + "and content_request.content_info_id= content_info.id "
                + "and content_request.enabled_flag= ? " + "union "
                + "select content_request.id, "
                + SQLUtil.getMatchSelectCall(ds, "content_info.dsc") + " "
                + "from "
                + ds.getTableNameWithDefaultSchema("content_access_log") + ", "
                + ds.getTableNameWithDefaultSchema("content_info") + " "
                + "where content_request.status_typ_id= ? " + "and "
                + SQLUtil.getMatchWhereCall(ds, "content_info.dsc") + " "
                + "and content_request.content_info_id= content_info.id "
                + "and content_request.enabled_flag= ? " + "order by 2 desc ";
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
            prepStmt.setString(2, RequestStatusTypesDictionary.PENDING);
            prepStmt.setString(3, keyword);
            prepStmt.setString(4, "Y");
            prepStmt.setString(5, keyword);
            prepStmt.setString(6, RequestStatusTypesDictionary.PENDING);
            prepStmt.setString(7, keyword);
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
                    .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_REQUESTS);
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, null);
        } finally {
            /*
             * close the recordset and the prepared statement
             */
            CloseUtil.closeResultSet(rs);
            CloseUtil.closePreparedStatement(prepStmt);
        }

        /*
         * return all content request primary keys
         */
        return FullTextUtil.getPrimaryKeysWithoutDuplicates(primaryKeys);
    }

    /**
     * Returns the next id for a new content request.
     * 
     * 
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    private static PrimaryKey getNextContentRequestPrimaryKey() {
        return NextPrimaryKeyUtil.getNextSequentialAlphanumericSingleKey(
                "content_request", EntitiesDictionary.CONTENT_REQUESTS,
                DataSessionSetGlobal.get().getSharedHibernateDataSession(
                        DataSessionTypeConstants.WIKI_SHARED));
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequest#storeApproval()
     */
    public void storeApproval() {
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
         * get the primary key of the content request
         */
        PrimaryKey recordPk = this.getPrimaryKey();

        /*
         * get the primary key of the content request entity
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_REQUESTS);

        /*
         * update this content request
         */
        try {
            /*
             * get the hibernate session
             */
            Session session = ds.getWriteSession();

            /*
             * load the content request mapping
             */
            ContentRequestMapping mapping = (ContentRequestMapping) session
                    .get(DefaultContentRequestMapping.class, recordPk
                            .toString());

            /*
             * update the content request using that mapping
             */
            if (mapping != null) {
                mapping.setStatusTypeId(RequestStatusTypesDictionary.APPROVED);
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
         * add an entry log and load the content request information
         */
        if (this.getIsDone()) {
            PrimaryKey typePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntryLogTypeConstants.APPROVAL);
            this.addEntryLog(entityPk, recordPk, typePk,
                    DataSessionTypeConstants.WIKI_SHARED);
            this.setPrimaryKeyClearCacheAndLoad(recordPk);
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequest#storeRejection()
     */
    public void storeRejection() {
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
         * get the primary key of the content request
         */
        PrimaryKey recordPk = this.getPrimaryKey();

        /*
         * get the primary key of the content request entity
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_REQUESTS);

        /*
         * update this content request
         */
        try {
            /*
             * get the hibernate session
             */
            Session session = ds.getWriteSession();

            /*
             * load the content request mapping
             */
            ContentRequestMapping mapping = (ContentRequestMapping) session
                    .get(DefaultContentRequestMapping.class, recordPk
                            .toString());

            /*
             * update the content request using that mapping
             */
            if (mapping != null) {
                mapping.setStatusTypeId(RequestStatusTypesDictionary.REJECTED);
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
         * add an entry log and load the content request information
         */
        if (this.getIsDone()) {
            PrimaryKey typePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntryLogTypeConstants.APPROVAL);
            this.addEntryLog(entityPk, recordPk, typePk,
                    DataSessionTypeConstants.WIKI_SHARED);
            this.setPrimaryKeyClearCacheAndLoad(recordPk);
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequest#storeCancellation()
     */
    public void storeCancellation() {
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
         * get the primary key of the content request
         */
        PrimaryKey recordPk = this.getPrimaryKey();

        /*
         * get the primary key of the content request entity
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_REQUESTS);

        /*
         * update this content request
         */
        try {
            /*
             * get the hibernate session
             */
            Session session = ds.getWriteSession();

            /*
             * load the content request mapping
             */
            ContentRequestMapping mapping = (ContentRequestMapping) session
                    .get(DefaultContentRequestMapping.class, recordPk
                            .toString());

            /*
             * update the content request using that mapping
             */
            if (mapping != null) {
                mapping.setStatusTypeId(RequestStatusTypesDictionary.CANCELLED);
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
         * add an entry log and load the content request information
         */
        if (this.getIsDone()) {
            PrimaryKey typePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntryLogTypeConstants.CANCELLATION);
            this.addEntryLog(entityPk, recordPk, typePk,
                    DataSessionTypeConstants.WIKI_SHARED);
            this.setPrimaryKeyClearCacheAndLoad(recordPk);
        }
    }

    /**
     * Creates a new article request in the database.
     * 
     * @param contentInfoPk
     *            primary key of a content info
     * @param body
     *            a String representing the body in HTML format
     * @param authorPk
     *            primary key of an author account
     * @param editorPk
     *            primary key of an editor account
     * @param associateEditorsPk
     *            primary key of an associate editor group
     * @param friendlyAddress
     *            a String representing a friendly address
     * @param parentArticlePk
     *            primary key of a parent article
     * @param classificationTypePk
     *            primary key of a classification type
     * @param requestorPk
     *            primary key of a requestor account
     * @param approverPk
     *            primary key of an approver account
     * @param ruleTypePk
     *            primary key of a rule type
     * @param comment
     *            a String representing the version notes
     * 
     * 
     */
    public void addNewArticleRequest(PrimaryKey contentInfoPk, String body,
            PrimaryKey authorPk, PrimaryKey editorPk,
            PrimaryKey associateEditorsPk, String friendlyAddress,
            PrimaryKey parentArticlePk, PrimaryKey classificationTypePk,
            PrimaryKey requestorPk, PrimaryKey approverPk,
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
         * find the next id
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_REQUESTS);
        SingleKey recordPk1 = (SingleKey) getNextContentRequestPrimaryKey();

        try {
            /*
             * get the hibernate session
             */
            Session session = ds.getWriteSession();

            /*
             * save the content request record
             */
            ContentRequestMapping mapping = getContentRequestMappingNewInstance();
            mapping.setId(recordPk1.toString());
            mapping.setRequestTypeId(ContentRequestTypesDictionary.ADD);
            if (contentInfoPk != null) {
                mapping.setContentInfoId(contentInfoPk.toString());
            } else {
                mapping.setContentInfoId(null);
            }
            mapping.setFriendlyAddress(friendlyAddress);
            mapping.setBody(body);
            mapping.setContentTypeId(ContentTypesDictionary.ARTICLE);
            if (parentArticlePk != null) {
                mapping.setParentContentId(parentArticlePk.toString());
            } else {
                mapping.setParentContentId(null);
            }
            mapping.setStatusTypeId(RequestStatusTypesDictionary.PENDING);
            if (classificationTypePk != null) {
                mapping.setContentClassTypeId(classificationTypePk.toString());
            } else {
                mapping.setContentClassTypeId(null);
            }
            if (ruleTypePk != null) {
                mapping.setContentRuleTypeId(ruleTypePk.toString());
            } else {
                mapping.setContentRuleTypeId(null);
            }
            mapping.setComment(comment);
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
         * insert the editor role
         */
        if (this.getIsDone()) {
            PrimaryKey editorRolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.EDITOR);
            AccountRoleXrefFactory.create(this, ds, entityPk, editorRolePk,
                    recordPk1, editorPk);
        }

        /*
         * insert the associate editor role
         */
        if ((this.getIsDone()) && (associateEditorsPk != null)) {
            PrimaryKey rolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.EDITOR);
            GroupRoleXrefFactory.create(this, ds, entityPk, rolePk, recordPk1,
                    associateEditorsPk);
        }

        /*
         * insert the requestor role
         */
        if (this.getIsDone()) {
            PrimaryKey requestorRolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.REQUESTOR);
            AccountRoleXrefFactory.create(this, ds, entityPk, requestorRolePk,
                    recordPk1, requestorPk);
        }

        /*
         * insert the approver role
         */
        if (this.getIsDone()) {
            PrimaryKey approverRolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.APPROVER);
            AccountRoleXrefFactory.create(this, ds, entityPk, approverRolePk,
                    recordPk1, approverPk);
        }

        /*
         * insert the author role
         */
        if (this.getIsDone()) {
            PrimaryKey authorRolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.AUTHOR);
            AccountRoleXrefFactory.create(this, ds, entityPk, authorRolePk,
                    recordPk1, authorPk);
        }

        /*
         * add an entry log and load the content request information
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
     * Creates a new image request in the database.
     * 
     * @param contentInfoPk
     *            primary key of a content info
     * @param authorPk
     *            primary key of an author account
     * @param friendlyAddress
     *            a friendly address
     * @param filePk
     *            primary key of a stored file
     * @param parentArticlePk
     *            primary key of a parent article
     * @param classificationTypePk
     *            primary key of a classification type
     * @param requestorPk
     *            primary key of a requestor account
     * @param approverPk
     *            primary key of an approver account
     * @param ruleTypePk
     *            primary key of a rule type
     * @param comment
     *            a comment
     * 
     * 
     */
    public void addNewImageRequest(PrimaryKey contentInfoPk,
            PrimaryKey authorPk, String friendlyAddress, PrimaryKey filePk,
            PrimaryKey parentArticlePk, PrimaryKey classificationTypePk,
            PrimaryKey requestorPk, PrimaryKey approverPk,
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
         * find the next id
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_REQUESTS);
        SingleKey recordPk1 = (SingleKey) getNextContentRequestPrimaryKey();

        try {
            /*
             * get the hibernate session
             */
            Session session = ds.getWriteSession();

            /*
             * save the content request record
             */
            ContentRequestMapping mapping = getContentRequestMappingNewInstance();
            mapping.setId(recordPk1.toString());
            mapping.setRequestTypeId(ContentRequestTypesDictionary.ADD);
            if (contentInfoPk != null) {
                mapping.setContentInfoId(contentInfoPk.toString());
            } else {
                mapping.setContentInfoId(null);
            }
            mapping.setFriendlyAddress(friendlyAddress);
            mapping.setContentTypeId(ContentTypesDictionary.IMAGE);
            if (parentArticlePk != null) {
                mapping.setParentContentId(parentArticlePk.toString());
            } else {
                mapping.setParentContentId(null);
            }
            mapping.setStatusTypeId(RequestStatusTypesDictionary.PENDING);
            if (classificationTypePk != null) {
                mapping.setContentClassTypeId(classificationTypePk.toString());
            } else {
                mapping.setContentClassTypeId(null);
            }
            if (ruleTypePk != null) {
                mapping.setContentRuleTypeId(ruleTypePk.toString());
            } else {
                mapping.setContentRuleTypeId(null);
            }
            mapping.setComment(comment);
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
         * insert the author role
         */
        if (this.getIsDone()) {
            PrimaryKey authorRolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.AUTHOR);
            AccountRoleXrefFactory.create(this, ds, entityPk, authorRolePk,
                    recordPk1, authorPk);
        }

        /*
         * insert the requestor role
         */
        if (this.getIsDone()) {
            PrimaryKey requestorRolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.REQUESTOR);
            AccountRoleXrefFactory.create(this, ds, entityPk, requestorRolePk,
                    recordPk1, requestorPk);
        }

        /*
         * insert the approver role
         */
        if (this.getIsDone()) {
            PrimaryKey approverRolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.APPROVER);
            AccountRoleXrefFactory.create(this, ds, entityPk, approverRolePk,
                    recordPk1, approverPk);
        }

        /*
         * insert the file role
         */
        if (this.getIsDone()) {
            PrimaryKey rolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.ATTACHMENT);

            StoredFileRoleXrefFactory.create(this, ds, entityPk, rolePk,
                    recordPk1, filePk);
        }

        /*
         * add an entry log and load the content request information
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
     * Creates a image removal request in the database.
     * 
     * @param childContentPk
     *            primary key of a child content
     * @param parentArticlePk
     *            primary key of a parent article
     * @param requestorPk
     *            primary key of a requestor account
     * @param approverPk
     *            primary key of an approver account
     * 
     * 
     */
    public void addImageRemovalRequest(PrimaryKey childContentPk,
            PrimaryKey parentArticlePk, PrimaryKey requestorPk,
            PrimaryKey approverPk) {
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
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_REQUESTS);
        SingleKey recordPk1 = (SingleKey) getNextContentRequestPrimaryKey();

        try {
            /*
             * get the hibernate session
             */
            Session session = ds.getWriteSession();

            /*
             * save the content request record
             */
            ContentRequestMapping mapping = getContentRequestMappingNewInstance();
            mapping.setId(recordPk1.toString());
            mapping.setRequestTypeId(ContentRequestTypesDictionary.REMOVE);
            if (childContentPk != null) {
                mapping.setChildContentId(childContentPk.toString());
            } else {
                mapping.setChildContentId(null);
            }
            mapping.setContentTypeId(ContentTypesDictionary.IMAGE);
            if (parentArticlePk != null) {
                mapping.setParentContentId(parentArticlePk.toString());
            } else {
                mapping.setParentContentId(null);
            }
            mapping.setStatusTypeId(RequestStatusTypesDictionary.PENDING);
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
         * insert the requestor role
         */
        if (this.getIsDone()) {
            PrimaryKey requestorRolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.REQUESTOR);
            AccountRoleXrefFactory.create(this, ds, entityPk, requestorRolePk,
                    recordPk1, requestorPk);
        }

        /*
         * insert the approver role
         */
        if (this.getIsDone()) {
            PrimaryKey approverRolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.APPROVER);
            AccountRoleXrefFactory.create(this, ds, entityPk, approverRolePk,
                    recordPk1, approverPk);
        }

        /*
         * add an entry log and load the content request information
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
     * Creates an article removal request in the database.
     * 
     * @param childContentPk
     *            primary key of a content
     * @param parentArticlePk
     *            primary key of a parent article
     * @param requestorPk
     *            primary key of a requestor account
     * @param approverPk
     *            primary key of an approver account
     * 
     * 
     */
    public void addArticleRemovalRequest(PrimaryKey childContentPk,
            PrimaryKey parentArticlePk, PrimaryKey requestorPk,
            PrimaryKey approverPk) {
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
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_REQUESTS);
        SingleKey recordPk1 = (SingleKey) getNextContentRequestPrimaryKey();

        try {
            /*
             * get the hibernate session
             */
            Session session = ds.getWriteSession();

            /*
             * save the content request record
             */
            ContentRequestMapping mapping = getContentRequestMappingNewInstance();
            mapping.setId(recordPk1.toString());
            mapping.setRequestTypeId(ContentRequestTypesDictionary.REMOVE);
            if (childContentPk != null) {
                mapping.setChildContentId(childContentPk.toString());
            } else {
                mapping.setChildContentId(null);
            }
            mapping.setContentTypeId(ContentTypesDictionary.ARTICLE);
            if (parentArticlePk != null) {
                mapping.setParentContentId(parentArticlePk.toString());
            } else {
                mapping.setParentContentId(null);
            }
            mapping.setStatusTypeId(RequestStatusTypesDictionary.PENDING);
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
         * insert the requestor role
         */
        if (this.getIsDone()) {
            PrimaryKey requestorRolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.REQUESTOR);
            AccountRoleXrefFactory.create(this, ds, entityPk, requestorRolePk,
                    recordPk1, requestorPk);
        }

        /*
         * insert the approver role
         */
        if (this.getIsDone()) {
            PrimaryKey approverRolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.APPROVER);
            AccountRoleXrefFactory.create(this, ds, entityPk, approverRolePk,
                    recordPk1, approverPk);
        }

        /*
         * add an entry log and load the content request information
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
     * Creates a image update request in the database.
     * 
     * @param childContentPk
     *            primary key of a child content
     * @param contentInfoPk
     *            primary key of a content info
     * @param authorPk
     *            primary key of an author account
     * @param friendlyAddress
     *            a friendly address
     * @param filePk
     *            primary key of a stored file
     * @param parentArticlePk
     *            primary key of a parent article
     * @param classificationTypePk
     *            primary key of a classification type
     * @param requestorPk
     *            primary key of a requestor account
     * @param approverPk
     *            primary key of an approver account
     * @param ruleTypePk
     *            primary key of a rule type
     * @param comment
     *            a comment
     * 
     * 
     */
    public void addImageUpdateRequest(PrimaryKey childContentPk,
            PrimaryKey contentInfoPk, PrimaryKey authorPk,
            String friendlyAddress, PrimaryKey filePk,
            PrimaryKey parentArticlePk, PrimaryKey classificationTypePk,
            PrimaryKey requestorPk, PrimaryKey approverPk,
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
         * find the next id
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_REQUESTS);
        SingleKey recordPk1 = (SingleKey) getNextContentRequestPrimaryKey();

        try {
            /*
             * get the hibernate session
             */
            Session session = ds.getWriteSession();

            /*
             * save the content request record
             */
            ContentRequestMapping mapping = getContentRequestMappingNewInstance();
            mapping.setId(recordPk1.toString());
            mapping.setRequestTypeId(ContentRequestTypesDictionary.EDIT);
            if (contentInfoPk != null) {
                mapping.setContentInfoId(contentInfoPk.toString());
            } else {
                mapping.setContentInfoId(null);
            }
            mapping.setContentTypeId(ContentTypesDictionary.ARTICLE);
            if (parentArticlePk != null) {
                mapping.setParentContentId(parentArticlePk.toString());
            } else {
                mapping.setParentContentId(null);
            }
            if (childContentPk != null) {
                mapping.setChildContentId(childContentPk.toString());
            } else {
                mapping.setChildContentId(null);
            }
            mapping.setFriendlyAddress(friendlyAddress);
            mapping.setStatusTypeId(RequestStatusTypesDictionary.PENDING);
            if (classificationTypePk != null) {
                mapping.setContentClassTypeId(classificationTypePk.toString());
            } else {
                mapping.setContentClassTypeId(null);
            }
            if (ruleTypePk != null) {
                mapping.setContentRuleTypeId(ruleTypePk.toString());
            } else {
                mapping.setContentRuleTypeId(null);
            }
            mapping.setComment(comment);
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
         * insert the author role
         */
        if (this.getIsDone()) {
            PrimaryKey authorRolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.AUTHOR);
            AccountRoleXrefFactory.create(this, ds, entityPk, authorRolePk,
                    recordPk1, authorPk);
        }

        /*
         * insert the requestor role
         */
        if (this.getIsDone()) {
            PrimaryKey requestorRolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.REQUESTOR);
            AccountRoleXrefFactory.create(this, ds, entityPk, requestorRolePk,
                    recordPk1, requestorPk);
        }

        /*
         * insert the approver role
         */
        if (this.getIsDone()) {
            PrimaryKey approverRolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.APPROVER);
            AccountRoleXrefFactory.create(this, ds, entityPk, approverRolePk,
                    recordPk1, approverPk);
        }

        /*
         * insert the file role
         */
        if (this.getIsDone()) {
            PrimaryKey rolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.ATTACHMENT);
            StoredFileRoleXrefFactory.create(this, ds, entityPk, rolePk,
                    recordPk1, filePk);
        }

        /*
         * add an entry log and load the content request information
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
     * Creates an article update request in the database.
     * 
     * @param childContentPk
     *            primary key of a child content
     * @param contentInfoPk
     *            primary key of a content info
     * @param body
     *            a String representing a body
     * @param authorPk
     *            primary key of an author account
     * @param editorPk
     *            primary key of an editor account
     * @param associateEditorsPk
     *            primary key of an associate editors group
     * @param friendlyAddress
     *            a String representing a friendly address
     * @param parentArticlePk
     *            primary key of a parent article
     * @param classificationTypePk
     *            primary key of a classification type
     * @param requestorPk
     *            primary key of a requestor account
     * @param approverPk
     *            primary key of an approver account
     * @param ruleTypePk
     *            primary key of a rule type
     * @param comment
     *            a comment
     * 
     * 
     */
    public void addArticleUpdateRequest(PrimaryKey childContentPk,
            PrimaryKey contentInfoPk, String body, PrimaryKey authorPk,
            PrimaryKey editorPk, PrimaryKey associateEditorsPk,
            String friendlyAddress, PrimaryKey parentArticlePk,
            PrimaryKey classificationTypePk, PrimaryKey requestorPk,
            PrimaryKey approverPk, PrimaryKey ruleTypePk, String comment) {
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
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_REQUESTS);
        SingleKey recordPk1 = (SingleKey) getNextContentRequestPrimaryKey();

        try {
            /*
             * get the hibernate session
             */
            Session session = ds.getWriteSession();

            /*
             * save the content request record
             */
            ContentRequestMapping mapping = getContentRequestMappingNewInstance();
            mapping.setId(recordPk1.toString());
            mapping.setRequestTypeId(ContentRequestTypesDictionary.EDIT);
            if (contentInfoPk != null) {
                mapping.setContentInfoId(contentInfoPk.toString());
            } else {
                mapping.setContentInfoId(null);
            }
            mapping.setBody(body);
            mapping.setContentTypeId(ContentTypesDictionary.ARTICLE);
            if (parentArticlePk != null) {
                mapping.setParentContentId(parentArticlePk.toString());
            } else {
                mapping.setParentContentId(null);
            }
            if (childContentPk != null) {
                mapping.setChildContentId(childContentPk.toString());
            } else {
                mapping.setChildContentId(null);
            }
            mapping.setFriendlyAddress(friendlyAddress);
            mapping.setStatusTypeId(RequestStatusTypesDictionary.PENDING);
            if (classificationTypePk != null) {
                mapping.setContentClassTypeId(classificationTypePk.toString());
            } else {
                mapping.setContentClassTypeId(null);
            }
            if (ruleTypePk != null) {
                mapping.setContentRuleTypeId(ruleTypePk.toString());
            } else {
                mapping.setContentRuleTypeId(null);
            }
            mapping.setComment(comment);
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
         * insert the requestor role
         */
        if (this.getIsDone()) {
            PrimaryKey requestorRolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.REQUESTOR);
            AccountRoleXrefFactory.create(this, ds, entityPk, requestorRolePk,
                    recordPk1, requestorPk);
        }

        /*
         * insert the approver role
         */
        if (this.getIsDone()) {
            PrimaryKey approverRolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.APPROVER);
            AccountRoleXrefFactory.create(this, ds, entityPk, approverRolePk,
                    recordPk1, approverPk);
        }

        /*
         * insert the author role
         */
        if (this.getIsDone()) {
            PrimaryKey authorRolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.AUTHOR);
            AccountRoleXrefFactory.create(this, ds, entityPk, authorRolePk,
                    recordPk1, authorPk);
        }

        /*
         * insert the editor role
         */
        if (this.getIsDone()) {
            PrimaryKey editorRolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.EDITOR);
            AccountRoleXrefFactory.create(this, ds, entityPk, editorRolePk,
                    recordPk1, editorPk);
        }

        /*
         * insert the associate editor role
         */
        if (this.getIsDone()) {
            PrimaryKey editorRolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.EDITOR);
            GroupRoleXrefFactory.create(this, ds, entityPk, editorRolePk,
                    recordPk1, associateEditorsPk);
        }

        /*
         * add an entry log and load the content request information
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
     * Creates a new reference request in the database.
     * 
     * @param childContentPk
     *            primary key of a child content
     * @param parentArticlePk
     *            primary key of a parent article
     * @param requestorPk
     *            primary key of a requestor account
     * @param approverPk
     *            primary key of an approver account
     * 
     * 
     */
    public void addNewReferenceRequest(PrimaryKey childContentPk,
            PrimaryKey parentArticlePk, PrimaryKey requestorPk,
            PrimaryKey approverPk) {
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
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_REQUESTS);
        SingleKey recordPk1 = (SingleKey) getNextContentRequestPrimaryKey();

        try {
            /*
             * get the hibernate session
             */
            Session session = ds.getWriteSession();

            /*
             * save the content request record
             */
            ContentRequestMapping mapping = getContentRequestMappingNewInstance();
            mapping.setId(recordPk1.toString());
            mapping.setRequestTypeId(ContentRequestTypesDictionary.ADD);
            if (childContentPk != null) {
                mapping.setChildContentId(childContentPk.toString());
            } else {
                mapping.setChildContentId(null);
            }
            mapping.setContentTypeId(ContentTypesDictionary.REFERENCE);
            if (parentArticlePk != null) {
                mapping.setParentContentId(parentArticlePk.toString());
            } else {
                mapping.setParentContentId(null);
            }
            mapping.setStatusTypeId(RequestStatusTypesDictionary.PENDING);
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
         * insert the requestor role
         */
        if (this.getIsDone()) {
            PrimaryKey requestorRolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.REQUESTOR);
            AccountRoleXrefFactory.create(this, ds, entityPk, requestorRolePk,
                    recordPk1, requestorPk);
        }

        /*
         * insert the approver role
         */
        if (this.getIsDone()) {
            PrimaryKey approverRolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.APPROVER);
            AccountRoleXrefFactory.create(this, ds, entityPk, approverRolePk,
                    recordPk1, approverPk);
        }

        /*
         * add an entry log and load the content request information
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
     * Creates a reference removal request in the database.
     * 
     * @param childContentPk
     *            primary key of a child content
     * @param parentArticlePk
     *            primary key of a parent article
     * @param requestorPk
     *            primary key of a requestor account
     * @param approverPk
     *            primary key of an approver account
     * 
     * 
     */
    public void addReferenceRemovalRequest(PrimaryKey childContentPk,
            PrimaryKey parentArticlePk, PrimaryKey requestorPk,
            PrimaryKey approverPk) {
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
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_REQUESTS);
        SingleKey recordPk1 = (SingleKey) getNextContentRequestPrimaryKey();

        try {
            /*
             * get the hibernate session
             */
            Session session = ds.getWriteSession();

            /*
             * save the content request record
             */
            ContentRequestMapping mapping = getContentRequestMappingNewInstance();
            mapping.setId(recordPk1.toString());
            mapping.setRequestTypeId(ContentRequestTypesDictionary.REMOVE);
            if (childContentPk != null) {
                mapping.setChildContentId(childContentPk.toString());
            } else {
                mapping.setChildContentId(null);
            }
            mapping.setContentTypeId(ContentTypesDictionary.REFERENCE);
            if (parentArticlePk != null) {
                mapping.setParentContentId(parentArticlePk.toString());
            } else {
                mapping.setParentContentId(null);
            }
            mapping.setStatusTypeId(RequestStatusTypesDictionary.PENDING);
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
         * insert the requestor role
         */
        if (this.getIsDone()) {
            PrimaryKey requestorRolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.REQUESTOR);
            AccountRoleXrefFactory.create(this, ds, entityPk, requestorRolePk,
                    recordPk1, requestorPk);
        }

        /*
         * insert the approver role
         */
        if (this.getIsDone()) {
            PrimaryKey approverRolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.APPROVER);
            AccountRoleXrefFactory.create(this, ds, entityPk, approverRolePk,
                    recordPk1, approverPk);
        }

        /*
         * add an entry log and load the content request information
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
     * Removes this content request.
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
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_REQUESTS);
        PrimaryKey recordPk = this.getPrimaryKey();

        try {
            /*
             * get the hibernate session
             */
            Session session = ds.getWriteSession();

            /*
             * load the content request mapping
             */
            ContentRequestMapping mapping = (ContentRequestMapping) session
                    .get(DefaultContentRequestMapping.class, recordPk
                            .toString());

            /*
             * retrieve the content request from the database
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

        if (this.getIsDone()) {
            AccountRoleXrefFactory.removeAll(this, ds, entityPk, recordPk);
        }

        if (this.getIsDone()) {
            GroupRoleXrefFactory.removeAll(this, ds, entityPk, recordPk);
        }

        if (this.getIsDone()) {
            StoredFileRoleXrefFactory.removeAll(this, ds, entityPk, recordPk);
        }

        /*
         * refresh this content request
         */
        if (this.getIsDone()) {
            PrimaryKey typePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(com.corendal.netapps.framework.core.constants.EntryLogTypeConstants.DELETE);
            this.addEntryLog(entityPk, recordPk, typePk,
                    DataSessionTypeConstants.WIKI_SHARED);
        }
    }

    /**
     * Returns the ordered list of all requests for a requestor primary key.
     * 
     * @param requestorPk
     *            primary key of a requestor account
     * 
     * 
     * @return a java.util.List object
     */
    public static final List<PrimaryKey> findEnabledByRequestorPrimaryKey(
            PrimaryKey requestorPk) {
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
        String selectStatement = "select content_request.id " + "from "
                + ds.getTableNameWithDefaultSchema("account_role_xref") + ", "
                + ds.getTableNameWithDefaultSchema("content_request") + " "
                + "where account_role_xref.role_id =? "
                + "and account_role_xref.record_id = content_request.id "
                + "and account_role_xref.account_id=? "
                + "and content_request.status_typ_id=? "
                + "and content_request.enabled_flag=? "
                + "and account_role_xref.entity_id=?";
        // FIXME: hibernate select query

        try {
            /*
             * create the prepared statement
             */
            prepStmt = ds.getReadConnection().prepareStatement(selectStatement);

            /*
             * associate the requestor
             */
            prepStmt.setString(1, RolesDictionary.REQUESTOR);
            prepStmt.setString(2, requestorPk.toString());
            prepStmt.setString(3, RequestStatusTypesDictionary.PENDING);
            prepStmt.setString(4, "Y");
            prepStmt.setString(5, EntitiesDictionary.CONTENT_REQUESTS);

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
                    .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_REQUESTS);
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, null);
        } finally {
            /*
             * close the recordset and the prepared statement
             */
            CloseUtil.closeResultSet(rs);
            CloseUtil.closePreparedStatement(prepStmt);
        }

        /*
         * return all content requests primary keys
         */
        return primaryKeys;
    }

    /**
     * Returns the ordered list of all requests for an approver primary key.
     * 
     * @param approverPk
     *            primary key of the approver to parse
     * 
     * 
     * @return a java.util.List object
     */
    public static final List<PrimaryKey> findEnabledByApproverPrimaryKey(
            PrimaryKey approverPk) {
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
        String selectStatement = "select content_request.id " + "from "
                + ds.getTableNameWithDefaultSchema("account_role_xref") + ", "
                + ds.getTableNameWithDefaultSchema("content_request") + " "
                + "where account_role_xref.role_id =? "
                + "and account_role_xref.record_id = content_request.id "
                + "and account_role_xref.account_id=? "
                + "and content_request.status_typ_id=? "
                + "and content_request.enabled_flag=? "
                + "and account_role_xref.entity_id=?";
        // FIXME: hibernate select query

        try {
            /*
             * create the prepared statement
             */
            prepStmt = ds.getReadConnection().prepareStatement(selectStatement);

            /*
             * associate the requestor
             */
            prepStmt.setString(1, RolesDictionary.APPROVER);
            prepStmt.setString(2, approverPk.toString());
            prepStmt.setString(3, RequestStatusTypesDictionary.PENDING);
            prepStmt.setString(4, "Y");
            prepStmt.setString(5, EntitiesDictionary.CONTENT_REQUESTS);

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
                    .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_REQUESTS);
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, null);
        } finally {
            /*
             * close the recordset and the prepared statement
             */
            CloseUtil.closeResultSet(rs);
            CloseUtil.closePreparedStatement(prepStmt);
        }

        /*
         * return all content requests primary keys
         */
        return primaryKeys;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequest#storeComment(java.lang.String)
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
         * get the primary key of this account
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_REQUESTS);
        PrimaryKey recordPk = this.getPrimaryKey();

        /*
         * update this content request
         */
        try {
            /*
             * get the hibernate session
             */
            Session session = ds.getWriteSession();

            /*
             * load the content request mapping
             */
            ContentRequestMapping mapping = (ContentRequestMapping) session
                    .get(DefaultContentRequestMapping.class, recordPk
                            .toString());

            /*
             * update the content request using that mapping
             */
            if (mapping != null) {
                mapping.setComment(comment);
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
         * refresh this content request
         */
        if (this.getIsDone()) {
            PrimaryKey typePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(com.corendal.netapps.framework.core.constants.EntryLogTypeConstants.UPDATE);
            this.addEntryLog(entityPk, recordPk, typePk,
                    DataSessionTypeConstants.WIKI_SHARED);
            this.clearCacheAndLoad();
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequest#storeFriendlyAddress(java.lang.String)
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
         * get the primary key of this account
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_REQUESTS);
        PrimaryKey recordPk = this.getPrimaryKey();

        /*
         * update this content request
         */
        try {
            /*
             * get the hibernate session
             */
            Session session = ds.getWriteSession();

            /*
             * load the content request mapping
             */
            ContentRequestMapping mapping = (ContentRequestMapping) session
                    .get(DefaultContentRequestMapping.class, recordPk
                            .toString());

            /*
             * update the content request using that mapping
             */
            if (mapping != null) {
                mapping.setFriendlyAddress(friendlyAddress);
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
         * refresh this content request
         */
        if (this.getIsDone()) {
            PrimaryKey typePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(com.corendal.netapps.framework.core.constants.EntryLogTypeConstants.UPDATE);
            this.addEntryLog(entityPk, recordPk, typePk,
                    DataSessionTypeConstants.WIKI_SHARED);
            this.clearCacheAndLoad();
        }
    }

    /**
     * Returns the ordered list of all pending requests for a content primary
     * key. This does not include the requests related to the contents of a
     * article.
     * 
     * @param contentPk
     *            primary key of the content to parse
     * 
     * 
     * @return a java.util.List object
     */
    public static final List<PrimaryKey> findEnabledPendingByContentPrimaryKey(
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
         * find the primary key of each invoice
         */
        try {
            /*
             * get the query
             */
            Query query = ds
                    .getReadNamedQuery(FIND_ENABLED_PENDING_BY_CONTENT_PRIMARY_KEY);

            /*
             * associate the enabled flag
             */
            query.setParameter("statusTypeId",
                    RequestStatusTypesDictionary.PENDING);
            query.setParameter("childContentId", contentPk.toString());
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
                    .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_REQUESTS);
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, null);
        }

        /*
         * return all content requests primary keys
         */
        return primaryKeys;
    }

    /**
     * Returns the ordered list of all pending requests for an article primary
     * key. This does not include the requests related to the article itself,
     * only the content requests to add, modify or delete a content in this
     * article
     * 
     * @param articlePk
     *            primary key of the article to parse
     * 
     * 
     * @return a java.util.List object
     */
    public static final List<PrimaryKey> findEnabledPendingByParentArticlePrimaryKey(
            PrimaryKey articlePk) {
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
         * find the primary key of each invoice
         */
        try {
            /*
             * get the query
             */
            Query query = ds
                    .getReadNamedQuery(FIND_ENABLED_PENDING_BY_PARENT_ARTICLE_PRIMARY_KEY);

            /*
             * associate the enabled flag
             */
            query.setParameter("statusTypeId",
                    RequestStatusTypesDictionary.PENDING);
            query.setParameter("parentContentId", articlePk.toString());
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
                    .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_REQUESTS);
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, null);
        }

        /*
         * return all content requests primary keys
         */
        return primaryKeys;
    }

    /**
     * Cleans up all data related to this entity.
     */
    public static void cleanup() {
        /*
         * get the entity primary key
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_REQUESTS);

        /*
         * get the dates
         */
        java.util.Date today = DateUtil.getTruncated(new java.util.Date());
        java.util.Date onMonthAgo = DateUtil.getRemovedDays(today, 30);
        Timestamp oneMonthAgoTimestamp = TimestampUtil
                .getDateToTimestamp(onMonthAgo);

        /*
         * initialize the prepared statement
         */
        PreparedStatement prepStmt = null;

        /*
         * get the JDBC data session
         */
        HibernateDataSession ds = DataSessionSetGlobal.get()
                .getSharedHibernateDataSession(
                        DataSessionTypeConstants.WIKI_SHARED);

        /*
         * set the delete statement
         */
        String deleteStatement = "delete from "
                + ds.getTableNameWithDefaultSchema("content_request") + " "
                + "where status_typ_id in (?, ?) "
                + "and not exists (select entry_log.entity_id from "
                + ds.getTableNameWithDefaultSchema("entry_log") + " "
                + "where entry_log.timestamp >= ? "
                + "and entry_log.entity_id = ? "
                + "and entry_log.record_id = content_request.id)";
        // FIXME: hibernate delete query

        try {
            /*
             * create the prepared statement
             */
            prepStmt = ds.getWriteConnection()
                    .prepareStatement(deleteStatement);

            /*
             * associate the id
             */
            prepStmt.setString(1, RequestStatusTypesDictionary.REJECTED);
            prepStmt.setString(2, RequestStatusTypesDictionary.CANCELLED);
            prepStmt.setTimestamp(3, oneMonthAgoTimestamp);
            prepStmt.setString(4, EntitiesDictionary.CONTENT_REQUESTS);

            /*
             * execute the query
             */
            prepStmt.executeUpdate();
        } catch (Exception e) {
            /*
             * log the exception
             */
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, null);
        } finally {
            /*
             * close the prepared statement
             */
            CloseUtil.closePreparedStatement(prepStmt);
        }

        /*
         * call the generic data cleanup
         */
        String tableName = "content_request";
        AbstractWriteDataBean.cleanup(entityPk, tableName,
                DataSessionTypeConstants.WIKI_SHARED);
    }
}

// end AbstractContentRequest

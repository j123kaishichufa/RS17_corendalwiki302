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
import com.corendal.netapps.framework.core.utils.DateUtil;
import com.corendal.netapps.framework.core.utils.FullTextUtil;
import com.corendal.netapps.framework.core.writedatafactories.AccountRoleXrefFactory;
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
import com.corendal.netapps.wiki.interfaces.Article;
import com.corendal.netapps.wiki.interfaces.ContentAddressMapping;
import com.corendal.netapps.wiki.interfaces.ContentBodyMapping;
import com.corendal.netapps.wiki.interfaces.ContentMapMapping;
import com.corendal.netapps.wiki.interfaces.ContentMapping;
import com.corendal.netapps.wiki.interfaces.ContentVersionMapping;
import com.corendal.netapps.wiki.writedatamappings.DefaultContentAddressMapping;
import com.corendal.netapps.wiki.writedatamappings.DefaultContentBodyMapping;
import com.corendal.netapps.wiki.writedatamappings.DefaultContentMapMapping;
import com.corendal.netapps.wiki.writedatamappings.DefaultContentMapping;
import com.corendal.netapps.wiki.writedatamappings.DefaultContentVersionMapping;

/**
 * AbstractArticle is the abstract class retrieving information about each
 * article of the application from the database.
 *
 * @version $Id: AbstractArticle.java,v 1.68 2008/11/19 17:12:04 tdanard Exp $
 */
public abstract class AbstractArticle extends AbstractWriteDataBean implements
        Cloneable, Article {
    /** Fully qualified name of this class. */
    private static final String ABSTRACT_CLASS_NAME = "com.corendal.netapps.wiki.writedatabeans.AbstractArticle";

    /** Version_num in the content_version table */
    private int versionNum;

    /** Version_info_id in the content_version table */
    private String versionInfoId;

    /** Body in the content_version table */
    private String rawBodyHTML;

    /** Body_text in the content_body table */
    private String bodyText;

    /** Body_html in the content_body table */
    private String bodyHTML;

    /** Request_id in the content_version table */
    private String requestId;

    /** Friendly address in the content_version table */
    private String friendlyAddress;

    /** Enabled_flag in the content table */
    private String enabledFlag;

    /** Author account_id from the the account_role_xref table */
    private String authorId;

    /** Editor account id from the the account_role_xref table */
    private String editorId;

    /** Associate editor group id from the the group_obj_role_xref table */
    private String associateEditorsId;

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
     * Flag indicating whether this article HTML version was populated.
     */
    private boolean isRawBodyHTMLPopulated;

    /**
     * Flag indicating whether this article text version was populated.
     */
    private boolean isBodyTextPopulated;

    /**
     * Flag indicating whether this article HTML version was populated.
     */
    private boolean isBodyHTMLPopulated;

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
     * Name of the named query for the storeComment method.
     */
    private static final String STORE_COMMENT = ABSTRACT_CLASS_NAME
            + ".storeComment";

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
     * Name of the named query for the removeBodies method.
     */
    private static final String REMOVE_BODIES = ABSTRACT_CLASS_NAME
            + ".removeBodies";

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
     * Name of the named query for the populateBodyText method.
     */
    private static final String POPULATE_BODY_TEXT = ABSTRACT_CLASS_NAME
            + ".populateBodyText";

    /**
     * Name of the named query for the populateBodyHTML method.
     */
    private static final String POPULATE_BODY_HTML = ABSTRACT_CLASS_NAME
            + ".populateBodyHTML";

    /**
     * Name of the named query for the findEnabledByRank point 1 method.
     */
    private static final String FIND_ENABLED_BY_RANK_1 = ABSTRACT_CLASS_NAME
            + ".findEnabledByRank.1";

    /**
     * Default class constructor.
     */
    protected AbstractArticle() {
        super(DataSessionTypeConstants.WIKI_SHARED);
        this.setModifyType(EntryLogTypeConstants.SUPERSEDE, true);
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
     * Returns a new instance of a content version mapping.
     *
     * @return a com.corendal.netapps.wiki.interfaces.ContentVerison object
     */
    protected ContentVersionMapping getContentVersionMappingNewInstance() {
        return new DefaultContentVersionMapping();
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
     * Returns a new instance of a content body mapping.
     *
     * @return a com.corendal.netapps.qadocumentation.interfaces.MaterialRequest
     *         object
     */
    protected ContentBodyMapping getContentBodyMappingNewInstance() {
        return new DefaultContentBodyMapping();
    }

    /**
     * Returns a new instance of a content address mapping.
     *
     * @return a com.corendal.netapps.qadocumentation.interfaces.ContentAddress
     *         object
     */
    protected ContentAddressMapping getContentAddressMappingNewInstance() {
        return new DefaultContentAddressMapping();
    }

    /**
     * Returns a clone. Overrides AbstractWriteDataBean.clone.
     */
    @Override
    public Object clone() {
        return (AbstractArticle) super.clone();
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
         * initialize the populate flag
         */
        this.isRawBodyHTMLPopulated = false;
        this.isBodyTextPopulated = false;
        this.isBodyHTMLPopulated = false;

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
                .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);
        PrimaryKey recordPk = this.getPrimaryKey();

        /*
         * retrieve the article information from the database
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
             * associate the id and the article type
             */
            prepStmt.setString(1, recordPk.toString());
            prepStmt.setString(2, ContentTypesDictionary.ARTICLE);
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
                prepStmt.setString(4, EntitiesDictionary.ARTICLE_VERSIONS);

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
         * load this write data bean
         */
        if (this.getIsFound()) {
            super.load(entityPk, recordPk);
        }
    }

    /**
     * Populates the body of this article.
     */
    private void populateRawBodyHTML() {
        /*
         * indicate that the body has been populated
         */
        this.isRawBodyHTMLPopulated = true;

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
                .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);
        PrimaryKey recordPk = this.getPrimaryKey();

        /*
         * retrieve the body HTML information from the database
         */
        if (this.getIsFound()) {
            String selectStatement = "select content_version.body " + "from "
                    + ds.getTableNameWithDefaultSchema("content_version")
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
                prepStmt = ds.getReadConnection().prepareStatement(
                        selectStatement);

                /*
                 * associate the id and the article type
                 */
                prepStmt.setString(1, recordPk.toString());
                prepStmt.setString(2, ContentTypesDictionary.ARTICLE);
                prepStmt.setString(3, "Y");

                /*
                 * execute the query
                 */
                rs = prepStmt.executeQuery();

                /*
                 * loop the results
                 */
                this.rawBodyHTML = null;

                while (rs.next()) {
                    this.rawBodyHTML = ds.getStringFromCharacterStream(rs, 1);
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
    }

    /**
     * Populates the body of this article.
     */
    private void populateBodyHTML() {
        /*
         * indicate that the body has been populated
         */
        this.isBodyHTMLPopulated = true;

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

        /*
         * retrieve the body html information from the database
         */
        if (this.getIsFound()) {
            try {
                /*
                 * get the query
                 */
                Query query = ds.getReadNamedQuery(POPULATE_BODY_HTML);

                /*
                 * associate the query parameters
                 */
                query.setParameter("contentVersionId", this.versionId);

                /*
                 * only get one content body
                 */
                query.setMaxResults(1);

                /*
                 * get content body mapping
                 */
                ContentBodyMapping contentBodyMapping = (ContentBodyMapping) query
                        .uniqueResult();

                this.bodyHTML = null;

                /*
                 * store values from mapping
                 */
                if (contentBodyMapping != null) {
                    this.bodyHTML = contentBodyMapping.getBodyHtml();
                }
            } catch (Exception e) {
                /*
                 * log the exception
                 */
                this.appendLoadTrace(e.getMessage());
                LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, recordPk);
            }
        }
    }

    /**
     * Populates the body of this article.
     */
    private void populateBodyText() {
        /*
         * indicate that the body has been populated
         */
        this.isBodyTextPopulated = true;

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

        /*
         * retrieve the body text information from the database
         */
        if (this.getIsFound()) {
            try {
                /*
                 * get the query
                 */
                Query query = ds.getReadNamedQuery(POPULATE_BODY_TEXT);

                /*
                 * associate the query parameters
                 */
                query.setParameter("contentVersionId", this.versionId);

                /*
                 * only get one content body
                 */
                query.setMaxResults(1);

                /*
                 * get content body mapping
                 */
                ContentBodyMapping contentBodyMapping = (ContentBodyMapping) query
                        .uniqueResult();

                this.bodyText = null;

                /*
                 * store values from mapping
                 */
                if (contentBodyMapping != null) {
                    this.bodyText = contentBodyMapping.getBodyText();
                }
            } catch (Exception e) {
                /*
                 * log the exception
                 */
                this.appendLoadTrace(e.getMessage());
                LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, recordPk);
            }
        }
    }

    /**
     * Populates this article if needed.
     *
     *
     *
     */
    private void populateBodyTextIfNeeded() {
        if (!(this.isBodyTextPopulated)) {
            this.populateBodyText();
        }
    }

    /**
     * Populates this article if needed.
     *
     *
     *
     */
    private void populateBodyHTMLIfNeeded() {
        if (!(this.isBodyHTMLPopulated)) {
            this.populateBodyHTML();
        }
    }

    /**
     * Populates this article if needed.
     *
     *
     *
     */
    private void populateRawBodyHTMLIfNeeded() {
        if (!(this.isRawBodyHTMLPopulated)) {
            this.populateRawBodyHTML();
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Article#setFriendlyAddressAndLoad(java.lang.String)
     */
    public void setFriendlyAddressAndLoad(String friendlyAddress) {
        this.friendlyAddress = CaseUtil
                .getLowerCaseDeleteAccents(friendlyAddress);
        this.loadByFriendlyAddress();
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Article#loadByFriendlyAddress()
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
            prepStmt.setString(4, ContentTypesDictionary.ARTICLE);

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
                    .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);
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
     * @see com.corendal.netapps.wiki.interfaces.Article#getComment()
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

    /*
     * @see com.corendal.netapps.wiki.interfaces.ResourceRequest#getDirectPreviewIconPrimaryKey()
     */
    public PrimaryKey getDirectPreviewIconPrimaryKey() {
        return PrimaryKeyUtil
                .getAlphanumericSingleKey(IconsDictionary.DIRECT_ARTICLE);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Previewed#getIndirectPreviewIconPrimaryKey()
     */
    public PrimaryKey getIndirectPreviewIconPrimaryKey() {
        return PrimaryKeyUtil
                .getAlphanumericSingleKey(IconsDictionary.INDIRECT_ARTICLE);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Previewed#getSecureDirectPreviewIconPrimaryKey()
     */
    public PrimaryKey getSecureDirectPreviewIconPrimaryKey() {
        return PrimaryKeyUtil
                .getAlphanumericSingleKey(IconsDictionary.SECURE_DIRECT_ARTICLE);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Previewed#getSecureIndirectPreviewIconPrimaryKey()
     */
    public PrimaryKey getSecureIndirectPreviewIconPrimaryKey() {
        return PrimaryKeyUtil
                .getAlphanumericSingleKey(IconsDictionary.SECURE_INDIRECT_ARTICLE);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Article#getEditorAccountPrimaryKey()
     */
    public PrimaryKey getEditorAccountPrimaryKey() {
        return PrimaryKeyUtil.getAlphanumericSingleKeyOrNull(this.editorId);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Article#getAssociateEditorsGroupPrimaryKey()
     */
    public PrimaryKey getAssociateEditorsGroupPrimaryKey() {
        return PrimaryKeyUtil
                .getAlphanumericSingleKeyOrNull(this.associateEditorsId);
    }

    /*
     * @see com.corendal.netapps.framework.workflow.interfaces.ProcessDefinition#getVersionPrimaryKey()
     */
    public PrimaryKey getVersionPrimaryKey() {
        return PrimaryKeyUtil.getAlphanumericSingleKeyOrNull(this.versionId);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Parented#getMainParentPrimaryKey()
     */
    public PrimaryKey getMainParentPrimaryKey() {
        return PrimaryKeyUtil.getAlphanumericSingleKeyOrNull(this.mainParentId);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Article#getFriendlyAddress()
     */
    public String getFriendlyAddress() {
        return this.friendlyAddress;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Article#getBodyText()
     */
    public String getBodyText() {
        this.populateBodyTextIfNeeded();
        return this.bodyText;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Article#getBodyHTML()
     */
    public String getBodyHTML() {
        this.populateBodyHTMLIfNeeded();
        return this.bodyHTML;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Article#getRawBodyHTML()
     */
    public String getRawBodyHTML() {
        this.populateRawBodyHTMLIfNeeded();
        return this.rawBodyHTML;
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
     * @see com.corendal.netapps.wiki.interfaces.ResourceRequest#getAuthorAccountPrimaryKey()
     */
    public PrimaryKey getAuthorAccountPrimaryKey() {
        return PrimaryKeyUtil.getAlphanumericSingleKeyOrNull(this.authorId);
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
     * @see com.corendal.netapps.wiki.interfaces.Article#storeAuthor(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey)
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
         * get the primary key of this article entity
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.ARTICLE_VERSIONS);
        PrimaryKey authorRolePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(RolesDictionary.AUTHOR);
        PrimaryKey recordPk = this.getVersionPrimaryKey();

        /*
         * remove account role xref from the database
         */
        AccountRoleXrefFactory.remove(this, ds, entityPk, authorRolePk,
                recordPk);

        /*
         * add the author record
         */
        if (this.getIsDone()) {
            AccountRoleXrefFactory.create(this, ds, entityPk, authorRolePk,
                    recordPk, authorPk);
        }

        /*
         * refresh the article
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
     * @see com.corendal.netapps.wiki.interfaces.Article#storeEditor(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey)
     */
    public void storeEditor(PrimaryKey editorPk) {
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
         * get the primary key of this article entity
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);
        PrimaryKey editorRolePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(RolesDictionary.EDITOR);
        PrimaryKey recordPk = this.getPrimaryKey();

        /*
         * remove account role xref from the database
         */
        AccountRoleXrefFactory.remove(this, ds, entityPk, editorRolePk,
                recordPk);

        /*
         * add the editor record
         */
        if ((this.getIsDone()) && (editorPk != null)) {
            AccountRoleXrefFactory.create(this, ds, entityPk, editorRolePk,
                    recordPk, editorPk);
        }

        /*
         * refresh the article
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
     * @see com.corendal.netapps.wiki.interfaces.Article#storeAssociateEditors(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey)
     */
    public void storeAssociateEditors(PrimaryKey associateEditorsPk) {
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
         * get the primary key of this article entity
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);
        PrimaryKey editorRolePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(RolesDictionary.EDITOR);
        PrimaryKey recordPk = this.getPrimaryKey();

        /*
         * remove account role xref from the database
         */
        GroupRoleXrefFactory.remove(this, ds, entityPk, editorRolePk, recordPk);

        /*
         * add the associate editors record
         */
        if (this.getIsDone()) {
            GroupRoleXrefFactory.create(this, ds, entityPk, editorRolePk,
                    recordPk, associateEditorsPk);
        }

        /*
         * refresh the article
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
     * Returns the ordered list of all article primary keys for a keyword.
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
         * retrieve the article information from the database
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
            prepStmt.setString(2, ContentTypesDictionary.ARTICLE);
            prepStmt.setString(3, keyword);
            prepStmt.setString(4, "Y");
            prepStmt.setString(5, "Y");
            prepStmt.setString(6, keyword);
            prepStmt.setString(7, ContentTypesDictionary.ARTICLE);
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
                    .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);
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
        return FullTextUtil.getPrimaryKeysWithoutDuplicates(primaryKeys);
    }

    /**
     * Returns the ordered list of all article primary keys for a keyword. The
     * body of the articles is looked at.
     *
     * @param keyword
     *            keyword to find
     *
     *
     * @return a java.util.List object
     */
    public static final List<PrimaryKey> findEnabledByDeepKeyword(String keyword) {
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
         * retrieve the article information from the database
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
                + "and content.enabled_flag= ? " + "union "
                + "select content.id, "
                + SQLUtil.getMatchSelectCall(ds, "content_body.body_text")
                + " " + "from " + ds.getTableNameWithDefaultSchema("content")
                + ", " + ds.getTableNameWithDefaultSchema("content_version")
                + ", " + ds.getTableNameWithDefaultSchema("content_body") + " "
                + "where content.content_typ_id= ? " + "and "
                + SQLUtil.getMatchWhereCall(ds, "content_body.body_text") + " "
                + "and content_version.id= content_body.content_version_id "
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
            prepStmt.setString(2, ContentTypesDictionary.ARTICLE);
            prepStmt.setString(3, keyword);
            prepStmt.setString(4, "Y");
            prepStmt.setString(5, "Y");
            prepStmt.setString(6, keyword);
            prepStmt.setString(7, ContentTypesDictionary.ARTICLE);
            prepStmt.setString(8, keyword);
            prepStmt.setString(9, "Y");
            prepStmt.setString(10, "Y");
            prepStmt.setString(11, keyword);
            prepStmt.setString(12, ContentTypesDictionary.ARTICLE);
            prepStmt.setString(13, keyword);
            prepStmt.setString(14, "Y");
            prepStmt.setString(15, "Y");

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
                    .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);
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
                "content", EntitiesDictionary.ARTICLES, DataSessionSetGlobal
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
                "content_version", EntitiesDictionary.ARTICLES,
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
                "content_map", EntitiesDictionary.ARTICLES,
                DataSessionSetGlobal.get().getSharedHibernateDataSession(
                        DataSessionTypeConstants.WIKI_SHARED));
    }

    /**
     * Creates an article in the database.
     *
     * @param contentInfoPk
     *            primary key of a content info
     * @param editorPk
     *            primary key of an editor account
     * @param associateEditorsPk
     *            primary key of an associate editors group
     * @param authorPk
     *            primary key of an author account
     * @param rawBodyHTML
     *            a String representing a body
     * @param parentContentPk
     *            primary key of a parent content
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
    public void add(PrimaryKey contentInfoPk, PrimaryKey editorPk,
            PrimaryKey associateEditorsPk, PrimaryKey authorPk,
            PrimaryKey parentContentPk, PrimaryKey contentRequestPk,
            PrimaryKey classificationTypePk, PrimaryKey ruleTypePk,
            String comment) {
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
         * get the articles entity
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);

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
            mapping.setContentTypeId(ContentTypesDictionary.ARTICLE);
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
         * insert the editor role
         */
        if (this.getIsDone()) {
            PrimaryKey editorRolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.EDITOR);
            AccountRoleXrefFactory.create(this, ds, entityPk, editorRolePk,
                    recordPk1, editorPk);
        }

        /*
         * insert the associate editors role
         */
        if (this.getIsDone()) {
            PrimaryKey rolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.EDITOR);
            GroupRoleXrefFactory.create(this, ds, entityPk, rolePk, recordPk1,
                    associateEditorsPk);
        }

        /*
         * insert the author role
         */
        if (this.getIsDone()) {
            PrimaryKey articleVersionsEntityPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntitiesDictionary.ARTICLE_VERSIONS);
            PrimaryKey authorRolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.AUTHOR);
            AccountRoleXrefFactory.create(this, ds, articleVersionsEntityPk,
                    authorRolePk, recordPk2, authorPk);
        }

        /*
         * add an entry log and load the article information
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
     * Returns the next id for a new content body.
     *
     *
     *
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    private static PrimaryKey getNextContentBodyAttributePrimaryKey() {
        return NextPrimaryKeyUtil.getNextSequentialAlphanumericSingleKey(
                "content_body", EntitiesDictionary.CONTENT_BODIES,
                DataSessionSetGlobal.get().getSharedHibernateDataSession(
                        DataSessionTypeConstants.WIKI_SHARED));
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Article#storeBodies(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public void storeBodies(String rawBodyHTML, String bodyText, String bodyHTML) {
        /*
         * get the JDBC data session
         */
        HibernateDataSession ds = DataSessionSetGlobal.get()
                .getSharedHibernateDataSession(
                        DataSessionTypeConstants.WIKI_SHARED);

        /*
         * remove the existing bodies
         */
        this.removeBodies();

        /*
         * update the raw body
         */
        if (this.getIsDone()) {
            /*
             * get the primary key of this article
             */
            PrimaryKey entityPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);
            PrimaryKey recordPk = this.getPrimaryKey();

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
                        .get(DefaultContentVersionMapping.class, this.versionId);

                /*
                 * update the content version using that mapping
                 */
                if (mapping != null) {
                    mapping.setBody(rawBodyHTML);
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
        }

        /*
         * insert the new body records
         */
        if (this.getIsDone()) {
            /*
             * get the primary key of this article
             */
            PrimaryKey entityPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);
            PrimaryKey recordPk = this.getPrimaryKey();

            /*
             * find the next content body id
             */
            SingleKey contentBodyPk = (SingleKey) getNextContentBodyAttributePrimaryKey();

            try {
                /*
                 * get the hibernate session
                 */
                Session session = ds.getWriteSession();

                /*
                 * save the invoice copy record
                 */
                ContentBodyMapping mapping = getContentBodyMappingNewInstance();
                mapping.setId(contentBodyPk.toString());
                mapping.setContentVersionId(this.versionId);
                mapping.setBodyText(bodyText);
                mapping.setBodyHtml(bodyHTML);
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
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Article#storeAddresses(java.util.List)
     */
    public void storeAddresses(List<String> addresses) {
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
                .getAlphanumericSingleKey(EntitiesDictionary.ARTICLE_VERSIONS);
        PrimaryKey recordPk = this.getPrimaryKey();

        /*
         * insert each address
         */
        for (String currentAddress : addresses) {
            /*
             * get the record id
             */
            PrimaryKey recordPk1 = AbstractArticle
                    .getNextContentAddressPrimaryKey();

            try {
                /*
                 * get the hibernate session
                 */
                Session session = ds.getWriteSession();

                /*
                 * save the invoice copy record
                 */
                ContentAddressMapping mapping = getContentAddressMappingNewInstance();
                mapping.setId(recordPk1.toString());
                mapping.setContentVersionId(this.versionId);
                if (currentAddress.length() > 255) {
                    mapping.setAddress(currentAddress.substring(0, 255));
                } else {
                    mapping.setAddress(currentAddress);
                }
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
        }
    }

    /**
     * Returns the next id for a new content address.
     *
     *
     *
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    private static PrimaryKey getNextContentAddressPrimaryKey() {
        return NextPrimaryKeyUtil.getNextSequentialAlphanumericSingleKey(
                "content_address", EntitiesDictionary.ARTICLE_VERSIONS,
                DataSessionSetGlobal.get().getSharedHibernateDataSession(
                        DataSessionTypeConstants.WIKI_SHARED));
    }

    /**
     * Removes the body text used to search for articles by keyword.
     */
    private void removeBodies() {
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
         * get the primary key of the group
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);
        PrimaryKey recordPk = this.getPrimaryKey();

        /*
         * set the record as "done" by default
         */
        this.setIsDone(true);

        try {
            /*
             * get the query
             */
            Query query = ds.getWriteNamedQuery(REMOVE_BODIES);

            /*
             * associate the query parameters
             */
            query.setParameter("contentVersionId", this.versionId);

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
     * @see com.corendal.netapps.wiki.interfaces.Article#supersede(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey)
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
         * get the articles entity
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);

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
                mapping.setBody(this.getRawBodyHTML());
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
         * insert the author role
         */
        if ((this.getIsDone()) && (this.authorId != null)) {
            PrimaryKey authorRolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.AUTHOR);
            PrimaryKey authorPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(this.authorId);
            PrimaryKey entityPk2 = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntitiesDictionary.ARTICLE_VERSIONS);
            AccountRoleXrefFactory.create(this, ds, entityPk2, authorRolePk,
                    recordPk2, authorPk);
        }

        /*
         * refresh the article
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
     * @see com.corendal.netapps.wiki.interfaces.Article#storeParentContent(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey)
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
         * get the primary key of this article entity
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);

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
                query.setParameter("mainFlag", "N");
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
         * refresh the article
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
     * @see com.corendal.netapps.wiki.interfaces.Article#storeComment(java.lang.String)
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
         * get the primary key of this article
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);

        try {
            /*
             * get the query
             */
            Query query = ds.getWriteNamedQuery(STORE_COMMENT);

            /*
             * associate the query parameters
             */
            query.setParameter("currentFlag", "Y");
            query.setParameter("contentId", this.getPrimaryKey().toString());
            query.setParameter("comment", comment);

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
         * refresh the article
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
     * stores the info of this article.
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
         * get the primary key of this article
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);

        try {
            /*
             * get the query
             */
            Query query = ds.getWriteNamedQuery(STORE_INFO);

            /*
             * associate the query parameters
             */
            query.setParameter("currentFlag", "Y");
            query.setParameter("contentId", this.getPrimaryKey().toString());
            query.setParameter("versionInfoId", infoPk.toString());

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
         * refresh the article
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
     * @see com.corendal.netapps.wiki.interfaces.Article#storeClassificationType(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey)
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
         * get the primary key of this article
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);

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
         * refresh the article
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
     * @see com.corendal.netapps.wiki.interfaces.Article#storeWhatsNewFlag(java.lang.String)
     */
    public void storeWhatsNewFlag(String whatsNewFlag) {
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
                .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);

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
                mapping.setWhatsNewFlag(whatsNewFlag);
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
     * @see com.corendal.netapps.wiki.interfaces.Article#storeRuleType(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey)
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
         * get the primary key of this article
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);

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
         * refresh the article
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
     * @see com.corendal.netapps.wiki.interfaces.Article#storeFriendlyAddress(java.lang.String)
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
         * get the primary key of this article
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);

        try {
            /*
             * get the query
             */
            Query query = ds.getWriteNamedQuery(STORE_FRIENDLY_ADDRESS);

            /*
             * associate the query parameters
             */
            query.setParameter("currentFlag", "Y");
            query.setParameter("contentId", this.getPrimaryKey().toString());
            query.setParameter("friendlyAddress", CaseUtil
                    .getLowerCaseDeleteAccents(friendlyAddress));

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
         * refresh the article
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
     * @see com.corendal.netapps.wiki.interfaces.Article#addAllowedAccessGroup(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey)
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
         * get the primary key of this article
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);
        PrimaryKey rolePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(RolesDictionary.ACCESS_GROUP_ALLOWED);
        PrimaryKey recordPk = this.getPrimaryKey();

        GroupRoleXrefFactory.create(this, ds, entityPk, rolePk, recordPk,
                groupPk);

        /*
         * refresh the article
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
     * @see com.corendal.netapps.wiki.interfaces.Article#addDeniedAccessGroup(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey)
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
         * get the primary key of this article
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);
        PrimaryKey rolePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(RolesDictionary.ACCESS_GROUP_DENIED);
        PrimaryKey recordPk = this.getPrimaryKey();

        GroupRoleXrefFactory.create(this, ds, entityPk, rolePk, recordPk,
                groupPk);

        /*
         * refresh the article
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
     * @see com.corendal.netapps.wiki.interfaces.Article#removeAccessGroup(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey)
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
         * get the primary key of this article entity
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);

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
         * refresh the article
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
     * @see com.corendal.netapps.wiki.interfaces.Article#remove(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey)
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
         * get the primary key of this content map
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);
        PrimaryKey recordPk = this.getPrimaryKey();

        try {
            /*
             * get the query
             */
            Query query = ds.getWriteNamedQuery(REMOVE_1);

            /*
             * associate the query parameters
             */
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
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, recordPk);
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
         * remove the body text
         */
        if (this.getIsDone()) {
            this.removeBodies();
        }

        /*
         * refresh this article
         */
        if (this.getIsDone()) {
            PrimaryKey typePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(com.corendal.netapps.framework.core.constants.EntryLogTypeConstants.DELETE);
            this.addEntryLog(entityPk, recordPk, typePk,
                    DataSessionTypeConstants.WIKI_SHARED);
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
            query1.setParameter("entityId", EntitiesDictionary.ARTICLES);
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
                                        .equals(ContentTypesDictionary.ARTICLE))) {
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
                    .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, null);
        }

        /*
         * return all document primary keys
         */
        return primaryKeys;
    }

    /**
     * Returns the name-ordered list of all article primary keys for an author
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
            prepStmt.setString(5, EntitiesDictionary.ARTICLE_VERSIONS);

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
                    .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);
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
     * Returns a ranked list of all article primary keys that have been
     * accessed.
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
                    .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);
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
                prepStmt.setString(1, ContentTypesDictionary.ARTICLE);
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
                        .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);
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
         * return all article primary keys
         */
        return primaryKeys;
    }

    /**
     * Returns the ordered list of all articles primary keys with a whats new
     * flag.
     *
     * @param whatsNewFlag
     *            enabled flag to find
     *
     *
     * @return a java.util.List object
     */
    public static final List<PrimaryKey> findEnabledByWhatsNewFlag(
            String whatsNewFlag) {
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
         * retrieve the article information from the database
         */
        String selectStatement = "select content_version.content_id " + "from "
                + ds.getTableNameWithDefaultSchema("content_version") + ", "
                + ds.getTableNameWithDefaultSchema("content") + " "
                + "where content.whats_new_flag like ? "
                + "and content.content_typ_id = ? "
                + "and content_version.current_flag = ? "
                + "and content.enabled_flag = ? "
                + "and content.id = content_version.content_id "
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
            prepStmt.setString(1, whatsNewFlag);
            prepStmt.setString(2, ContentTypesDictionary.ARTICLE);
            prepStmt.setString(3, "Y");
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
                    .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);
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
     * Returns the ordered list of all article primary keys for an editor
     * primary key.
     *
     * @param editorPk
     *            primary key of the article to parse
     *
     *
     * @return a java.util.List object
     */
    public static final List<PrimaryKey> findEnabledByEditorPrimaryKey(
            PrimaryKey editorPk) {
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
                + "where account_role_xref.record_id = content.id "
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
             * associate the editor
             */
            prepStmt.setString(1, RolesDictionary.EDITOR);
            prepStmt.setString(2, editorPk.toString());
            prepStmt.setString(3, "Y");
            prepStmt.setString(4, "Y");
            prepStmt.setString(5, EntitiesDictionary.ARTICLES);

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
                    .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);
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
     * Returns the ordered list of all article primary keys for a referenced
     * content primary key.
     *
     * @param contentPk
     *            primary key of the referenced content to parse
     *
     *
     * @return a java.util.List object
     */
    public static final List<PrimaryKey> findEnabledByReferencedContentPrimaryKey(
            PrimaryKey contentPk) {
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
                + ds.getTableNameWithDefaultSchema("content_map") + ", "
                + ds.getTableNameWithDefaultSchema("content") + " "
                + "where content_map.parent_content_id = content.id "
                + "and content_map.child_content_id = ? "
                + "and content_map.main_flag = ? "
                + "and content.enabled_flag = ? " + "order by content.ord asc";
        // FIXME: hibernate select query

        try {
            /*
             * create the prepared statement
             */
            prepStmt = ds.getReadConnection().prepareStatement(selectStatement);

            /*
             * associate the editor
             */
            prepStmt.setString(1, contentPk.toString());
            prepStmt.setString(2, "N");
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
                    .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);
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
     * Returns the ordered list of all enabled articles created during a date
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
         * retrieve the article information from the database
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
            prepStmt.setString(1, EntitiesDictionary.ARTICLES);
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
                    .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);
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
     * Returns the ordered list of all enabled articles created after a date.
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
         * retrieve the article information from the database
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
            prepStmt.setString(1, EntitiesDictionary.ARTICLES);
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
                    .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);
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
     * Returns the ordered list of all enabled articles created after a date.
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
         * retrieve the article information from the database
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
            prepStmt.setString(1, EntitiesDictionary.ARTICLES);
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
                    .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);
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
     * Returns the ordered list of all enabled articles modified during a date
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
         * retrieve the article information from the database
         */
        String selectStatement = "select distinct content.id, content.ord "
                + "from " + ds.getTableNameWithDefaultSchema("content") + ", "
                + ds.getTableNameWithDefaultSchema("entry_log") + " "
                + "where entry_log.entity_id = ? "
                + "and entry_log.timestamp >= ? "
                + "and entry_log.timestamp < ? "
                + "and entry_log.timestamp between ? and ? "
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
            prepStmt.setString(1, EntitiesDictionary.ARTICLES);
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
                    .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);
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
     * Returns the ordered list of all enabled articles modified after a date.
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
         * retrieve the article information from the database
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
            prepStmt.setString(1, EntitiesDictionary.ARTICLES);
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
                    .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);
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
     * Returns the ordered list of all enabled articles modified after a date.
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
         * retrieve the article information from the database
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
            prepStmt.setString(1, EntitiesDictionary.ARTICLES);
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
                    .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);
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
     * Returns the ordered list of all article primary keys for an article
     * primary key.
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
         * retrieve the article information from the database
         */
        String selectStatement = "select content.id " + "from "
                + ds.getTableNameWithDefaultSchema("content") + ", "
                + ds.getTableNameWithDefaultSchema("content_map") + " "
                + "where content_map.parent_content_id=? "
                + "and content_map.main_flag=? "
                + "and content.content_typ_id=? "
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
            prepStmt.setString(3, ContentTypesDictionary.ARTICLE);
            prepStmt.setString(4, "Y");

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
                    .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);
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
     * Returns the ordered list of all enabled article primary keys.
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
                    .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, null);
        }

        /*
         * return all article primary keys
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
                .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);
        String tableName = "content";
        AbstractWriteDataBean.cleanup(entityPk, tableName,
                DataSessionTypeConstants.WIKI_SHARED);
    }
}

// end AbstractArticle

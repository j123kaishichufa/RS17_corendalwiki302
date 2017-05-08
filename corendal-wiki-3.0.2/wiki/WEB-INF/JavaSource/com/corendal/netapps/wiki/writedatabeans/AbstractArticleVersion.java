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
import com.corendal.netapps.wiki.interfaces.ArticleVersion;
import com.corendal.netapps.wiki.interfaces.ContentBodyMapping;
import com.corendal.netapps.wiki.interfaces.ContentVersionMapping;
import com.corendal.netapps.wiki.writedatamappings.DefaultContentVersionMapping;

/**
 * AbstractArticleVersion is the abstract class retrieving information about
 * each article version of the application from the database.
 * 
 * @version $Id: AbstractArticleVersion.java,v 1.1 2005/09/06 21:25:33 tdanard
 *          Exp $
 */
public abstract class AbstractArticleVersion extends AbstractWriteDataBean
        implements Cloneable, ArticleVersion {
    /** Fully qualified name of this class. */
    private static final String ABSTRACT_CLASS_NAME = "com.corendal.netapps.wiki.writedatabeans.AbstractArticleVersion";

    /** Author account_id from the the account_role_xref table */
    private String authorId;

    /** Content_id in the content_version table */
    private String contentId;

    /** Version_num in the content_version table */
    private int versionNum;

    /** Version_info_id in the content_version table */
    private String versionInfoId;

    /** Body_text in the content_body table */
    private String bodyText;

    /** Body_html in the content_body table */
    private String bodyHTML;

    /** Body in the content_version table */
    private String rawBodyHTML;

    /** Request_id in the content_version table */
    private String requestId;

    /** Current_flag in the content table */
    private String currentFlag;

    /** Enabled_flag in the content table */
    private String enabledFlag;

    /** Content_rule_typ_id in the content table */
    private String contentRuleTypeId;

    /** Cmnt in the content_version table */
    private String comment;

    /**
     * Name of the named query for the findEnabledByArticlePrimaryKey method.
     */
    private static final String FIND_ENABLED_BY_ARTICLE_PRIMARY_KEY = ABSTRACT_CLASS_NAME
            + ".findEnabledByArticlePrimaryKey";

    /**
     * Name of the named query for the removeBodies method.
     */
    private static final String REMOVE_BODIES = ABSTRACT_CLASS_NAME
            + ".removeBodies";

    /**
     * Name of the named query for the removeBodies method.
     */
    private static final String MAKE_OBSOLETE = ABSTRACT_CLASS_NAME
            + ".makeObsolete";

    /**
     * Name of the named query for the removeAddresses method.
     */
    private static final String REMOVE_ADDRESSES = ABSTRACT_CLASS_NAME
            + ".removeAddresses";

    /**
     * Name of the named query for the load method.
     */
    private static final String LOAD = ABSTRACT_CLASS_NAME + ".load";

    /**
     * Default class constructor.
     */
    protected AbstractArticleVersion() {
        super(DataSessionTypeConstants.WIKI_SHARED);
    }

    /**
     * Returns a clone. Overrides AbstractWriteDataBean.clone.
     */
    @Override
    public Object clone() {
        return (AbstractArticleVersion) super.clone();
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
         * get the primary key of the article version
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.ARTICLE_VERSIONS);
        PrimaryKey recordPk = this.getPrimaryKey();

        /*
         * retrieve the article version information from the database
         */
        String selectStatement = "select content_version.version_num, "
                + "content_version.version_info_id, content_version.request_id, "
                + "content.enabled_flag, content_version.current_flag, content_version.content_id, "
                + "content_version.body, content.content_rule_typ_id, content_version.cmnt "
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
             * associate the id and the article version type
             */
            prepStmt.setString(1, recordPk.toString());
            prepStmt.setString(2, ContentTypesDictionary.ARTICLE);

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
                this.rawBodyHTML = ds.getStringFromCharacterStream(rs, 7);
                this.contentRuleTypeId = rs.getString(8);
                this.comment = ds.getStringFromCharacterStream(rs, 9);
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
         * retrieve the body text information from the database
         */
        if (this.getIsFound()) {
            try {
                /*
                 * get the query
                 */
                Query query = ds.getReadNamedQuery(LOAD);

                /*
                 * associate the query parameters
                 */
                query.setParameter("contentVersionId", recordPk.toString());

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
                this.bodyText = null;

                /*
                 * store values from mapping
                 */
                if (contentBodyMapping != null) {
                    this.bodyHTML = contentBodyMapping.getBodyHtml();
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

    /*
     * @see com.corendal.netapps.wiki.interfaces.ArticleVersion#getAuthorAccountPrimaryKey()
     */
    public PrimaryKey getAuthorAccountPrimaryKey() {
        return PrimaryKeyUtil.getAlphanumericSingleKeyOrNull(this.authorId);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Previewed#getDirectPreviewIconPrimaryKey()
     */
    public PrimaryKey getDirectPreviewIconPrimaryKey() {
        return PrimaryKeyUtil
                .getAlphanumericSingleKey(IconsDictionary.DIRECT_ARTICLE);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Previewed#getSecureDirectPreviewIconPrimaryKey()
     */
    public PrimaryKey getSecureDirectPreviewIconPrimaryKey() {
        return PrimaryKeyUtil
                .getAlphanumericSingleKey(IconsDictionary.SECURE_DIRECT_ARTICLE);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Previewed#getIndirectPreviewIconPrimaryKey()
     */
    public PrimaryKey getIndirectPreviewIconPrimaryKey() {
        return PrimaryKeyUtil
                .getAlphanumericSingleKey(IconsDictionary.INDIRECT_ARTICLE);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Previewed#getSecureIndirectPreviewIconPrimaryKey()
     */
    public PrimaryKey getSecureIndirectPreviewIconPrimaryKey() {
        return PrimaryKeyUtil
                .getAlphanumericSingleKey(IconsDictionary.SECURE_INDIRECT_ARTICLE);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ArticleVersion#getVersionNum()
     */
    public int getVersionNum() {
        return this.versionNum;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ArticleVersion#getContentPrimaryKey()
     */
    public PrimaryKey getContentPrimaryKey() {
        return PrimaryKeyUtil.getAlphanumericSingleKeyOrNull(this.contentId);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ArticleVersion#getBodyText()
     */
    public String getBodyText() {
        return this.bodyText;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ArticleVersion#getBodyHTML()
     */
    public String getBodyHTML() {
        return this.bodyHTML;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ArticleVersion#getBodyHTML()
     */
    public String getRawBodyHTML() {
        return this.rawBodyHTML;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ArticleVersion#getInfoPrimaryKey()
     */
    public PrimaryKey getInfoPrimaryKey() {
        return PrimaryKeyUtil
                .getAlphanumericSingleKeyOrNull(this.versionInfoId);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ArticleVersion#getRequestPrimaryKey()
     */
    public PrimaryKey getRequestPrimaryKey() {
        return PrimaryKeyUtil.getAlphanumericSingleKeyOrNull(this.requestId);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Classified#getRuleTypePrimaryKey()
     */
    public PrimaryKey getRuleTypePrimaryKey() {
        return PrimaryKeyUtil
                .getAlphanumericSingleKeyOrNull(this.contentRuleTypeId);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ArticleVersion#getComment()
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

    /*
     * @see com.corendal.netapps.wiki.interfaces.ArticleVersion#getCurrentFlag()
     */
    public String getCurrentFlag() {
        return this.currentFlag;
    }

    /**
     * Returns the version-ordered list of all article version primary keys for
     * an address.
     * 
     * 
     * 
     * @return a java.util.List object
     */
    public static final List<PrimaryKey> findEnabledByAddress(String address) {
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
        String selectStatement = "select content_version.id "
                + "from "
                + ds.getTableNameWithDefaultSchema("content_version")
                + ", "
                + ds.getTableNameWithDefaultSchema("content_address")
                + " "
                + "where content_address.address = ?"
                + "and content_address.enabled_flag = ? "
                + "and content_version.enabled_flag = ? "
                + "and content_version.id = content_address.content_version_id "
                + "order by content_version.id asc";
        // FIXME: hibernate select query

        try {
            /*
             * create the prepared statement
             */
            prepStmt = ds.getReadConnection().prepareStatement(selectStatement);

            /*
             * associate the article
             */
            if (address.length() > 255) {
                prepStmt.setString(1, address.substring(0, 255));
            } else {
                prepStmt.setString(1, address);
            }
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
                    .getAlphanumericSingleKey(EntitiesDictionary.ARTICLE_VERSIONS);
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, null);
        } finally {
            /*
             * close the recordset and the prepared statement
             */
            CloseUtil.closeResultSet(rs);
            CloseUtil.closePreparedStatement(prepStmt);
        }

        /*
         * return all article version primary keys
         */
        return primaryKeys;
    }

    /**
     * Returns the version-ordered list of all article version primary keys for
     * an article primary key.
     * 
     * @param articlePk
     *            primary key of the article to parse
     * 
     * 
     * @return a java.util.List object
     */
    public static final List<PrimaryKey> findEnabledByArticlePrimaryKey(
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
         * find the primary key of each content
         */
        try {
            /*
             * get the query
             */
            Query query = ds
                    .getReadNamedQuery(FIND_ENABLED_BY_ARTICLE_PRIMARY_KEY);

            /*
             * associate the enabled flag
             */
            query.setParameter("contentId", articlePk.toString());
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
                    .getAlphanumericSingleKey(EntitiesDictionary.ARTICLE_VERSIONS);
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, null);
        }

        /*
         * return all article version primary keys
         */
        return primaryKeys;
    }

    /**
     * Removes the addresses.
     */
    private void removeAddresses() {
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
            Query query = ds.getWriteNamedQuery(REMOVE_ADDRESSES);

            /*
             * associate the query parameters
             */
            query.setParameter("contentVersionId", recordPk.toString());

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

    /**
     * Removes the body.
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
            query.setParameter("contentVersionId", recordPk.toString());

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
     * @see com.corendal.netapps.wiki.interfaces.ArticleVersion#makeObsolete()
     */
    public void makeObsolete() {
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
            Query query = ds.getWriteNamedQuery(MAKE_OBSOLETE);

            /*
             * associate the query parameters
             */
            query.setParameter("bodyHtml", "");
            query.setParameter("contentVersionId", recordPk.toString());

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
         * refresh this article version
         */
        if (this.getIsDone()) {
            PrimaryKey typePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntryLogTypeConstants.UPDATE);
            this.addEntryLog(entityPk, recordPk, typePk,
                    DataSessionTypeConstants.WIKI_SHARED);
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ArticleVersion#remove()
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
                .getAlphanumericSingleKey(EntitiesDictionary.ARTICLE_VERSIONS);
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
         * remove the body
         */
        if (this.getIsDone()) {
            this.removeBodies();
        }

        /*
         * remove the addresses
         */
        if (this.getIsDone()) {
            this.removeAddresses();
        }

        /*
         * refresh this article version
         */
        if (this.getIsDone()) {
            PrimaryKey typePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntryLogTypeConstants.DELETE);
            this.addEntryLog(entityPk, recordPk, typePk,
                    DataSessionTypeConstants.WIKI_SHARED);
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ArticleVersion#storeCurrentFlag(java.lang.String)
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
         * get the primary key of this article version
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.ARTICLE_VERSIONS);

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
         * refresh the article version
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
                .getAlphanumericSingleKey(EntitiesDictionary.ARTICLE_VERSIONS);
        String tableName = "content_version";
        AbstractWriteDataBean.cleanup(entityPk, tableName,
                DataSessionTypeConstants.WIKI_SHARED);
    }
}

// end AbstractArticleVersion

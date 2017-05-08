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

import org.hibernate.Session;

import com.corendal.netapps.framework.core.constants.EntryLogTypeConstants;
import com.corendal.netapps.framework.core.databeans.AbstractWriteDataBean;
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
import com.corendal.netapps.wiki.interfaces.ContentAccessLog;
import com.corendal.netapps.wiki.interfaces.ContentAccessLogMapping;
import com.corendal.netapps.wiki.writedatamappings.DefaultContentAccessLogMapping;

/**
 * AbstractContentAccessLog is the abstract class retrieving information about
 * each image content access in the database.
 * 
 * @version $Id: AbstractContentAccessLog.java,v 1.1 2005/09/06 21:25:33 tdanard
 *          Exp $
 */
public abstract class AbstractContentAccessLog extends AbstractWriteDataBean
        implements Cloneable, ContentAccessLog {
    /** Fully qualified name of this class. */
    private static final String ABSTRACT_CLASS_NAME = "com.corendal.netapps.wiki.writedatabeans.AbstractContentAccessLog";

    /** Content_id in the content_access_log table */
    private String contentId;

    /**
     * Default class constructor.
     */
    protected AbstractContentAccessLog() {
        super(DataSessionTypeConstants.WIKI_SHARED);
    }

    /**
     * Returns a new instance of a content access log mapping.
     * 
     * @return a com.corendal.netapps.qadocumentation.interfaces.ContentAccessLog
     *         object
     */
    protected ContentAccessLogMapping getContentAccessLogMappingNewInstance() {
        return new DefaultContentAccessLogMapping();
    }

    /**
     * Returns a clone. Overrides AbstractWriteDataBean.clone.
     */
    @Override
    public Object clone() {
        return (AbstractContentAccessLog) super.clone();
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
         * get the primary key of the content access log
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_ACCESS_LOGS);
        PrimaryKey recordPk = this.getPrimaryKey();

        /*
         * retrieve the content info information from the database
         */
        try {
            /*
             * get the hibernate session
             */
            Session session = ds.getReadSession();

            /*
             * load the content access log mapping
             */
            ContentAccessLogMapping mapping = (ContentAccessLogMapping) session
                    .get(DefaultContentAccessLogMapping.class, recordPk
                            .toString());
            /*
             * retrieve the content access log information from the database
             */
            if (mapping != null) {
                /*
                 * set the record as "found"
                 */
                this.setIsFound(true);

                /*
                 * remember the content access log information
                 */
                this.contentId = mapping.getContentId();
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
     * @see com.corendal.netapps.wiki.interfaces.ContentAccessLog#getContentPrimaryKey()
     */
    public PrimaryKey getContentPrimaryKey() {
        return PrimaryKeyUtil.getAlphanumericSingleKeyOrNull(this.contentId);
    }

    /**
     * Returns the next id for a new content access log.
     * 
     * 
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    private static PrimaryKey getNextPrimaryKey() {
        return NextPrimaryKeyUtil.getNextSequentialAlphanumericSingleKey(
                "content_access_log", EntitiesDictionary.CONTENT_ACCESS_LOGS,
                DataSessionSetGlobal.get().getSharedHibernateDataSession(
                        DataSessionTypeConstants.WIKI_SHARED));
    }

    /**
     * Creates a content access log entry in the database with a name and
     * description.
     * 
     * @param contentPk
     *            primary key of the content
     * 
     * 
     */
    public void add(PrimaryKey contentPk) {
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
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_ACCESS_LOGS);
        SingleKey recordPk = (SingleKey) getNextPrimaryKey();

        try {
            /*
             * get the hibernate session
             */
            Session session = ds.getWriteSession();

            /*
             * save the invoice copy record
             */
            ContentAccessLogMapping mapping = getContentAccessLogMappingNewInstance();
            mapping.setId(recordPk.toString());
            mapping.setContentId(contentPk.toString());
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
         * add an entry log and load the content access log information
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
     * Returns the ordered list of all content access log primary keys for a
     * content primary key and a proxy account primary key.
     * 
     * @param contentPk
     *            primary key of the content to query
     * @param accountPk
     *            primary key of the account to query
     * 
     * 
     * @return a java.util.List object
     */
    public static final List<PrimaryKey> findByContentAndAccountPrimaryKeys(
            PrimaryKey contentPk, PrimaryKey accountPk) {
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
        String selectStatement = "select content_access_log.id " + "from "
                + ds.getTableNameWithDefaultSchema("content_access_log") + ", "
                + ds.getTableNameWithDefaultSchema("entry_log") + " "
                + "where content_access_log.content_id=? "
                + "and entry_log.entity_id=? "
                + "and entry_log.proxy_account_id=? "
                + "and content_access_log.id=entry_log.record_id "
                + "order by entry_log.timestamp desc";
        // FIXME: hibernate select query

        try {
            /*
             * create the prepared statement
             */
            prepStmt = ds.getReadConnection().prepareStatement(selectStatement);

            /*
             * associate the keyword
             */
            prepStmt.setString(1, contentPk.toString());
            prepStmt.setString(2, EntitiesDictionary.CONTENT_ACCESS_LOGS);
            prepStmt.setString(3, accountPk.toString());

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
                    .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_ACCESS_LOGS);
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
     * Cleans up all data related to this entity.
     */
    public static void cleanup() {
        /*
         * remove all access logs after 60 days
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_ACCESS_LOGS);
        String tableName = "content_access_log";
        AbstractWriteDataBean.cleanupHistory(entityPk, tableName, 60,
                DataSessionTypeConstants.WIKI_SHARED);

        /*
         * call the generic data cleanup
         */
        AbstractWriteDataBean.cleanup(entityPk, tableName,
                DataSessionTypeConstants.WIKI_SHARED);
    }
}

// end AbstractContentAccessLog

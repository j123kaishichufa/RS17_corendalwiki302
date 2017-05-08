/*  
 * Corendal NetApps Framework - Java framework for web applications  
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
package com.corendal.netapps.wiki.writedatafactories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;

import org.hibernate.Query;

import com.corendal.netapps.framework.core.constants.EntryLogTypeConstants;
import com.corendal.netapps.framework.core.utils.DateUtil;
import com.corendal.netapps.framework.modelaccess.globals.DataSessionSetGlobal;
import com.corendal.netapps.framework.modelaccess.interfaces.HibernateDataSession;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.CloseUtil;
import com.corendal.netapps.framework.modelaccess.utils.LoggerUtil;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.framework.modelaccess.utils.TimestampUtil;
import com.corendal.netapps.wiki.constants.DataSessionTypeConstants;
import com.corendal.netapps.wiki.dictionaries.ContentTypesDictionary;
import com.corendal.netapps.wiki.dictionaries.EntitiesDictionary;
import com.corendal.netapps.wiki.interfaces.ContentAccessDay;
import com.corendal.netapps.wiki.writedatabeans.DefaultContentAccessDay;

/**
 * ContentAccessLogFactory is the static class building new content access log
 * instances.
 * 
 * @version $Id: ContentAccessDayFactory.java,v 1.1 2005/09/06 21:25:36 tdanard
 *          Exp $
 */
public final class ContentAccessDayFactory {
    /** Fully qualified name of this class. */
    private static final String STATIC_CLASS_NAME = "com.corendal.netapps.wiki.writedatafactories.ContentAccessDayFactory";

    /**
     * Name of the named query for the findEnabledByRank point 1 method.
     */
    private static final String REFRESH = STATIC_CLASS_NAME + ".refresh";

    /**
     * Private constructor to stop anyone from instantiating this class - the
     * static references should be used explicitly.
     */
    private ContentAccessDayFactory() {
        // this class is only used for storing constants
    }

    /**
     * Returns an existing content access day.
     * 
     * @param contentAccessDayPk
     *            primary key of a content access day
     * 
     * 
     * @return a ContentAccessDay object
     */
    public static final ContentAccessDay findByPrimaryKey(
            PrimaryKey contentAccessDayPk) {
        DefaultContentAccessDay contentAccessDay = new DefaultContentAccessDay();
        contentAccessDay.setPrimaryKeyAndLoad(contentAccessDayPk);
        return contentAccessDay;
    }

    /**
     * Creates a new content access day entry.
     * 
     * @param contentPk
     *            primary key of the content
     * @param accessDate
     *            a Date object representing the day the content was accessed
     * @param accessNum
     *            an int specifying the number of accesses during the day
     * @param accessRank
     *            an int specifying the rank of this content for the day
     * 
     * 
     * @return a ContentAccessDay object
     */
    public static ContentAccessDay create(PrimaryKey contentPk,
            Date accessDate, int accessNum, int accessRank) {
        DefaultContentAccessDay contentAccessDay = new DefaultContentAccessDay();
        contentAccessDay.add(contentPk, accessDate, accessNum, accessRank);

        return contentAccessDay;
    }

    /**
     * Fills the content_access_day with data for one day and for one content
     * type.
     * 
     * @param contentTypePk
     *            primary key of a content type
     * @param currentDate
     *            a Date object representing the day to be refreshed
     * 
     * 
     */
    public static void refresh(PrimaryKey contentTypePk,
            java.util.Date currentDate) {
        /*
         * initialize the prepared statement and the result set
         */
        PreparedStatement prepStmt = null;
        ResultSet rs = null;

        /*
         * get the date one day after the current date
         */
        long afterCurrentDateInMilliseconds = (DateUtil.getAddedDays(
                currentDate, 1)).getTime();
        java.util.Date afterCurrentDate = new Date(
                afterCurrentDateInMilliseconds - 1);

        /*
         * get the JDBC data session
         */
        HibernateDataSession ds = DataSessionSetGlobal.get()
                .getSharedHibernateDataSession(
                        DataSessionTypeConstants.WIKI_SHARED);

        /*
         * retrieve the content access information from the database
         */
        String selectStatement = "select content_access_log.content_id,"
                + "count(content_access_log.content_id) itemCount " + "from "
                + ds.getTableNameWithDefaultSchema("content_access_log") + ", "
                + ds.getTableNameWithDefaultSchema("content") + ", "
                + ds.getTableNameWithDefaultSchema("entry_log") + " "
                + "where content_access_log.content_id = content.id "
                + "and content_access_log.id = entry_log.record_id "
                + "and entry_log.timestamp >= ? "
                + "and entry_log.timestamp < ? " + "and entry_log.typ_id = ? "
                + "and entry_log.entity_id = ? "
                + "and content.content_typ_id = ? "
                + "and content.enabled_flag = ? "
                + "group by content_access_log.content_id "
                + "order by itemCount desc ";
        // FIXME: hibernate select query

        try {
            /*
             * create the prepared statement
             */
            prepStmt = ds.getReadConnection().prepareStatement(selectStatement);

            /*
             * convert the dates into timestamps
             */
            Timestamp currentSQLTimestamp = TimestampUtil
                    .getDateToTimestamp(currentDate);
            Timestamp afterCurrentSQLTimestamp = TimestampUtil
                    .getDateToTimestamp(afterCurrentDate);

            /*
             * associate the dates
             */
            prepStmt.setTimestamp(1, currentSQLTimestamp);
            prepStmt.setTimestamp(2, afterCurrentSQLTimestamp);
            prepStmt.setString(3, EntryLogTypeConstants.INSERT);
            prepStmt.setString(4, EntitiesDictionary.CONTENT_ACCESS_LOGS);
            prepStmt.setString(5, contentTypePk.toString());
            prepStmt.setString(6, "Y");

            /*
             * execute the query
             */
            rs = prepStmt.executeQuery();

            /*
             * loop the results
             */
            int rank = 1;
            int lastCount = 0;
            int index = 1;

            while (rs.next()) {
                PrimaryKey contentPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(rs.getString(1));

                if ((lastCount == 0) || (lastCount != rs.getInt(2))) {
                    rank = index;
                }

                lastCount = rs.getInt(2);

                Date currentSQLDate = DateUtil.getDateToSQLDate(currentDate);
                ContentAccessDayFactory.create(contentPk, currentSQLDate,
                        lastCount, rank);
                index = index + 1;
            }
        } catch (Exception e) {
            /*
             * log the exception
             */
            PrimaryKey entityPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_ACCESS_DAYS);
            LoggerUtil.logError(STATIC_CLASS_NAME, e, entityPk, null);
        } finally {
            /*
             * close the recordset and the prepared statement
             */
            CloseUtil.closeResultSet(rs);
            CloseUtil.closePreparedStatement(prepStmt);
        }
    }

    /**
     * Fills the content_access_day with data for one day.
     * 
     * @param currentDate
     *            a Date object representing the day to be refreshed
     * 
     * 
     */
    public static void refresh(java.util.Date currentDate) {
        /*
         * get the different content types
         */
        PrimaryKey articleTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.ARTICLE);
        PrimaryKey imageTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.IMAGE);

        /*
         * refresh all types
         */
        refresh(articleTypePk, currentDate);
        refresh(imageTypePk, currentDate);
    }

    /**
     * Fills the content_access_day with all missing data.
     * 
     * 
     * 
     */
    public static void refresh() {
        /*
         * get the JDBC data session
         */
        HibernateDataSession ds = DataSessionSetGlobal.get()
                .getSharedHibernateDataSession(
                        DataSessionTypeConstants.WIKI_SHARED);

        /*
         * retrieve the content access day information from the database
         */
        Date maxAccessDate = null;

        try {
            /*
             * get the query
             */
            Query query = ds.getReadNamedQuery(REFRESH);

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
                    .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_ACCESS_DAYS);
            LoggerUtil.logError(STATIC_CLASS_NAME, e, entityPk, null);
        }

        /*
         * set the date to yesterday if no log found
         */
        java.util.Date truncatedMaxAccessDate;
        java.util.Date today = DateUtil.getTruncated(new java.util.Date());

        if (maxAccessDate == null) {
            truncatedMaxAccessDate = DateUtil.getRemovedDays(today, 15);
        } else {
            truncatedMaxAccessDate = DateUtil.getTruncated(maxAccessDate);
        }

        /*
         * refresh for each missing day
         */
        long timeGapInDays = DateUtil.getTimeGapInDays(truncatedMaxAccessDate,
                today);

        for (int i = 1; i < timeGapInDays; i++) {
            java.util.Date currentDate = DateUtil.getAddedDays(
                    truncatedMaxAccessDate, i);
            refresh(currentDate);
        }
    }

}

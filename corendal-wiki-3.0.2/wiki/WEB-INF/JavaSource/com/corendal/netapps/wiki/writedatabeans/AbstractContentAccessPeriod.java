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
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

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
import com.corendal.netapps.wiki.interfaces.ContentAccessPeriod;
import com.corendal.netapps.wiki.interfaces.ContentAccessPeriodMapping;
import com.corendal.netapps.wiki.writedatamappings.DefaultContentAccessPeriodMapping;

/**
 * AbstractContentAccessPeriod is the abstract class retrieving information
 * about each content access period in the database.
 * 
 * @version $Id: AbstractContentAccessPeriod.java,v 1.1 2005/09/06 21:25:33
 *          tdanard Exp $
 */
public abstract class AbstractContentAccessPeriod extends AbstractWriteDataBean
        implements Cloneable, ContentAccessPeriod {
    /** Fully qualified name of this class. */
    private static final String ABSTRACT_CLASS_NAME = "com.corendal.netapps.wiki.writedatabeans.AbstractContentAccessPeriod";

    /** Content_id in the content_access_period table */
    private String contentId;

    /** Access_date in the content_access_period table */
    private Date accessDate;

    /** Access_num in the content_access_period table */
    private int accessNum;

    /** Access_rank in the content_access_period table */
    private int accessRank;

    /**
     * Name of the named query for the findByContentPrimaryKey method.
     */
    private static final String FIND_BY_CONTENT_PRIMARY_KEY = ABSTRACT_CLASS_NAME
            + ".findByContentPrimaryKey";

    /**
     * Default class constructor.
     */
    protected AbstractContentAccessPeriod() {
        super(DataSessionTypeConstants.WIKI_SHARED);
    }

    /**
     * Returns a new instance of a content access period mapping.
     * 
     * @return a com.corendal.netapps.wiki.interfaces.ContentAccessPeriod object
     */
    protected ContentAccessPeriodMapping getContentAccessPeriodMappingNewInstance() {
        return new DefaultContentAccessPeriodMapping();
    }

    /**
     * Returns a clone. Overrides AbstractReadDataBean.clone.
     */
    @Override
    public Object clone() {
        return (AbstractContentAccessPeriod) super.clone();
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
         * get the primary key of the content access period
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_ACCESS_PERIODS);
        PrimaryKey recordPk = this.getPrimaryKey();

        try {
            /*
             * get the hibernate session
             */
            Session session = ds.getReadSession();

            /*
             * load the content access period mapping
             */
            ContentAccessPeriodMapping mapping = (ContentAccessPeriodMapping) session
                    .get(DefaultContentAccessPeriodMapping.class, recordPk
                            .toString());
            /*
             * retrieve the content access period information from the database
             */
            if (mapping != null) {
                /*
                 * set the record as "found"
                 */
                this.setIsFound(true);

                /*
                 * remember the content access period information
                 */
                this.contentId = mapping.getContentId();
                this.accessDate = mapping.getAccessDate();
                this.accessNum = mapping.getAccessNumber();
                this.accessRank = mapping.getAccessRank();
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
     * @see com.corendal.netapps.wiki.interfaces.ContentAccessPeriod#getContentPrimaryKey()
     */
    public PrimaryKey getContentPrimaryKey() {
        return PrimaryKeyUtil.getAlphanumericSingleKeyOrNull(this.contentId);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentAccessPeriod#getAccessDate()
     */
    public Date getAccessDate() {
        if (this.accessDate != null) {
            return (Date) this.accessDate.clone();
        } else {
            return null;
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentAccessPeriod#getAccessNum()
     */
    public int getAccessNum() {
        return this.accessNum;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentAccessPeriod#getAccessRank()
     */
    public int getAccessRank() {
        return this.accessRank;
    }

    /**
     * Returns the date-ordered list of all content access period primary keys
     * for a content primary key.
     * 
     * @param contentPk
     *            primary key of the content to parse
     * 
     * 
     * @return a java.util.List object
     */
    public static final List<PrimaryKey> findByContentPrimaryKey(
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
         * find the primary key of each content access period
         */
        try {
            /*
             * get the query
             */
            Query query = ds.getReadNamedQuery(FIND_BY_CONTENT_PRIMARY_KEY);

            /*
             * associate the enabled flag
             */
            query.setParameter("contentId", contentPk.toString());

            /*
             * loop through the results
             */
            primaryKeys = ds.getPrimaryKeys(query);
        } catch (Exception e) {
            /*
             * log the exception
             */
            PrimaryKey entityPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_ACCESS_PERIODS);
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, null);
        }

        /*
         * return all content access period primary keys
         */
        return primaryKeys;
    }

    /**
     * Returns the next id for a new content access period.
     * 
     * 
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    private static PrimaryKey getNextPrimaryKey() {
        return NextPrimaryKeyUtil.getNextSequentialAlphanumericSingleKey(
                "content_access_period",
                EntitiesDictionary.CONTENT_ACCESS_PERIODS, DataSessionSetGlobal
                        .get().getSharedHibernateDataSession(
                                DataSessionTypeConstants.WIKI_SHARED));
    }

    /**
     * Creates a content access period entry in the database.
     * 
     * @param contentPk
     *            primary key of the content
     * @param accessDate
     *            a Date object representing the last day of a period
     * @param accessNum
     *            an int specifying the number of accesses during that period
     * @param accessRank
     *            an int specifying the rank of the that content during that
     *            period
     * 
     * 
     */
    public void add(PrimaryKey contentPk, Date accessDate, int accessNum,
            int accessRank) {
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
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_ACCESS_PERIODS);
        SingleKey recordPk = (SingleKey) getNextPrimaryKey();

        try {
            /*
             * get the hibernate session
             */
            Session session = ds.getWriteSession();

            /*
             * save the content access period record
             */
            ContentAccessPeriodMapping mapping = getContentAccessPeriodMappingNewInstance();
            mapping.setId(recordPk.toString());
            if (contentPk != null) {
                mapping.setContentId(contentPk.toString());
            } else {
                mapping.setContentId(null);
            }
            mapping.setAccessDate(accessDate);
            mapping.setAccessNumber(accessNum);
            mapping.setAccessRank(accessRank);
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
         * load the content access period information
         */
        if (this.getIsDone()) {
            this.setPrimaryKeyClearCacheAndLoad(recordPk);
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
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_ACCESS_PERIODS);
        String tableName = "content_access_period";
        AbstractWriteDataBean.cleanup(entityPk, tableName,
                DataSessionTypeConstants.WIKI_SHARED);
    }
}

// end AbstractContentAccessPeriod

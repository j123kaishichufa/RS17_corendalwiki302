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
package com.corendal.netapps.wiki.readdatabeans;

import java.util.ArrayList;
import java.util.List;

import com.corendal.netapps.framework.core.databeans.AbstractReadDataBean;
import com.corendal.netapps.framework.core.utils.FullTextUtil;
import com.corendal.netapps.framework.modelaccess.globals.DataSessionSetGlobal;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.interfaces.XMLSourceDataSession;
import com.corendal.netapps.framework.modelaccess.interfaces.XMLSourceQuery;
import com.corendal.netapps.framework.modelaccess.utils.LoggerUtil;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.constants.DataSessionTypeConstants;
import com.corendal.netapps.wiki.dictionaries.EntitiesDictionary;
import com.corendal.netapps.wiki.interfaces.ContentSubscriptionMode;
import com.corendal.netapps.wiki.interfaces.ContentSubscriptionModeLoadHandler;
import com.corendal.netapps.wiki.readdatahandlers.DefaultContentSubscriptionModeLoadHandler;

/**
 * AbstractContentSubscriptionMode is the abstract class retrieving information
 * about each content subscription mode of the application from the database.
 * 
 * @version $Id: AbstractContentSubscriptionMode.java,v 1.1 2005/09/06 21:25:34
 *          tdanard Exp $
 */
public abstract class AbstractContentSubscriptionMode extends
        AbstractReadDataBean implements Cloneable, ContentSubscriptionMode {
    /** Fully qualified name of this class. */
    private static final String ABSTRACT_CLASS_NAME = "com.corendal.netapps.wiki.readdatabeans.AbstractContentSubscriptionMode";

    /** Name of the named query for the findByEnabledFlag method. */
    private static final String FIND_BY_ENABLED_FLAG_QUERY_NAME = ABSTRACT_CLASS_NAME
            + ".findByEnabledFlag";

    /** Name of the named query for the findEnabledByKeyword method. */
    private static final String FIND_ENABLED_BY_KEYWORD_QUERY_NAME = ABSTRACT_CLASS_NAME
            + ".findEnabledByKeyword";

    /** Nam in the content subscription mode table */
    private String name;

    /** Dsc in the content subscription mode table */
    private String description;

    /** Cmnt in the content subscription mode table */
    private String comment;

    /** Order in the content subscription mode table */
    private int order;

    /** Enabled_flag in the content subscription mode table */
    private String enabledFlag;

    /**
     * Default class constructor.
     */
    protected AbstractContentSubscriptionMode() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractReadDataBean.clone.
     */
    @Override
    public Object clone() {
        return (AbstractContentSubscriptionMode) super.clone();
    }

    /**
     * Returns a new instance of a content subscription mode load handler
     * 
     * @return a
     *         com.corendal.netapps.wiki.interfaces.ContentSubscriptionModeLoadHandler
     *         object
     */
    protected ContentSubscriptionModeLoadHandler getContentSubscriptionModeLoadHandlerNewInstance() {
        return new DefaultContentSubscriptionModeLoadHandler();
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
         * get the XML data session
         */
        XMLSourceDataSession ds = DataSessionSetGlobal.get()
                .getSetupXMLSourceDataSession(
                        DataSessionTypeConstants.WIKI_SETUP);

        /*
         * get the primary key of the content subscription mode
         */
        PrimaryKey recordPk = this.getPrimaryKey();

        try {
            /*
             * load the content subscription mode handler
             */
            ContentSubscriptionModeLoadHandler handler = this
                    .getContentSubscriptionModeLoadHandlerNewInstance();
            handler = (ContentSubscriptionModeLoadHandler) ds
                    .getLoadHandlerUsingReadLoadHandlerCache(
                            handler.getClass(), recordPk.toString());

            /*
             * retrieve the content subscription mode information from the
             * database
             */
            if (handler != null) {
                this.setIsFound(true);
                this.name = handler.getName();
                this.description = handler.getDescription();
                this.comment = handler.getComment();
                this.order = handler.getOrder();
                this.enabledFlag = handler.getEnabledFlag();
            }
        } catch (Exception e) {
            /*
             * log the exception
             */
            this.appendLoadTrace(e.getMessage());

            PrimaryKey entityPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_SUBSCRIPTION_MODES);
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, recordPk);
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentSubscriptionMode#getName()
     */
    public String getName() {
        return this.name;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentSubscriptionMode#getDescription()
     */
    public String getDescription() {
        return this.description;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentSubscriptionMode#getComment()
     */
    public String getComment() {
        return this.comment;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentSubscriptionMode#getOrder()
     */
    public int getOrder() {
        return this.order;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Enabled#getEnabledFlag()
     */
    public String getEnabledFlag() {
        return this.enabledFlag;
    }

    /**
     * Returns the ordered list of all content subscription mode primary keys.
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
         * get the XML data session
         */
        XMLSourceDataSession ds = DataSessionSetGlobal.get()
                .getSetupXMLSourceDataSession(
                        DataSessionTypeConstants.WIKI_SETUP);

        try {
            /*
             * get the query
             */
            XMLSourceQuery query = ds
                    .getNamedQuery(FIND_BY_ENABLED_FLAG_QUERY_NAME);

            /*
             * associate the enabled flag
             */
            query.setParameter("enabledFlag", enabledFlag);

            /*
             * get the results
             */
            primaryKeys = query.getPrimaryKeysUsingReadQueryCache();
        } catch (Exception e) {
            /*
             * log the exception
             */
            PrimaryKey entityPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_SUBSCRIPTION_MODES);
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, null);
        }

        /*
         * return all content subscription mode primary keys
         */
        return primaryKeys;
    }

    /**
     * Returns the ordered list of all content subscription mode primary keys
     * for a keyword.
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
         * get the XML data session
         */
        XMLSourceDataSession ds = DataSessionSetGlobal.get()
                .getSetupXMLSourceDataSession(
                        DataSessionTypeConstants.WIKI_SETUP);

        try {
            /*
             * get the query
             */
            XMLSourceQuery query = ds
                    .getNamedQuery(FIND_ENABLED_BY_KEYWORD_QUERY_NAME);

            /*
             * associate the keyword
             */
            query.setParameter("keyword", keyword);

            /*
             * get the results
             */
            primaryKeys = query.getPrimaryKeysUsingReadQueryCache();
        } catch (Exception e) {
            /*
             * log the exception
             */
            PrimaryKey entityPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_SUBSCRIPTION_MODES);
            LoggerUtil.logError(ABSTRACT_CLASS_NAME, e, entityPk, null);
        }

        /*
         * return all content subscription mode primary keys
         */
        return FullTextUtil.getPrimaryKeysWithoutDuplicates(primaryKeys);
    }
}

// end AbstractContentSubscriptionMode

/*  
 * Corendal NetApps QA Documentation - Web application for document management  
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
package com.corendal.netapps.wiki.batches;

import java.util.List;

import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.StandardReadBean;
import com.corendal.netapps.framework.core.utils.HTMLFormatUtil;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.LoggerUtil;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.framework.runtime.batches.AbstractDatabaseExecutableBatch;
import com.corendal.netapps.wiki.commons.DefaultArticleBodyHighlighter;
import com.corendal.netapps.wiki.dictionaries.ArticlesDictionary;
import com.corendal.netapps.wiki.dictionaries.EntitiesDictionary;
import com.corendal.netapps.wiki.interfaces.ArticleBodyHighlighter;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.interfaces.StandardArticleFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleFactory;

/**
 * ArticleBodiesBatch is the class updating article bodies.
 * 
 * @version $Id: ArticleBodiesBatch.java,v 1.9 2007/09/03 19:41:59 tdanard Exp $
 */
public class ArticleBodiesBatch extends AbstractDatabaseExecutableBatch
        implements Cloneable {
    /** Version uid for serialization. */
    private static final long serialVersionUID = 1L;

    /** Fully qualified name of this class. */
    public static final String DEFAULT_CLASS_NAME = "com.corendal.netapps.wiki.batches.ArticleBodiesBatch";

    /**
     * Default class constructor.
     */
    public ArticleBodiesBatch() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractDatabaseExecutableBatch.clone.
     */
    @Override
    public Object clone() {
        return (ArticleBodiesBatch) super.clone();
    }

    /**
     * Execution method. Implements abstract method
     * AbstractDatabaseExecutableBatch.answerExecution.
     */
    @Override
    public void answerExecution() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardArticleFactory srif = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);

        /*
         * get the primary key of the home article
         */
        PrimaryKey homeArticlePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ArticlesDictionary.HOME);

        /*
         * get the list of all articles
         */
        List<StandardReadBean> articles = srif.findByEnabledFlag("Y");

        /*
         * add each text and description
         */
        for (int i = 0; i < articles.size(); i++) {

            StandardArticle sri = (StandardArticle) articles.get(i);
            try {
                /*
                 * extract the text from the raw body
                 */
                String rawBodyHTML = sri.getRawBodyHTML();
                String bodyText = HTMLFormatUtil.getHTMLToText(rawBodyHTML);

                /*
                 * highlight the raw body
                 */
                ArticleBodyHighlighter highlighter = new DefaultArticleBodyHighlighter();
                highlighter.process(sri.getPrimaryKey(), rawBodyHTML,
                        homeArticlePk, false);

                /*
                 * update the bodies
                 */
                sri.storeBodies(rawBodyHTML, bodyText, highlighter
                        .getModifiedBodyHTML());

                /*
                 * update the addresses
                 */
                sri.storeAddresses(highlighter.getModifiedLocations());
            } catch (Exception e) {
                LoggerUtil.logError(DEFAULT_CLASS_NAME, e, PrimaryKeyUtil
                        .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES),
                        sri.getPrimaryKey());
            }

        }
    }
}

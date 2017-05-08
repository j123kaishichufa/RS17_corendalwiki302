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

import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.wiki.interfaces.Article;
import com.corendal.netapps.wiki.writedatabeans.DefaultArticle;

/**
 * ArticleFactory is the static class building new Article instances.
 * 
 * @version $Id: ArticleFactory.java,v 1.7 2007/09/02 21:56:31 tdanard Exp $
 */
public final class ArticleFactory {
    /**
     * Private constructor to stop anyone from instantiating this class - the
     * static references should be used explicitly.
     */
    private ArticleFactory() {
        // this class is only used for storing constants
    }

    /**
     * Returns an existing article.
     * 
     * @param articlePk
     *            primary key of an article
     * 
     * 
     * @return a Article object
     */
    public static final Article findByPrimaryKey(PrimaryKey articlePk) {
        DefaultArticle article = new DefaultArticle();
        article.setPrimaryKeyAndLoad(articlePk);
        return article;
    }

    /**
     * Creates a new article.
     * 
     * @param contentInfoPk
     *            primary key of a content info
     * @param authorPk
     *            primary key of an author account
     * @param editorPk
     *            primary key of an editor account
     * @param associateEditorsPk
     *            primary key of an associate editors group
     * @param body
     *            a String representing a body
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
     * @return an Article object
     */
    public static final Article create(PrimaryKey contentInfoPk,
            PrimaryKey editorPk, PrimaryKey associateEditorsPk,
            PrimaryKey authorPk, PrimaryKey parentContentPk,
            PrimaryKey contentRequestPk, PrimaryKey classificationTypePk,
            PrimaryKey ruleTypePk, String comment) {
        DefaultArticle article = new DefaultArticle();
        article.add(contentInfoPk, editorPk, associateEditorsPk, authorPk,
                parentContentPk, contentRequestPk, classificationTypePk,
                ruleTypePk, comment);

        return article;
    }

}

// end ArticleFactory

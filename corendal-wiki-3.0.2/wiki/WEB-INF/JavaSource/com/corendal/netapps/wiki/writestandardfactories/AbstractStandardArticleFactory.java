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
package com.corendal.netapps.wiki.writestandardfactories;

import java.util.ArrayList;
import java.util.List;

import com.corendal.netapps.framework.configuration.utils.StringUtil;
import com.corendal.netapps.framework.core.interfaces.StandardBean;
import com.corendal.netapps.framework.core.interfaces.StandardReadBean;
import com.corendal.netapps.framework.core.standardfactories.AbstractStandardWriteBeanFactory;
import com.corendal.netapps.framework.core.throwables.CannotDoBeanException;
import com.corendal.netapps.framework.core.utils.FullTextUtil;
import com.corendal.netapps.framework.core.utils.ObjectUtil;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.wiki.interfaces.Article;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.interfaces.StandardArticleFactory;
import com.corendal.netapps.wiki.utils.ArticleUtil;
import com.corendal.netapps.wiki.writedatabeans.AbstractArticle;
import com.corendal.netapps.wiki.writedatafactories.ArticleFactory;
import com.corendal.netapps.wiki.writestandardbeans.DefaultStandardArticle;

/**
 * AbstractStandardArticleFactory is the abstract class building new
 * StandardArticle instances.
 * 
 * @version $Id: AbstractStandardArticleFactory.java,v 1.1 2005/09/06 21:25:35
 *          tdanard Exp $
 */
public abstract class AbstractStandardArticleFactory extends
        AbstractStandardWriteBeanFactory implements Cloneable,
        StandardArticleFactory {

    /**
     * Class constructor.
     * 
     * 
     * 
     */
    protected AbstractStandardArticleFactory() {
        super(DefaultStandardArticle.class, true);
    }

    /**
     * Returns a clone. Overrides AbstractStandardWriteBeanFactory.clone.
     */
    @Override
    public Object clone() {
        return (AbstractStandardArticleFactory) super.clone();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardReadBeanFactory#getStandardReadBeanNewInstance(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public StandardReadBean getStandardReadBeanNewInstance() {
        if (this.getClassName().equals(DefaultStandardArticle.class.getName())) {
            return new DefaultStandardArticle();
        } else {
            return super.getStandardReadBeanNewInstance();
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardArticleFactory#findEnabledPrimaryKeysByWhatsNewFlag(java.lang.String,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<PrimaryKey> findEnabledPrimaryKeysByWhatsNewFlag(
            String whatsNewFlag) {
        return AbstractArticle.findEnabledByWhatsNewFlag(whatsNewFlag);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardArticleFactory#findEnabledByWhatsNewFlag(java.lang.String,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<StandardArticle> findEnabledByWhatsNewFlag(String whatsNewFlag) {
        List<StandardArticle> standardArticles = new ArrayList<StandardArticle>();
        List<PrimaryKey> primaryKeys = AbstractArticle
                .findEnabledByWhatsNewFlag(whatsNewFlag);

        for (PrimaryKey sdPk : primaryKeys) {
            StandardArticle sd = (StandardArticle) this.findByPrimaryKey(sdPk);
            standardArticles.add(sd);
        }

        return standardArticles;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardArticleFactory#findByFriendlyAddress(java.lang.String,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public StandardArticle findByFriendlyAddress(String friendlyAddress) {
        StandardArticle sd = (StandardArticle) this
                .getStandardReadBeanNewInstance();
        sd.setFriendlyAddressAndLoad(friendlyAddress);

        return sd;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardArticleFactory#createArticle(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      java.lang.String,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      java.lang.String,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public StandardBean createArticle(PrimaryKey contentInfoPk,
            PrimaryKey editorPk, PrimaryKey associateEditorsPk,
            PrimaryKey authorPk, PrimaryKey parentContentPk,
            PrimaryKey contentRequestPk, PrimaryKey classificationTypePk,
            PrimaryKey ruleTypePk, String comment) {
        /*
         * initialize the result and get the data session set
         */
        StandardArticle result = null;
        /*
         * find the editor of the parent article
         */
        StandardArticle parentArticle = (StandardArticle) this
                .findByPrimaryKey(parentContentPk);

        /*
         * do not store the editor if this person is the editor of the parent
         * article
         */
        PrimaryKey actualEditorPk = editorPk;

        if (parentArticle.getEditorStandardAccount().getPrimaryKey().equals(
                editorPk)) {
            actualEditorPk = null;
        }

        /*
         * create the article
         */
        Article article = ArticleFactory.create(contentInfoPk, actualEditorPk,
                associateEditorsPk, authorPk, parentContentPk,
                contentRequestPk, classificationTypePk, ruleTypePk, comment);

        if ((article != null) && (article.getIsDone())) {
            result = (StandardArticle) this.findByPrimaryKey(article
                    .getPrimaryKey());
            result.setIsDone(article.getIsDone());
            result.setStoreTrace(article.getStoreTrace());
        } else if (article != null) {
            throw new CannotDoBeanException(article.getStoreTrace());
        } else {
            throw new CannotDoBeanException();
        }

        /*
         * return
         */
        return result;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardArticleFactory#findPartialEnabledByAuthorPrimaryKey(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      int, int,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<StandardArticle> findPartialEnabledByAuthorPrimaryKey(
            PrimaryKey pk, int indexStart, int indexEnd) {
        List<StandardArticle> standardArticles = new ArrayList<StandardArticle>();
        List<PrimaryKey> primaryKeys = AbstractArticle
                .findEnabledByAuthorPrimaryKey(pk);

        for (int i = 0; i < indexStart; i++) {
            standardArticles.add(null);
        }

        for (int i = indexStart; i < (indexEnd + 1); i++) {
            PrimaryKey sdPk = primaryKeys.get(i);
            StandardArticle sd = (StandardArticle) this.findByPrimaryKey(sdPk);
            standardArticles.add(sd);
        }

        for (int i = indexEnd + 1; i < primaryKeys.size(); i++) {
            standardArticles.add(null);
        }

        return standardArticles;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardArticleFactory#findCountOfEnabledByAuthorPrimaryKey(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public int findCountOfEnabledByAuthorPrimaryKey(PrimaryKey pk) {
        List<PrimaryKey> primaryKeys = AbstractArticle
                .findEnabledByAuthorPrimaryKey(pk);

        /*
         * return
         */
        return primaryKeys.size();
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardArticleFactory#findEnabledPrimaryKeysByAuthorPrimaryKey(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<PrimaryKey> findEnabledPrimaryKeysByAuthorPrimaryKey(
            PrimaryKey pk) {
        return AbstractArticle.findEnabledByAuthorPrimaryKey(pk);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardArticleFactory#findEnabledByAuthorPrimaryKey(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<StandardArticle> findEnabledByAuthorPrimaryKey(PrimaryKey pk) {
        List<StandardArticle> standardArticles = new ArrayList<StandardArticle>();
        List<PrimaryKey> primaryKeys = AbstractArticle
                .findEnabledByAuthorPrimaryKey(pk);

        for (PrimaryKey sdPk : primaryKeys) {
            StandardArticle sd = (StandardArticle) this.findByPrimaryKey(sdPk);
            standardArticles.add(sd);
        }

        return standardArticles;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardArticleFactory#findEnabledByRank(int,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<StandardArticle> findEnabledByRank(int cnt) {
        List<StandardArticle> standardArticles = new ArrayList<StandardArticle>();
        List<PrimaryKey> primaryKeys = AbstractArticle.findEnabledByRank(cnt);

        /*
         * add data beans to list
         */
        for (PrimaryKey sdPk : primaryKeys) {
            StandardArticle sd = (StandardArticle) this.findByPrimaryKey(sdPk);
            standardArticles.add(sd);
        }

        return standardArticles;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardArticleFactory#findEnabledPrimaryKeysByCreationOrder(int,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<PrimaryKey> findEnabledPrimaryKeysByCreationOrder(int cnt) {
        return AbstractArticle.findEnabledByCreationOrder(cnt);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardArticleFactory#findEnabledByCreationOrder(int,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<StandardArticle> findEnabledByCreationOrder(int cnt) {
        List<StandardArticle> standardArticles = new ArrayList<StandardArticle>();
        List<PrimaryKey> primaryKeys = AbstractArticle
                .findEnabledByCreationOrder(cnt);

        /*
         * add data beans to list
         */
        for (PrimaryKey sdPk : primaryKeys) {
            StandardArticle sd = (StandardArticle) this.findByPrimaryKey(sdPk);
            standardArticles.add(sd);
        }

        return standardArticles;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardArticleFactory#findEnabledByReferencedContentPrimaryKey(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<StandardArticle> findEnabledByReferencedContentPrimaryKey(
            PrimaryKey pk) {
        List<StandardArticle> standardArticles = new ArrayList<StandardArticle>();
        List<PrimaryKey> primaryKeys = AbstractArticle
                .findEnabledByReferencedContentPrimaryKey(pk);

        for (PrimaryKey srPk : primaryKeys) {
            StandardArticle sd = (StandardArticle) this.findByPrimaryKey(srPk);
            standardArticles.add(sd);
        }

        return standardArticles;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardArticleFactory#findEnabledByEditorPrimaryKey(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<StandardArticle> findEnabledByEditorPrimaryKey(PrimaryKey pk) {
        List<StandardArticle> standardDirectories = new ArrayList<StandardArticle>();
        List<PrimaryKey> primaryKeys = AbstractArticle
                .findEnabledByEditorPrimaryKey(pk);

        for (PrimaryKey srPk : primaryKeys) {
            StandardArticle sd = (StandardArticle) this.findByPrimaryKey(srPk);
            standardDirectories.add(sd);
        }

        return standardDirectories;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardArticleFactory#findEnabledPrimaryKeysByEditorPrimaryKey(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<PrimaryKey> findEnabledPrimaryKeysByEditorPrimaryKey(
            PrimaryKey pk) {
        return AbstractArticle.findEnabledByEditorPrimaryKey(pk);
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardReadBeanFactory#findEnabledPrimaryKeysByDeepKeyword(java.lang.String,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public List<PrimaryKey> findEnabledPrimaryKeysByDeepKeyword(String keyword,
            boolean areAllKeywordsRequired) {
        /*
         * initialize the list of primary keys
         */
        List<PrimaryKey> primaryKeys = new ArrayList<PrimaryKey>();

        List<String> tokens = FullTextUtil
                .getUpperCaseDeleteAccentsTokens(keyword);
        for (String currentToken : tokens) {
            List<PrimaryKey> currentPrimaryKeys = (List<PrimaryKey>) ObjectUtil
                    .getObjectUsingFindByDeepKeyword(this.getClassName(),
                            FullTextUtil.getDoubleQuotedToken(currentToken));
            primaryKeys = FullTextUtil.getMergedListOfScoredPrimaryKeys(
                    primaryKeys, currentPrimaryKeys, areAllKeywordsRequired);
        }

        /*
         * return
         */
        return primaryKeys;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardReadBeanFactory#findEnabledByDeepKeyword(java.lang.String,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public List<StandardReadBean> findEnabledByDeepKeyword(String keyword,
            boolean areAllKeywordsRequired) {
        /*
         * initialize the list of primary keys
         */
        List<PrimaryKey> primaryKeys = new ArrayList<PrimaryKey>();

        List<String> tokens = FullTextUtil
                .getUpperCaseDeleteAccentsTokens(keyword);
        for (String currentToken : tokens) {
            List<PrimaryKey> currentPrimaryKeys = (List<PrimaryKey>) ObjectUtil
                    .getObjectUsingFindByDeepKeyword(this.getClassName(),
                            FullTextUtil.getDoubleQuotedToken(currentToken));
            primaryKeys = FullTextUtil.getMergedListOfScoredPrimaryKeys(
                    primaryKeys, currentPrimaryKeys, areAllKeywordsRequired);
        }

        /*
         * assemble the results
         */
        List<StandardReadBean> result = new ArrayList<StandardReadBean>();

        for (PrimaryKey pk : primaryKeys) {
            StandardBean si = this.findByPrimaryKey(pk);
            result.add((StandardReadBean) si);
        }

        /*
         * return
         */
        return result;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardArticleFactory#findByAbsoluteOrRelativeLocation(java.lang.String,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public StandardArticle findByAbsoluteOrRelativeLocation(String location) {
        /*
         * extract the friendly address
         */
        String friendlyAddress = ArticleUtil
                .getFriendlyAddressFromLocation(location);

        /*
         * find a match
         */
        if (!(StringUtil.getIsEmpty(friendlyAddress))) {
            StandardArticle article = this
                    .findByFriendlyAddress(friendlyAddress);

            return article;
        } else {
            return null;
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardArticleFactory#findDirectEnabledByParentArticlePrimaryKey(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<StandardArticle> findDirectEnabledByParentArticlePrimaryKey(
            PrimaryKey pk) {
        List<StandardArticle> standardArticles = new ArrayList<StandardArticle>();
        List<PrimaryKey> primaryKeys = AbstractArticle
                .findDirectEnabledByParentArticlePrimaryKey(pk);

        for (PrimaryKey srPk : primaryKeys) {
            StandardArticle sr = (StandardArticle) this.findByPrimaryKey(srPk);
            standardArticles.add(sr);
        }

        return standardArticles;
    }

    public List<PrimaryKey> findDirectEnabledPrimaryKeysByParentArticlePrimaryKey(
            PrimaryKey pk) {
        return AbstractArticle.findDirectEnabledByParentArticlePrimaryKey(pk);
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardReadBeanFactory#findPrimaryKeysByEnabledFlag(java.lang.String,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public List<PrimaryKey> findPrimaryKeysByEnabledFlag(String enabledFlag) {
        return AbstractArticle.findByEnabledFlag(enabledFlag);
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardPatchSetFactory#findByEnabledFlag(java.lang.String,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public List<StandardReadBean> findByEnabledFlag(String enabledFlag) {
        List<PrimaryKey> primaryKeys = AbstractArticle
                .findByEnabledFlag(enabledFlag);
        List<StandardReadBean> result = new ArrayList<StandardReadBean>();

        for (PrimaryKey pk : primaryKeys) {
            StandardArticle si = (StandardArticle) this.findByPrimaryKey(pk);
            result.add(si);
        }

        return result;
    }
}

// end AbstractStandardArticleFactory

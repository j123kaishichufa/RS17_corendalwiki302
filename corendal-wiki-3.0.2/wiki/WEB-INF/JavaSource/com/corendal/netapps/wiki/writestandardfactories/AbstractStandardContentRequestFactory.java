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

import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.StandardBean;
import com.corendal.netapps.framework.core.interfaces.StandardReadBean;
import com.corendal.netapps.framework.core.standardfactories.AbstractStandardWriteBeanFactory;
import com.corendal.netapps.framework.core.throwables.CannotDoBeanException;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.wiki.interfaces.ContentRequest;
import com.corendal.netapps.wiki.interfaces.StandardContentInfo;
import com.corendal.netapps.wiki.interfaces.StandardContentInfoFactory;
import com.corendal.netapps.wiki.interfaces.StandardContentRequest;
import com.corendal.netapps.wiki.interfaces.StandardContentRequestFactory;
import com.corendal.netapps.wiki.writedatabeans.AbstractContentRequest;
import com.corendal.netapps.wiki.writedatafactories.ContentRequestFactory;
import com.corendal.netapps.wiki.writestandardbeans.DefaultStandardContentRequest;

/**
 * AbstractStandardContentRequestFactory is the abstract class building new
 * StandardContentRequest instances.
 * 
 * @version $Id: AbstractStandardContentRequestFactory.java,v 1.1 2005/09/06
 *          21:25:35 tdanard Exp $
 */
public abstract class AbstractStandardContentRequestFactory extends
        AbstractStandardWriteBeanFactory implements Cloneable,
        StandardContentRequestFactory {

    /**
     * Class constructor.
     * 
     * 
     * 
     */
    protected AbstractStandardContentRequestFactory() {
        super(DefaultStandardContentRequest.class, true);
    }

    /**
     * Returns a clone. Overrides AbstractStandardWriteBeanFactory.clone.
     */
    @Override
    public Object clone() {
        return (AbstractStandardContentRequestFactory) super.clone();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardReadBeanFactory#getStandardReadBeanNewInstance(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public StandardReadBean getStandardReadBeanNewInstance() {
        if (this.getClassName().equals(
                DefaultStandardContentRequest.class.getName())) {
            return new DefaultStandardContentRequest();
        } else {
            return super.getStandardReadBeanNewInstance();
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentRequestFactory#createNewArticleRequest(java.lang.String,
     *      java.lang.String, java.lang.String,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      java.lang.String,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      java.lang.String,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public StandardBean createNewArticleRequest(String name,
            String description, String body, PrimaryKey authorPk,
            PrimaryKey editorPk, PrimaryKey associateEditorsPk,
            String friendlyAddress, PrimaryKey parentArticlePk,
            PrimaryKey classificationTypePk, PrimaryKey requestorPk,
            PrimaryKey approverPk, PrimaryKey ruleTypePk, String comment) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentInfoFactory srif = (StandardContentInfoFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentInfoFactory.class);

        /*
         * create the content info
         */
        StandardContentRequest result = null;
        StandardContentInfo sri = (StandardContentInfo) srif.create(name,
                description);

        /*
         * create the content request
         */
        if ((sri != null) && (sri.getIsDone())) {
            PrimaryKey contentInfoPk = sri.getPrimaryKey();
            ContentRequest contentRequest = ContentRequestFactory
                    .createNewArticleRequest(contentInfoPk, body, authorPk,
                            editorPk, associateEditorsPk, friendlyAddress,
                            parentArticlePk, classificationTypePk, requestorPk,
                            approverPk, ruleTypePk, comment);

            if ((contentRequest != null) && (contentRequest.getIsDone())) {
                result = (StandardContentRequest) this
                        .findByPrimaryKey(contentRequest.getPrimaryKey());
                result.setIsDone(contentRequest.getIsDone());
                result.setStoreTrace(contentRequest.getStoreTrace());
            } else if (contentRequest != null) {
                throw new CannotDoBeanException(contentRequest.getStoreTrace());
            } else {
                throw new CannotDoBeanException();
            }
        } else if (sri != null) {
            throw new CannotDoBeanException(sri.getStoreTrace());
        } else {
            throw new CannotDoBeanException();
        }

        /*
         * return
         */
        return result;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentRequestFactory#createNewImageRequest(java.lang.String,
     *      java.lang.String,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      java.lang.String,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      java.lang.String,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public StandardBean createNewImageRequest(String name, String description,
            PrimaryKey authorPk, String friendlyAddress, PrimaryKey filePk,
            PrimaryKey parentArticlePk, PrimaryKey classificationTypePk,
            PrimaryKey requestorPk, PrimaryKey approverPk,
            PrimaryKey ruleTypePk, String comment) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentInfoFactory srif = (StandardContentInfoFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentInfoFactory.class);

        /*
         * create the content info
         */
        StandardContentRequest result = null;
        StandardContentInfo sri = (StandardContentInfo) srif.create(name,
                description);

        /*
         * create the content request
         */
        if ((sri != null) && (sri.getIsDone())) {
            PrimaryKey contentInfoPk = sri.getPrimaryKey();
            ContentRequest contentRequest = ContentRequestFactory
                    .createNewImageRequest(contentInfoPk, authorPk,
                            friendlyAddress, filePk, parentArticlePk,
                            classificationTypePk, requestorPk, approverPk,
                            ruleTypePk, comment);

            if ((contentRequest != null) && (contentRequest.getIsDone())) {
                result = (StandardContentRequest) this
                        .findByPrimaryKey(contentRequest.getPrimaryKey());
                result.setIsDone(contentRequest.getIsDone());
                result.setStoreTrace(contentRequest.getStoreTrace());
            } else if (contentRequest != null) {
                throw new CannotDoBeanException(contentRequest.getStoreTrace());
            } else {
                throw new CannotDoBeanException();
            }
        } else if (sri != null) {
            throw new CannotDoBeanException(sri.getStoreTrace());
        } else {
            throw new CannotDoBeanException();
        }

        /*
         * return
         */
        return result;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentRequestFactory#createImageRemovalRequest(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public StandardBean createImageRemovalRequest(PrimaryKey childContentPk,
            PrimaryKey parentArticlePk, PrimaryKey requestorPk,
            PrimaryKey approverPk) {
        /*
         * create the content request
         */
        StandardContentRequest result = null;
        ContentRequest contentRequest = ContentRequestFactory
                .createImageRemovalRequest(childContentPk, parentArticlePk,
                        requestorPk, approverPk);

        if ((contentRequest != null) && (contentRequest.getIsDone())) {
            result = (StandardContentRequest) this
                    .findByPrimaryKey(contentRequest.getPrimaryKey());
            result.setIsDone(contentRequest.getIsDone());
            result.setStoreTrace(contentRequest.getStoreTrace());
        } else if (contentRequest != null) {
            throw new CannotDoBeanException(contentRequest.getStoreTrace());
        } else {
            throw new CannotDoBeanException();
        }

        /*
         * return
         */
        return result;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentRequestFactory#createArticleRemovalRequest(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public StandardBean createArticleRemovalRequest(PrimaryKey childContentPk,
            PrimaryKey parentArticlePk, PrimaryKey requestorPk,
            PrimaryKey approverPk) {
        /*
         * create the content request
         */
        StandardContentRequest result = null;
        ContentRequest contentRequest = ContentRequestFactory
                .createArticleRemovalRequest(childContentPk, parentArticlePk,
                        requestorPk, approverPk);

        if ((contentRequest != null) && (contentRequest.getIsDone())) {
            result = (StandardContentRequest) this
                    .findByPrimaryKey(contentRequest.getPrimaryKey());
            result.setIsDone(contentRequest.getIsDone());
            result.setStoreTrace(contentRequest.getStoreTrace());
        } else if (contentRequest != null) {
            throw new CannotDoBeanException(contentRequest.getStoreTrace());
        } else {
            throw new CannotDoBeanException();
        }

        /*
         * return
         */
        return result;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentRequestFactory#findEnabledByRequestorPrimaryKey(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<StandardContentRequest> findEnabledByRequestorPrimaryKey(
            PrimaryKey pk) {
        List<StandardContentRequest> standardContentRequests = new ArrayList<StandardContentRequest>();
        List<PrimaryKey> primaryKeys = AbstractContentRequest
                .findEnabledByRequestorPrimaryKey(pk);

        for (PrimaryKey srPk : primaryKeys) {
            StandardContentRequest sr = (StandardContentRequest) this
                    .findByPrimaryKey(srPk);
            standardContentRequests.add(sr);
        }

        return standardContentRequests;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentRequestFactory#findEnabledPrimaryKeysByRequestorPrimaryKey(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<PrimaryKey> findEnabledPrimaryKeysByRequestorPrimaryKey(
            PrimaryKey pk) {
        return AbstractContentRequest.findEnabledByRequestorPrimaryKey(pk);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentRequestFactory#findEnabledByApproverPrimaryKey(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<StandardContentRequest> findEnabledByApproverPrimaryKey(
            PrimaryKey pk) {
        List<StandardContentRequest> standardContentRequests = new ArrayList<StandardContentRequest>();
        List<PrimaryKey> primaryKeys = AbstractContentRequest
                .findEnabledByApproverPrimaryKey(pk);

        for (PrimaryKey srPk : primaryKeys) {
            StandardContentRequest sr = (StandardContentRequest) this
                    .findByPrimaryKey(srPk);
            standardContentRequests.add(sr);
        }

        return standardContentRequests;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentRequestFactory#findEnabledPrimaryKeysByApproverPrimaryKey(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<PrimaryKey> findEnabledPrimaryKeysByApproverPrimaryKey(
            PrimaryKey pk) {
        return AbstractContentRequest.findEnabledByApproverPrimaryKey(pk);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentRequestFactory#findEnabledPendingByContentPrimaryKey(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<StandardContentRequest> findEnabledPendingByContentPrimaryKey(
            PrimaryKey pk) {
        List<StandardContentRequest> standardContentRequests = new ArrayList<StandardContentRequest>();
        List<PrimaryKey> primaryKeys = AbstractContentRequest
                .findEnabledPendingByContentPrimaryKey(pk);

        for (PrimaryKey srPk : primaryKeys) {
            StandardContentRequest sr = (StandardContentRequest) this
                    .findByPrimaryKey(srPk);
            standardContentRequests.add(sr);
        }

        return standardContentRequests;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentRequestFactory#findEnabledPendingByParentArticlePrimaryKey(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<StandardContentRequest> findEnabledPendingByParentArticlePrimaryKey(
            PrimaryKey pk) {
        List<StandardContentRequest> standardContentRequests = new ArrayList<StandardContentRequest>();
        List<PrimaryKey> primaryKeys = AbstractContentRequest
                .findEnabledPendingByParentArticlePrimaryKey(pk);

        for (PrimaryKey srPk : primaryKeys) {
            StandardContentRequest sr = (StandardContentRequest) this
                    .findByPrimaryKey(srPk);
            standardContentRequests.add(sr);
        }

        return standardContentRequests;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentRequestFactory#createArticleUpdateRequest(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      java.lang.String, java.lang.String, java.lang.String,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      java.lang.String,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      java.lang.String,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public StandardBean createArticleUpdateRequest(PrimaryKey childContentPk,
            String name, String description, String body, PrimaryKey authorPk,
            PrimaryKey editorPk, PrimaryKey associateEditorsPk,
            String friendlyAddress, PrimaryKey parentArticlePk,
            PrimaryKey classificationTypePk, PrimaryKey requestorPk,
            PrimaryKey approverPk, PrimaryKey ruleTypePk, String comment) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentInfoFactory srif = (StandardContentInfoFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentInfoFactory.class);

        /*
         * create the content info
         */
        StandardContentRequest result = null;
        StandardContentInfo sri = (StandardContentInfo) srif.create(name,
                description);

        /*
         * create the content request
         */
        if ((sri != null) && (sri.getIsDone())) {
            PrimaryKey contentInfoPk = sri.getPrimaryKey();
            ContentRequest contentRequest = ContentRequestFactory
                    .createArticleUpdateRequest(childContentPk, contentInfoPk,
                            body, authorPk, editorPk, associateEditorsPk,
                            friendlyAddress, parentArticlePk,
                            classificationTypePk, requestorPk, approverPk,
                            ruleTypePk, comment);

            if ((contentRequest != null) && (contentRequest.getIsDone())) {
                result = (StandardContentRequest) this
                        .findByPrimaryKey(contentRequest.getPrimaryKey());
                result.setIsDone(contentRequest.getIsDone());
                result.setStoreTrace(contentRequest.getStoreTrace());
            } else if (contentRequest != null) {
                throw new CannotDoBeanException(contentRequest.getStoreTrace());
            } else {
                throw new CannotDoBeanException();
            }
        } else if (sri != null) {
            throw new CannotDoBeanException(sri.getStoreTrace());
        } else {
            throw new CannotDoBeanException();
        }

        /*
         * return
         */
        return result;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentRequestFactory#createImageUpdateRequest(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      java.lang.String, java.lang.String,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      java.lang.String,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      java.lang.String,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public StandardBean createImageUpdateRequest(PrimaryKey childContentPk,
            String name, String description, PrimaryKey authorPk,
            String friendlyAddress, PrimaryKey filePk,
            PrimaryKey parentArticlePk, PrimaryKey classificationTypePk,
            PrimaryKey requestorPk, PrimaryKey approverPk,
            PrimaryKey ruleTypePk, String comment) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentInfoFactory srif = (StandardContentInfoFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentInfoFactory.class);

        /*
         * create the content info
         */
        StandardContentRequest result = null;
        StandardContentInfo sri = (StandardContentInfo) srif.create(name,
                description);

        /*
         * create the content request
         */
        if ((sri != null) && (sri.getIsDone())) {
            PrimaryKey contentInfoPk = sri.getPrimaryKey();
            ContentRequest contentRequest = ContentRequestFactory
                    .createImageUpdateRequest(childContentPk, contentInfoPk,
                            authorPk, friendlyAddress, filePk, parentArticlePk,
                            classificationTypePk, requestorPk, approverPk,
                            ruleTypePk, comment);

            if ((contentRequest != null) && (contentRequest.getIsDone())) {
                result = (StandardContentRequest) this
                        .findByPrimaryKey(contentRequest.getPrimaryKey());
                result.setIsDone(contentRequest.getIsDone());
                result.setStoreTrace(contentRequest.getStoreTrace());
            } else if (contentRequest != null) {
                throw new CannotDoBeanException(contentRequest.getStoreTrace());
            } else {
                throw new CannotDoBeanException();
            }
        } else if (sri != null) {
            throw new CannotDoBeanException(sri.getStoreTrace());
        } else {
            throw new CannotDoBeanException();
        }

        /*
         * return
         */
        return result;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentRequestFactory#createNewReferenceRequest(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public StandardBean createNewReferenceRequest(PrimaryKey childContentPk,
            PrimaryKey parentArticlePk, PrimaryKey requestorPk,
            PrimaryKey approverPk) {
        /*
         * create the content request
         */
        StandardContentRequest result = null;
        ContentRequest contentRequest = ContentRequestFactory
                .createNewReferenceRequest(childContentPk, parentArticlePk,
                        requestorPk, approverPk);

        if ((contentRequest != null) && (contentRequest.getIsDone())) {
            result = (StandardContentRequest) this
                    .findByPrimaryKey(contentRequest.getPrimaryKey());
            result.setIsDone(contentRequest.getIsDone());
            result.setStoreTrace(contentRequest.getStoreTrace());
        } else if (contentRequest != null) {
            throw new CannotDoBeanException(contentRequest.getStoreTrace());
        } else {
            throw new CannotDoBeanException();
        }

        /*
         * return
         */
        return result;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentRequestFactory#createReferenceRemovalRequest(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public StandardBean createReferenceRemovalRequest(
            PrimaryKey childContentPk, PrimaryKey parentArticlePk,
            PrimaryKey requestorPk, PrimaryKey approverPk) {
        /*
         * create the content request
         */
        StandardContentRequest result = null;
        ContentRequest contentRequest = ContentRequestFactory
                .createReferenceRemovalRequest(childContentPk, parentArticlePk,
                        requestorPk, approverPk);

        if ((contentRequest != null) && (contentRequest.getIsDone())) {
            result = (StandardContentRequest) this
                    .findByPrimaryKey(contentRequest.getPrimaryKey());
            result.setIsDone(contentRequest.getIsDone());
            result.setStoreTrace(contentRequest.getStoreTrace());
        } else if (contentRequest != null) {
            throw new CannotDoBeanException(contentRequest.getStoreTrace());
        } else {
            throw new CannotDoBeanException();
        }

        /*
         * return
         */
        return result;
    }
}

// end AbstractStandardContentRequestFactory

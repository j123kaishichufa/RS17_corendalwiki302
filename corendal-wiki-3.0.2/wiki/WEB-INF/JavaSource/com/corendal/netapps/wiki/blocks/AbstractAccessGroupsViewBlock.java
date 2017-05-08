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
package com.corendal.netapps.wiki.blocks;

import java.util.ArrayList;
import java.util.List;

import com.corendal.netapps.framework.configuration.utils.CaseUtil;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.StandardMessage;
import com.corendal.netapps.framework.core.interfaces.StandardMessageFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardMessageFactory;
import com.corendal.netapps.framework.core.utils.ConcatUtil;
import com.corendal.netapps.framework.helpdesk.blocks.AbstractGroupSearchResultsBlock;
import com.corendal.netapps.framework.helpdesk.interfaces.StandardGroup;
import com.corendal.netapps.framework.helpdesk.interfaces.StandardGroupFactory;
import com.corendal.netapps.framework.helpdesk.writestandardfactories.DefaultStandardGroupFactory;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.dictionaries.ContentClassificationTypesDictionary;
import com.corendal.netapps.wiki.dictionaries.MessagesDictionary;
import com.corendal.netapps.wiki.dictionaries.RolesDictionary;
import com.corendal.netapps.wiki.interfaces.Searched;
import com.corendal.netapps.wiki.interfaces.StandardContent;
import com.corendal.netapps.wiki.interfaces.StandardContentFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentFactory;

/**
 * AbstractAccessGroupsViewBlock is the class describing and printing the lkist
 * of searched subscribers.
 * 
 * @version $Id: AbstractAccessGroupsViewBlock.java,v 1.1 2005/09/06 21:25:28
 *          tdanard Exp $
 */
public abstract class AbstractAccessGroupsViewBlock extends
        AbstractGroupSearchResultsBlock implements Cloneable {
    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractAccessGroupsViewBlock() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractGroupSearchResultsBlock.clone.
     */
    @Override
    public Object clone() {
        return (AbstractAccessGroupsViewBlock) super.clone();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBlock#initStandardBlock(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public void initStandardBlock() {
        /*
         * initialize the standard results block
         */
        super.initStandardBlock();

        /*
         * verify the visibility of this block
         */
        Searched searched = this.getViewedSearched();

        if (searched != null) {
            /*
             * get the primary key of the "no login required" classification
             */
            PrimaryKey noLoginRequiredPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(ContentClassificationTypesDictionary.NO_LOGIN_REQUIRED);
            PrimaryKey loginRequiredPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(ContentClassificationTypesDictionary.LOGIN_REQUIRED);
            PrimaryKey sameAsParentPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(ContentClassificationTypesDictionary.SAME_AS_PARENT);

            /*
             * verify the classification type
             */
            PrimaryKey classificationTypePk = searched
                    .getClassificationTypePrimaryKey();
            this
                    .setIsVisible(!(classificationTypePk
                            .equals(noLoginRequiredPk)
                            || classificationTypePk.equals(loginRequiredPk) || classificationTypePk
                            .equals(sameAsParentPk)));
        } else {
            this.setIsVisible(false);
        }
    }

    /**
     * Returns the description of a group.
     * 
     * @return a java.lang.String object
     */
    @Override
    public String getGroupDescriptionText(StandardGroup group) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentFactory srf = (StandardContentFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentFactory.class);
        StandardMessageFactory smf = (StandardMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardMessageFactory.class);

        /*
         * get the current content
         */
        PrimaryKey contentPk = this.getViewedSearched().getPrimaryKey();

        /*
         * get the factories of the instances used in this procedure
         */
        StandardContent currentContent = (StandardContent) srf
                .findByPrimaryKey(contentPk);

        /*
         * get the default description
         */
        String descriptionText = super.getGroupDescriptionText(group);

        /*
         * get the current content
         */
        if (currentContent.getHasAllowedAccessGroup(group.getPrimaryKey())) {
            PrimaryKey accessPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(MessagesDictionary.DSP_ALLOW);
            StandardMessage accessMessage = (StandardMessage) smf
                    .findByPrimaryKey(accessPk);
            String accessMessageText = CaseUtil
                    .getLowerCaseDeleteAccents(accessMessage
                            .getCurrentMessageText());
            descriptionText = ConcatUtil.getConcatWithBrackets(descriptionText,
                    accessMessageText, descriptionText, accessMessageText);
        } else {
            PrimaryKey accessPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(MessagesDictionary.DSP_DENY);
            StandardMessage accessMessage = (StandardMessage) smf
                    .findByPrimaryKey(accessPk);
            String accessMessageText = CaseUtil
                    .getLowerCaseDeleteAccents(accessMessage
                            .getCurrentMessageText());
            descriptionText = ConcatUtil.getConcatWithBrackets(descriptionText,
                    accessMessageText, descriptionText, accessMessageText);
        }

        /*
         * return
         */
        return descriptionText;
    }

    /**
     * Returns the list of groups to print. Overrides
     * AbstractGroupSearchResultsBlock.getGroupsFound.
     * 
     * 
     * 
     * @return a java.util.List object
     */
    @Override
    protected List getGroupsFound() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardGroupFactory sgf = (StandardGroupFactory) pfs
                .getStandardBeanFactory(DefaultStandardGroupFactory.class);

        /*
         * build the list of subscriptions
         */
        List<StandardGroup> groupsFound = null;
        Searched searched = this.getViewedSearched();

        if (searched != null) {
            PrimaryKey accessGroupAllowedRolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.ACCESS_GROUP_ALLOWED);
            groupsFound = sgf.findEnabledByRecordAndRolePrimaryKeys(searched
                    .getPrimaryKey(), accessGroupAllowedRolePk);

            PrimaryKey accessGroupDeniedRolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.ACCESS_GROUP_DENIED);
            List<StandardGroup> deniedGroupsFound = sgf
                    .findEnabledByRecordAndRolePrimaryKeys(searched
                            .getPrimaryKey(), accessGroupDeniedRolePk);
            groupsFound.addAll(deniedGroupsFound);
        } else {
            groupsFound = new ArrayList<StandardGroup>();
        }

        /*
         * return
         */
        return groupsFound;
    }

    /**
     * Returns the standard searched to be viewed.
     * 
     * 
     * 
     * @return a Searched object
     */
    public abstract Searched getViewedSearched();

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBlock#getRecordName(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public String getRecordName() {
        Searched sp = this.getViewedSearched();

        if (sp != null) {
            return sp.getNameText();
        } else {
            return null;
        }
    }
}

// end AbstractAccessGroupsViewBlock

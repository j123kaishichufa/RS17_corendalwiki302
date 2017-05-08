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
package com.corendal.netapps.wiki.readstandardbeans;

import com.corendal.netapps.framework.core.utils.ConcatUtil;
import com.corendal.netapps.framework.core.utils.EntityUtil;
import com.corendal.netapps.framework.core.utils.TextFormatUtil;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.constants.HTTPParameterConstants;
import com.corendal.netapps.wiki.dictionaries.EntitiesDictionary;
import com.corendal.netapps.wiki.interfaces.StandardContentSubscriptionMode;
import com.corendal.netapps.wiki.readdatabeans.AbstractContentSubscriptionMode;

/**
 * AbstractStandardContentSubscriptionMode is the abstract class handling
 * information about each content classification type of the application.
 * 
 * @version $Id: AbstractStandardContentSubscriptionMode.java,v 1.1 2005/09/06
 *          21:25:34 tdanard Exp $
 */
public abstract class AbstractStandardContentSubscriptionMode extends
        AbstractContentSubscriptionMode implements Cloneable,
        StandardContentSubscriptionMode {

    /**
     * Class constructor.
     * 
     * 
     * 
     */
    protected AbstractStandardContentSubscriptionMode() {
        // no initialization required
    }

    /**
     * Returns a clone. Overrides AbstractContentSubscriptionMode.clone.
     */
    @Override
    public Object clone() {
        return (AbstractStandardContentSubscriptionMode) super.clone();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBean#initStandardBean(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public void initStandardBean() {
        if (this.getIsFound()) {
            this.setSortValue(this.getNameText());
        }
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Named#getNameText(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getNameText() {
        return this.getName();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Named#getNameEncoded(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getNameEncoded() {
        return TextFormatUtil.getTextToEncoded(this.getNameText());
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Named#getNameHTML(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getNameHTML() {
        return TextFormatUtil.getTextToHTML(this.getNameText());
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Described#getDescriptionText(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getDescriptionText() {
        return this.getDescription();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Described#getDescriptionEncoded(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getDescriptionEncoded() {
        return TextFormatUtil.getTextToEncoded(this.getDescriptionText());
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Described#getDescriptionHTML(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getDescriptionHTML() {
        return TextFormatUtil.getTextToHTML(this.getDescriptionText());
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Commented#getCommentText(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getCommentText() {
        return this.getComment();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Commented#getCommentEncoded(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getCommentEncoded() {
        return TextFormatUtil.getTextToEncoded(this.getCommentText());
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Commented#getCommentHTML(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getCommentHTML() {
        return TextFormatUtil.getTextToHTML(this.getCommentText());
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Matched#getMatchNameText(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getMatchNameText() {
        return this.getNameText();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Matched#getMatchNameEncoded(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getMatchNameEncoded() {
        return this.getNameEncoded();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Matched#getMatchNameHTML(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getMatchNameHTML() {
        return this.getNameHTML();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.DescribedShort#getShortDescriptionText(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getShortDescriptionText() {
        return this.getDescriptionText();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.DescribedShort#getShortDescriptionEncoded(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getShortDescriptionEncoded() {
        return this.getDescriptionEncoded();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.DescribedShort#getShortDescriptionHTML(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getShortDescriptionHTML() {
        return this.getDescriptionHTML();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.DescribedLong#getLongDescriptionText(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getLongDescriptionText() {
        return ConcatUtil.getConcatWithDot(this.getDescriptionText(), this
                .getCommentText(), this.getDescriptionText(), this
                .getCommentText());
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.DescribedLong#getLongDescriptionEncoded(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getLongDescriptionEncoded() {
        return ConcatUtil.getConcatWithDot(this.getDescriptionText(), this
                .getCommentText(), this.getDescriptionEncoded(), this
                .getCommentEncoded());
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.DescribedLong#getLongDescriptionHTML(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getLongDescriptionHTML() {
        return ConcatUtil.getConcatWithDot(this.getDescriptionText(), this
                .getCommentText(), this.getDescriptionHTML(), this
                .getCommentHTML());
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Located#getRelativeLocation(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getRelativeLocation() {
        return EntityUtil
                .getRelativeLocation(
                        PrimaryKeyUtil
                                .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_SUBSCRIPTION_MODES),
                        HTTPParameterConstants.CONTENT_SUBSCRIPTION_MODE_ID,
                        this.getPrimaryKey().toString());
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Located#getDefaultLocation(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getDefaultLocation() {
        return EntityUtil
                .getDefaultLocation(
                        PrimaryKeyUtil
                                .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_SUBSCRIPTION_MODES),
                        HTTPParameterConstants.CONTENT_SUBSCRIPTION_MODE_ID,
                        this.getPrimaryKey().toString());
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Located#getAbsoluteLocation(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getAbsoluteLocation() {
        return EntityUtil
                .getAbsoluteLocation(
                        PrimaryKeyUtil
                                .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_SUBSCRIPTION_MODES),
                        HTTPParameterConstants.CONTENT_SUBSCRIPTION_MODE_ID,
                        this.getPrimaryKey().toString());
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.AccessKeyed#getAccessKeyText(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getAccessKeyText() {
        return null;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.AccessKeyed#getAccessKeyEncoded(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getAccessKeyEncoded() {
        return null;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.AccessKeyed#getAccessKeyHTML(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getAccessKeyHTML() {
        return null;
    }
}

// end AbstractStandardContentSubscriptionMode

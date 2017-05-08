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
package com.corendal.netapps.wiki.readdatahandlers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.corendal.netapps.framework.modelaccess.handlers.AbstractLoadHandler;
import com.corendal.netapps.wiki.interfaces.ContentRequestTypeLoadHandler;
import com.corendal.netapps.wiki.readdataelements.ContentRequestTypeElements;

/**
 * AbstractContentRequestTypeLoadHandler is the abstract class processing XML
 * entities representing a content request type found after a load query.
 * 
 * @version $Id: AbstractContentRequestTypeLoadHandler.java,v 1.1 2005/09/06
 *          21:25:33 tdanard Exp $
 */
public abstract class AbstractContentRequestTypeLoadHandler extends
        AbstractLoadHandler implements ContentRequestTypeLoadHandler {
    /** Name in the content request type XML entity. */
    private StringBuilder name;

    /** Description in the content request type XML entity. */
    private StringBuilder description;

    /** Comment in the content request type XML entity. */
    private StringBuilder comment;

    /** Order in the content request type XML entity. */
    private int order;

    /** Enabled-flag in the content request type XML entity. */
    private String enabledFlag;

    /** Boolean associated with name for parsing. */
    private boolean isNameCollected;

    /** Boolean associated with description for parsing. */
    private boolean isDescriptionCollected;

    /** Boolean associated with comment for parsing. */
    private boolean isCommentCollected;

    /**
     * Default class constructor.
     */
    protected AbstractContentRequestTypeLoadHandler() {
        // parent class constructor is called
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequestTypeLoadHandler#getName()
     */
    public String getName() {
        return this.getTrimmedString(this.name);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequestTypeLoadHandler#getDescription()
     */
    public String getDescription() {
        return this.getTrimmedString(this.description);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequestTypeLoadHandler#getComment()
     */
    public String getComment() {
        return this.getTrimmedString(this.comment);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequestTypeLoadHandler#getOrder()
     */
    public int getOrder() {
        return this.order;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequestTypeLoadHandler#getEnabledFlag()
     */
    public String getEnabledFlag() {
        return this.getTrimmedString(this.enabledFlag);
    }

    /**
     * Processes an element. Overrides AbstractHandler.processElement.
     * 
     * @param namespaceUri
     *            URI of the namespace where the element was found
     * @param localName
     *            local name of the element found
     * @param qualifiedName
     *            qualified name of the element found
     * @param attributes
     *            list of attributes of the element
     */
    @Override
    protected void processElement(String namespaceUri, String localName,
            String qualifiedName, Attributes attributes, boolean isStart)
            throws SAXException {
        super.processElement(namespaceUri, localName, qualifiedName,
                attributes, isStart);
        if (qualifiedName.equals(ContentRequestTypeElements.ENTITY)) {
            if (isStart) {
                this.enabledFlag = this.getStringAttributeValue(attributes,
                        ContentRequestTypeElements.ENABLED_FLAG);
                this.order = this.getIntegerAttributeValue(attributes,
                        ContentRequestTypeElements.ORDER);
            }
        } else if (qualifiedName.equals(ContentRequestTypeElements.NAME)) {
            this.isNameCollected = isStart;
        } else if (qualifiedName.equals(ContentRequestTypeElements.DESCRIPTION)) {
            this.isDescriptionCollected = isStart;
        } else if (qualifiedName.equals(ContentRequestTypeElements.COMMENT)) {
            this.isCommentCollected = isStart;
        } else {
            this.setIsSomethingCollected(false);
        }
    }

    /**
     * Handles the characters found.
     * 
     * @param chars
     *            list of characters
     * @param startIndex
     *            index of the character to start with
     * @param length
     *            number of characters found
     */
    @Override
    public void characters(char[] chars, int startIndex, int length) {
        if (this.getIsSomethingCollected()) {
            StringBuilder dataString = this.getDataString(chars, startIndex,
                    length);
            if (this.isNameCollected) {
                this.name = this.getAddedDataString(this.name, dataString);
            } else if (this.isDescriptionCollected) {
                this.description = this.getAddedDataString(this.description,
                        dataString);
            } else if (this.isCommentCollected) {
                this.comment = this
                        .getAddedDataString(this.comment, dataString);
            }
        }
    }
}

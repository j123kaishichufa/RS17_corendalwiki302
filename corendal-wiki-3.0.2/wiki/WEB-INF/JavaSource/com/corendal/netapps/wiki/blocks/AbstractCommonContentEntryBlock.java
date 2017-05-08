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

import com.corendal.netapps.framework.core.blocks.AbstractStandardEntryFormBlock;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.StandardEntity;
import com.corendal.netapps.framework.core.interfaces.StandardEntityFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardEntityFactory;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.dictionaries.EntitiesDictionary;

/**
 * AbstractCommonContentEntryBlock is the parent block common to all content
 * creating, editing or viewing blocks.
 * 
 * @version $Id: AbstractCommonContentEntryBlock.java,v 1.1 2005/09/06 21:25:28
 *          tdanard Exp $
 */
public abstract class AbstractCommonContentEntryBlock extends
        AbstractStandardEntryFormBlock implements Cloneable {
    /** Name entered by the user */
    private String name;

    /** Password entered by the user */
    private String description;

    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractCommonContentEntryBlock() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractStandardEntryFormBlock.clone.
     */
    @Override
    public Object clone() {
        return (AbstractCommonContentEntryBlock) super.clone();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBlock#initStandardBlock(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public void initStandardBlock() {
        /*
         * init the entry form block
         */
        super.initStandardBlock();

        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardEntityFactory sef = (StandardEntityFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntityFactory.class);

        /*
         * set the entity
         */
        PrimaryKey contentsPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENTS);
        StandardEntity entity = (StandardEntity) sef
                .findByPrimaryKey(contentsPk);
        this.setStandardEntity(entity);
    }

    /**
     * Sets the name entered by the user.
     * 
     * @param name
     *            name to set
     * @see #getContentName
     */
    protected void setContentName(String name) {
        this.name = name;
    }

    /**
     * Returns the name entered by the user.
     * 
     * @return a java.lang.String object
     * @see #setContentName
     */
    protected String getContentName() {
        return this.name;
    }

    /**
     * Sets the description entered by the user.
     * 
     * @param description
     *            description to set
     * @see #getContentDescription
     */
    protected void setContentDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the description entered by the user.
     * 
     * @return a java.lang.String object@see #setContentDescription
     */
    protected String getContentDescription() {
        return this.description;
    }
}

// end AbstractCommonContentEntryBlock

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

import com.corendal.netapps.framework.core.interfaces.StandardReadBean;
import com.corendal.netapps.framework.core.standardfactories.AbstractStandardWriteBeanFactory;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.wiki.interfaces.StandardArticleVersion;
import com.corendal.netapps.wiki.interfaces.StandardArticleVersionFactory;
import com.corendal.netapps.wiki.writedatabeans.AbstractArticleVersion;
import com.corendal.netapps.wiki.writestandardbeans.DefaultStandardArticleVersion;

/**
 * AbstractStandardArticleVersionFactory is the abstract class building new
 * StandardArticleVersion instances.
 * 
 * @version $Id: AbstractStandardArticleVersionFactory.java,v 1.1 2005/09/06
 *          21:25:35 tdanard Exp $
 */
public abstract class AbstractStandardArticleVersionFactory extends
        AbstractStandardWriteBeanFactory implements Cloneable,
        StandardArticleVersionFactory {

    /**
     * Class constructor.
     * 
     * 
     * 
     */
    protected AbstractStandardArticleVersionFactory() {
        super(DefaultStandardArticleVersion.class, true);
    }

    /**
     * Returns a clone. Overrides AbstractStandardWriteBeanFactory.clone.
     */
    @Override
    public Object clone() {
        return (AbstractStandardArticleVersionFactory) super.clone();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardReadBeanFactory#getStandardReadBeanNewInstance(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public StandardReadBean getStandardReadBeanNewInstance() {
        if (this.getClassName().equals(
                DefaultStandardArticleVersion.class.getName())) {
            return new DefaultStandardArticleVersion();
        } else {
            return super.getStandardReadBeanNewInstance();
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardArticleVersionFactory#findEnabledByArticlePrimaryKey(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<StandardArticleVersion> findEnabledByArticlePrimaryKey(
            PrimaryKey pk) {
        List<StandardArticleVersion> standardArticleVersions = new ArrayList<StandardArticleVersion>();
        List<PrimaryKey> primaryKeys = AbstractArticleVersion
                .findEnabledByArticlePrimaryKey(pk);

        for (PrimaryKey sdvPk : primaryKeys) {
            StandardArticleVersion sdv = (StandardArticleVersion) this
                    .findByPrimaryKey(sdvPk);
            standardArticleVersions.add(sdv);
        }

        return standardArticleVersions;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardArticleVersionFactory#findEnabledByArticlePrimaryKey(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<StandardArticleVersion> findEnabledByAddress(String address) {
        List<StandardArticleVersion> standardArticleVersions = new ArrayList<StandardArticleVersion>();
        List<PrimaryKey> primaryKeys = AbstractArticleVersion
                .findEnabledByAddress(address);

        for (PrimaryKey sdvPk : primaryKeys) {
            StandardArticleVersion sdv = (StandardArticleVersion) this
                    .findByPrimaryKey(sdvPk);
            standardArticleVersions.add(sdv);
        }

        return standardArticleVersions;
    }
}

// end AbstractStandardArticleVersionFactory

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
import com.corendal.netapps.wiki.interfaces.ContentSubscription;
import com.corendal.netapps.wiki.writedatabeans.DefaultContentSubscription;

/**
 * ContentSubscriptionFactory is the static class building new
 * ContentSubscription instances.
 * 
 * @version $Id: ContentSubscriptionFactory.java,v 1.1 2005/09/06 21:25:36
 *          tdanard Exp $
 */
public final class ContentSubscriptionFactory {
    /**
     * Private constructor to stop anyone from instantiating this class - the
     * static references should be used explicitly.
     */
    private ContentSubscriptionFactory() {
        // this class is only used for storing constants
    }

    /**
     * Returns an existing content subscription.
     * 
     * @param contentSubscriptionPk
     *            primary key of a content subscription
     * 
     * 
     * @return a ContentSubscription object
     */
    public static final ContentSubscription findByPrimaryKey(
            PrimaryKey contentSubscriptionPk) {
        DefaultContentSubscription contentSubscription = new DefaultContentSubscription();
        contentSubscription.setPrimaryKeyAndLoad(contentSubscriptionPk);
        return contentSubscription;
    }

    /**
     * Creates a new content subscription.
     * 
     * @param contentPk
     *            primary key of the content
     * @param typePk
     *            primary key of the type of the content subscription
     * @param modePk
     *            primary key of the mode of the content subscription
     * 
     * 
     * @return a ContentSubscription object
     */
    public static final ContentSubscription create(PrimaryKey contentPk,
            PrimaryKey typePk, PrimaryKey modePk) {
        DefaultContentSubscription contentSubscription = new DefaultContentSubscription();
        contentSubscription.add(contentPk, typePk, modePk);

        return contentSubscription;
    }
}

// end ContentSubscriptionFactory

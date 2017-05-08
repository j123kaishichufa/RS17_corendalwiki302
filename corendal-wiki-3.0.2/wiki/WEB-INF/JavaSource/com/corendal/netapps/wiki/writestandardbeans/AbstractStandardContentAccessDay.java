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
package com.corendal.netapps.wiki.writestandardbeans;

import com.corendal.netapps.wiki.interfaces.StandardContentAccessDay;
import com.corendal.netapps.wiki.writedatabeans.AbstractContentAccessDay;

/**
 * AbstractStandardContentAccessDay is the abstract class handling information
 * about each content access day of the application.
 * 
 * @version $Id: AbstractStandardContentAccessDay.java,v 1.1 2005/09/06 21:25:31
 *          tdanard Exp $
 */
public abstract class AbstractStandardContentAccessDay extends
        AbstractContentAccessDay implements Cloneable, StandardContentAccessDay {

    /**
     * Class constructor.
     * 
     * 
     * 
     */
    protected AbstractStandardContentAccessDay() {
        // no initialization required
    }

    /**
     * Returns a clone. Overrides AbstractContentAccessDay.clone.
     */
    @Override
    public Object clone() {
        return (AbstractStandardContentAccessDay) super.clone();
    }

    /**
     * Initializes this object as a standard bean.
     * 
     * 
     * 
     */
    public void initStandardBean() {
        // no initialization required
    }
}

// end AbstractStandardContentAccessDay

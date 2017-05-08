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

import com.corendal.netapps.framework.configuration.utils.StringUtil;
import com.corendal.netapps.wiki.interfaces.StandardContentInfo;
import com.corendal.netapps.wiki.writedatabeans.AbstractContentInfo;

/**
 * AbstractStandardContentInfo is the abstract class handling information about
 * each content info of the application.
 * 
 * @version $Id: AbstractStandardContentInfo.java,v 1.1 2005/09/06 21:25:31
 *          tdanard Exp $
 */
public abstract class AbstractStandardContentInfo extends AbstractContentInfo
        implements Cloneable, StandardContentInfo {

    /**
     * Class constructor.
     * 
     * 
     * 
     */
    protected AbstractStandardContentInfo() {
        // no initialization required
    }

    /**
     * Returns a clone. Overrides AbstractContentInfo.clone.
     */
    @Override
    public Object clone() {
        return (AbstractStandardContentInfo) super.clone();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBean#initStandardBean(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public void initStandardBean() {
        // no initialization required
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentInfo#getIsIdentical(com.corendal.netapps.wiki.interfaces.StandardContentInfo)
     */
    public boolean getIsIdentical(StandardContentInfo sri) {
        if (this.equals(sri)) {
            return true;
        } else {
            return StringUtil.getIsIdentical(this.getName(), sri.getName())
                    && StringUtil.getIsIdentical(this.getDescription(), sri
                            .getDescription());
        }
    }
}

// end AbstractStandardContentInfo

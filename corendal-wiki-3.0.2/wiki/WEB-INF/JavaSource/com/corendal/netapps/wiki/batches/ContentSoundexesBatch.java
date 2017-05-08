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
package com.corendal.netapps.wiki.batches;

import java.util.List;

import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.StandardReadBean;
import com.corendal.netapps.framework.core.utils.SoundexUtil;
import com.corendal.netapps.framework.runtime.batches.AbstractDatabaseExecutableBatch;
import com.corendal.netapps.wiki.interfaces.StandardContentInfo;
import com.corendal.netapps.wiki.interfaces.StandardContentInfoFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentInfoFactory;

/**
 * ContentSoundexesBatch is the class updating content Soundexes.
 * 
 * @version $Id: ContentSoundexesBatch.java,v 1.1 2005/09/06 21:25:35 tdanard
 *          Exp $
 */
public class ContentSoundexesBatch extends AbstractDatabaseExecutableBatch
        implements Cloneable {
    /** Version uid for serialization. */
    private static final long serialVersionUID = 1L;

    /** Fully qualified name of this class. */
    public static final String DEFAULT_CLASS_NAME = "com.corendal.netapps.wiki.batches.ContentSoundexesBatch";

    /**
     * Default class constructor.
     */
    public ContentSoundexesBatch() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractDatabaseExecutableBatch.clone.
     */
    @Override
    public Object clone() {
        return (ContentSoundexesBatch) super.clone();
    }

    /**
     * Execution method. Implements abstract method
     * AbstractDatabaseExecutableBatch.answerExecution.
     */
    @Override
    public void answerExecution() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentInfoFactory srif = (StandardContentInfoFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentInfoFactory.class);

        /*
         * get the list of all content infos
         */
        List<StandardReadBean> contentInfos = srif.findByEnabledFlag("Y");

        /*
         * add each text and description
         */
        for (int i = 0; i < contentInfos.size(); i++) {
            StandardContentInfo sri = (StandardContentInfo) contentInfos.get(i);
            String name = sri.getName();
            String description = sri.getDescription();
            SoundexUtil.parseSoundex(name);
            SoundexUtil.parseSoundex(description);
        }
    }
}

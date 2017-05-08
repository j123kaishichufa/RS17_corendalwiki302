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
package com.corendal.netapps.wiki.patches;

import com.corendal.netapps.framework.core.constants.FieldTypeConstants;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.ConfigManager;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.FormFieldWriter;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.StandardField;
import com.corendal.netapps.framework.core.interfaces.StandardFormField;
import com.corendal.netapps.framework.core.interfaces.StandardPage;
import com.corendal.netapps.framework.core.interfaces.StandardPageFactory;
import com.corendal.netapps.framework.core.interfaces.StandardPropertySet;
import com.corendal.netapps.framework.core.managers.DefaultConfigManager;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardPageFactory;
import com.corendal.netapps.framework.core.utils.TextFormatUtil;
import com.corendal.netapps.framework.core.writers.AbstractFormFieldWriter;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.dictionaries.FieldsDictionary;
import com.corendal.netapps.wiki.dictionaries.PagesDictionary;

/**
 * CustomFormFieldWriter is the concrete class printing results blocks.
 * 
 * @version $Id: CustomFormFieldWriter.java,v 1.2 2005/09/13 18:35:18 tdanard
 *          Exp $
 */
public final class CustomFormFieldWriter extends AbstractFormFieldWriter
        implements Cloneable, FormFieldWriter {
    /** Version uid for serialization. */
    private static final long serialVersionUID = 1L;

    /**
     * Default class constructor.
     */
    public CustomFormFieldWriter() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractWriter.clone.
     */
    @Override
    public Object clone() {
        return (CustomFormFieldWriter) super.clone();
    }

    /**
     * Returns a field for input outside of a cell.
     * 
     * @param sff
     *            standard form field to print
     * 
     * 
     * @return a java.lang.String object
     */
    @Override
    public String getHTMLNoCell(StandardFormField sff) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardPageFactory spf = (StandardPageFactory) pfs
                .getStandardBeanFactory(DefaultStandardPageFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        ConfigManager cfm = (ConfigManager) pms
                .getManager(DefaultConfigManager.class);

        /*
         * initialize the result
         */
        StringBuilder result = new StringBuilder();

        /*
         * get the standard field being printed
         */
        StandardField sf = sff.getStandardField();

        /*
         * verify which field is being printed
         */
        PrimaryKey articleBodyFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_BODY);

        if ((sf.getPrimaryKey().equals(articleBodyFieldPk))
                && (!(sff.getType().equals(FieldTypeConstants.PRINTONLY)))) {
            /*
             * get the property set
             */
            StandardPropertySet prop = cfm.getStandardPropertySet();

            /*
             * build the icons directory location
             */
            String imagesFolder = prop.getImagesFolder() + "/rte/";

            /*
             * build the rte directory location
             */
            String cgiFolder = prop.getRelativeCgiFolder() + "/rte/";

            /*
             * get the goto pages
             */
            PrimaryKey gotoArticlePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(PagesDictionary.GOTO_ARTICLE);
            StandardPage gotoArticle = (StandardPage) spf
                    .findByPrimaryKey(gotoArticlePk);
            PrimaryKey gotoImagePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(PagesDictionary.GOTO_IMAGE);
            StandardPage gotoImage = (StandardPage) spf
                    .findByPrimaryKey(gotoImagePk);

            /*
             * get the goto locations
             */
            String articleLocation = gotoArticle.getAbsoluteLocation();
            String imageLocation = gotoImage.getAbsoluteLocation();

            /*
             * build the css location
             */
            String cssLocation = prop.getRelativeCgiFolder() + "/css/"
                    + "screen.css";

            /*
             * print the javascript call. The two calls must be separate because
             * of a FireFox bug
             */
            result
                    .append("<script language=\"javascript\" type=\"text/javascript\">\n");
            result.append("<!-- \n");
            result.append("initRTE(\"" + imagesFolder + "\", \"" + cgiFolder
                    + "\", \"" + cssLocation + "\", \"" + articleLocation
                    + "\", \"" + imageLocation + "\", \""
                    + "contentbody" + "\");\n");
            result.append("//-->\n");
            result.append("</script>\n");
            result
                    .append("<script language=\"javascript\" type=\"text/javascript\">\n");
            result.append("<!-- \n");
            result.append("writeRichText('"
                    + sff.getParameterName()
                    + "', '"
                    + TextFormatUtil.getTextToJavascript(sff
                            .getUsedDefaultValueText())
                    + "', 500, 350, true, false);\n");
            result.append("//-->\n");
            result.append("</script>\n");

            /*
             * print the code when no javascript is available
             */
            result.append("<noscript>\n");
            result.append(super.getHTMLNoCell(sff) + "\n");
            result.append("</noscript>\n");

            /*
             * return
             */
            return result.toString();
        } else {
            return super.getHTMLNoCell(sff);
        }
    }
}

// end CustomFormFieldWriter

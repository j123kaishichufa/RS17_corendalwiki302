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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.corendal.netapps.framework.configuration.utils.StringUtil;
import com.corendal.netapps.framework.core.blocks.AbstractStandardSearchFormBlock;
import com.corendal.netapps.framework.core.constants.CanvasTypeConstants;
import com.corendal.netapps.framework.core.constants.HTTPParameterConstants;
import com.corendal.netapps.framework.core.constants.LayoutTypeConstants;
import com.corendal.netapps.framework.core.dictionaries.IconsDictionary;
import com.corendal.netapps.framework.core.formfactories.DefaultStandardFormIconFactory;
import com.corendal.netapps.framework.core.formfactories.DefaultStandardFormMessageFactory;
import com.corendal.netapps.framework.core.formfactories.DefaultStandardSearchFormButtonFactory;
import com.corendal.netapps.framework.core.formfactories.DefaultStandardSearchFormFieldFactory;
import com.corendal.netapps.framework.core.formfactories.DefaultStandardSearchFormLabelFactory;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.glossaryfactories.DefaultStandardGlossaryEntityFactory;
import com.corendal.netapps.framework.core.interfaces.AccountHistoryManager;
import com.corendal.netapps.framework.core.interfaces.ContentManager;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.FormLabelWriter;
import com.corendal.netapps.framework.core.interfaces.LocaleManager;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.PortletOrServletLogicContext;
import com.corendal.netapps.framework.core.interfaces.StandardAccount;
import com.corendal.netapps.framework.core.interfaces.StandardAccountFactory;
import com.corendal.netapps.framework.core.interfaces.StandardEntity;
import com.corendal.netapps.framework.core.interfaces.StandardEntityFactory;
import com.corendal.netapps.framework.core.interfaces.StandardFormIcon;
import com.corendal.netapps.framework.core.interfaces.StandardFormIconFactory;
import com.corendal.netapps.framework.core.interfaces.StandardFormMessage;
import com.corendal.netapps.framework.core.interfaces.StandardFormMessageFactory;
import com.corendal.netapps.framework.core.interfaces.StandardGlossaryEntity;
import com.corendal.netapps.framework.core.interfaces.StandardGlossaryEntityFactory;
import com.corendal.netapps.framework.core.interfaces.StandardLabel;
import com.corendal.netapps.framework.core.interfaces.StandardLocale;
import com.corendal.netapps.framework.core.interfaces.StandardLookup;
import com.corendal.netapps.framework.core.interfaces.StandardLookupFactory;
import com.corendal.netapps.framework.core.interfaces.StandardMessage;
import com.corendal.netapps.framework.core.interfaces.StandardMessageFactory;
import com.corendal.netapps.framework.core.interfaces.StandardReadBean;
import com.corendal.netapps.framework.core.interfaces.StandardSearchFormButton;
import com.corendal.netapps.framework.core.interfaces.StandardSearchFormButtonFactory;
import com.corendal.netapps.framework.core.interfaces.StandardSearchFormField;
import com.corendal.netapps.framework.core.interfaces.StandardSearchFormFieldFactory;
import com.corendal.netapps.framework.core.interfaces.StandardSearchFormLabel;
import com.corendal.netapps.framework.core.interfaces.StandardSearchFormLabelFactory;
import com.corendal.netapps.framework.core.interfaces.StandardSimpleAccount;
import com.corendal.netapps.framework.core.interfaces.StandardWriteBeanFactory;
import com.corendal.netapps.framework.core.interfaces.WriterSet;
import com.corendal.netapps.framework.core.managers.DefaultAccountHistoryManager;
import com.corendal.netapps.framework.core.managers.DefaultContentManager;
import com.corendal.netapps.framework.core.managers.DefaultLocaleManager;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardEntityFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardLookupFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardMessageFactory;
import com.corendal.netapps.framework.core.utils.BlockUtil;
import com.corendal.netapps.framework.core.utils.ConcatUtil;
import com.corendal.netapps.framework.core.utils.DateFormatUtil;
import com.corendal.netapps.framework.core.utils.DateUtil;
import com.corendal.netapps.framework.core.utils.HTTPUtil;
import com.corendal.netapps.framework.core.utils.LocationUtil;
import com.corendal.netapps.framework.core.utils.SoundexUtil;
import com.corendal.netapps.framework.core.writers.DefaultFormLabelWriter;
import com.corendal.netapps.framework.core.writestandardfactories.DefaultStandardAccountFactory;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.dictionaries.ArticlesDictionary;
import com.corendal.netapps.wiki.dictionaries.BlocksDictionary;
import com.corendal.netapps.wiki.dictionaries.ButtonsDictionary;
import com.corendal.netapps.wiki.dictionaries.EntitiesDictionary;
import com.corendal.netapps.wiki.dictionaries.FieldsDictionary;
import com.corendal.netapps.wiki.dictionaries.MessagesDictionary;
import com.corendal.netapps.wiki.interfaces.ContentHistoryManager;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.interfaces.StandardArticleFactory;
import com.corendal.netapps.wiki.interfaces.StandardContentFactory;
import com.corendal.netapps.wiki.managers.DefaultContentHistoryManager;
import com.corendal.netapps.wiki.utils.ContentUtil;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentFactory;

/**
 * AbstractContentsAdvancedSearchBlock is the parent block common to all
 * advanced content searching blocks.
 * 
 * @version $Id: AbstractContentsAdvancedSearchBlock.java,v 1.1 2005/09/06
 *          21:25:27 tdanard Exp $
 */
public abstract class AbstractContentsAdvancedSearchBlock extends
        AbstractStandardSearchFormBlock implements Cloneable {
    /** Require all keywords entered by the user */
    private String requireAllKeywords;

    /** Keyword entered by the user */
    private String keyword;

    /** Created from entered by the user */
    private String createdDateFromS;

    /** Created to entered by the user */
    private String createdDateToS;

    /** Modified from entered by the user */
    private String modifiedDateFromS;

    /** Modified to entered by the user */
    private String modifiedDateToS;

    /** Author entered by the user */
    private String author;

    /** Parent article entered by the user */
    private String parentArticle;

    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findEnabledPrimaryKeysBy methods. The creation of new instances is
     * too consuming when caching and cloning technics can be used instead.
     */
    protected AbstractContentsAdvancedSearchBlock() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractStandardSearchFormBlock.clone.
     */
    @Override
    public Object clone() {
        return (AbstractContentsAdvancedSearchBlock) super.clone();
    }

    /**
     * Sets the type of the parent article field. OVerride this method to limit
     * the articles available for searching.
     * 
     * @param contentParentArticleField
     *            a field to set the type of
     * 
     * 
     */
    protected void setContentParentArticleFieldType(
            StandardSearchFormField contentParentArticleField) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        ContentHistoryManager hm = (ContentHistoryManager) pms
                .getManager(DefaultContentHistoryManager.class);

        /*
         * initialize the names and values
         */
        ArrayList<String> contentParentArticleNames = new ArrayList<String>();
        ArrayList<String> contentParentArticleValues = new ArrayList<String>();

        /*
         * get the recent articles
         */
        List<PrimaryKey> articles = hm.getHistoryWithArticlesOnly();

        /*
         * get the home article
         */
        PrimaryKey homeArticlePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ArticlesDictionary.HOME);
        StandardArticle homeArticle = (StandardArticle) sdf
                .findByPrimaryKey(homeArticlePk);

        /*
         * add the home article
         */
        contentParentArticleValues.add("");
        contentParentArticleNames.add(homeArticle.getPathText());

        /*
         * add the recent articles
         */
        for (int i = 0; i < articles.size(); i++) {
            PrimaryKey currentArticlePk = (PrimaryKey) articles.get(i);
            StandardArticle currentArticle = (StandardArticle) sdf
                    .findByPrimaryKey(currentArticlePk);
            contentParentArticleValues.add(currentArticlePk.toString());
            contentParentArticleNames.add(currentArticle.getPathText());
        }

        /*
         * assign the names and values
         */
        contentParentArticleField.setSelectType(contentParentArticleValues,
                contentParentArticleNames);
        contentParentArticleField.setFirstDefaultValue("");
    }

    /**
     * Builds the layout of the form for an advanced search.
     * 
     * 
     * 
     */
    protected void build() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardSearchFormLabelFactory ssflf = (StandardSearchFormLabelFactory) pfs
                .getStandardBeanFactory(DefaultStandardSearchFormLabelFactory.class);
        StandardSearchFormButtonFactory ssfbf = (StandardSearchFormButtonFactory) pfs
                .getStandardBeanFactory(DefaultStandardSearchFormButtonFactory.class);
        StandardSearchFormFieldFactory ssfff = (StandardSearchFormFieldFactory) pfs
                .getStandardBeanFactory(DefaultStandardSearchFormFieldFactory.class);
        StandardEntityFactory sef = (StandardEntityFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntityFactory.class);
        StandardGlossaryEntityFactory sgef = (StandardGlossaryEntityFactory) pfs
                .getStandardBeanFactory(DefaultStandardGlossaryEntityFactory.class);
        StandardFormMessageFactory sfmf = (StandardFormMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardFormMessageFactory.class);
        StandardLookupFactory slkf = (StandardLookupFactory) pfs
                .getStandardBeanFactory(DefaultStandardLookupFactory.class);
        StandardFormIconFactory sficonf = (StandardFormIconFactory) pfs
                .getStandardBeanFactory(DefaultStandardFormIconFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        ContentManager cm = (ContentManager) pms
                .getManager(DefaultContentManager.class);
        LocaleManager lm = (LocaleManager) pms
                .getManager(DefaultLocaleManager.class);

        /*
         * get the writers used in this procedure
         */
        WriterSet pws = AnyLogicContextGlobal.get().getWriterSet();
        FormLabelWriter flw = (FormLabelWriter) pws
                .getWriter(DefaultFormLabelWriter.class);

        /*
         * get the current locale
         */
        StandardLocale currentLocale = lm.getPrimaryStandardLocale();

        /*
         * instantiate all icons
         */
        PrimaryKey spacerPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(IconsDictionary.SPACER);
        StandardFormIcon spacer = (StandardFormIcon) sficonf
                .findByPrimaryKey(spacerPk);
        spacer.setSize(4, 1);

        /*
         * set the layout type to multiple to provide a tabbing where the submit
         * button is reached before the options
         */
        this.setLayoutType(LayoutTypeConstants.BORDER);

        /*
         * set the entity of the search form
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENTS);
        StandardEntity entity = (StandardEntity) sef.findByPrimaryKey(entityPk);
        this.setStandardEntity(entity);

        /*
         * get the list of search entities
         */
        List<StandardEntity> entities = new ArrayList<StandardEntity>();
        PrimaryKey articlesEntityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);
        StandardEntity articlesEntity = (StandardEntity) sef
                .findByPrimaryKey(articlesEntityPk);
        entities.add(articlesEntity);

        PrimaryKey imagesEntityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.IMAGES);
        StandardEntity imagesEntity = (StandardEntity) sef
                .findByPrimaryKey(imagesEntityPk);
        entities.add(imagesEntity);

        /*
         * get the primary keys of the fields
         */
        PrimaryKey requireAllKeywordsFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(com.corendal.netapps.framework.core.dictionaries.FieldsDictionary.SEARCH_REQUIRE_ALL_KEYWORDS);
        PrimaryKey contentKeywordFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_KEYWORD);
        PrimaryKey contentParentArticleFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_PARENT_ARTICLE);
        PrimaryKey contentAuthorFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_AUTHOR);
        PrimaryKey contentCreatedFromFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_CREATED_FROM);
        PrimaryKey contentCreatedToFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_CREATED_TO);
        PrimaryKey contentModifiedFromFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_MODIFIED_FROM);
        PrimaryKey contentModifiedToFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_MODIFIED_TO);

        /*
         * get the primary keys of the labels
         */
        PrimaryKey requireAllKeywordsLabelPk = ssflf.getPrimaryKey(entityPk,
                requireAllKeywordsFieldPk);
        PrimaryKey contentKeywordLabelPk = ssflf.getPrimaryKey(entityPk,
                contentKeywordFieldPk);
        PrimaryKey contentParentArticleLabelPk = ssflf.getPrimaryKey(entityPk,
                contentParentArticleFieldPk);
        PrimaryKey contentAuthorLabelPk = ssflf.getPrimaryKey(entityPk,
                contentAuthorFieldPk);
        PrimaryKey contentCreatedLabelPk = ssflf.getPrimaryKey(entityPk,
                contentCreatedFromFieldPk);
        PrimaryKey contentModifiedLabelPk = ssflf.getPrimaryKey(entityPk,
                contentModifiedFromFieldPk);

        /*
         * instantiate all lookups
         */
        PrimaryKey accountLookupPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(com.corendal.netapps.framework.helpdesk.dictionaries.LookupsDictionary.ACCOUNT);
        StandardLookup accountLookup = (StandardLookup) slkf
                .findByPrimaryKey(accountLookupPk);
        PrimaryKey calendarDateLookupPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(com.corendal.netapps.framework.core.dictionaries.LookupsDictionary.CALENDAR_DATE);
        StandardLookup calendarDateLookup = (StandardLookup) slkf
                .findByPrimaryKey(calendarDateLookupPk);

        /*
         * get the search fields
         */
        StandardSearchFormField requireAllKeywordsField = (StandardSearchFormField) ssfff
                .findByPrimaryKey(requireAllKeywordsFieldPk);
        requireAllKeywordsField.setCheckBoxType();

        StandardSearchFormField contentKeywordField = (StandardSearchFormField) ssfff
                .findByPrimaryKey(contentKeywordFieldPk);
        StandardSearchFormField contentParentArticleField = (StandardSearchFormField) ssfff
                .findByPrimaryKey(contentParentArticleFieldPk);
        StandardSearchFormField contentAuthorField = (StandardSearchFormField) ssfff
                .findByPrimaryKey(contentAuthorFieldPk);
        contentAuthorField.setStandardLookup(accountLookup);

        StandardSearchFormField contentCreatedFromField = (StandardSearchFormField) ssfff
                .findByPrimaryKey(contentCreatedFromFieldPk);
        contentCreatedFromField.setStandardLookup(calendarDateLookup);

        StandardSearchFormField contentModifiedFromField = (StandardSearchFormField) ssfff
                .findByPrimaryKey(contentModifiedFromFieldPk);
        contentModifiedFromField.setStandardLookup(calendarDateLookup);

        StandardSearchFormField contentCreatedToField = (StandardSearchFormField) ssfff
                .findByPrimaryKey(contentCreatedToFieldPk);
        contentCreatedToField.setStandardLookup(calendarDateLookup);

        StandardSearchFormField contentModifiedToField = (StandardSearchFormField) ssfff
                .findByPrimaryKey(contentModifiedToFieldPk);
        contentModifiedToField.setStandardLookup(calendarDateLookup);

        /*
         * get the search labels
         */
        StandardSearchFormLabel requireAllKeywordsLabel = (StandardSearchFormLabel) ssflf
                .findByPrimaryKey(requireAllKeywordsLabelPk);
        StandardSearchFormLabel contentKeywordLabel = (StandardSearchFormLabel) ssflf
                .findByPrimaryKey(contentKeywordLabelPk);
        StandardSearchFormLabel contentParentArticleLabel = (StandardSearchFormLabel) ssflf
                .findByPrimaryKey(contentParentArticleLabelPk);
        StandardSearchFormLabel contentAuthorLabel = (StandardSearchFormLabel) ssflf
                .findByPrimaryKey(contentAuthorLabelPk);
        StandardSearchFormLabel contentCreatedLabel = (StandardSearchFormLabel) ssflf
                .findByPrimaryKey(contentCreatedLabelPk);
        StandardSearchFormLabel contentModifiedLabel = (StandardSearchFormLabel) ssflf
                .findByPrimaryKey(contentModifiedLabelPk);

        /*
         * get the "to" form message
         */
        PrimaryKey intervalToFormMessagePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(com.corendal.netapps.framework.core.dictionaries.MessagesDictionary.DSP_INTERVAL_TO);
        StandardFormMessage intervalToFormMessage1 = (StandardFormMessage) sfmf
                .findByPrimaryKey(intervalToFormMessagePk);
        StandardFormMessage intervalToFormMessage2 = (StandardFormMessage) intervalToFormMessage1
                .clone();

        /*
         * associate them
         */
        requireAllKeywordsLabel
                .associateStandardFormField(requireAllKeywordsField);
        contentKeywordLabel.associateStandardFormField(contentKeywordField);
        contentAuthorLabel.associateStandardFormField(contentAuthorField);
        contentCreatedLabel.associateStandardFormField(contentCreatedFromField);
        intervalToFormMessage1
                .associateStandardFormField(contentCreatedToField);
        contentModifiedLabel
                .associateStandardFormField(contentModifiedFromField);
        intervalToFormMessage2
                .associateStandardFormField(contentModifiedToField);
        contentParentArticleLabel
                .associateStandardFormField(contentParentArticleField);

        /*
         * get the search button
         */
        PrimaryKey contentAdvancedSearchButtonPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ButtonsDictionary.ADVANCED_SEARCH);
        StandardSearchFormButton contentAdvancedSearchButton = (StandardSearchFormButton) ssfbf
                .findByPrimaryKey(contentAdvancedSearchButtonPk);
        contentAdvancedSearchButton.setAlign(currentLocale.getRightAlign());

        /*
         * get the content type label
         */
        PrimaryKey contentTypeFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_TYPE);
        PrimaryKey contentTypeLabelPdk = ssflf.getPrimaryKey(entityPk,
                contentTypeFieldPk);
        StandardSearchFormLabel contentTypeLabel = (StandardSearchFormLabel) ssflf
                .findByPrimaryKey(contentTypeLabelPdk);

        /*
         * set the values of the parent article field
         */
        setContentParentArticleFieldType(contentParentArticleField);

        /*
         * set this label as mandatory
         */
        StandardSearchFormLabel contentTypeLabelClone = (StandardSearchFormLabel) contentTypeLabel
                .clone();
        contentTypeLabelClone.setAsMandatory();

        /*
         * get the label
         */
        String legendHTML = flw.getHTMLNoCellForLegend(contentTypeLabelClone);

        /*
         * set the max sizes of the form
         */
        this.setMaxSizes(9, 6, CanvasTypeConstants.CENTER); // 9

        // columns,
        // 6 lines
        this.setMaxSizesAndLegend(2, entities.size(),
                CanvasTypeConstants.SOUTH, legendHTML); // 2
        // columns,

        // x lines
        /*
         * add the label and the field
         */
        this.setCurrentCanvasType(CanvasTypeConstants.CENTER);
        this.addStandardFormObject(contentKeywordLabel, 1, 1);
        this.addStandardFormObject(contentKeywordField, 2, 1);
        this.addStandardFormObject(requireAllKeywordsField, 7, 1);
        this.addStandardFormObject(requireAllKeywordsLabel, 8, 1);
        this.addStandardFormObject(contentParentArticleLabel, 1, 2);
        this.addStandardFormObject(contentParentArticleField, 2, 2);
        this.addStandardFormObject(contentAuthorLabel, 1, 3);
        this.addStandardFormObject(contentAuthorField, 2, 3);
        this.addStandardFormObject(contentCreatedLabel, 1, 4);
        this.addStandardFormObject(contentCreatedFromField, 2, 4);
        this.addStandardFormObject(intervalToFormMessage1, 3, 4);
        this.addStandardFormObject(spacer, 4, 4);
        this.addStandardFormObject(contentCreatedToField, 5, 4);
        this.addStandardFormObject(contentModifiedLabel, 1, 5);
        this.addStandardFormObject(contentModifiedFromField, 2, 5);
        this.addStandardFormObject(intervalToFormMessage2, 3, 5);
        this.addStandardFormObject(spacer, 4, 5);
        this.addStandardFormObject(contentModifiedToField, 5, 5);
        this.addStandardFormObject(contentAdvancedSearchButton, 9, 3);

        /*
         * add the entity selection label
         */
        this.setCurrentCanvasType(CanvasTypeConstants.SOUTH);

        /*
         * get the entity selection check box
         */
        StandardSearchFormField contentTypeField = (StandardSearchFormField) ssfff
                .findByPrimaryKey(contentTypeFieldPk);
        contentTypeField.setCheckBoxType();

        /*
         * add the entities
         */
        for (int i = 0; i < entities.size(); i++) {
            /*
             * get the current entity
             */
            StandardEntity currentEntity = entities.get(i);

            /*
             * create the glossary entity
             */
            StandardGlossaryEntity gse = (StandardGlossaryEntity) sgef
                    .findByPrimaryKey(currentEntity.getPrimaryKey());

            /*
             * add this glossary entity to the glossary
             */
            cm.getGlossary().addTerm(gse);

            /*
             * clone the field and modify the field clone
             */
            StandardSearchFormField contentTypeFieldClone = (StandardSearchFormField) contentTypeField
                    .clone();
            contentTypeFieldClone.setOptionValueText(currentEntity
                    .getPrimaryKey().toString());

            if (i == 0) {
                contentTypeFieldClone.setFirstDefaultValue(currentEntity
                        .getPrimaryKey().toString()); // checked by default
            }

            /*
             * clone the label and modify the name of the label
             */
            contentTypeLabelClone = (StandardSearchFormLabel) contentTypeLabel
                    .clone();

            StandardLabel sl3 = contentTypeLabelClone.getStandardLabel();

            sl3.setCurrentNameText(currentEntity.getFirstUpperPluralNameText());
            sl3.setCurrentNameEncoded(currentEntity
                    .getFirstUpperPluralNameEncoded());
            sl3.setCurrentNameHTML(currentEntity.getFirstUpperPluralNameHTML());

            /*
             * build the definition of each entity
             */
            String myNameText = currentEntity.getFirstUpperSingularNameText();
            String myNameEncoded = currentEntity
                    .getFirstUpperSingularNameEncoded();
            String myNameHTML = currentEntity.getFirstUpperSingularNameHTML();

            String myShortDescriptionText = currentEntity
                    .getShortDescriptionText();
            String myShortDescriptionEncoded = currentEntity
                    .getShortDescriptionEncoded();
            String myShortDescriptionHTML = currentEntity
                    .getShortDescriptionHTML();

            String myCurrentShortDescriptionText = ConcatUtil
                    .getConcatWithColumn(myNameText, myShortDescriptionText,
                            myNameText, myShortDescriptionText);
            String myCurrentShortDescriptionEncoded = ConcatUtil
                    .getConcatWithColumn(myNameText, myShortDescriptionText,
                            myNameEncoded, myShortDescriptionEncoded);
            String myCurrentShortDescriptionHTML = ConcatUtil
                    .getConcatWithColumn(myNameText, myShortDescriptionText,
                            myNameHTML, myShortDescriptionHTML);

            /*
             * modify the description of the label
             */
            sl3.setCurrentShortDescriptionText(myCurrentShortDescriptionText);
            sl3
                    .setCurrentShortDescriptionEncoded(myCurrentShortDescriptionEncoded);
            sl3.setCurrentShortDescriptionHTML(myCurrentShortDescriptionHTML);

            /*
             * associate the field with the label
             */
            contentTypeLabelClone
                    .associateStandardFormField(contentTypeFieldClone);

            /*
             * add these two objects
             */
            this.addStandardFormObject(contentTypeFieldClone, 1, i + 1);
            this.addStandardFormObject(contentTypeLabelClone, 2, i + 1);
        }

        /*
         * extract the data entries from the form for validation
         */
        this.requireAllKeywords = requireAllKeywordsField.getRequestValue();
        this.keyword = contentKeywordField.getRequestValue();
        this.createdDateFromS = contentCreatedFromField.getRequestValue();
        this.createdDateToS = contentCreatedToField.getRequestValue();
        this.modifiedDateFromS = contentModifiedFromField.getRequestValue();
        this.modifiedDateToS = contentModifiedToField.getRequestValue();
        this.author = contentAuthorField.getRequestValue();
        this.parentArticle = contentParentArticleField.getRequestValue();
    }

    /**
     * Returns results for a factory.
     * 
     * @param currentFactory
     *            factory of the instances to find
     * @param contentPrimaryKeys
     *            list of content primary keys
     * @param isDeepKeywordSearchPerformed
     *            boolean indicating whether a deep keyword search should be
     *            performed
     * 
     * current tier object
     * @return a java.util.List object
     */
    protected List getCurrentResults(StandardWriteBeanFactory currentFactory,
            List<PrimaryKey> contentPrimaryKeys,
            boolean isDeepKeywordSearchPerformed) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardAccountFactory saf = (StandardAccountFactory) pfs
                .getStandardBeanFactory(DefaultStandardAccountFactory.class);

        /*
         * convert the strings into dates
         */
        Date createdDateFrom = DateFormatUtil
                .getParsedShortFormattedDate(this.createdDateFromS);
        Date createdDateTo = DateFormatUtil
                .getParsedShortFormattedDate(this.createdDateToS);
        Date modifiedDateFrom = DateFormatUtil
                .getParsedShortFormattedDate(this.modifiedDateFromS);
        Date modifiedDateTo = DateFormatUtil
                .getParsedShortFormattedDate(this.modifiedDateToS);

        /*
         * start the search
         */
        boolean isFirstCriterion = true;
        List<PrimaryKey> primaryKeys = new ArrayList<PrimaryKey>();

        /*
         * filter by keyword
         */
        if (isDeepKeywordSearchPerformed) {
            if (!(StringUtil.getIsEmpty(this.keyword))) {
                List<PrimaryKey> partialResults = currentFactory
                        .findEnabledPrimaryKeysByDeepKeyword(this.keyword,
                                this.requireAllKeywords.equals("Y"));

                if (isFirstCriterion) {
                    primaryKeys.addAll(partialResults);
                    isFirstCriterion = false;
                } else {
                    primaryKeys.retainAll(partialResults);
                }
            }
        } else {
            if (!(StringUtil.getIsEmpty(this.keyword))) {
                List<PrimaryKey> partialResults = currentFactory
                        .findEnabledPrimaryKeysByKeyword(this.keyword,
                                this.requireAllKeywords.equals("Y"));

                if (isFirstCriterion) {
                    primaryKeys.addAll(partialResults);
                    isFirstCriterion = false;
                } else {
                    primaryKeys.retainAll(partialResults);
                }
            }
        }

        /*
         * filter by author
         */
        if (!(StringUtil.getIsEmpty(this.author))) {
            List<PrimaryKey> partialResults = null;
            List<StandardSimpleAccount> accounts = saf
                    .findEnabledByNameOrLogin(this.author);
            StandardAccount account = (StandardAccount) accounts.get(0);

            if (currentFactory instanceof StandardArticleFactory) {
                StandardArticleFactory currentArticleFactory = (StandardArticleFactory) currentFactory;
                partialResults = currentArticleFactory
                        .findEnabledPrimaryKeysByAuthorPrimaryKey(account
                                .getPrimaryKey());
            } else {
                partialResults = new ArrayList<PrimaryKey>();
            }

            if (isFirstCriterion) {
                primaryKeys.addAll(partialResults);
                isFirstCriterion = false;
            } else {
                primaryKeys.retainAll(partialResults);
            }
        }

        /*
         * filter by created date
         */
        if ((createdDateFrom != null) && (createdDateTo != null)) {
            createdDateTo = DateUtil.getAddedDays(createdDateTo, 1);

            List<PrimaryKey> partialResults = currentFactory
                    .findEnabledPrimaryKeysByBoundCreatedDate(createdDateFrom,
                            createdDateTo);

            if (isFirstCriterion) {
                primaryKeys.addAll(partialResults);
                isFirstCriterion = false;
            } else {
                primaryKeys.retainAll(partialResults);
            }
        } else {
            if (createdDateFrom != null) {
                List<PrimaryKey> partialResults = currentFactory
                        .findEnabledPrimaryKeysByLowerBoundCreatedDate(createdDateFrom);

                if (isFirstCriterion) {
                    primaryKeys.addAll(partialResults);
                    isFirstCriterion = false;
                } else {
                    primaryKeys.retainAll(partialResults);
                }
            }

            if (createdDateTo != null) {
                createdDateTo = DateUtil.getAddedDays(createdDateTo, 1);

                List<PrimaryKey> partialResults = currentFactory
                        .findEnabledPrimaryKeysByUpperBoundCreatedDate(createdDateTo);

                if (isFirstCriterion) {
                    primaryKeys.addAll(partialResults);
                    isFirstCriterion = false;
                } else {
                    primaryKeys.retainAll(partialResults);
                }
            }
        }

        /*
         * filter by modified date
         */
        if ((modifiedDateFrom != null) && (modifiedDateTo != null)) {
            modifiedDateTo = DateUtil.getAddedDays(modifiedDateTo, 1);

            List<PrimaryKey> partialResults = currentFactory
                    .findEnabledPrimaryKeysByBoundModifiedDate(
                            modifiedDateFrom, modifiedDateTo);

            if (isFirstCriterion) {
                primaryKeys.addAll(partialResults);
                isFirstCriterion = false;
            } else {
                primaryKeys.retainAll(partialResults);
            }
        } else {
            if (modifiedDateFrom != null) {
                List<PrimaryKey> partialResults = currentFactory
                        .findEnabledPrimaryKeysByLowerBoundModifiedDate(modifiedDateFrom);

                if (isFirstCriterion) {
                    primaryKeys.addAll(partialResults);
                    isFirstCriterion = false;
                } else {
                    primaryKeys.retainAll(partialResults);
                }
            }

            if (modifiedDateTo != null) {
                modifiedDateTo = DateUtil.getAddedDays(modifiedDateTo, 1);

                List<PrimaryKey> partialResults = currentFactory
                        .findEnabledPrimaryKeysByUpperBoundModifiedDate(modifiedDateTo);

                if (isFirstCriterion) {
                    primaryKeys.addAll(partialResults);
                    isFirstCriterion = false;
                } else {
                    primaryKeys.retainAll(partialResults);
                }
            }
        }

        /*
         * filter by parent article
         */
        if (!(StringUtil.getIsEmpty(this.parentArticle))) {
            primaryKeys.retainAll(contentPrimaryKeys);
        }

        /*
         * convert the primary keys into " +
         * ds.getTableNameWithDefaultSchema("content") + "s
         */
        List currentResults = new ArrayList<StandardReadBean>();

        for (int i = 0; i < primaryKeys.size(); i++) {
            PrimaryKey currentPk = primaryKeys.get(i);
            StandardReadBean currentReadDataBean = (StandardReadBean) currentFactory
                    .findByPrimaryKey(currentPk);

            if (currentReadDataBean.getIsFound()) { // ensures the content is

                // of the correct type
                currentResults.add(currentReadDataBean);
            }
        }

        /*
         * filter out the invisible contents
         */
        return ContentUtil.getFilteredContents(currentResults);
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardFormBlock#validateExtra(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public void validateExtra() throws IOException {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardSearchFormFieldFactory ssfff = (StandardSearchFormFieldFactory) pfs
                .getStandardBeanFactory(DefaultStandardSearchFormFieldFactory.class);
        StandardMessageFactory smf = (StandardMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardMessageFactory.class);
        StandardEntityFactory sef = (StandardEntityFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntityFactory.class);
        StandardContentFactory srf = (StandardContentFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentFactory.class);
        StandardAccountFactory saf = (StandardAccountFactory) pfs
                .getStandardBeanFactory(DefaultStandardAccountFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        AccountHistoryManager hm = (AccountHistoryManager) pms
                .getManager(DefaultAccountHistoryManager.class);

        /*
         * get the content type field
         */
        PrimaryKey contentTypeFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_TYPE);
        StandardSearchFormField contentTypeField = (StandardSearchFormField) ssfff
                .findByPrimaryKey(contentTypeFieldPk);

        /*
         * get the list of search entities
         */
        List<StandardEntity> entities = new ArrayList<StandardEntity>();
        PrimaryKey articlesEntityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);
        StandardEntity articlesEntity = (StandardEntity) sef
                .findByPrimaryKey(articlesEntityPk);
        PrimaryKey imagesEntityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.IMAGES);
        StandardEntity imagesEntity = (StandardEntity) sef
                .findByPrimaryKey(imagesEntityPk);

        /*
         * populate the list of selected entities
         */
        for (String currentResourceTypeId : contentTypeField.getRequestValues()) {
            if ((currentResourceTypeId != null)
                    && (currentResourceTypeId
                            .equals(EntitiesDictionary.ARTICLES))) {
                if (!(entities.contains(articlesEntity))) {
                    entities.add(articlesEntity);
                }
            }

            if ((currentResourceTypeId != null)
                    && (currentResourceTypeId.equals(EntitiesDictionary.IMAGES))) {
                if (!(entities.contains(imagesEntity))) {
                    entities.add(imagesEntity);
                }
            }
        }

        /*
         * verify at least one resource type was selected
         */
        this.setIsValid(entities.size() > 0);

        if (!(this.getIsValid())) {
            PrimaryKey errorPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(MessagesDictionary.ERR_AT_LEAST_ONE_CONTENT_TYPE);
            StandardMessage error = (StandardMessage) smf
                    .findByPrimaryKey(errorPk);
            error.printBufferWithIconHTML();
        }

        /*
         * verify that no wildcard was used
         */
        if (this.getIsValid()) {
            int pos = this.keyword.indexOf("%");

            if (pos != -1) {
                this.setIsValid(false);

                PrimaryKey errorPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(com.corendal.netapps.framework.core.dictionaries.MessagesDictionary.ERR_WILDCARD_NOT_SUPPORTED);
                StandardMessage error = (StandardMessage) smf
                        .findByPrimaryKey(errorPk);
                error.printBufferWithIconHTML();
            }
        }

        /*
         * verify at least one criteria was entered
         */
        if (this.getIsValid()) {
            boolean isEmpty = true;

            if (!(StringUtil.getIsEmpty(this.keyword))) {
                isEmpty = false;
            }

            if (!(StringUtil.getIsEmpty(this.author))) {
                isEmpty = false;
            }

            if (!(StringUtil.getIsEmpty(this.createdDateFromS))) {
                isEmpty = false;
            }

            if (!(StringUtil.getIsEmpty(this.createdDateToS))) {
                isEmpty = false;
            }

            if (!(StringUtil.getIsEmpty(this.modifiedDateFromS))) {
                isEmpty = false;
            }

            if (!(StringUtil.getIsEmpty(this.modifiedDateToS))) {
                isEmpty = false;
            }

            this.setIsValid(!(isEmpty));

            if (isEmpty) {
                PrimaryKey errorPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(com.corendal.netapps.framework.core.dictionaries.MessagesDictionary.ERR_FORM_NOT_FILLED);
                StandardMessage error = (StandardMessage) smf
                        .findByPrimaryKey(errorPk);
                error.printBufferWithIconHTML();
            }
        }

        /*
         * check whether there is an account with that author name
         */
        List<PrimaryKey> accounts = null;

        if (this.getIsValid()) {
            if (!(StringUtil.getIsEmpty(this.author))) {
                /*
                 * check the author
                 */
                accounts = saf.findEnabledPrimaryKeysByNameOrLogin(this.author);

                /*
                 * verify only one account matches
                 */
                int size = accounts.size();

                if (size != 1) {
                    this.setIsValid(false);

                    /*
                     * if no account could be found, print the "no found"
                     * message. otherwise, print the "too many accounts found"
                     * message
                     */
                    if (size == 0) {
                        PrimaryKey errorPk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.ERR_NO_ACCOUNT_FOR_AUTHOR);
                        StandardMessage error = (StandardMessage) smf
                                .findByPrimaryKey(errorPk);
                        error.printBufferWithIconHTML();

                        PrimaryKey authorPk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_AUTHOR);
                        StandardSearchFormField field = (StandardSearchFormField) ssfff
                                .findByPrimaryKey(authorPk);
                        field.setFocusIfFirst(this.getFormName());
                    } else {
                        PrimaryKey errorPk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.ERR_TOO_MANY_ACCOUNTS_FOR_AUTHOR);
                        StandardMessage error = (StandardMessage) smf
                                .findByPrimaryKey(errorPk);
                        error.printBufferWithIconHTML();

                        PrimaryKey authorPk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_AUTHOR);
                        StandardSearchFormField field = (StandardSearchFormField) ssfff
                                .findByPrimaryKey(authorPk);
                        field.setFocusIfFirst(this.getFormName());
                    }
                } else {
                    /*
                     * add this account to the history
                     */
                    PrimaryKey accountPk = (PrimaryKey) accounts.get(0);
                    hm.add(accountPk);
                }
            }
        }

        /*
         * check the created date from
         */
        if (this.getIsValid()) {
            if (!(StringUtil.getIsEmpty(this.createdDateFromS))) {
                Date createdDateFrom = DateFormatUtil
                        .getParsedShortFormattedDate(this.createdDateFromS);
                this.setIsValid(createdDateFrom != null);

                if (createdDateFrom == null) {
                    PrimaryKey errorPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(com.corendal.netapps.framework.core.dictionaries.MessagesDictionary.ERR_DATE_FORMAT_INVALID);
                    StandardMessage error = (StandardMessage) smf
                            .findByPrimaryKey(errorPk);

                    PrimaryKey contentCreatedFromFieldPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(FieldsDictionary.CONTENT_CREATED_FROM);
                    StandardSearchFormField contentCreatedFromField = (StandardSearchFormField) ssfff
                            .findByPrimaryKey(contentCreatedFromFieldPk);

                    error.replaceMessageText(contentCreatedFromField
                            .getStandardField().getContextualNameText(), 1);
                    error.replaceMessageEncoded(contentCreatedFromField
                            .getStandardField().getContextualNameEncoded(), 1);
                    error.replaceMessageHTML(contentCreatedFromField
                            .getStandardField().getContextualNameHTML(), 1);
                    error.printBufferWithIconHTML();
                }
            }
        }

        /*
         * check the created date to
         */
        if (this.getIsValid()) {
            if (!(StringUtil.getIsEmpty(this.createdDateToS))) {
                Date createdDateTo = DateFormatUtil
                        .getParsedShortFormattedDate(this.createdDateToS);
                this.setIsValid(createdDateTo != null);

                if (createdDateTo == null) {
                    PrimaryKey errorPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(com.corendal.netapps.framework.core.dictionaries.MessagesDictionary.ERR_DATE_FORMAT_INVALID);
                    StandardMessage error = (StandardMessage) smf
                            .findByPrimaryKey(errorPk);

                    PrimaryKey contentCreatedToFieldPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(FieldsDictionary.CONTENT_CREATED_TO);
                    StandardSearchFormField contentCreatedToField = (StandardSearchFormField) ssfff
                            .findByPrimaryKey(contentCreatedToFieldPk);

                    error.replaceMessageText(contentCreatedToField
                            .getStandardField().getContextualNameText(), 1);
                    error.replaceMessageEncoded(contentCreatedToField
                            .getStandardField().getContextualNameEncoded(), 1);
                    error.replaceMessageHTML(contentCreatedToField
                            .getStandardField().getContextualNameHTML(), 1);
                    error.printBufferWithIconHTML();
                }
            }
        }

        /*
         * check the modified date from
         */
        if (this.getIsValid()) {
            if (!(StringUtil.getIsEmpty(this.modifiedDateFromS))) {
                Date modifiedDateFrom = DateFormatUtil
                        .getParsedShortFormattedDate(this.modifiedDateFromS);
                this.setIsValid(modifiedDateFrom != null);

                if (modifiedDateFrom == null) {
                    PrimaryKey errorPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(com.corendal.netapps.framework.core.dictionaries.MessagesDictionary.ERR_DATE_FORMAT_INVALID);
                    StandardMessage error = (StandardMessage) smf
                            .findByPrimaryKey(errorPk);

                    PrimaryKey contentModifiedFromFieldPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(FieldsDictionary.CONTENT_MODIFIED_FROM);
                    StandardSearchFormField contentModifiedFromField = (StandardSearchFormField) ssfff
                            .findByPrimaryKey(contentModifiedFromFieldPk);

                    error.replaceMessageText(contentModifiedFromField
                            .getStandardField().getContextualNameText(), 1);
                    error.replaceMessageEncoded(contentModifiedFromField
                            .getStandardField().getContextualNameEncoded(), 1);
                    error.replaceMessageHTML(contentModifiedFromField
                            .getStandardField().getContextualNameHTML(), 1);
                    error.printBufferWithIconHTML();
                }
            }
        }

        /*
         * check the modified date to
         */
        if (this.getIsValid()) {
            if (!(StringUtil.getIsEmpty(this.modifiedDateToS))) {
                Date modifiedDateTo = DateFormatUtil
                        .getParsedShortFormattedDate(this.modifiedDateToS);
                this.setIsValid(modifiedDateTo != null);

                if (modifiedDateTo == null) {
                    PrimaryKey errorPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(com.corendal.netapps.framework.core.dictionaries.MessagesDictionary.ERR_DATE_FORMAT_INVALID);
                    StandardMessage error = (StandardMessage) smf
                            .findByPrimaryKey(errorPk);

                    PrimaryKey contentModifiedToFieldPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(FieldsDictionary.CONTENT_MODIFIED_TO);
                    StandardSearchFormField contentModifiedToField = (StandardSearchFormField) ssfff
                            .findByPrimaryKey(contentModifiedToFieldPk);

                    error.replaceMessageText(contentModifiedToField
                            .getStandardField().getContextualNameText(), 1);
                    error.replaceMessageEncoded(contentModifiedToField
                            .getStandardField().getContextualNameEncoded(), 1);
                    error.replaceMessageHTML(contentModifiedToField
                            .getStandardField().getContextualNameHTML(), 1);
                    error.printBufferWithIconHTML();
                }
            }
        }

        /*
         * check the created date interval
         */
        if (this.getIsValid()) {
            if (!(StringUtil.getIsEmpty(this.createdDateFromS))) {
                if (!(StringUtil.getIsEmpty(this.createdDateToS))) {
                    Date createdDateFrom = DateFormatUtil
                            .getParsedShortFormattedDate(this.createdDateFromS);
                    Date createdDateTo = DateFormatUtil
                            .getParsedShortFormattedDate(this.createdDateToS);
                    this
                            .setIsValid(createdDateFrom
                                    .compareTo(createdDateTo) <= 0);

                    if (createdDateFrom.compareTo(createdDateTo) > 0) {
                        PrimaryKey errorPk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(com.corendal.netapps.framework.core.dictionaries.MessagesDictionary.ERR_DATE_INTERVAL_INVALID);
                        StandardMessage error = (StandardMessage) smf
                                .findByPrimaryKey(errorPk);

                        PrimaryKey contentCreatedToFieldPk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_CREATED);
                        StandardSearchFormField contentCreatedToField = (StandardSearchFormField) ssfff
                                .findByPrimaryKey(contentCreatedToFieldPk);

                        error.replaceMessageText(contentCreatedToField
                                .getStandardField().getContextualNameText(), 1);
                        error.replaceMessageEncoded(contentCreatedToField
                                .getStandardField().getContextualNameEncoded(),
                                1);
                        error.replaceMessageHTML(contentCreatedToField
                                .getStandardField().getContextualNameHTML(), 1);
                        error.printBufferWithIconHTML();
                    }
                }
            }
        }

        /*
         * check the modified date interval
         */
        if (this.getIsValid()) {
            if (!(StringUtil.getIsEmpty(this.modifiedDateFromS))) {
                if (!(StringUtil.getIsEmpty(this.modifiedDateToS))) {
                    Date modifiedDateFrom = DateFormatUtil
                            .getParsedShortFormattedDate(this.modifiedDateFromS);
                    Date modifiedDateTo = DateFormatUtil
                            .getParsedShortFormattedDate(this.modifiedDateToS);
                    this
                            .setIsValid(modifiedDateFrom
                                    .compareTo(modifiedDateTo) <= 0);

                    if (modifiedDateFrom.compareTo(modifiedDateTo) > 0) {
                        PrimaryKey errorPk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(com.corendal.netapps.framework.core.dictionaries.MessagesDictionary.ERR_DATE_INTERVAL_INVALID);
                        StandardMessage error = (StandardMessage) smf
                                .findByPrimaryKey(errorPk);

                        PrimaryKey contentModifiedToFieldPk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_MODIFIED);
                        StandardSearchFormField contentModifiedToField = (StandardSearchFormField) ssfff
                                .findByPrimaryKey(contentModifiedToFieldPk);

                        error.replaceMessageText(contentModifiedToField
                                .getStandardField().getContextualNameText(), 1);
                        error.replaceMessageEncoded(contentModifiedToField
                                .getStandardField().getContextualNameEncoded(),
                                1);
                        error.replaceMessageHTML(contentModifiedToField
                                .getStandardField().getContextualNameHTML(), 1);
                        error.printBufferWithIconHTML();
                    }
                }
            }
        }

        /*
         * get the list of contents in the selected article
         */
        List<PrimaryKey> contentPrimaryKeys = null;

        if (this.getIsValid()) {
            if (!(StringUtil.getIsEmpty(this.parentArticle))) {
                PrimaryKey parentArticlePk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(this.parentArticle);
                contentPrimaryKeys = srf
                        .findRecursiveEnabledPrimaryKeysByParentArticlePrimaryKey(parentArticlePk);
            }
        }

        /*
         * initialize the result for results
         */
        int simpleResultsSize = 0;
        int additionalResultsSize = 0;
        ArrayList<List> simpleResults = new ArrayList<List>();
        ArrayList<List> additionalResults = new ArrayList<List>();

        /*
         * build the intermediary list of simpleResults
         */
        if (this.getIsValid()) {
            /*
             * build the list of simple results for each entity
             */
            for (int i = 0; i < entities.size(); i++) {
                /*
                 * get the current entity
                 */
                StandardEntity currentEntity = entities.get(i);

                /*
                 * clone the content type field
                 */
                StandardSearchFormField contentTypeFieldClone = (StandardSearchFormField) contentTypeField
                        .clone();
                contentTypeFieldClone.setOptionValueText(currentEntity
                        .getPrimaryKey().toString());

                /*
                 * verify the entity was selected and that a factory is
                 * associated with this entity
                 */
                if ((contentTypeFieldClone.getRequestValue()
                        .equals(currentEntity.getPrimaryKey().toString()))
                        && (currentEntity.getFactoryClassName() != null)) {
                    /*
                     * find the factory for this entity
                     */
                    StandardWriteBeanFactory currentFactory = (StandardWriteBeanFactory) pfs
                            .getStandardBeanFactory(currentEntity
                                    .getFactoryClassName());

                    /*
                     * build the list of simpleResults for this entity and add
                     * this list to the final list
                     */
                    if (currentFactory != null) {
                        List currentResults = this.getCurrentResults(
                                currentFactory, contentPrimaryKeys, false);
                        simpleResults.add(currentResults);
                        simpleResultsSize = simpleResultsSize
                                + currentResults.size();
                    }
                }
            }
        }

        /*
         * build the intermediary list of additional results when a keyword is
         * entered
         */
        if ((this.getIsValid()) && (!(StringUtil.getIsEmpty(this.keyword)))) {
            /*
             * build the list of additional results for each entity
             */
            for (int i = 0; i < entities.size(); i++) {
                /*
                 * get the current entity
                 */
                StandardEntity currentEntity = entities.get(i);

                /*
                 * clone the content type field
                 */
                StandardSearchFormField contentTypeFieldClone = (StandardSearchFormField) contentTypeField
                        .clone();
                contentTypeFieldClone.setOptionValueText(currentEntity
                        .getPrimaryKey().toString());

                /*
                 * verify the entity was selected and that a factory is
                 * associated with this entity
                 */
                if ((contentTypeFieldClone.getRequestValue()
                        .equals(currentEntity.getPrimaryKey().toString()))
                        && (currentEntity.getFactoryClassName() != null)) {
                    /*
                     * find the factory for this entity
                     */
                    StandardWriteBeanFactory currentFactory = (StandardWriteBeanFactory) pfs
                            .getStandardBeanFactory(currentEntity
                                    .getFactoryClassName());

                    /*
                     * build the list of additional results for this entity and
                     * add this list to the final list
                     */
                    if (currentFactory != null) {
                        List currentResults = this.getCurrentResults(
                                currentFactory, contentPrimaryKeys, true);
                        additionalResults.add(currentResults);
                        additionalResultsSize = additionalResultsSize
                                + currentResults.size();
                    }
                }
            }
        }

        /*
         * associate these lists to this form block
         */
        this.setResults(simpleResults, simpleResultsSize);
        this.setAdditionalResults(additionalResults, additionalResultsSize);

        /*
         * analyse the keyword
         */
        if (this.getIsValid()) {
            if (!(StringUtil.getIsEmpty(this.keyword))) {
                SoundexUtil.analyseKeyword(this, this.keyword,
                        simpleResultsSize + additionalResultsSize);
            }
        }

        /*
         * redirect in case of full text results matching
         */
        if ((simpleResultsSize == 0) && (additionalResultsSize > 0)) {
            PrimaryKey resultsPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(BlocksDictionary.CONTENT_ADVANCED_RESULTS);
            PrimaryKey moreResultsPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(BlocksDictionary.CONTENT_ADVANCED_MORE_RESULTS);
            List<PrimaryKey> currentSelectedBlockPrimaryKeys = BlockUtil
                    .getCurrentSelectedBlockPrimaryKeys();
            if ((!(currentSelectedBlockPrimaryKeys.contains(resultsPk)))
                    && (!(currentSelectedBlockPrimaryKeys
                            .contains(moreResultsPk)))) {

                /*
                 * redirect to the current
                 */
                String redirectURL = LocationUtil.getCurrentAbsoluteLocation();

                /*
                 * add the full text results block id
                 */
                redirectURL = HTTPUtil.getAddedParameterURL(redirectURL,
                        HTTPParameterConstants.SELECTED_BLOCK_ID,
                        BlocksDictionary.CONTENT_ADVANCED_MORE_RESULTS);

                /*
                 * do the redirect
                 */
                ((PortletOrServletLogicContext) AnyLogicContextGlobal.get())
                        .commitAndSendAbsoluteRedirect(redirectURL);
            }
        }
    }
}

// end AbstractContentsAdvancedSearchBlock

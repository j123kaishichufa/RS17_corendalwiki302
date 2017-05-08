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
import java.util.List;

import com.corendal.netapps.framework.configuration.utils.CaseUtil;
import com.corendal.netapps.framework.core.blocks.AbstractStandardEntryFormBlock;
import com.corendal.netapps.framework.core.constants.AccessTypeConstants;
import com.corendal.netapps.framework.core.constants.CanvasTypeConstants;
import com.corendal.netapps.framework.core.constants.HTTPParameterConstants;
import com.corendal.netapps.framework.core.constants.LayoutTypeConstants;
import com.corendal.netapps.framework.core.formfactories.DefaultStandardEntryFormButtonFactory;
import com.corendal.netapps.framework.core.formfactories.DefaultStandardEntryFormFieldFactory;
import com.corendal.netapps.framework.core.formfactories.DefaultStandardEntryFormLabelFactory;
import com.corendal.netapps.framework.core.formfactories.DefaultStandardFormMessageFactory;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.AccessManager;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.LinkWriter;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.PortletOrServletLogicContext;
import com.corendal.netapps.framework.core.interfaces.StandardAccount;
import com.corendal.netapps.framework.core.interfaces.StandardEntity;
import com.corendal.netapps.framework.core.interfaces.StandardEntityFactory;
import com.corendal.netapps.framework.core.interfaces.StandardEntryFormButton;
import com.corendal.netapps.framework.core.interfaces.StandardEntryFormButtonFactory;
import com.corendal.netapps.framework.core.interfaces.StandardEntryFormField;
import com.corendal.netapps.framework.core.interfaces.StandardEntryFormFieldFactory;
import com.corendal.netapps.framework.core.interfaces.StandardEntryFormLabel;
import com.corendal.netapps.framework.core.interfaces.StandardEntryFormLabelFactory;
import com.corendal.netapps.framework.core.interfaces.StandardFormMessage;
import com.corendal.netapps.framework.core.interfaces.StandardFormMessageFactory;
import com.corendal.netapps.framework.core.interfaces.StandardLookup;
import com.corendal.netapps.framework.core.interfaces.StandardLookupFactory;
import com.corendal.netapps.framework.core.interfaces.StandardMessage;
import com.corendal.netapps.framework.core.interfaces.StandardMessageFactory;
import com.corendal.netapps.framework.core.interfaces.StandardPage;
import com.corendal.netapps.framework.core.interfaces.StandardPageFactory;
import com.corendal.netapps.framework.core.interfaces.WriterSet;
import com.corendal.netapps.framework.core.managers.DefaultAccessManager;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardEntityFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardLookupFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardMessageFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardPageFactory;
import com.corendal.netapps.framework.core.utils.ConcatUtil;
import com.corendal.netapps.framework.core.utils.HTTPUtil;
import com.corendal.netapps.framework.core.utils.MessageUtil;
import com.corendal.netapps.framework.core.writers.DefaultLinkWriter;
import com.corendal.netapps.framework.helpdesk.dictionaries.LookupsDictionary;
import com.corendal.netapps.framework.helpdesk.interfaces.GroupHistoryManager;
import com.corendal.netapps.framework.helpdesk.interfaces.StandardGroup;
import com.corendal.netapps.framework.helpdesk.interfaces.StandardGroupFactory;
import com.corendal.netapps.framework.helpdesk.managers.DefaultGroupHistoryManager;
import com.corendal.netapps.framework.helpdesk.writestandardfactories.DefaultStandardGroupFactory;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.dictionaries.BlocksDictionary;
import com.corendal.netapps.wiki.dictionaries.ButtonsDictionary;
import com.corendal.netapps.wiki.dictionaries.EntitiesDictionary;
import com.corendal.netapps.wiki.dictionaries.FieldsDictionary;
import com.corendal.netapps.wiki.dictionaries.MessagesDictionary;
import com.corendal.netapps.wiki.dictionaries.PagesDictionary;
import com.corendal.netapps.wiki.dictionaries.RolesDictionary;
import com.corendal.netapps.wiki.interfaces.ContentManager;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.interfaces.StandardArticleFactory;
import com.corendal.netapps.wiki.interfaces.StandardContent;
import com.corendal.netapps.wiki.interfaces.StandardContentFactory;
import com.corendal.netapps.wiki.interfaces.StandardImage;
import com.corendal.netapps.wiki.managers.DefaultContentManager;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentFactory;

/**
 * AbstractImageAccessGroupEntryBlock is the parent block common to all image
 * viewing blocks.
 * 
 * @version $Id: AbstractImageAccessGroupEntryBlock.java,v 1.1 2005/09/06
 *          21:25:28 tdanard Exp $
 */
public abstract class AbstractImageAccessGroupEntryBlock extends
        AbstractStandardEntryFormBlock implements Cloneable {
    /** Fully qualified name of this class. */
    private static final String ABSTRACT_CLASS_NAME = "com.corendal.netapps.wiki.blocks.AbstractImageAccessGroupEntryBlock";

    /** Group name entered by the user */
    private String groupName;

    /** Deny access entered by the user */
    private String denyAccess;

    /** Image to be viewed */
    private StandardImage image;

    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractImageAccessGroupEntryBlock() {
        this.image = null;
    }

    /**
     * Returns a clone. Overrides AbstractStandardEntryFormBlock.clone.
     */
    @Override
    public Object clone() {
        return (AbstractImageAccessGroupEntryBlock) super.clone();
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
        PrimaryKey imagesPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.IMAGES);
        StandardEntity entity = (StandardEntity) sef.findByPrimaryKey(imagesPk);
        this.setStandardEntity(entity);
    }

    /**
     * Builds the layout of the form. This procedure is used when creating a
     * request.
     * 
     * 
     * 
     */
    private void build() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardEntryFormLabelFactory seflf = (StandardEntryFormLabelFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntryFormLabelFactory.class);
        StandardEntryFormButtonFactory sefbf = (StandardEntryFormButtonFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntryFormButtonFactory.class);
        StandardEntryFormFieldFactory sefff = (StandardEntryFormFieldFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntryFormFieldFactory.class);
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);
        StandardLookupFactory slkf = (StandardLookupFactory) pfs
                .getStandardBeanFactory(DefaultStandardLookupFactory.class);
        StandardGroupFactory sgf = (StandardGroupFactory) pfs
                .getStandardBeanFactory(DefaultStandardGroupFactory.class);
        StandardMessageFactory smf = (StandardMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardMessageFactory.class);
        StandardPageFactory spf = (StandardPageFactory) pfs
                .getStandardBeanFactory(DefaultStandardPageFactory.class);

        /*
         * get the writers used in this procedure
         */
        WriterSet pws = AnyLogicContextGlobal.get().getWriterSet();
        LinkWriter lw = (LinkWriter) pws.getWriter(DefaultLinkWriter.class);

        /*
         * get the entity of the form
         */
        StandardEntity entity = this.getStandardEntity();

        /*
         * find the parent article
         */
        PrimaryKey imagePk = this.image.getPrimaryKey();
        PrimaryKey parentArticlePk = this.image.getMainParentPrimaryKey();
        StandardArticle parentArticle = (StandardArticle) sdf
                .findByPrimaryKey(parentArticlePk);

        /*
         * set the layout type to multiple to provide a tabbing where the submit
         * button is the last object to reach
         */
        this.setLayoutType(LayoutTypeConstants.BORDER);

        /*
         * set the max sizes of the form
         */
        this.setMaxSizes(2, 4, CanvasTypeConstants.CENTER); // 3

        // columns,
        // 4 lines
        this.setMaxSizes(3, 1, CanvasTypeConstants.SOUTH); // 3

        // columns,
        // 1 line

        /*
         * get the primary keys of the required instances
         */
        PrimaryKey entityPk = entity.getPrimaryKey();
        PrimaryKey nameFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_NAME);
        PrimaryKey descriptionFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_DESCRIPTION);
        PrimaryKey groupNameFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(com.corendal.netapps.framework.helpdesk.dictionaries.FieldsDictionary.GROUP_NAME);
        PrimaryKey parentArticleFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_PARENT_ARTICLE);
        PrimaryKey denyAccessFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_DENY_ACCESS);

        PrimaryKey nameLabelPk = seflf.getPrimaryKey(entityPk, nameFieldPk);
        PrimaryKey descriptionLabelPk = seflf.getPrimaryKey(entityPk,
                descriptionFieldPk);
        PrimaryKey groupNameLabelPk = seflf.getPrimaryKey(entityPk,
                groupNameFieldPk);
        PrimaryKey parentArticleLabelPk = seflf.getPrimaryKey(entityPk,
                parentArticleFieldPk);
        PrimaryKey denyAccessLabelPk = seflf.getPrimaryKey(entityPk,
                denyAccessFieldPk);

        /*
         * instantiate all lookups
         */
        PrimaryKey groupLookupPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(LookupsDictionary.GROUP);
        StandardLookup groupLookup = (StandardLookup) slkf
                .findByPrimaryKey(groupLookupPk);

        /*
         * add the "create group" message to the lookup
         */
        PrimaryKey newGroupMessagePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(MessagesDictionary.DSP_NEW_GROUP);
        StandardMessage newGroupMessage = (StandardMessage) smf
                .findByPrimaryKey(newGroupMessagePk);

        PrimaryKey addNewGroupPagePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(PagesDictionary.ADD_NEW_GROUP);
        StandardPage addNewGroupPage = (StandardPage) spf
                .findByPrimaryKey(addNewGroupPagePk);
        String addNewGroupLocation = addNewGroupPage.getDefaultLocation();

        groupLookup.setAdditionalHTML(lw.getBreakableLinkHTML(
                addNewGroupLocation, null, newGroupMessage
                        .getCurrentMessageHTML(), null));

        /*
         * instantiate all entry fields
         */
        StandardEntryFormField nameField = (StandardEntryFormField) sefff
                .findByPrimaryKey(nameFieldPk);
        StandardEntryFormField descriptionField = (StandardEntryFormField) sefff
                .findByPrimaryKey(descriptionFieldPk);
        StandardEntryFormField groupNameField = (StandardEntryFormField) sefff
                .findByPrimaryKey(groupNameFieldPk);
        StandardEntryFormField parentArticleField = (StandardEntryFormField) sefff
                .findByPrimaryKey(parentArticleFieldPk);
        StandardEntryFormField denyAccessField = (StandardEntryFormField) sefff
                .findByPrimaryKey(denyAccessFieldPk);
        denyAccessField.setCheckBoxType();

        if (this.getAccessType().equals(AccessTypeConstants.NEW)) {
            groupNameField.setStandardLookup(groupLookup);
            groupNameField.setIsWildcardSubstituted(true);
        } else {
            /*
             * find the access messages
             */
            PrimaryKey allowedPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(MessagesDictionary.DSP_ALLOW);
            PrimaryKey deniedPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(MessagesDictionary.DSP_DENY);
            StandardMessage allowed = (StandardMessage) smf
                    .findByPrimaryKey(allowedPk);
            StandardMessage denied = (StandardMessage) smf
                    .findByPrimaryKey(deniedPk);
            String allowedText = CaseUtil.getLowerCaseDeleteAccents(allowed
                    .getCurrentMessageText());
            String deniedText = CaseUtil.getLowerCaseDeleteAccents(denied
                    .getCurrentMessageText());

            /*
             * find all groups
             */
            PrimaryKey allowedAccessGroupRolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.ACCESS_GROUP_ALLOWED);
            PrimaryKey deniedAccessGroupRolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.ACCESS_GROUP_DENIED);
            List<StandardGroup> allowedGroups = sgf
                    .findEnabledByRecordAndRolePrimaryKeys(imagePk,
                            allowedAccessGroupRolePk);
            List<StandardGroup> deniedGroups = sgf
                    .findEnabledByRecordAndRolePrimaryKeys(imagePk,
                            deniedAccessGroupRolePk);
            ArrayList<String> names = new ArrayList<String>();
            ArrayList<String> values = new ArrayList<String>();

            for (StandardGroup group : allowedGroups) {
                if ((group != null) && (group.getIsFound())) {
                    String nameText = group.getNameText();
                    names.add(nameText);
                    nameText = ConcatUtil.getConcatWithBrackets(nameText,
                            allowedText, nameText, allowedText);

                    values.add(nameText);
                }
            }

            for (StandardGroup group : deniedGroups) {
                if ((group != null) && (group.getIsFound())) {
                    String nameText = group.getNameText();
                    names.add(nameText);
                    nameText = ConcatUtil.getConcatWithBrackets(nameText,
                            deniedText, nameText, deniedText);

                    values.add(nameText);
                }
            }

            groupNameField.setSelectType(names, values);
        }

        /*
         * instantiate all entry labels
         */
        StandardEntryFormLabel nameLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(nameLabelPk);
        StandardEntryFormLabel descriptionLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(descriptionLabelPk);
        StandardEntryFormLabel groupNameLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(groupNameLabelPk);
        StandardEntryFormLabel parentArticleLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(parentArticleLabelPk);
        StandardEntryFormLabel denyAccessLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(denyAccessLabelPk);

        /*
         * associate them
         */
        nameLabel.associateStandardFormField(nameField);
        descriptionLabel.associateStandardFormField(descriptionField);
        groupNameLabel.associateStandardFormField(groupNameField);
        parentArticleLabel.associateStandardFormField(parentArticleField);
        denyAccessLabel.associateStandardFormField(denyAccessField);

        /*
         * set the mandatory objects
         */
        groupNameLabel.setAsMandatory();
        groupNameField.setAsMandatory();

        /*
         * set the appropriate fields in print only mode
         */
        nameField.setPrintOnlyType();
        descriptionField.setPrintOnlyType();
        parentArticleField.setPrintOnlyType();

        /*
         * fill with default values
         */
        nameField.setFirstDefaultValue(this.image.getNameText());
        descriptionField.setFirstDefaultValue(this.image
                .getLongDescriptionText());
        groupNameField.setFirstDefaultValue(this.groupName);
        parentArticleField.setFirstDefaultValue(parentArticle.getPathText());
        parentArticleField.setFirstDefaultValueHTML(parentArticle
                .getPathHTMLWithLink());

        /*
         * set the current line number
         */
        int lineNumber = 0;

        /*
         * add those objects
         */
        this.setCurrentCanvasType(CanvasTypeConstants.CENTER);
        this.addStandardFormObject(nameLabel, 1, ++lineNumber);
        this.addStandardFormObject(nameField, 2, lineNumber);
        this.addStandardFormObject(descriptionLabel, 1, ++lineNumber);
        this.addStandardFormObject(descriptionField, 2, lineNumber);
        this.addStandardFormObject(parentArticleLabel, 1, ++lineNumber);
        this.addStandardFormObject(parentArticleField, 2, lineNumber);
        this.addStandardFormObject(groupNameLabel, 1, ++lineNumber);
        this.addStandardFormObject(groupNameField, 2, lineNumber);

        /*
         * instantiate and add the submit button
         */
        this.setCurrentCanvasType(CanvasTypeConstants.SOUTH);

        if (this.getAccessType().equals(AccessTypeConstants.NEW)) {
            PrimaryKey addAccessGroupPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(ButtonsDictionary.ADD_ACCESS_GROUP);
            StandardEntryFormButton addAccessGroup = (StandardEntryFormButton) sefbf
                    .findByPrimaryKey(addAccessGroupPk);
            this.addStandardFormObject(addAccessGroup, 1, 1);
            this.addStandardFormObject(denyAccessField, 2, 1);
            this.addStandardFormObject(denyAccessLabel, 3, 1);
        } else {
            PrimaryKey removeAccessGroupPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(ButtonsDictionary.REMOVE_ACCESS_GROUP);
            StandardEntryFormButton removeAccessGroup = (StandardEntryFormButton) sefbf
                    .findByPrimaryKey(removeAccessGroupPk);
            this.addStandardFormObject(removeAccessGroup, 1, 1);
        }

        /*
         * extract the data entries from the form for validation
         */
        this.groupName = groupNameField.getRequestValue();
        this.denyAccess = denyAccessField.getRequestValue();
    }

    /**
     * Builds the layout of the form for a new record.
     * 
     * 
     * 
     */
    protected void buildNewRecord() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardFormMessageFactory sfmf = (StandardFormMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardFormMessageFactory.class);
        StandardEntityFactory sef = (StandardEntityFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntityFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        AccessManager am = (AccessManager) pms
                .getManager(DefaultAccessManager.class);
        ContentManager resm = (ContentManager) pms
                .getManager(DefaultContentManager.class);

        /*
         * get the current account
         */
        StandardAccount sa = am.getProxyStandardAccount();

        /*
         * verify that the image exists
         */
        if ((this.image.getIsFound()) && (this.image.getIsVisible())) {
            /*
             * find the parent article primary key
             */
            PrimaryKey parentArticlePk = this.image.getMainParentPrimaryKey();

            /*
             * print the form
             */
            if (resm.getIsRequestRequirementWaived(sa.getPrimaryKey(),
                    parentArticlePk)) {
                this.build();
            } else {
                this.setMaxSizes(1, 1, CanvasTypeConstants.CENTER); // 1

                // column,
                // 1
                // line
                PrimaryKey noImageAccessGroupEditingPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.ERR_NO_IMAGE_ACCESS_GROUP_EDITING);
                StandardFormMessage noImageAccessGroupEditingMessage = (StandardFormMessage) sfmf
                        .findByPrimaryKey(noImageAccessGroupEditingPk);
                this.addStandardFormObject(noImageAccessGroupEditingMessage, 1,
                        1);
            }
        } else {
            PrimaryKey contentsPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntitiesDictionary.CONTENTS);
            StandardEntity entity = (StandardEntity) sef
                    .findByPrimaryKey(contentsPk);
            this.setStandardEntity(entity);
            this.buildNoRecordFound();
        }
    }

    /**
     * Builds the layout of the form for a record to remove.
     * 
     * 
     * 
     */
    protected void buildRemoveRecord() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardFormMessageFactory sfmf = (StandardFormMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardFormMessageFactory.class);
        StandardGroupFactory sgf = (StandardGroupFactory) pfs
                .getStandardBeanFactory(DefaultStandardGroupFactory.class);
        StandardEntityFactory sef = (StandardEntityFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntityFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        AccessManager am = (AccessManager) pms
                .getManager(DefaultAccessManager.class);
        ContentManager resm = (ContentManager) pms
                .getManager(DefaultContentManager.class);

        /*
         * get the current account
         */
        StandardAccount sa = am.getProxyStandardAccount();

        /*
         * verify that the image exists
         */
        PrimaryKey imagePk = this.image.getPrimaryKey();

        if ((this.image.getIsFound()) && (this.image.getIsVisible())) {
            /*
             * find the parent article primary key
             */
            PrimaryKey parentArticlePk = this.image.getMainParentPrimaryKey();

            /*
             * get the list of access groups
             */
            PrimaryKey allowedAccessGroupRolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.ACCESS_GROUP_ALLOWED);
            List<StandardGroup> groups = sgf
                    .findEnabledByRecordAndRolePrimaryKeys(imagePk,
                            allowedAccessGroupRolePk);
            PrimaryKey deniedAccessGroupRolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.ACCESS_GROUP_DENIED);
            List<StandardGroup> deniedGroups = sgf
                    .findEnabledByRecordAndRolePrimaryKeys(imagePk,
                            deniedAccessGroupRolePk);
            groups.addAll(deniedGroups);

            /*
             * print the form
             */
            if (resm.getIsRequestRequirementWaived(sa.getPrimaryKey(),
                    parentArticlePk)) {
                /*
                 * verify that the image has been added to
                 */
                if (groups.size() > 0) {
                    this.build();
                } else {
                    this.setMaxSizes(1, 1, CanvasTypeConstants.CENTER); // 1

                    // column,
                    // 1
                    // line
                    PrimaryKey noImageAccessGroupPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.ERR_NO_IMAGE_ACCESS_GROUP);
                    StandardFormMessage noImageAccessGroupMessage = (StandardFormMessage) sfmf
                            .findByPrimaryKey(noImageAccessGroupPk);
                    StandardMessage imageMessage = noImageAccessGroupMessage
                            .getStandardMessage();
                    imageMessage
                            .replaceMessageText(this.image.getNameText(), 1);
                    imageMessage.replaceMessageEncoded(this.image
                            .getNameEncoded(), 1);
                    imageMessage
                            .replaceMessageHTML(this.image.getNameHTML(), 1);
                    this.addStandardFormObject(noImageAccessGroupMessage, 1, 1);
                }
            } else {
                this.setMaxSizes(1, 1, CanvasTypeConstants.CENTER); // 1

                // column,
                // 1
                // line
                PrimaryKey noImageAccessGroupEditingPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.ERR_NO_IMAGE_ACCESS_GROUP_EDITING);
                StandardFormMessage noImageAccessGroupEditingMessage = (StandardFormMessage) sfmf
                        .findByPrimaryKey(noImageAccessGroupEditingPk);
                this.addStandardFormObject(noImageAccessGroupEditingMessage, 1,
                        1);
            }
        } else {
            PrimaryKey contentsPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntitiesDictionary.CONTENTS);
            StandardEntity entity = (StandardEntity) sef
                    .findByPrimaryKey(contentsPk);
            this.setStandardEntity(entity);
            this.buildNoRecordFound();
        }
    }

    /**
     * Prints the validation specific to this form.
     * 
     * 
     * 
     * @throws IOException
     *             when a redirect occurs
     */
    public void validateExtraNewRecord() throws IOException {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardMessageFactory smf = (StandardMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardMessageFactory.class);
        StandardEntryFormFieldFactory sefff = (StandardEntryFormFieldFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntryFormFieldFactory.class);
        StandardGroupFactory sgf = (StandardGroupFactory) pfs
                .getStandardBeanFactory(DefaultStandardGroupFactory.class);
        StandardContentFactory srf = (StandardContentFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        GroupHistoryManager hm = (GroupHistoryManager) pms
                .getManager(DefaultGroupHistoryManager.class);

        /*
         * check whether there is a group with that group name
         */
        List<PrimaryKey> groups = null;

        if (this.getIsValid()) {
            /*
             * check the group name
             */
            groups = sgf.findEnabledPrimaryKeysByName(this.groupName);

            /*
             * verify only one account matches
             */
            int size = groups.size();

            if (size != 1) {
                this.setIsValid(false);

                /*
                 * if no group could be found, print the "no found" message.
                 * otherwise, print the "too many access groups found" message
                 */
                if (size == 0) {
                    PrimaryKey errorPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.ERR_NO_GROUP_FOR_ACCESS_GROUP_NAME);
                    StandardMessage error = (StandardMessage) smf
                            .findByPrimaryKey(errorPk);
                    error.printBufferWithIconHTML();

                    PrimaryKey groupPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(com.corendal.netapps.framework.helpdesk.dictionaries.FieldsDictionary.GROUP_NAME);
                    StandardEntryFormField field = (StandardEntryFormField) sefff
                            .findByPrimaryKey(groupPk);
                    field.setFocusIfFirst(this.getFormName());
                } else {
                    PrimaryKey errorPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.ERR_TOO_MANY_GROUPS_FOR_ACCESS_GROUP_NAME);
                    StandardMessage error = (StandardMessage) smf
                            .findByPrimaryKey(errorPk);
                    error.printBufferWithIconHTML();

                    PrimaryKey groupPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(com.corendal.netapps.framework.helpdesk.dictionaries.FieldsDictionary.GROUP_NAME);
                    StandardEntryFormField field = (StandardEntryFormField) sefff
                            .findByPrimaryKey(groupPk);
                    field.setFocusIfFirst(this.getFormName());
                }
            } else {
                /*
                 * add this group to the history
                 */
                PrimaryKey groupPk = groups.get(0);
                hm.add(groupPk);
            }
        }

        /*
         * verify that this group is not already added
         */
        if (this.getIsValid()) {
            StandardContent content = (StandardContent) srf
                    .findByPrimaryKey(this.image.getPrimaryKey());
            PrimaryKey groupPk = groups.get(0);

            if (content.getHasAccessGroup(groupPk)) {
                /*
                 * invalidate the form
                 */
                this.setIsValid(false);

                /*
                 * get the error message
                 */
                PrimaryKey errorPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.ERR_ALREADY_IMAGE_ACCESS_GROUP);
                StandardMessage error = (StandardMessage) smf
                        .findByPrimaryKey(errorPk);

                /*
                 * get the group name
                 */
                StandardGroup group = (StandardGroup) sgf
                        .findByPrimaryKey(groupPk);

                /*
                 * add the name of the group to the message
                 */
                error.replaceMessageText(group.getNameText(), 1);
                error.replaceMessageEncoded(group.getNameEncoded(), 1);
                error.replaceMessageHTML(group.getNameHTML(), 1);
                error.printBufferWithIconHTML();

                /*
                 * set the focus
                 */
                PrimaryKey groupNameFieldPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(com.corendal.netapps.framework.helpdesk.dictionaries.FieldsDictionary.GROUP_NAME);
                StandardEntryFormField field = (StandardEntryFormField) sefff
                        .findByPrimaryKey(groupNameFieldPk);
                field.setFocusIfFirst(this.getFormName());
            }
        }

        /*
         * try to add the access group
         */
        if (this.getIsValid()) {
            PrimaryKey groupPk = groups.get(0);

            if (this.denyAccess.equals("Y")) {
                this.image.addDeniedAccessGroup(groupPk);
            } else {
                this.image.addAllowedAccessGroup(groupPk);
            }

            this.setIsValid(this.image.getIsDone());

            if (!(this.image.getIsDone())) {
                MessageUtil.printBufferInternalErrorHTML(ABSTRACT_CLASS_NAME,
                        this.image.getStoreTrace());
            }
        }

        /*
         * print the acknowledgment information
         */
        if (this.getIsValid()) {
            /*
             * get the validation message
             */
            PrimaryKey validationPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(MessagesDictionary.VLD_ACCESS_GROUP_ADDED);
            StandardMessage validation = (StandardMessage) smf
                    .findByPrimaryKey(validationPk);

            /*
             * get the group name
             */
            PrimaryKey groupPk = groups.get(0);
            StandardGroup group = (StandardGroup) sgf.findByPrimaryKey(groupPk);

            /*
             * add the name of the image to the message
             */
            validation.replaceMessageText(group.getNameText(), 1);
            validation.replaceMessageEncoded(group.getNameEncoded(), 1);
            validation.replaceMessageHTML(group.getNameHTML(), 1);

            /*
             * print the message
             */
            validation.printBufferWithIconHTML();
        }

        /*
         * if everything went ok, store the validation message in the validation
         * buffer and redirect the user to the article detail page. otherwise,
         * rollback the current transaction
         */
        if (this.getIsValid()) {
            /*
             * redirect to the properties
             */
            String redirectURL = this.image.getPropertiesAbsoluteLocation();
            redirectURL = HTTPUtil.getAddedParameterURL(redirectURL,
                    HTTPParameterConstants.SELECTED_BLOCK_ID,
                    BlocksDictionary.IMAGE_ACCESS_GROUPS);

            ((PortletOrServletLogicContext) AnyLogicContextGlobal.get())
                    .commitAndSendAbsoluteRedirect(redirectURL);
        } else {
            AnyLogicContextGlobal.get().rollbackAndStartTransaction();
        }
    }

    /**
     * Prints the validation specific to this form.
     * 
     * 
     * 
     * @throws IOException
     *             when a redirect occurs
     */
    public void validateExtraRemoveRecord() throws IOException {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardMessageFactory smf = (StandardMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardMessageFactory.class);
        StandardEntryFormFieldFactory sefff = (StandardEntryFormFieldFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntryFormFieldFactory.class);
        StandardGroupFactory sgf = (StandardGroupFactory) pfs
                .getStandardBeanFactory(DefaultStandardGroupFactory.class);
        StandardContentFactory srf = (StandardContentFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        GroupHistoryManager hm = (GroupHistoryManager) pms
                .getManager(DefaultGroupHistoryManager.class);

        /*
         * check whether there is a group with that group name
         */
        List<PrimaryKey> groups = null;

        if (this.getIsValid()) {
            /*
             * check the group name
             */
            groups = sgf.findEnabledPrimaryKeysByName(this.groupName);

            /*
             * verify only one group matches
             */
            int size = groups.size();

            if (size != 1) {
                this.setIsValid(false);

                /*
                 * if no group could be found, print the "no found" message.
                 * otherwise, print the "too many access groups found" message
                 */
                if (size == 0) {
                    PrimaryKey errorPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.ERR_NO_GROUP_FOR_ACCESS_GROUP_NAME);
                    StandardMessage error = (StandardMessage) smf
                            .findByPrimaryKey(errorPk);
                    error.printBufferWithIconHTML();

                    PrimaryKey groupPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(com.corendal.netapps.framework.helpdesk.dictionaries.FieldsDictionary.GROUP_NAME);
                    StandardEntryFormField field = (StandardEntryFormField) sefff
                            .findByPrimaryKey(groupPk);
                    field.setFocusIfFirst(this.getFormName());
                } else {
                    PrimaryKey errorPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.ERR_TOO_MANY_GROUPS_FOR_ACCESS_GROUP_NAME);
                    StandardMessage error = (StandardMessage) smf
                            .findByPrimaryKey(errorPk);
                    error.printBufferWithIconHTML();

                    PrimaryKey groupPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(com.corendal.netapps.framework.helpdesk.dictionaries.FieldsDictionary.GROUP_NAME);
                    StandardEntryFormField field = (StandardEntryFormField) sefff
                            .findByPrimaryKey(groupPk);
                    field.setFocusIfFirst(this.getFormName());
                }
            } else {
                /*
                 * add this group to the history
                 */
                PrimaryKey groupPk = groups.get(0);
                hm.add(groupPk);
            }
        }

        /*
         * verify that this group is already added
         */
        if (this.getIsValid()) {
            StandardContent content = (StandardContent) srf
                    .findByPrimaryKey(this.image.getPrimaryKey());
            PrimaryKey groupPk = groups.get(0);

            if (!(content.getHasAccessGroup(groupPk))) {
                /*
                 * invalidate the form
                 */
                this.setIsValid(false);

                /*
                 * get the error message
                 */
                PrimaryKey errorPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.ERR_NOT_IMAGE_ACCESS_GROUP);
                StandardMessage error = (StandardMessage) smf
                        .findByPrimaryKey(errorPk);

                /*
                 * get the group name
                 */
                StandardGroup group = (StandardGroup) sgf
                        .findByPrimaryKey(groupPk);

                /*
                 * add the name of the group to the message
                 */
                error.replaceMessageText(group.getNameText(), 1);
                error.replaceMessageEncoded(group.getNameEncoded(), 1);
                error.replaceMessageHTML(group.getNameHTML(), 1);
                error.printBufferWithIconHTML();

                /*
                 * set the focus
                 */
                PrimaryKey groupNameFieldPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(com.corendal.netapps.framework.helpdesk.dictionaries.FieldsDictionary.GROUP_NAME);
                StandardEntryFormField field = (StandardEntryFormField) sefff
                        .findByPrimaryKey(groupNameFieldPk);
                field.setFocusIfFirst(this.getFormName());
            }
        }

        /*
         * try to remove the access group
         */
        if (this.getIsValid()) {
            PrimaryKey groupPk = groups.get(0);
            this.image.removeAccessGroup(groupPk);
            this.setIsValid(this.image.getIsDone());

            if (!(this.image.getIsDone())) {
                MessageUtil.printBufferInternalErrorHTML(ABSTRACT_CLASS_NAME,
                        this.image.getStoreTrace());
            }
        }

        /*
         * print the acknowledgment information
         */
        if (this.getIsValid()) {
            /*
             * get the validation message
             */
            PrimaryKey validationPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(MessagesDictionary.VLD_ACCESS_GROUP_REMOVED);
            StandardMessage validation = (StandardMessage) smf
                    .findByPrimaryKey(validationPk);

            /*
             * get the group name
             */
            PrimaryKey groupPk = groups.get(0);
            StandardGroup group = (StandardGroup) sgf.findByPrimaryKey(groupPk);

            /*
             * add the name of the group to the message
             */
            validation.replaceMessageText(group.getNameText(), 1);
            validation.replaceMessageEncoded(group.getNameEncoded(), 1);
            validation.replaceMessageHTML(group.getNameHTML(), 1);

            /*
             * print the message
             */
            validation.printBufferWithIconHTML();
        }

        /*
         * if everything went ok, store the validation message in the validation
         * buffer and redirect the user to the article detail page. otherwise,
         * rollback the current transaction
         */
        if (this.getIsValid()) {
            /*
             * redirect to the same page if the current user is the editor
             */
            String redirectURL = this.image.getPropertiesAbsoluteLocation();
            redirectURL = HTTPUtil.getAddedParameterURL(redirectURL,
                    HTTPParameterConstants.SELECTED_BLOCK_ID,
                    BlocksDictionary.IMAGE_ACCESS_GROUPS);

            ((PortletOrServletLogicContext) AnyLogicContextGlobal.get())
                    .commitAndSendAbsoluteRedirect(redirectURL);
        } else {
            AnyLogicContextGlobal.get().rollbackAndStartTransaction();
        }
    }

    /**
     * Sets the image viewed by this form.
     * 
     * @param sd
     *            standard image viewed by this form
     * @see #getStandardImage
     */
    public void setStandardImage(StandardImage sd) {
        this.image = sd;
    }

    /**
     * Returns the image viewed by this form.
     * 
     * @return a com.corendal.netapps.wiki.interfaces.StandardImage object
     * @see #setStandardImage
     */
    public StandardImage getStandardImage() {
        return this.image;
    }

    /**
     * Sets the default group name to use when this form is printed for the
     * first time.
     * 
     * @param groupName
     *            groupName to set
     */
    public void setFirstTimeAccessGroup(String groupName) {
        this.groupName = groupName;
    }
}

// end AbstractImageAccessGroupEntryBlock

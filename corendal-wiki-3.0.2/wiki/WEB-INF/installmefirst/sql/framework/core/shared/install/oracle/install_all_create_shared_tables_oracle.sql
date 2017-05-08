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
 *
 * Version: $Id: install_all_create_shared_tables_oracle.sql,v 1.8 2007/11/07 04:51:13 tdanard Exp $
 */

select '/framework/core/shared/install/oracle/install_all_create_shared_tables_oracle.sql' from dual;

@./sql/framework/core/shared/create/oracle/create_account_table_oracle.sql;
@./sql/framework/core/shared/create/oracle/create_account_attr_table_oracle.sql;
@./sql/framework/core/shared/create/oracle/create_person_table_oracle.sql;
@ ./sql/framework/core/shared/create/oracle/create_person_phone_xref_table_oracle.sql;
@./sql/framework/core/shared/create/oracle/create_person_phys_addr_xref_table_oracle.sql;
@./sql/framework/core/shared/create/oracle/create_phys_addr_table_oracle.sql;
@./sql/framework/core/shared/create/oracle/create_phone_table_oracle.sql;
@./sql/framework/core/shared/create/oracle/create_account_role_xref_table_oracle.sql;
@./sql/framework/core/shared/create/oracle/create_password_table_oracle.sql;
@./sql/framework/core/shared/create/oracle/create_username_search_table_oracle.sql;
@./sql/framework/core/shared/create/oracle/create_error_log_table_oracle.sql;
@./sql/framework/core/shared/create/oracle/create_entry_log_table_oracle.sql;
@./sql/framework/core/shared/create/oracle/create_email_table_oracle.sql;
@./sql/framework/core/shared/create/oracle/create_email_attr_table_oracle.sql;
@./sql/framework/core/shared/create/oracle/create_response_log_table_oracle.sql;
@./sql/framework/core/shared/create/oracle/create_search_log_table_oracle.sql;
@./sql/framework/core/shared/create/oracle/create_soundex_word_table_oracle.sql;
@./sql/framework/core/shared/create/oracle/create_person_ssn_table_oracle.sql;
@./sql/framework/core/shared/create/oracle/create_warning_table_oracle.sql;
@./sql/framework/core/shared/create/oracle/create_upper_search_table_oracle.sql;
@./sql/framework/core/shared/create/oracle/create_file_obj_table_oracle.sql;
@./sql/framework/core/shared/create/oracle/create_file_obj_role_xref_table_oracle.sql;
@./sql/framework/core/shared/create/oracle/create_file_obj_binary_obj_xref_table_oracle.sql;
@./sql/framework/core/shared/create/oracle/create_binary_obj_table_oracle.sql;
@./sql/framework/core/shared/create/oracle/create_ldap_control_table_oracle.sql;
@./sql/framework/core/shared/create/oracle/create_ldap_control_role_xref_table_oracle.sql;
@./sql/framework/core/shared/create/oracle/create_batch_table_oracle.sql;
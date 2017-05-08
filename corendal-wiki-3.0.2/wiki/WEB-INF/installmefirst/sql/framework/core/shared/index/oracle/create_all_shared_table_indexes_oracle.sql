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
 * Version: $Id: create_all_shared_table_indexes_oracle.sql,v 1.10 2007/11/06 17:36:33 tdanard Exp $
 */
 
select '/framework/core/shared/index/oracle/create_all_shared_table_indexes_oracle.sql' from dual;
 
create index upper_search_nam_idx on upper_search(nam);
create index upper_search_field_obj_id_idx on upper_search(field_obj_id);
create index upper_search_entity_id_idx on upper_search(entity_id);
create index upper_search_record_id_idx on upper_search(record_id);

create index account_person_id_idx on account(person_id);

create index person_phone_xref_person_1_idx on person_phone_xref(person_id);
create index person_phone_xref_phone_id_idx on person_phone_xref(phone_id);

create index person_phys_addr_xref_per1_idx on person_phys_addr_xref(person_id);
create index person_phys_addr_xref_phy1_idx on person_phys_addr_xref(phys_addr_id);
create index person_phys_addr_xref_phy2_idx on person_phys_addr_xref(phys_addr_typ_id);

create index phys_addr_city_id_idx on phys_addr(city_id);
create index phys_addr_zip_cod_id_idx on phys_addr(zip_cod_id);
create index phys_addr_state_id_idx on phys_addr(state_id);
create index phys_addr_country_id_idx on phys_addr(country_id);

create index account_role_xref_account1_idx on account_role_xref(account_id);
create index account_role_xref_role_id_idx on account_role_xref(role_id);
create index account_role_xref_entity_1_idx on account_role_xref(entity_id);
create index account_role_xref_record_1_idx on account_role_xref(record_id);
create index account_role_xref_common_idx on account_role_xref(entity_id,role_id,record_id);
create index account_role_xref_common2_idx on account_role_xref(entity_id,role_id,account_id);

create index email_attr_email_id_idx on email_attr(email_id);
create index email_attr_attr_id_idx on email_attr(attr_id);
create index email_attr_common_idx on email_attr(email_id, attr_id);

create index entry_log_entity_id_idx on entry_log(entity_id);
create index entry_log_record_id_idx on entry_log(record_id);
create index entry_log_typ_id_idx on entry_log(typ_id);
create index entry_log_session_obj_id_idx on entry_log(session_obj_id);
create index entry_log_actual_account_1_idx on entry_log(actual_account_id);
create index entry_log_actual_proxy_id_idx on entry_log(proxy_account_id);
create index entry_log_timestamp_idx on entry_log(timestamp);

create index entry_log_common1_idx on entry_log(entity_id,record_id,typ_id);
create index entry_log_common2_idx on entry_log(entity_id,record_id);
create index entry_log_common3_idx on entry_log(entity_id,record_id,timestamp);
create index entry_log_common4_idx on entry_log(entity_id,record_id,typ_id,timestamp);

create index error_log_entity_id_idx on error_log(entity_id);
create index error_log_record_id_idx on error_log(record_id);
create index error_log_timestamp_idx on error_log(timestamp);

create index response_log_page_id_idx on response_log(page_id);
create index response_log_timestamp_idx on response_log(timestamp);
create index response_log_typ_id_idx on response_log(response_log_typ_id);

create index password_account_id_idx on password(account_id);

create index username_search_person_id_idx on username_search(person_id);
create index username_search_account_id_idx on username_search(account_id);

create index soundex_word_word_idx on soundex_word(word);
create index soundex_word_soundex_idx on soundex_word(soundex);
create index soundex_word_app_id_idx on soundex_word(app_id);

create index search_log_criteria_idx on search_log(criteria);

create index file_obj_role_xref_file_o1_idx on file_obj_role_xref(file_obj_id);
create index file_obj_role_xref_role_id_idx on file_obj_role_xref(role_id);
create index file_obj_role_xref_entity1_idx on file_obj_role_xref(entity_id);
create index file_obj_role_xref_record1_idx on file_obj_role_xref(record_id);

create index file_obj_binary_obj_xref_1_idx on file_obj_binary_obj_xref(file_obj_id);
create index file_obj_binary_obj_xref_2_idx on file_obj_binary_obj_xref(binary_obj_id);
create index file_obj_binary_obj_xref_3_idx on file_obj_binary_obj_xref(mirror_id);

create index search_log_app_id_idx on search_log(app_id);
create index search_log_block_id_idx on search_log(block_id);

create index ldap_control_role_xref_fi1_idx on ldap_control_role_xref(ldap_control_id);
create index ldap_control_role_xref_ro1_idx on ldap_control_role_xref(role_id);
create index ldap_control_role_xref_en1_idx on ldap_control_role_xref(entity_id);
create index ldap_control_role_xref_re1_idx on ldap_control_role_xref(record_id);

create index person_ssn_country_id_idx on person_ssn(country_id);
create index person_ssn_person_id_idx on person_ssn(person_id);

create index phone_country_id_idx on phone(country_id);


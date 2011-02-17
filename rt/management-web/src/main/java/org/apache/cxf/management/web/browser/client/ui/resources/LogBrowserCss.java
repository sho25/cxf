begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|management
operator|.
name|web
operator|.
name|browser
operator|.
name|client
operator|.
name|ui
operator|.
name|resources
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|resources
operator|.
name|client
operator|.
name|CssResource
import|;
end_import

begin_interface
specifier|public
interface|interface
name|LogBrowserCss
extends|extends
name|CssResource
block|{
name|String
name|navigationSidebarSlot
parameter_list|()
function_decl|;
name|String
name|viewerSlot
parameter_list|()
function_decl|;
comment|/* Error Dialog styles */
name|String
name|errorDialog
parameter_list|()
function_decl|;
name|String
name|errorDialogGlass
parameter_list|()
function_decl|;
name|String
name|errorDialogTitle
parameter_list|()
function_decl|;
name|String
name|errorDialogButtons
parameter_list|()
function_decl|;
name|String
name|errorDialogErrorType
parameter_list|()
function_decl|;
comment|/* Access control tab styles */
name|String
name|accessControlTab
parameter_list|()
function_decl|;
name|String
name|accessControlTabErrorMessage
parameter_list|()
function_decl|;
name|String
name|accessControlTabRememberMeCheckbox
parameter_list|()
function_decl|;
name|String
name|accessControlTabSignInButton
parameter_list|()
function_decl|;
comment|/* Browser tab styles*/
name|String
name|browserTabLoadingMessage
parameter_list|()
function_decl|;
name|String
name|browserTabNoEntriesMessage
parameter_list|()
function_decl|;
name|String
name|browserTabSelectedRow
parameter_list|()
function_decl|;
name|String
name|browserTabManageSubscriptionsButton
parameter_list|()
function_decl|;
name|String
name|browserTabToolBar
parameter_list|()
function_decl|;
name|String
name|browserTabEntryTableHeaders
parameter_list|()
function_decl|;
name|String
name|browserTabEntrySelectableTable
parameter_list|()
function_decl|;
name|String
name|browserTabSubscriptionsSideBar
parameter_list|()
function_decl|;
name|String
name|browserTabSubscriptionsHeader
parameter_list|()
function_decl|;
name|String
name|browserTabEntryDetailsSection
parameter_list|()
function_decl|;
name|String
name|browserTabEntryDetailsContent
parameter_list|()
function_decl|;
name|String
name|browserTabNavigationLink
parameter_list|()
function_decl|;
comment|/* Settings tab styles */
name|String
name|settingsTabHeader
parameter_list|()
function_decl|;
name|String
name|settingsTabBackButton
parameter_list|()
function_decl|;
name|String
name|settingsTabTitle
parameter_list|()
function_decl|;
name|String
name|settingsTabToolBar
parameter_list|()
function_decl|;
name|String
name|settingsTabContent
parameter_list|()
function_decl|;
name|String
name|settingsTabFeedList
parameter_list|()
function_decl|;
comment|/*  Feed's entry (in settings tab) styles */
name|String
name|feedEntry
parameter_list|()
function_decl|;
name|String
name|feedEntryNameLabel
parameter_list|()
function_decl|;
name|String
name|feedEntryUrlLabel
parameter_list|()
function_decl|;
name|String
name|feedEntryButtons
parameter_list|()
function_decl|;
name|String
name|feedEntryRemoveButton
parameter_list|()
function_decl|;
name|String
name|feedEntryEditButton
parameter_list|()
function_decl|;
comment|/* Edit feed dialog (in settings tab) styles */
name|String
name|editFeedDialogErrorMessage
parameter_list|()
function_decl|;
name|String
name|editFeedDialogButtons
parameter_list|()
function_decl|;
name|String
name|editFeedDialogAddButton
parameter_list|()
function_decl|;
block|}
end_interface

end_unit


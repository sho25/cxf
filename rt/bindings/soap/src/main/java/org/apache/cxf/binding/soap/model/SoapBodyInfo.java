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
name|binding
operator|.
name|soap
operator|.
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|model
operator|.
name|MessagePartInfo
import|;
end_import

begin_class
specifier|public
class|class
name|SoapBodyInfo
block|{
specifier|private
name|List
argument_list|<
name|MessagePartInfo
argument_list|>
name|parts
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|MessagePartInfo
argument_list|>
name|attachments
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|String
name|use
decl_stmt|;
specifier|public
name|List
argument_list|<
name|MessagePartInfo
argument_list|>
name|getParts
parameter_list|()
block|{
return|return
name|parts
return|;
block|}
specifier|public
name|void
name|setParts
parameter_list|(
name|List
argument_list|<
name|MessagePartInfo
argument_list|>
name|parts
parameter_list|)
block|{
name|this
operator|.
name|parts
operator|=
name|parts
expr_stmt|;
block|}
specifier|public
name|String
name|getUse
parameter_list|()
block|{
return|return
name|use
return|;
block|}
specifier|public
name|void
name|setUse
parameter_list|(
name|String
name|use
parameter_list|)
block|{
name|this
operator|.
name|use
operator|=
name|use
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|MessagePartInfo
argument_list|>
name|getAttachments
parameter_list|()
block|{
return|return
name|attachments
return|;
block|}
specifier|public
name|void
name|setAttachments
parameter_list|(
name|List
argument_list|<
name|MessagePartInfo
argument_list|>
name|attachments
parameter_list|)
block|{
name|this
operator|.
name|attachments
operator|=
name|attachments
expr_stmt|;
block|}
block|}
end_class

end_unit


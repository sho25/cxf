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
name|attachment
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|activation
operator|.
name|DataHandler
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
name|message
operator|.
name|Attachment
import|;
end_import

begin_class
specifier|public
class|class
name|AttachmentImpl
implements|implements
name|Attachment
block|{
specifier|private
name|DataHandler
name|dataHandler
decl_stmt|;
specifier|private
name|String
name|id
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|xop
decl_stmt|;
specifier|public
name|AttachmentImpl
parameter_list|(
name|String
name|idParam
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|idParam
expr_stmt|;
block|}
specifier|public
name|AttachmentImpl
parameter_list|(
name|String
name|idParam
parameter_list|,
name|DataHandler
name|handlerParam
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|idParam
expr_stmt|;
name|this
operator|.
name|dataHandler
operator|=
name|handlerParam
expr_stmt|;
name|this
operator|.
name|dataHandler
operator|.
name|setCommandMap
argument_list|(
name|AttachmentUtil
operator|.
name|getCommandMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
specifier|public
name|DataHandler
name|getDataHandler
parameter_list|()
block|{
return|return
name|dataHandler
return|;
block|}
specifier|public
name|void
name|setDataHandler
parameter_list|(
name|DataHandler
name|dataHandler
parameter_list|)
block|{
name|this
operator|.
name|dataHandler
operator|=
name|dataHandler
expr_stmt|;
name|this
operator|.
name|dataHandler
operator|.
name|setCommandMap
argument_list|(
name|AttachmentUtil
operator|.
name|getCommandMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setHeader
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|headers
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getHeader
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|String
name|value
init|=
name|headers
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
name|value
operator|==
literal|null
condition|?
name|headers
operator|.
name|get
argument_list|(
name|name
operator|.
name|toLowerCase
argument_list|()
argument_list|)
else|:
name|value
return|;
block|}
specifier|public
name|Iterator
argument_list|<
name|String
argument_list|>
name|getHeaderNames
parameter_list|()
block|{
return|return
name|headers
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isXOP
parameter_list|()
block|{
return|return
name|xop
return|;
block|}
specifier|public
name|void
name|setXOP
parameter_list|(
name|boolean
name|xopParam
parameter_list|)
block|{
name|this
operator|.
name|xop
operator|=
name|xopParam
expr_stmt|;
block|}
block|}
end_class

end_unit


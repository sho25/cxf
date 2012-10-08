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
name|aegis
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

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
name|Map
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
name|aegis
operator|.
name|type
operator|.
name|TypeMapping
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
name|interceptor
operator|.
name|Fault
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

begin_comment
comment|/**  * Holds information about the message request and response. Applications should not need to   * work with this class.  *   * @author<a href="mailto:dan@envoisolutions.com">Dan Diephouse</a>  * @since Feb 13, 2004  */
end_comment

begin_class
specifier|public
class|class
name|Context
block|{
specifier|private
name|AegisContext
name|globalContext
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|Attachment
argument_list|>
name|attachments
decl_stmt|;
specifier|private
name|Fault
name|fault
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Object
argument_list|>
name|properties
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|namedProperties
decl_stmt|;
specifier|public
name|Context
parameter_list|(
name|AegisContext
name|aegisContext
parameter_list|)
block|{
name|this
operator|.
name|globalContext
operator|=
name|aegisContext
expr_stmt|;
name|this
operator|.
name|properties
operator|=
operator|new
name|HashMap
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Object
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|namedProperties
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
expr_stmt|;
block|}
specifier|public
name|TypeMapping
name|getTypeMapping
parameter_list|()
block|{
return|return
name|globalContext
operator|.
name|getTypeMapping
argument_list|()
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|Attachment
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
name|Collection
argument_list|<
name|Attachment
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
specifier|public
name|boolean
name|isWriteXsiTypes
parameter_list|()
block|{
return|return
name|globalContext
operator|.
name|isWriteXsiTypes
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isReadXsiTypes
parameter_list|()
block|{
return|return
name|globalContext
operator|.
name|isReadXsiTypes
argument_list|()
return|;
block|}
specifier|public
name|void
name|setFault
parameter_list|(
name|Fault
name|fault
parameter_list|)
block|{
name|this
operator|.
name|fault
operator|=
name|fault
expr_stmt|;
block|}
specifier|public
name|Fault
name|getFault
parameter_list|()
block|{
return|return
name|fault
return|;
block|}
specifier|public
name|AegisContext
name|getGlobalContext
parameter_list|()
block|{
return|return
name|globalContext
return|;
block|}
specifier|public
name|boolean
name|isMtomEnabled
parameter_list|()
block|{
return|return
name|globalContext
operator|.
name|isMtomEnabled
argument_list|()
return|;
block|}
comment|// bus-style properties for internal state management.
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getProperty
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|key
parameter_list|)
block|{
return|return
name|key
operator|.
name|cast
argument_list|(
name|properties
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|void
name|setProperty
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
name|properties
operator|.
name|put
argument_list|(
name|value
operator|.
name|getClass
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|//named properties to solve other problems
specifier|public
name|void
name|setProperty
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|namedProperties
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
parameter_list|<
name|T
parameter_list|>
name|T
name|getProperty
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|type
operator|.
name|cast
argument_list|(
name|namedProperties
operator|.
name|get
argument_list|(
name|name
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit


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
name|tools
operator|.
name|misc
operator|.
name|processor
operator|.
name|address
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
name|tools
operator|.
name|common
operator|.
name|ToolConstants
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
name|tools
operator|.
name|common
operator|.
name|ToolContext
import|;
end_import

begin_class
specifier|public
class|class
name|JmsAddress
implements|implements
name|Address
block|{
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getNamespaces
parameter_list|(
specifier|final
name|ToolContext
name|context
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|ns
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
name|ns
operator|.
name|put
argument_list|(
literal|"jms"
argument_list|,
name|ToolConstants
operator|.
name|NS_JMS_ADDRESS
argument_list|)
expr_stmt|;
return|return
name|ns
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|buildAddressArguments
parameter_list|(
specifier|final
name|ToolContext
name|context
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|args
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|context
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|JMS_ADDR_DEST_STYLE
argument_list|)
condition|)
block|{
name|args
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|JMS_ADDR_DEST_STYLE
argument_list|,
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|JMS_ADDR_DEST_STYLE
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|context
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|JMS_ADDR_INIT_CTX
argument_list|)
condition|)
block|{
name|args
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|JMS_ADDR_INIT_CTX
argument_list|,
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|JMS_ADDR_INIT_CTX
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|context
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|JMS_ADDR_JNDI_DEST
argument_list|)
condition|)
block|{
name|args
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|JMS_ADDR_JNDI_DEST
argument_list|,
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|JMS_ADDR_JNDI_DEST
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|context
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|JMS_ADDR_JNDI_FAC
argument_list|)
condition|)
block|{
name|args
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|JMS_ADDR_JNDI_FAC
argument_list|,
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|JMS_ADDR_JNDI_FAC
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|context
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|JMS_ADDR_JNDI_URL
argument_list|)
condition|)
block|{
name|args
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|JMS_ADDR_JNDI_URL
argument_list|,
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|JMS_ADDR_JNDI_URL
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|context
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|JMS_ADDR_SUBSCRIBER_NAME
argument_list|)
condition|)
block|{
name|args
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|JMS_ADDR_SUBSCRIBER_NAME
argument_list|,
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|JMS_ADDR_SUBSCRIBER_NAME
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|args
return|;
block|}
block|}
end_class

end_unit


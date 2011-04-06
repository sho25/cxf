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
name|ws
operator|.
name|policy
operator|.
name|blueprint
package|;
end_package

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|aries
operator|.
name|blueprint
operator|.
name|ParserContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|aries
operator|.
name|blueprint
operator|.
name|mutable
operator|.
name|MutableBeanMetadata
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
name|configuration
operator|.
name|blueprint
operator|.
name|AbstractBPBeanDefinitionParser
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
name|ws
operator|.
name|policy
operator|.
name|attachment
operator|.
name|external
operator|.
name|ExternalAttachmentProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|blueprint
operator|.
name|reflect
operator|.
name|BeanProperty
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|blueprint
operator|.
name|reflect
operator|.
name|Metadata
import|;
end_import

begin_class
specifier|public
class|class
name|ExternalAttachmentProviderBPDefinitionParser
extends|extends
name|AbstractBPBeanDefinitionParser
block|{
specifier|public
name|Metadata
name|parse
parameter_list|(
name|Element
name|element
parameter_list|,
name|ParserContext
name|context
parameter_list|)
block|{
name|MutableBeanMetadata
name|attachmentProvider
init|=
name|context
operator|.
name|createMetadata
argument_list|(
name|MutableBeanMetadata
operator|.
name|class
argument_list|)
decl_stmt|;
name|attachmentProvider
operator|.
name|setRuntimeClass
argument_list|(
name|ExternalAttachmentProvider
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|hasBusProperty
argument_list|()
condition|)
block|{
name|boolean
name|foundBus
init|=
literal|false
decl_stmt|;
for|for
control|(
name|BeanProperty
name|bp
range|:
name|attachmentProvider
operator|.
name|getProperties
argument_list|()
control|)
block|{
if|if
condition|(
literal|"bus"
operator|.
name|equals
argument_list|(
name|bp
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|foundBus
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|foundBus
condition|)
block|{
name|attachmentProvider
operator|.
name|addProperty
argument_list|(
literal|"bus"
argument_list|,
name|getBusRef
argument_list|(
name|context
argument_list|,
literal|"cxf"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|parseAttributes
argument_list|(
name|element
argument_list|,
name|context
argument_list|,
name|attachmentProvider
argument_list|)
expr_stmt|;
name|parseChildElements
argument_list|(
name|element
argument_list|,
name|context
argument_list|,
name|attachmentProvider
argument_list|)
expr_stmt|;
return|return
name|attachmentProvider
return|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|hasBusProperty
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit


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
name|bus
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
name|common
operator|.
name|util
operator|.
name|StringUtils
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
name|BusDefinitionParser
extends|extends
name|AbstractBPBeanDefinitionParser
block|{
specifier|public
name|BusDefinitionParser
parameter_list|()
block|{     }
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
name|String
name|bname
init|=
name|element
operator|.
name|hasAttribute
argument_list|(
literal|"bus"
argument_list|)
condition|?
name|element
operator|.
name|getAttribute
argument_list|(
literal|"bus"
argument_list|)
else|:
literal|"cxf"
decl_stmt|;
name|String
name|id
init|=
name|element
operator|.
name|hasAttribute
argument_list|(
literal|"id"
argument_list|)
condition|?
name|element
operator|.
name|getAttribute
argument_list|(
literal|"id"
argument_list|)
else|:
literal|null
decl_stmt|;
name|MutableBeanMetadata
name|cxfBean
init|=
name|getBus
argument_list|(
name|context
argument_list|,
name|bname
argument_list|)
decl_stmt|;
name|parseAttributes
argument_list|(
name|element
argument_list|,
name|context
argument_list|,
name|cxfBean
argument_list|)
expr_stmt|;
name|parseChildElements
argument_list|(
name|element
argument_list|,
name|context
argument_list|,
name|cxfBean
argument_list|)
expr_stmt|;
name|context
operator|.
name|getComponentDefinitionRegistry
argument_list|()
operator|.
name|removeComponentDefinition
argument_list|(
name|bname
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|id
argument_list|)
condition|)
block|{
name|cxfBean
operator|.
name|addProperty
argument_list|(
literal|"id"
argument_list|,
name|createValue
argument_list|(
name|context
argument_list|,
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|cxfBean
return|;
block|}
specifier|protected
name|void
name|processBusAttribute
parameter_list|(
name|Element
name|element
parameter_list|,
name|ParserContext
name|ctx
parameter_list|,
name|MutableBeanMetadata
name|bean
parameter_list|,
name|String
name|val
parameter_list|)
block|{
comment|//nothing
block|}
specifier|protected
name|boolean
name|hasBusProperty
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|mapElement
parameter_list|(
name|ParserContext
name|ctx
parameter_list|,
name|MutableBeanMetadata
name|bean
parameter_list|,
name|Element
name|el
parameter_list|,
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
literal|"inInterceptors"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"inFaultInterceptors"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"outInterceptors"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"outFaultInterceptors"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"features"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|bean
operator|.
name|addProperty
argument_list|(
name|name
argument_list|,
name|parseListData
argument_list|(
name|ctx
argument_list|,
name|bean
argument_list|,
name|el
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"properties"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|bean
operator|.
name|addProperty
argument_list|(
name|name
argument_list|,
name|parseMapData
argument_list|(
name|ctx
argument_list|,
name|bean
argument_list|,
name|el
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


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
name|frontend
operator|.
name|blueprint
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

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
name|SimpleBPBeanDefinitionParser
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
name|frontend
operator|.
name|ClientProxyFactoryBean
import|;
end_import

begin_class
specifier|public
class|class
name|ClientProxyFactoryBeanDefinitionParser
extends|extends
name|SimpleBPBeanDefinitionParser
block|{
specifier|public
name|ClientProxyFactoryBeanDefinitionParser
parameter_list|()
block|{
name|this
argument_list|(
name|ClientProxyFactoryBean
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ClientProxyFactoryBeanDefinitionParser
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
name|super
argument_list|(
name|cls
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getFactorySuffix
parameter_list|()
block|{
return|return
literal|".proxyFactory"
return|;
block|}
specifier|public
name|String
name|getFactoryCreateType
parameter_list|(
name|Element
name|element
parameter_list|)
block|{
return|return
name|element
operator|.
name|getAttribute
argument_list|(
literal|"serviceClass"
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|mapAttribute
parameter_list|(
name|MutableBeanMetadata
name|bean
parameter_list|,
name|Element
name|e
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|val
parameter_list|,
name|ParserContext
name|context
parameter_list|)
block|{
if|if
condition|(
literal|"endpointName"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"serviceName"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|QName
name|q
init|=
name|parseQName
argument_list|(
name|e
argument_list|,
name|val
argument_list|)
decl_stmt|;
name|bean
operator|.
name|addProperty
argument_list|(
name|name
argument_list|,
name|createValue
argument_list|(
name|context
argument_list|,
name|q
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|mapToProperty
argument_list|(
name|bean
argument_list|,
name|name
argument_list|,
name|val
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
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
literal|"properties"
argument_list|,
name|this
operator|.
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
elseif|else
if|if
condition|(
literal|"binding"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|setFirstChildAsProperty
argument_list|(
name|el
argument_list|,
name|ctx
argument_list|,
name|bean
argument_list|,
literal|"bindingConfig"
argument_list|)
expr_stmt|;
block|}
elseif|else
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
operator|||
literal|"schemaLocations"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"handlers"
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
name|this
operator|.
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
else|else
block|{
name|setFirstChildAsProperty
argument_list|(
name|el
argument_list|,
name|ctx
argument_list|,
name|bean
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
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


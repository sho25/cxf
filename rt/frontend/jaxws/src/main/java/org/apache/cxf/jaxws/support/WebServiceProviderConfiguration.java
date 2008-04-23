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
name|jaxws
operator|.
name|support
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|activation
operator|.
name|DataSource
import|;
end_import

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
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPMessage
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
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
name|factory
operator|.
name|ReflectionServiceFactoryBean
import|;
end_import

begin_class
specifier|public
class|class
name|WebServiceProviderConfiguration
extends|extends
name|JaxWsServiceConfiguration
block|{
specifier|private
name|JaxWsImplementorInfo
name|implInfo
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Boolean
name|isOperation
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
return|return
name|method
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"invoke"
argument_list|)
operator|&&
name|method
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
operator|==
literal|1
operator|&&
operator|(
name|Source
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|method
operator|.
name|getParameterTypes
argument_list|()
index|[
literal|0
index|]
argument_list|)
operator|||
name|SOAPMessage
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|method
operator|.
name|getParameterTypes
argument_list|()
index|[
literal|0
index|]
argument_list|)
operator|||
name|DataSource
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|method
operator|.
name|getParameterTypes
argument_list|()
index|[
literal|0
index|]
argument_list|)
operator|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setServiceFactory
parameter_list|(
name|ReflectionServiceFactoryBean
name|serviceFactory
parameter_list|)
block|{
name|super
operator|.
name|setServiceFactory
argument_list|(
name|serviceFactory
argument_list|)
expr_stmt|;
name|implInfo
operator|=
operator|(
operator|(
name|JaxWsServiceFactoryBean
operator|)
name|serviceFactory
operator|)
operator|.
name|getJaxWsImplementorInfo
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getServiceName
parameter_list|()
block|{
name|QName
name|service
init|=
name|implInfo
operator|.
name|getServiceName
argument_list|()
decl_stmt|;
if|if
condition|(
name|service
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
else|else
block|{
return|return
name|service
operator|.
name|getLocalPart
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|getServiceNamespace
parameter_list|()
block|{
name|QName
name|service
init|=
name|implInfo
operator|.
name|getServiceName
argument_list|()
decl_stmt|;
if|if
condition|(
name|service
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
else|else
block|{
return|return
name|service
operator|.
name|getNamespaceURI
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|QName
name|getEndpointName
parameter_list|()
block|{
return|return
name|implInfo
operator|.
name|getEndpointName
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getWsdlURL
parameter_list|()
block|{
name|String
name|wsdlLocation
init|=
name|implInfo
operator|.
name|getWsdlLocation
argument_list|()
decl_stmt|;
if|if
condition|(
name|wsdlLocation
operator|!=
literal|null
operator|&&
name|wsdlLocation
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
return|return
name|wsdlLocation
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|Boolean
name|isWrapped
parameter_list|(
name|Method
name|m
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit


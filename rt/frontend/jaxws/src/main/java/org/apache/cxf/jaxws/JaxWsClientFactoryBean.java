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
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|soap
operator|.
name|SOAPBinding
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
name|binding
operator|.
name|soap
operator|.
name|SoapBindingConfiguration
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
name|ClientFactoryBean
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
name|jaxws
operator|.
name|binding
operator|.
name|soap
operator|.
name|JaxWsSoapBindingConfiguration
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
name|jaxws
operator|.
name|support
operator|.
name|JaxWsServiceFactoryBean
import|;
end_import

begin_comment
comment|/**  * Bean to help easily create Client endpoints for JAX-WS.  */
end_comment

begin_class
specifier|public
class|class
name|JaxWsClientFactoryBean
extends|extends
name|ClientFactoryBean
block|{
specifier|public
name|JaxWsClientFactoryBean
parameter_list|()
block|{
name|super
argument_list|(
operator|new
name|JaxWsServiceFactoryBean
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|SoapBindingConfiguration
name|createSoapBindingConfig
parameter_list|()
block|{
return|return
operator|new
name|JaxWsSoapBindingConfiguration
argument_list|(
operator|(
name|JaxWsServiceFactoryBean
operator|)
name|getServiceFactory
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setBindingId
parameter_list|(
name|String
name|bind
parameter_list|)
block|{
name|super
operator|.
name|setBindingId
argument_list|(
name|bind
argument_list|)
expr_stmt|;
if|if
condition|(
name|SOAPBinding
operator|.
name|SOAP11HTTP_BINDING
operator|.
name|equals
argument_list|(
name|bind
argument_list|)
operator|||
name|SOAPBinding
operator|.
name|SOAP12HTTP_BINDING
operator|.
name|equals
argument_list|(
name|bind
argument_list|)
condition|)
block|{
name|setBindingConfig
argument_list|(
operator|new
name|JaxWsSoapBindingConfiguration
argument_list|(
operator|(
name|JaxWsServiceFactoryBean
operator|)
name|getServiceFactory
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SOAPBinding
operator|.
name|SOAP11HTTP_MTOM_BINDING
operator|.
name|equals
argument_list|(
name|bind
argument_list|)
operator|||
name|SOAPBinding
operator|.
name|SOAP12HTTP_MTOM_BINDING
operator|.
name|equals
argument_list|(
name|bind
argument_list|)
condition|)
block|{
name|setBindingConfig
argument_list|(
operator|new
name|JaxWsSoapBindingConfiguration
argument_list|(
operator|(
name|JaxWsServiceFactoryBean
operator|)
name|getServiceFactory
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
operator|(
operator|(
name|JaxWsSoapBindingConfiguration
operator|)
name|getBindingConfig
argument_list|()
operator|)
operator|.
name|setMtomEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


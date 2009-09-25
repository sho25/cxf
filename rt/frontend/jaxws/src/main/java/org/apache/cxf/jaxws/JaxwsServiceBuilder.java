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
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|frontend
operator|.
name|AbstractServiceFactory
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
name|JaxWsImplementorInfo
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

begin_class
specifier|public
class|class
name|JaxwsServiceBuilder
extends|extends
name|AbstractServiceFactory
block|{
specifier|final
name|JaxWsServiceFactoryBean
name|serviceFactory
decl_stmt|;
specifier|public
name|JaxwsServiceBuilder
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
name|serviceFactory
operator|=
operator|new
name|JaxWsServiceFactoryBean
argument_list|()
expr_stmt|;
comment|//As this is a javatowsdl tool, explictly populate service model from class
name|serviceFactory
operator|.
name|setPopulateFromClass
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|setServiceFactory
argument_list|(
name|serviceFactory
argument_list|)
expr_stmt|;
name|setBindingConfig
argument_list|(
operator|new
name|JaxWsSoapBindingConfiguration
argument_list|(
name|serviceFactory
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|validate
parameter_list|()
block|{
name|Class
name|clz
init|=
name|getServiceClass
argument_list|()
decl_stmt|;
if|if
condition|(
name|java
operator|.
name|rmi
operator|.
name|Remote
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|clz
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"JAXWS SEIs may not implement the java.rmi.Remote interface."
argument_list|)
throw|;
block|}
block|}
specifier|public
name|File
name|getOutputFile
parameter_list|()
block|{
name|JaxWsImplementorInfo
name|jaxwsImpl
init|=
name|serviceFactory
operator|.
name|getJaxWsImplementorInfo
argument_list|()
decl_stmt|;
name|String
name|wsdlLocation
init|=
name|jaxwsImpl
operator|.
name|getWsdlLocation
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|wsdlLocation
argument_list|)
condition|)
block|{
try|try
block|{
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
name|wsdlLocation
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"file"
operator|.
name|equals
argument_list|(
name|uri
operator|.
name|getScheme
argument_list|()
argument_list|)
operator|||
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|uri
operator|.
name|getScheme
argument_list|()
argument_list|)
condition|)
block|{
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return
name|f
return|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|//ignore
block|}
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|wsdlLocation
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return
name|f
return|;
block|}
block|}
return|return
name|super
operator|.
name|getOutputFile
argument_list|()
return|;
block|}
block|}
end_class

end_unit


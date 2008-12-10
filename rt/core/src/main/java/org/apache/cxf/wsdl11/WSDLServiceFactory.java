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
name|wsdl11
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Definition
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|WSDLException
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|Bus
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
name|i18n
operator|.
name|Message
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
name|logging
operator|.
name|LogUtils
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
name|Service
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
name|ServiceImpl
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
name|AbstractServiceFactoryBean
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
name|ServiceConstructionException
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
name|ServiceInfo
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
name|wsdl
operator|.
name|WSDLManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|XmlSchemaException
import|;
end_import

begin_class
specifier|public
class|class
name|WSDLServiceFactory
extends|extends
name|AbstractServiceFactoryBean
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|WSDLServiceFactory
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|URL
name|wsdlUrl
decl_stmt|;
specifier|private
name|QName
name|serviceName
decl_stmt|;
specifier|private
name|Definition
name|definition
decl_stmt|;
specifier|public
name|WSDLServiceFactory
parameter_list|(
name|Bus
name|b
parameter_list|,
name|Definition
name|d
parameter_list|)
block|{
name|setBus
argument_list|(
name|b
argument_list|)
expr_stmt|;
name|definition
operator|=
name|d
expr_stmt|;
block|}
specifier|public
name|WSDLServiceFactory
parameter_list|(
name|Bus
name|b
parameter_list|,
name|Definition
name|d
parameter_list|,
name|QName
name|sn
parameter_list|)
block|{
name|this
argument_list|(
name|b
argument_list|,
name|d
argument_list|)
expr_stmt|;
name|serviceName
operator|=
name|sn
expr_stmt|;
block|}
specifier|public
name|WSDLServiceFactory
parameter_list|(
name|Bus
name|b
parameter_list|,
name|URL
name|url
parameter_list|)
block|{
name|setBus
argument_list|(
name|b
argument_list|)
expr_stmt|;
name|wsdlUrl
operator|=
name|url
expr_stmt|;
try|try
block|{
comment|// use wsdl manager to parse wsdl or get cached definition
name|definition
operator|=
name|getBus
argument_list|()
operator|.
name|getExtension
argument_list|(
name|WSDLManager
operator|.
name|class
argument_list|)
operator|.
name|getDefinition
argument_list|(
name|wsdlUrl
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WSDLException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|ServiceConstructionException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"SERVICE_CREATION_MSG"
argument_list|,
name|LOG
argument_list|)
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|public
name|WSDLServiceFactory
parameter_list|(
name|Bus
name|b
parameter_list|,
name|String
name|url
parameter_list|)
block|{
name|setBus
argument_list|(
name|b
argument_list|)
expr_stmt|;
try|try
block|{
comment|// use wsdl manager to parse wsdl or get cached definition
name|definition
operator|=
name|getBus
argument_list|()
operator|.
name|getExtension
argument_list|(
name|WSDLManager
operator|.
name|class
argument_list|)
operator|.
name|getDefinition
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WSDLException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|ServiceConstructionException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"SERVICE_CREATION_MSG"
argument_list|,
name|LOG
argument_list|)
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|public
name|WSDLServiceFactory
parameter_list|(
name|Bus
name|b
parameter_list|,
name|URL
name|url
parameter_list|,
name|QName
name|sn
parameter_list|)
block|{
name|this
argument_list|(
name|b
argument_list|,
name|url
argument_list|)
expr_stmt|;
name|serviceName
operator|=
name|sn
expr_stmt|;
block|}
specifier|public
name|WSDLServiceFactory
parameter_list|(
name|Bus
name|b
parameter_list|,
name|String
name|url
parameter_list|,
name|QName
name|sn
parameter_list|)
block|{
name|setBus
argument_list|(
name|b
argument_list|)
expr_stmt|;
try|try
block|{
comment|// use wsdl manager to parse wsdl or get cached definition
name|definition
operator|=
name|getBus
argument_list|()
operator|.
name|getExtension
argument_list|(
name|WSDLManager
operator|.
name|class
argument_list|)
operator|.
name|getDefinition
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WSDLException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|ServiceConstructionException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"SERVICE_CREATION_MSG"
argument_list|,
name|LOG
argument_list|)
argument_list|,
name|ex
argument_list|)
throw|;
block|}
name|serviceName
operator|=
name|sn
expr_stmt|;
block|}
specifier|public
name|Service
name|create
parameter_list|()
block|{
name|List
argument_list|<
name|ServiceInfo
argument_list|>
name|services
decl_stmt|;
if|if
condition|(
name|serviceName
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|services
operator|=
operator|new
name|WSDLServiceBuilder
argument_list|(
name|getBus
argument_list|()
argument_list|)
operator|.
name|buildServices
argument_list|(
name|definition
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XmlSchemaException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|ServiceConstructionException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"SERVICE_CREATION_MSG"
argument_list|,
name|LOG
argument_list|)
argument_list|,
name|ex
argument_list|)
throw|;
block|}
if|if
condition|(
name|services
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|ServiceConstructionException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"NO_SERVICE_EXC"
argument_list|,
name|LOG
argument_list|)
argument_list|)
throw|;
block|}
else|else
block|{
comment|//@@TODO  - this isn't good, need to return all the services
name|serviceName
operator|=
name|services
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getName
argument_list|()
expr_stmt|;
comment|//get all the service info's that match that first one.
name|Iterator
argument_list|<
name|ServiceInfo
argument_list|>
name|it
init|=
name|services
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|it
operator|.
name|next
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|serviceName
argument_list|)
condition|)
block|{
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
else|else
block|{
name|javax
operator|.
name|wsdl
operator|.
name|Service
name|wsdlService
init|=
name|definition
operator|.
name|getService
argument_list|(
name|serviceName
argument_list|)
decl_stmt|;
if|if
condition|(
name|wsdlService
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ServiceConstructionException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"NO_SUCH_SERVICE_EXC"
argument_list|,
name|LOG
argument_list|,
name|serviceName
argument_list|)
argument_list|)
throw|;
block|}
try|try
block|{
name|services
operator|=
operator|new
name|WSDLServiceBuilder
argument_list|(
name|getBus
argument_list|()
argument_list|)
operator|.
name|buildServices
argument_list|(
name|definition
argument_list|,
name|wsdlService
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XmlSchemaException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|ServiceConstructionException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"SERVICE_CREATION_MSG"
argument_list|,
name|LOG
argument_list|)
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
name|ServiceImpl
name|service
init|=
operator|new
name|ServiceImpl
argument_list|(
name|services
argument_list|)
decl_stmt|;
name|setService
argument_list|(
name|service
argument_list|)
expr_stmt|;
return|return
name|service
return|;
block|}
block|}
end_class

end_unit


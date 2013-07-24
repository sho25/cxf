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
name|transport
operator|.
name|http
operator|.
name|blueprint
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|Unmarshaller
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilderFactory
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
name|cxf
operator|.
name|common
operator|.
name|jaxb
operator|.
name|JAXBContextCache
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
name|common
operator|.
name|util
operator|.
name|PackageUtils
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
name|security
operator|.
name|ProxyAuthorizationPolicy
import|;
end_import

begin_class
specifier|public
class|class
name|ProxyAuthorizationPolicyHolder
extends|extends
name|ProxyAuthorizationPolicy
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
name|ProxyAuthorizationPolicyHolder
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|String
name|parsedElement
decl_stmt|;
specifier|private
name|ProxyAuthorizationPolicy
name|proxyAuthorizationPolicy
decl_stmt|;
specifier|private
name|JAXBContext
name|jaxbContext
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|jaxbClasses
decl_stmt|;
specifier|public
name|ProxyAuthorizationPolicyHolder
parameter_list|()
block|{     }
specifier|public
name|void
name|init
parameter_list|()
block|{
try|try
block|{
name|DocumentBuilderFactory
name|docFactory
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|docFactory
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Element
name|element
init|=
name|docFactory
operator|.
name|newDocumentBuilder
argument_list|()
operator|.
name|parse
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|parsedElement
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
operator|.
name|getDocumentElement
argument_list|()
decl_stmt|;
name|proxyAuthorizationPolicy
operator|=
operator|(
name|ProxyAuthorizationPolicy
operator|)
name|getJaxbObject
argument_list|(
name|element
argument_list|,
name|ProxyAuthorizationPolicy
operator|.
name|class
argument_list|)
expr_stmt|;
name|this
operator|.
name|setAuthorization
argument_list|(
name|proxyAuthorizationPolicy
operator|.
name|getAuthorization
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|setAuthorizationType
argument_list|(
name|proxyAuthorizationPolicy
operator|.
name|getAuthorizationType
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|setPassword
argument_list|(
name|proxyAuthorizationPolicy
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|setUserName
argument_list|(
name|proxyAuthorizationPolicy
operator|.
name|getUserName
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Could not process configuration."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|destroy
parameter_list|()
block|{              }
specifier|public
name|String
name|getParsedElement
parameter_list|()
block|{
return|return
name|parsedElement
return|;
block|}
specifier|public
name|void
name|setParsedElement
parameter_list|(
name|String
name|parsedElement
parameter_list|)
block|{
name|this
operator|.
name|parsedElement
operator|=
name|parsedElement
expr_stmt|;
block|}
specifier|protected
name|Object
name|getJaxbObject
parameter_list|(
name|Element
name|parent
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|c
parameter_list|)
block|{
try|try
block|{
name|Unmarshaller
name|umr
init|=
name|getContext
argument_list|(
name|c
argument_list|)
operator|.
name|createUnmarshaller
argument_list|()
decl_stmt|;
name|JAXBElement
argument_list|<
name|?
argument_list|>
name|ele
init|=
operator|(
name|JAXBElement
argument_list|<
name|?
argument_list|>
operator|)
name|umr
operator|.
name|unmarshal
argument_list|(
name|parent
argument_list|)
decl_stmt|;
return|return
name|ele
operator|.
name|getValue
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|JAXBException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Unable to parse property due to "
operator|+
name|e
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
specifier|protected
specifier|synchronized
name|JAXBContext
name|getContext
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
if|if
condition|(
name|jaxbContext
operator|==
literal|null
operator|||
name|jaxbClasses
operator|==
literal|null
operator|||
operator|!
name|jaxbClasses
operator|.
name|contains
argument_list|(
name|cls
argument_list|)
condition|)
block|{
try|try
block|{
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|tmp
init|=
operator|new
name|HashSet
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|jaxbClasses
operator|!=
literal|null
condition|)
block|{
name|tmp
operator|.
name|addAll
argument_list|(
name|jaxbClasses
argument_list|)
expr_stmt|;
block|}
name|JAXBContextCache
operator|.
name|addPackage
argument_list|(
name|tmp
argument_list|,
name|PackageUtils
operator|.
name|getPackageName
argument_list|(
name|cls
argument_list|)
argument_list|,
name|cls
operator|==
literal|null
condition|?
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
else|:
name|cls
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|cls
operator|!=
literal|null
condition|)
block|{
name|boolean
name|hasOf
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|c
range|:
name|tmp
control|)
block|{
if|if
condition|(
name|c
operator|.
name|getPackage
argument_list|()
operator|==
name|cls
operator|.
name|getPackage
argument_list|()
operator|&&
literal|"ObjectFactory"
operator|.
name|equals
argument_list|(
name|c
operator|.
name|getSimpleName
argument_list|()
argument_list|)
condition|)
block|{
name|hasOf
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|hasOf
condition|)
block|{
name|tmp
operator|.
name|add
argument_list|(
name|cls
argument_list|)
expr_stmt|;
block|}
block|}
name|JAXBContextCache
operator|.
name|scanPackages
argument_list|(
name|tmp
argument_list|)
expr_stmt|;
name|JAXBContextCache
operator|.
name|CachedContextAndSchemas
name|ccs
init|=
name|JAXBContextCache
operator|.
name|getCachedContextAndSchemas
argument_list|(
name|tmp
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|jaxbClasses
operator|=
name|ccs
operator|.
name|getClasses
argument_list|()
expr_stmt|;
name|jaxbContext
operator|=
name|ccs
operator|.
name|getContext
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JAXBException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|jaxbContext
return|;
block|}
block|}
end_class

end_unit

